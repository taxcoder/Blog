package com.tanx.blog.interceptor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tanx.blog.constant.Constant;
import com.tanx.blog.entity.po.WebStation;
import com.tanx.blog.mapper.WebStationMapper;
import com.tanx.blog.service.WebStationService;
import com.tanx.blog.utils.RedisOperationUtils;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

/**
 * @description: 更新登录时间
 * @project_name: blog-server
 * @author: 谭翔
 * @date: 2024/1/8 18:51
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateLoginTimeInterceptor implements HandlerInterceptor {

    @Resource
    private WebStationMapper webStationMapper;
    @Resource
    private RedisOperationUtils operationUtils;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if(!operationUtils.hasKey(Constant.LOGIN_TIME)) {
            WebStation webStation = webStationMapper.selectOne(new QueryWrapper<>());
            webStation.setLoginUpdateTime(new Timestamp(System.currentTimeMillis()));
            webStationMapper.updateById(webStation);
            operationUtils.set(Constant.WEB_KEY, webStation);
            operationUtils.setEx(Constant.LOGIN_TIME, String.valueOf(webStation.getLoginUpdateTime()), 1, TimeUnit.HOURS);
        }
    }
}
