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
 * 文章表
 *
 * @TableName b_article
 */
@TableName(value = "b_article")
@Data
public class Article implements Serializable {
    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 文章ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 分类ID
     */
    private Integer classificationId;
    /**
     * 文章标题
     */
    private String title;
    /**
     * 是否置顶
     */
    private boolean isTop;
    /**
     * 文章显示图片
     */
    private String image;
    /**
     * 文章内容地址
     */
    private String contentUrl;
    /**
     * 文章开头一部分
     */
    private String prefixContent;
    /**
     * 是否处于回收站
     */
    private Boolean recovery;
    /**
     *  浏览量
     */
    private long lookCount;
    /**
     * 点赞数
     */
    private long likeCount;
    /**
     * 上传文章时所处的省份
     */
    private String address;
    /**
     * 进入回收站的时间
     */
    private Date RemoveTime;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;

    public Article() {
    }

    public Article(Integer id, Boolean recovery, Date removeTime) {
        this.id = id;
        this.recovery = recovery;
        RemoveTime = removeTime;
    }
}