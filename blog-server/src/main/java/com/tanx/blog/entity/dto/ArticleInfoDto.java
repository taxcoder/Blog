package com.tanx.blog.entity.dto;

import lombok.Data;

/**
 * @description: 文章信息类，只包含ID和标题
 * @project_name: blog-server
 * @author: 谭翔
 * @date: 2023/10/2 13:28
 */

@Data
public class ArticleInfoDto {

    /**
     * 文章ID
     */
    private Integer id;

    /**
     * 文章标题
     */
    private String title;
}
