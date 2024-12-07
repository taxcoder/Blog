package com.tanx.blog.entity.vo;

import io.swagger.v3.oas.annotations.Parameter;

import java.util.ArrayList;

/**
 * @description: 更新标签和文章连接
 * @project_name: blog-server
 * @author: 谭翔
 * @date: 2023/9/28 18:58
 */
public class UpdateTagInfoVo {

    @Parameter(name = "name", description = "标签名称")
    private String name;

    @Parameter(name = "articles", description = "包含文章ID的数组")
    private ArrayList<Integer> articles;
}
