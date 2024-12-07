package com.tanx.blog.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 阿里云OSS配置信息
 * @TableName s_aliyun_oss
 */
@TableName(value ="s_aliyun_oss")
@Data
public class AliyunOss implements Serializable {
    /**
     * 表ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * bucket的名称
     */
    private String bucketName;

    /**
     * 域名地址
     */
    private String domainName;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}