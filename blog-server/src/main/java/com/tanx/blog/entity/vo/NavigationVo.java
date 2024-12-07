package com.tanx.blog.entity.vo;

/**
 * @description:
 * @project_name: blog-server
 * @author: 谭翔
 * @date: 2024/4/21 19:12
 */

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 存储导航分类信息
 * @TableName d_classification_navigation
 */

@Data
@TableName(value ="b_navigation")
public class NavigationVo implements Serializable {
    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 导航分类ID
     */
    private String classificationNavigationName;


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
     * 网站是否可以联通
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


    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public NavigationVo() {
    }

    public NavigationVo(Integer id, String classificationNavigationName, String url, String title, String description, String favicon, boolean isOk, Date createTime, Date updateTime) {
        this.id = id;
        this.classificationNavigationName = classificationNavigationName;
        this.url = url;
        this.title = title;
        this.description = description;
        this.favicon = favicon;
        this.isOk = isOk;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }
}