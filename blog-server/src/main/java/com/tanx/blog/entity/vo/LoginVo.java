package com.tanx.blog.entity.vo;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @description: 登录Vo
 * @project_name: blog-server
 * @author: 谭翔
 * @date: 2023/10/25 12:08
 */

@Data
public class LoginVo {

    @Email(message = "账户名格式错误！")
    @NotBlank(message = "账号名不能为空!")
    @Length(max = 40, message = "账号名长度过长！")
    private String email;

    @NotBlank(message = "密码不能为空!")
    private String password;

    private boolean remember;
}
