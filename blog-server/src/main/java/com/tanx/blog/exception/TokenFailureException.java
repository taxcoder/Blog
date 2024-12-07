package com.tanx.blog.exception;

/**
 * @description:
 * @project_name: blog-server
 * @author: 谭翔
 * @date: 2023/11/5 19:00
 */
public class TokenFailureException extends RuntimeException {

    public TokenFailureException() {
        super("token已失效！");
    }
}
