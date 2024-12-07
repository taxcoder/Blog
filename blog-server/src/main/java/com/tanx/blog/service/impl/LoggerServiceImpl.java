package com.tanx.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tanx.blog.entity.po.Logger;
import com.tanx.blog.service.LoggerService;
import com.tanx.blog.mapper.LoggerMapper;
import com.tanx.blog.utils.RedisOperationUtils;
import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
* @author 谭翔
* @description 针对表【s_logger】的数据库操作Service实现
* @createDate 2024-04-26 19:24:42
*/
@Service
public class LoggerServiceImpl extends ServiceImpl<LoggerMapper, Logger>
    implements LoggerService{

    @Resource
    private RedisOperationUtils operationUtils;
    @Resource
    private LoggerMapper loggerMapper;

    @Async
    @Override
    public void pushLogger(Logger logger) {
        loggerMapper.insert(logger);
    }
}




