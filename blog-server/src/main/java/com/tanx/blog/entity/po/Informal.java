package com.tanx.blog.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 随笔表
 *
 * @TableName b_informal
 */
@TableName(value = "b_informal")
@Data
public class Informal implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 随笔ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 文章内容
     */
    private String content;
    /**
     * 温度
     */
    private Integer temperature;
    /**
     * 心情的emoji
     */
    private String mood;
    /**
     * 天气
     */
    private Integer weather;
    /**
     * 省份
     */
    private String province;
    /**
     * 是否处于回收站
     */
    private Boolean recovery;
    /**
     * 点赞数
     */
    private Integer likeCount;
    /**
     * 进入回收站的时间
     */
    private Timestamp removeTime;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
}