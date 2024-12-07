package com.tanx.blog.service;

import com.tanx.blog.entity.po.Logger;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.scheduling.annotation.Async;

/**
* @author 谭翔
* @description 针对表【s_logger】的数据库操作Service
* @createDate 2024-04-26 19:24:42
*/
public interface LoggerService extends IService<Logger> {

    public void pushLogger(Logger logger);

}
