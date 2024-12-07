package com.tanx.blog.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * emoji表情
 *
 * @TableName d_emoji
 */
@TableName(value = "d_emoji")
@Data
public class Emoji implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * emoji表情ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * emoji图片
     */
    private String icon;

    /**
     * emoji名称
     */
    private String name;
    /**
     * 组名
     */
    private String groupName;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
}