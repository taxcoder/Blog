package com.tanx.blog.filter;

import cn.hutool.core.util.StrUtil;
import com.tanx.blog.constant.Constant;
import com.tanx.blog.exception.NoTokenException;
import com.tanx.blog.exception.TokenFailureException;
import com.tanx.blog.exception.TokenFormatErrorException;
import com.tanx.blog.exception.TokenVerifyErrorException;
import com.tanx.blog.handler.JwtHandler;
import com.tanx.blog.handler.UserDetailHandler;
import com.tanx.blog.mapper.WebStationMapper;
import com.tanx.blog.utils.RedisOperationUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

/**
 * @description: jwt过滤器
 * @project_name: blog-server
 * @author: 谭翔
 * @date: 2023/10/25 20:59
 */

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtHandler jwtService;

    @Autowired
    private WebStationMapper webStationMapper;

    @Resource
    private RedisOperationUtils redisOperationUtils;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    @Override
    protected void doFilterInternal(HttpServletRequest req, @NotNull HttpServletResponse res, @NotNull FilterChain filterChain) throws ServletException, IOException {
        // 如果是api开头的端口，则直接通过
        if (req.getServletPath().startsWith("/api/") || req.getServletPath().equals("/favicon.ico") || "OPTIONS".equals(req.getMethod())) {
            filterChain.doFilter(req, res);
            return;
        }

        //从header中获取token
        String authHeader = req.getHeader("Authorization");
        if (authHeader == null) {
            resolver.resolveException(req, res, null, new NoTokenException());
            return;
        }
        //解析jwt获取用户名
        String username;
        try {
            username = jwtService.extractUsername(authHeader);
            if (username == null || username.length() == 0) {
                throw new TokenFormatErrorException();
            }
            String key;
            // 判断是后台接口，还是前台需要权限访问的接口，后台接口的token持续时间可以比较长
            if (req.getServletPath().startsWith("/auth/")) {
                // 从redis取出token，没有取出则表示登录，取出则表示已登录
                key = StrUtil.format(Constant.REDIS_TOKEN_KEY, username);
            } else {
                key = StrUtil.format(Constant.REDIS_TEMP_TOKEN_KEY, username);
            }
            // 判断redis内是否保存着token，如果存在则取出token进行校验,不存在则抛出token不存在
            if (!redisOperationUtils.hasKey(key)) {
                throw new TokenFailureException();
            }
            String token = (String) redisOperationUtils.get(key);
            // 如果没有从redis取出数据，或者是取出的token的username和传递的token的username不一致，则表示该token是虚假token，则抛出错误
            if (!authHeader.equals(token) || !jwtService.isTokenValid(token, username)) {
                throw new TokenVerifyErrorException();
            }
        } catch (TokenFormatErrorException e) {
            log.error("token格式错误！");
            resolver.resolveException(req, res, null, new TokenFormatErrorException());
            return;
        } catch (Exception e) {
            log.error("token验证失败！");
            resolver.resolveException(req, res, null, new TokenVerifyErrorException());
            return;
        }

        if (UserDetailHandler.getCurrentUser() == null) {
            // 没用，只是用来过一遍springSecurity
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(null, null, null);
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        filterChain.doFilter(req, res);
    }
}
