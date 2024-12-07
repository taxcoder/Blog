package com.tanx.blog.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 文章表和标签表的中间表
 *
 * @TableName r_article_tag
 */
@TableName(value = "r_article_tag")
@Data
public class ArticleTag implements Serializable {
    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 文本打印ID
     */
    @TableId(value = "id", type = IdType.AUTO)

    private Integer id;
    /**
     * 文章ID
     */
    @TableField(value = "article_id")
    private Integer articleId;
    /**
     * 标签ID
     */
    @TableField(value = "tag_id")
    private Integer tagId;
    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;
    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    public ArticleTag() {
    }

    public ArticleTag(Integer articleId, Integer tagId) {
        this.articleId = articleId;
        this.tagId = tagId;
    }
}