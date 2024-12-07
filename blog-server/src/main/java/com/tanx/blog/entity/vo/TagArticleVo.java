package com.tanx.blog.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;

/**
 * @description: 标签文章类
 * @project_name: blog-server
 * @author: 谭翔
 * @date: 2023/9/27 23:54
 */

@Data
@Schema(title = "标签文章Vo", description = "标签和文章关联的Vo")
public class TagArticleVo {

    @Schema(name = "tagName", description = "新的标签名称")
    private String name;

    private ArrayList<Integer> articles;
}
