package com.tanx.blog.exception;

/**
 * @description:
 * @project_name: blog-server
 * @author: 谭翔
 * @date: 2023/10/27 20:52
 */
public class TokenFormatErrorException extends RuntimeException {

    public TokenFormatErrorException() {
        super("token格式错误！");
    }
}
