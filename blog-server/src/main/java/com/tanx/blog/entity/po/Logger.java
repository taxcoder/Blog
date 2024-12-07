package com.tanx.blog.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName s_logger
 */
@TableName(value ="s_logger")
@Data
public class Logger implements Serializable {
    /**
     * 
     */
    private Long id;

    /**
     * 
     */
    private String content;

    /**
     * 
     */
    private Boolean isSolve;

    /**
     * 
     */
    private Boolean level;

    /**
     * 
     */
    private Date createTime;

    /**
     * 
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}