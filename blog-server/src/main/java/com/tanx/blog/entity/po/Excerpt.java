package com.tanx.blog.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 优美语句摘抄表
 * @TableName b_excerpt
 */
@TableName(value ="b_excerpt")
@Data
public class Excerpt implements Serializable {
    /**
     *  摘抄ID 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     *  内容 
     */
    private String content;

    /**
     *  创建时间 
     */
    private Date createTime;

    /**
     *  更新时间 
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}