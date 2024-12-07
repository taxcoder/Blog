package com.tanx.blog.service;

import com.tanx.blog.entity.common.OssEntity;
import com.tanx.blog.entity.po.Images;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * @description: 上传
 * @project_name: blog-server
 * @author: 谭翔
 * @date: 2023/9/28 17:26
 */
public interface UploadService {

    public String uploadHead(MultipartFile file, OssEntity oss);

    public String uploadBackground(MultipartFile file, OssEntity oss);

    Map<String,Object> uploadEmoji(MultipartFile[] files, OssEntity oss, String groupName,String[] names);

    List<Images> uploadAtlasImages(MultipartFile[] files, OssEntity oss, long timestamp);

    String uploadOnceImage(MultipartFile files, OssEntity oss, String newBucketName, String prefix);
}
