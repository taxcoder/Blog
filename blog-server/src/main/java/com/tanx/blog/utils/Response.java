package com.tanx.blog.utils;

import lombok.Data;
import org.apache.http.HttpStatus;

@Data
public class Response<T> {

    private int code;

    private String message;

    private T data;

    private Response() {
    }

    public static <T> Response<T> success(String message, T data) {
        return build(HttpStatus.SC_OK, message, data);
    }

    public static <T> Response<T> success(T data) {
        return success("请求成功！", data);
    }

    public static <T> Response<T> error(int code, String message) {
        return build(code, message, null);
    }

    public static <T> Response<T> error(int code, String message, T data) {
        return build(code, message, data);
    }


    public static <T> Response<T> error(ResponseEnumUtils utils) {
        return error(utils.getCode(), utils.getMsg());
    }

    private static <T> Response<T> build(int code, String message, T data) {
        Response<T> response = new Response<>();
        if (data != null) response.setData(data);
        response.setCode(code);
        response.setMessage(message);
        return response;
    }
}