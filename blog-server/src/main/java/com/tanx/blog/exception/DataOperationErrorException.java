package com.tanx.blog.exception;

/**
 * @description: 参数异常类
 * @project_name: blog-server
 * @author: 谭翔
 * @date: 2023/9/27 18:43
 */
public class DataOperationErrorException extends RuntimeException {

    public DataOperationErrorException(String message) {
        super(message);
    }
}
