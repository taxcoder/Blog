package com.tanx.blog.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import lombok.Data;

/**
 * 记录生活照片
 * @TableName b_images
 */
@TableName(value ="b_images")
@Data
public class Images implements Serializable {
    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 图片URL
     */
    private String url;

    /**
     * 上传图片的时间
     */
    private Timestamp uploadTime;

    /**
     * 创建时间
     */
    private Timestamp createTime;

    /**
     * 更新时间
     */
    private Timestamp updateTime;

    public Images() {
    }

    public Images(Long id, String url, Timestamp uploadTime, Timestamp createTime, Timestamp updateTime) {
        this.id = id;
        this.url = url;
        this.uploadTime = uploadTime;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Images(String url, Timestamp uploadTime) {
        this.url = url;
        this.uploadTime = uploadTime;
    }

    public Images(String url, Timestamp uploadTime, Timestamp createTime, Timestamp updateTime) {
        this.url = url;
        this.uploadTime = uploadTime;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}