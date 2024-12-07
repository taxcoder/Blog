package com.tanx.blog.exception;

/**
 * @description: 无权访问异常类
 * @project_name: blog-server
 * @author: 谭翔
 * @date: 2023/10/27 11:50
 */

public class UnAuthorizationException extends Exception {

    public UnAuthorizationException() {
        super("权限不足！");
    }
}
