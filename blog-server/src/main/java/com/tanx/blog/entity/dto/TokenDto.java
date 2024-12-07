package com.tanx.blog.entity.dto;

import lombok.Data;

/**
 * @description: token
 * @project_name: blog-server
 * @author: 谭翔
 * @date: 2023/10/27 10:53
 */

@Data
public class TokenDto {

    /**
     * token实体类
     */
    private Integer id;

    private String token;

    //是否失效(这里指手动失效)
    private boolean revoked;

    //是否过期
    private boolean expired;


    private Integer userId;
}
