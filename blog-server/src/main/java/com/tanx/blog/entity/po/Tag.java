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
 * 标签表
 *
 * @TableName b_tag
 */
@TableName(value = "b_tag")
@Data
public class Tag implements Serializable {
    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 标签ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 标签名称
     */
    private String name;
    /**
     * 是否可以删除
     */
    private Boolean isRemove;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;

    public Tag() {
    }

    public Tag(String name) {
        this.name = name;
    }

    public Tag(String name,boolean isRemove) {
        this.name = name;
        this.isRemove = isRemove;
    }
}