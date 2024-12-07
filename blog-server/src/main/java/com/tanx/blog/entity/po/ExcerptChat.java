package com.tanx.blog.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 摘抄链接聊天表
 * @TableName r_excerpt_chat
 */
@TableName(value ="r_excerpt_chat")
@Data
public class ExcerptChat implements Serializable {
    /**
     *  摘抄链接聊天表ID 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     *  聊天表ID 
     */
    private Long chatId;

    /**
     *  摘抄表ID 
     */
    private Long excerptId;

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