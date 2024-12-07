package com.tanx.blog.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 聊天表
 * @TableName b_chat
 */
@TableName(value ="b_chat")
@Data
public class Chat implements Serializable {
    /**
     *  聊天表ID 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 作者名称
     */
    private String author;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 访客还是博主
     */
    private boolean status;

    /**
     * 头像
     */
    private String image;

    /**
     * 网站地址
     */
    private String webUrl;

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