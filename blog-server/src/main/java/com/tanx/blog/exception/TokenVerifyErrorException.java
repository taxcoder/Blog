package com.tanx.blog.exception;

/**
 * @description: token格式异常
 * @project_name: blog-server
 * @author: 谭翔
 * @date: 2023/10/27 10:39
 */
public class TokenVerifyErrorException extends RuntimeException {

    public TokenVerifyErrorException() {
        super("token格式异常！");
    }
}
