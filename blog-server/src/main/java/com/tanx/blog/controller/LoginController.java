package com.tanx.blog.controller;

import cn.hutool.core.util.StrUtil;
import com.tanx.blog.constant.Constant;
import com.tanx.blog.entity.vo.LoginVo;
import com.tanx.blog.exception.TokenVerifyErrorException;
import com.tanx.blog.handler.JwtHandler;
import com.tanx.blog.handler.UserDetailHandler;
import com.tanx.blog.utils.RedisOperationUtils;
import com.tanx.blog.utils.Response;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * @description: 登录控制类
 * @project_name: blog-server
 * @author: 谭翔
 * @date: 2023/10/25 12:06
 */

@Slf4j
@Validated
@RestController
public class LoginController {

    @Resource
    AuthenticationManager authenticationManager;
    @Resource
    private JwtHandler jwtHandler;
    @Resource
    private RedisOperationUtils operationUtils;

    @Operation(summary = "后台的登录，token持续时间最长可达7天")
    @PostMapping("/api/login")
    public Response<String> login(@Valid @RequestBody LoginVo loginVo) {
        try {
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(loginVo.getEmail(), loginVo.getPassword());
            Authentication authentication = authenticationManager.authenticate(auth);
            UserDetailHandler.setCurrentUser(authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String key = StrUtil.format(Constant.REDIS_TOKEN_KEY, loginVo.getEmail());
            // 如果已經d登陆过，直接返回token
            if(operationUtils.hasKey(key)) {
                return Response.success("登录成功！", String.valueOf(operationUtils.get(key)));
            }
            // 生成token
            HashMap<String, Object> map = new HashMap<>() {{
                put("username", loginVo.getEmail());
            }};

            String token = loginVo.isRemember() ? jwtHandler.generateRefreshToken(map, userDetails) : jwtHandler.generateToken(map, userDetails);

            // 存入redis，设置过期时间，只要在redis内存在则表示已登陆
            operationUtils.setEx(key, token, loginVo.isRemember() ? jwtHandler.getRefreshExpiration() : jwtHandler.getJwtExpiration(), TimeUnit.MILLISECONDS);
            return Response.success("登录成功！", token);
        } catch (BadCredentialsException e) {
            throw new RuntimeException("用户名或密码错误！");
        } catch (Exception e) {
            log.error("e:{}", e.getMessage());
            throw new RuntimeException("服务器异常，请重新尝试！");
        }
    }

    @Operation(summary = "前台的登录，token持续时间较短")
    @PostMapping("/api/web/login")
    public Response<String> webLogin(@Valid @RequestBody LoginVo loginVo) {
        try {
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(loginVo.getEmail(), loginVo.getPassword());
            Authentication authentication = authenticationManager.authenticate(auth);
            UserDetailHandler.setCurrentUser(authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String key = StrUtil.format(Constant.REDIS_TEMP_TOKEN_KEY, loginVo.getEmail());
            // 如果已經d登陆过，直接返回token
            if(operationUtils.hasKey(key)) {
                return Response.success("登录成功！", String.valueOf(operationUtils.get(key)));
            }
            // 生成token
            HashMap<String, Object> map = new HashMap<>() {{
                put("username", loginVo.getEmail());
            }};

            String token = jwtHandler.generateTempToken(map, userDetails);
            // 存入redis，设置过期时间，只要在redis内存在则表示已登陆
            operationUtils.setEx(key, token, jwtHandler.getTempTokenExpiration(), TimeUnit.MILLISECONDS);
            return Response.success("登录成功！", token);
        } catch (BadCredentialsException e) {
            throw new RuntimeException("用户名或密码错误！");
        } catch (Exception e) {
            log.error("e:{}", e.getMessage());
            throw new RuntimeException("服务器异常，请重新尝试！");
        }
    }

    @PostMapping("/auth/logout/{email}")
    public Response<String> logout(@PathVariable @NotBlank(message = "登出失败！") String email) {
        try {
            // 删除token
            operationUtils.delete(StrUtil.format(Constant.REDIS_TOKEN_KEY, email));
            SecurityContextHolder.clearContext();
            return Response.success("登出成功！", null);
        } catch (TokenVerifyErrorException e) {
            throw new TokenVerifyErrorException();
        } catch (Exception e) {
            throw new RuntimeException("服务器异常，请重新尝试！");
        }
    }

    @PostMapping("/admin/logout/{email}")
    public Response<String> adminLogout(@PathVariable @NotBlank(message = "登出失败！") String email) {
        try {
            // 删除token
            operationUtils.delete(StrUtil.format(Constant.REDIS_TEMP_TOKEN_KEY, email));
            SecurityContextHolder.clearContext();
            return Response.success("登出成功！", null);
        } catch (TokenVerifyErrorException e) {
            throw new TokenVerifyErrorException();
        } catch (Exception e) {
            throw new RuntimeException("服务器异常，请重新尝试！");
        }
    }

    @Operation(summary = "判断token是否有效")
    @PostMapping("/admin/isLogin")
    public Response<String> tokenTimeout() {
        return Response.success("成功");
    }
}
