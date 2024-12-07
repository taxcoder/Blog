package com.tanx.blog.interceptor;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.tanx.blog.service.WebStationService;
import com.tanx.blog.utils.IPUtils;
import com.tanx.blog.utils.RedisOperationUtils;
import com.tanx.blog.utils.Response;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
@Component
@RequiredArgsConstructor
public class RepeatRequestInterceptor implements HandlerInterceptor {
    
    @Autowired
    private RedisOperationUtils operationUtils;

    private static final String LOCK_IP_URL_KEY = "lock_ip_{}";

    private static final String IP_URL_REQ_TIME = "ip_url_times_{}";

    // 1秒超过4次就防刷
    private static final long LIMIT_TIMES = 5;

    // 防刷黑名单60秒
    private static final long IP_LOCK_TIME = 60;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        log.info("request请求地址uri={},ip={}", httpServletRequest.getRequestURI(), IPUtils.getIpAddress(httpServletRequest));
        if (ipIsLock(IPUtils.getIpAddress(httpServletRequest))) {
            log.info("ip访问被禁止={}", IPUtils.getIpAddress(httpServletRequest));
            returnJson(httpServletResponse, JSON.toJSONString(Response.error(423,"ip访问被禁止")));
            return false;
        }
        if (!addRequestTime(IPUtils.getIpAddress(httpServletRequest), httpServletRequest.getRequestURI())) {
            returnJson(httpServletResponse, JSON.toJSONString(Response.error(423,"ip访问被禁止")));
            return false;
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }

    /**
     * 判断ip是否被禁用
     *
     * @param ip ip ip地址
     * @return 返回状态
     */
    private Boolean ipIsLock(String ip) {
        return operationUtils.hasKey(StrUtil.format(LOCK_IP_URL_KEY, ip));
    }

    /**
     * 记录请求次数
     *
     * @param ip ip地址
     * @param uri 请求的uri
     * @return 返回
     */
    private Boolean addRequestTime(String ip, String uri) {
        String key = StrUtil.format(IP_URL_REQ_TIME, ip + uri);
        if (operationUtils.hasKey(key)) {
            // 如果key存在，次数+1
            long count = operationUtils.incrBy(key, 1);
            log.info("count:{}", count);
            if (count >= LIMIT_TIMES) {
                // 如果超过限制次数，则设置ip被禁用 60秒
                operationUtils.getLock(StrUtil.format(LOCK_IP_URL_KEY, ip), ip, IP_LOCK_TIME);
                operationUtils.delete(key);
                return false;
            }
        } else {
            // ip+uri请求次数为1，1秒后过期
            operationUtils.getLock(key, 1, 1);
            log.info("记录请求次数1");
        }
        return true;
    }

    private void returnJson(HttpServletResponse response, String json) throws Exception {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/json; charset=utf-8");
        try (PrintWriter writer = response.getWriter()) {
            writer.print(json);
        } catch (IOException e) {
            log.error("LoginInterceptor response error ---> {}", e.getMessage(), e);
        }
    }
}