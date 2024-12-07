package com.tanx.blog.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @description: 管理员信息Vo
 * @project_name: blog-server
 * @author: 谭翔
 * @date: 2023/10/14 12:02
 */
@Data
public class UserVo {

    @NotBlank(message = "用户名不能上传为空！")
    @Length(max = 30, message = "用户名字符过长！")
    @Schema(name = "userName", description = "需要修改的用户名")
    private String userName;

    @NotBlank(message = "登录名不能上传为空！")
    @Email(message = "邮箱格式错误")
    @Length(max = 40, message = "登录名字符过长！")
    @Schema(name = "email", description = "需要修改的登录名")
    private String email;

    @NotBlank(message = "确认密码不能上传为空！")
    @Length(max = 300, message = "确认密码字符过长！")
    @Schema(name = "oldPassword", description = "用于判断是否具有权限")
    private String oldPassword;
}
