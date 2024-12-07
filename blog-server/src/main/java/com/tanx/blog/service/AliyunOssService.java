package com.tanx.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tanx.blog.entity.common.OssEntity;
import com.tanx.blog.entity.po.AliyunOss;

import java.util.List;

/**
 * @author 谭翔
 * @description 针对表【s_aliyun_oss(阿里云OSS配置信息)】的数据库操作Service
 * @createDate 2024-01-02 21:20:11
 */
public interface AliyunOssService extends IService<AliyunOss> {


    public List<AliyunOss> init();

    public OssEntity updateOss(OssEntity oss, String bucketName);
}
