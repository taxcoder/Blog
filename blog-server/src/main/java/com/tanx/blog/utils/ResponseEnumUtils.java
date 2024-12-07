package com.tanx.blog.utils;

import lombok.Getter;

@Getter
public enum ResponseEnumUtils {
    // 参数异常
    PARAM_ERROR(412, "参数异常！"),

    LOGIN_ERROR(401, "账号或密码错误！"),

    GET_DATA_FAIL(500, "数据获取失败！");

    private int code;
    private String msg;

    ResponseEnumUtils(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}