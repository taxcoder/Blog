package com.tanx.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tanx.blog.constant.Constant;
import com.tanx.blog.entity.common.OssEntity;
import com.tanx.blog.entity.po.AliyunOss;
import com.tanx.blog.mapper.AliyunOssMapper;
import com.tanx.blog.service.AliyunOssService;
import com.tanx.blog.utils.RedisOperationUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 谭翔
 * @description 针对表【s_aliyun_oss(阿里云OSS配置信息)】的数据库操作Service实现
 * @createDate 2024-01-02 21:20:11
 */
@Slf4j
@Service
public class AliyunOssServiceImpl extends ServiceImpl<AliyunOssMapper, AliyunOss>
        implements AliyunOssService {

    @Resource
    private RedisOperationUtils operationUtils;
    @Override
    public List<AliyunOss> init() {
        List<AliyunOss> list = list();
        HashMap<String, AliyunOss> hashMap = new HashMap<>((int) Math.ceil(list.size() + list.size() * 0.75));
        list.forEach(l -> hashMap.put(l.getBucketName(), l));
        operationUtils.hPutAll(Constant.ALIYUN_OSS, hashMap);
        return list;
    }

    public OssEntity updateOss(OssEntity oss, String bucketName) {
        AliyunOss aliyunOss;
        // 进入对应bucket
        if (!operationUtils.hasKey(Constant.ALIYUN_OSS)) {
            List<AliyunOss> init = init();
            aliyunOss = init.stream().filter(l -> l.getBucketName().equals(bucketName)).toList().get(0);
        } else {
            aliyunOss = (AliyunOss) operationUtils.hGet(Constant.ALIYUN_OSS, bucketName);
        }
        oss.setBucketName(aliyunOss.getBucketName());
        oss.setEndpoint(aliyunOss.getDomainName());
        return oss;
    }
}




