package com.tanx.blog.entity.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class WebStationDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 网站ID
     */
    private Integer id;

    /**
     * 网站名称
     */
    private String name;

    /**
     * 头像信息
     */
    private String headIcon;

    /**
     * 背景图
     */
    private String backgroundImage;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 格言,副标题
     */
    private String motto;

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
     * 作者最后在线时间
     */
    private Long loginUpdateTime;

    /**
     * 网站总文字数量
     */
    private Long totalTextQuantity;

    /**
     * 创建时间
     */
    private Long createTime;

    public WebStationDto(com.tanx.blog.entity.po.WebStation webStation) {
        this.id = webStation.getId();
        this.name = webStation.getName();
        this.headIcon = webStation.getHeadIcon();
        this.userName = webStation.getUserName();
        this.motto = webStation.getMotto();
        this.forTheRecord = webStation.getForTheRecord();
        this.bulletinBoard = webStation.getBulletinBoard();
        this.totalVisits = webStation.getTotalVisits();
        this.publicSecurityRegistrationNumber = webStation.getPublicSecurityRegistrationNumber();
        this.loginUpdateTime = webStation.getLoginUpdateTime().getTime();
        this.totalTextQuantity = webStation.getTotalTextQuantity();
        this.createTime = webStation.getCreateTime().getTime();
        this.backgroundImage = webStation.getBackgroundImage();
    }
}