package com.tanx.blog.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * @description: 接受标签类
 * @project_name: blog-server
 * @author: 谭翔
 * @date: 2023/10/3 15:11
 */

@Data
@Schema(title = "标签Vo", description = "用于增加标签")
public class TagVo {

    @NotNull(message = "请传入正确的参数！")
    private List<String> name;
}
