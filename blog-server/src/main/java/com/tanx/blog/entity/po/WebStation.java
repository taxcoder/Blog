package com.tanx.blog.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 网站信息表
 *
 * @TableName s_web_station
 */
@TableName(value = "s_web_station")
@Data
public class WebStation implements Serializable {
    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 网站ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 网站名称
     */
    private String name;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 登录邮箱
     */
    private String email;
    /**
     * 密码
     */
    private String password;
    /**
     * 头像信息
     */
    private String headIcon;
    /**
     * 背景图
     */
    private String backgroundImage;
    /**
     * 格言,副标题
     */
    private String motto;
    /**
     * 关于我
     */
    private String content;
    /**
     * 网站备案号
     */
    private String forTheRecord;
    /**
     * 公告栏
     */
    private String bulletinBoard;
    /**
     * 网站所有的访问人数
     */
    private Long totalVisits;
    /**
     * 公安备案号
     */
    private String publicSecurityRegistrationNumber;
    /**
     * 网站内容更新时间
     */
    private Timestamp loginUpdateTime;
    /**
     * 网站总文字数量
     */
    private Long totalTextQuantity;
    /**
     * 创建时间
     */
    private Timestamp createTime;
    /**
     * 更新时间
     */
    private Timestamp updateTime;
}