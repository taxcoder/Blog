package com.tanx.blog.exception;

/**
 * @description: 不存在token
 * @project_name: blog-server
 * @author: 谭翔
 * @date: 2023/10/27 10:42
 */
public class NoTokenException extends RuntimeException {

    public NoTokenException() {
        super("token不存在！");
    }
}
