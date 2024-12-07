package com.tanx.blog.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 导航网站信息
 * @TableName b_navigation
 */
@TableName(value ="b_navigation")
@Data
public class Navigation implements Serializable {
    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 导航分类ID
     */
    private Integer classificationNavigationId;

    /**
     * 网站url
     */
    private String url;

    /**
     * 导航名
     */
    private String title;

    /**
     * 描述
     */
    private String description;

    /**
     * 图标
     */
    private String favicon;

    /**
     * 网站是否可以访问
     */
    private boolean isOk;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public Navigation() {
    }

    public Navigation(Integer id, Integer classificationNavigationId, String title, String description, String favicon, boolean isOk, Date createTime, Date updateTime) {
        this.id = id;
        this.classificationNavigationId = classificationNavigationId;
        this.title = title;
        this.description = description;
        this.favicon = favicon;
        this.isOk = isOk;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Navigation(Integer id, Integer classificationNavigationId) {
        this.id = id;
        this.classificationNavigationId = classificationNavigationId;
    }
}