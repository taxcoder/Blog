package com.tanx.blog.entity.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 标签表
 *
 * @TableName b_tag
 */
@Data
public class TagDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 标签ID
     */
    private Integer id;

    /**
     * 标签名称
     */
    private String name;

    /**
     * 是否可以删除
     */
    private boolean isRemove;

    /**
     * 关联文章个数
     */
    private Integer articleCount;

    /**
     * 创建时间
     */
    private Timestamp createTime;
}