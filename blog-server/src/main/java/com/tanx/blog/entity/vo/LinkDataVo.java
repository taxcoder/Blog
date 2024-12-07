package com.tanx.blog.entity.vo;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * @description:
 * @project_name: blog-server
 * @author: 谭翔
 * @date: 2024/5/2 19:12
 */

@Data
public class LinkDataVo {


    @NotNull(message = "分类ID不能为空")
    @Min(value = 1, message = "ID不能小于1")
    private Integer select;

    private Integer[] datas;
}
