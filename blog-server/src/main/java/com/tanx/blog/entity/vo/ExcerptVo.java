package com.tanx.blog.entity.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * @description:
 * @project_name: blog-server
 * @author: 谭翔
 * @date: 2024/4/27 16:07
 */

@Data
public class ExcerptVo {

    /**
     *  摘抄ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     *  内容
     */
    private String content;

    /**
     * 回复个数
     */
    private Integer chatNumber;

    /**
     *  创建时间
     */
    private Date createTime;

    /**
     *  更新时间
     */
    private Date updateTime;
}
