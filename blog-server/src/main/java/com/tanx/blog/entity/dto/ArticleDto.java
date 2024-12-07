package com.tanx.blog.entity.dto;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class ArticleDto {
    /**
     * 文章ID
     */
    private Integer id;

    /**
     * 分类
     */
    private ClassificationDto classification;

    /**
     * 标签
     */
    private List<TagDto> tag;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 是否置顶
     */
    private boolean isTop;

    /**
     * 头像
     */
    private String headIcon;

    /**
     * 作者
     */
    private String author;

    /**
     * 文章显示图片
     */
    private String image;

    /**
     * 文章开头一部分
     */
    private String prefixContent;

    /**
     * 文章存储地址
     */
    private String contentUrl;

    /**
     * 浏览量
     */
    private long lookCount;

    /**
     * 点赞数
     */
    private long likeCount;

    /**
     * 发布文章时，所处的省份
     */
    private String address;

    /**
     * 进入回收站的时间
     */
    private Timestamp removeTime;

    /**
     * 创建时间
     */
    private Timestamp createTime;

    /**
     * 更新时间
     */
    private Timestamp updateTime;
}
