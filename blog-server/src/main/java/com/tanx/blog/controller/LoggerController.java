package com.tanx.blog.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 日志控制类
 * @project_name: blog-server
 * @author: 谭翔
 * @date: 2024/4/26 19:25
 */

@Slf4j
@Validated
@RestController
@Tag(name = "日志管理")
@RequestMapping("/auth/logger")
public class LoggerController {


}
