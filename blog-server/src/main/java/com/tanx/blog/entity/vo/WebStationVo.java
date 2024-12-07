package com.tanx.blog.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @description:
 * @project_name: blog-server
 * @author: 谭翔
 * @date: 2023/10/13 21:28
 */

@Data
public class WebStationVo {

    /**
     * 网站名称
     */
    @NotBlank(message = "网站名不能为空！")
    @Length(max = 40, message = "网站名字符长度过长！")
    @Schema(name = "name", description = "网站名")
    private String name;

    /**
     * 格言,副标题
     */
    @NotBlank(message = "格言内容不能为空！")
    @Length(max = 60, message = "格言内容字符长度过长！")
    @Schema(name = "motto", description = "格言")
    private String motto;

    /**
     * 网站备案号
     */
    @NotBlank(message = "网站备案号不能为空！")
    @Length(max = 50, message = "网站备案号字符长度过长！")
    @Schema(name = "forTheRecord", description = "网站备案号")
    private String forTheRecord;

    /**
     * 公告栏
     */
    @NotBlank(message = "公告栏内容不能为空！")
    @Length(max = 200, message = "公告栏字符长度过长！")
    @Schema(name = "bulletinBoard", description = "公告栏")
    private String bulletinBoard;

    /**
     * 公安备案号
     */
    @NotBlank(message = "公安备案号不能为空！")
    @Length(max = 50, message = "公安备案号字符长度过长！")
    @Schema(name = "publicSecurityRegistrationNumber", description = "公安备案号")
    private String publicSecurityRegistrationNumber;
}
