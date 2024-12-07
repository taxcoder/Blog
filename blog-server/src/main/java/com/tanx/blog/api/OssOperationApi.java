package com.tanx.blog.api;

import com.aliyun.oss.*;
import com.aliyun.oss.internal.OSSHeaders;
import com.aliyun.oss.model.*;
import com.tanx.blog.entity.common.OssEntity;
import com.tanx.blog.exception.DataOperationErrorException;
import com.tanx.blog.service.LoggerService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author tanxiang
 * @date 2022/06/23 16:00
 */
@Slf4j
public class OssOperationApi {

    private static volatile OssOperationApi ossOperationApi;

    public static OssOperationApi getInstance() {
        if (ossOperationApi == null) {
            synchronized (OssOperationApi.class) {
                if (ossOperationApi == null) {
                    ossOperationApi = new OssOperationApi();
                }
            }
        }
        return ossOperationApi;
    }

    /**
     * 下载oss上面的markdown文件
     *
     * @param fileName 文件名称
     */
    public String downloadMarkDown(String fileName, OssEntity oss) {
        // 如果自己传入了bucket，使用自己的
        String condition = oss.getEndpoint() + "/";
        log.info("condition:{}", condition);
        log.info("fileName:{}", fileName);
        String name = UriUtils.decode(fileName.substring(fileName.indexOf(condition) + condition.length()), StandardCharsets.UTF_8);
        // 创建OSSClient实例。
        OSS ossClient = getInstance().ossClient(oss);
        log.info("开始下载文件:{}", name);
        OSSObject ossObject = ossClient.getObject(oss.getBucketName(), name);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(ossObject.getObjectContent()))) {
            // 如果读取错误，该对象不会创建
            StringBuilder buffer = new StringBuilder();
            while (true) {
                String line = reader.readLine();
                if (line == null) break;
                String newLine = line;
                if (line.contains(":**")) newLine = line.replaceAll(":\\*\\*", ":**&nbsp;");
                if (line.contains("：**")) newLine = line.replaceAll("：\\*\\*", "：**&nbsp;");
                buffer.append(newLine).append(System.getProperty("line.separator"));
            }
            return buffer.toString();
        } catch (OSSException | IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            // 关闭OSS连接
            ossClient.shutdown();
        }
    }

    /**
     * 上传文件，默认是字符串
     *
     * @param fileRoute 文件路径
     * @param oss       oss对象
     * @return 返回上传之后的路径
     */
    public String uploadContent(String fileRoute, String content, OssEntity oss) {
        return uploadContent(fileRoute, content.getBytes(), oss, CannedAccessControlList.Private);
    }

    /**
     * 上传文件，默认是字符串
     *
     * @param fileRoute 文件路径
     * @param oss       oss对象
     * @return 返回上传之后的路径
     */
    public String uploadContent(String fileRoute, String content, OssEntity oss, String bucket) {
        return uploadContent(fileRoute, content.getBytes(), oss, CannedAccessControlList.Private);
    }

    /**
     * 上传文件，默认是字符串
     *
     * @param fileRoute  文件路径
     * @param oss        oss对象
     * @param permission 访问权限
     * @return 返回上传之后的路径
     */
    public String uploadContent(String fileRoute, String content, OssEntity oss, CannedAccessControlList permission) {
        return uploadContent(fileRoute, content.getBytes(), oss, permission);
    }

    /**
     * 上传文件，传入字节数组
     *
     * @param fileRoute  文件路径
     * @param oss        oss对象
     * @param permission 访问权限
     * @return 返回上传之后的路径
     */
    public String uploadContent(String fileRoute, byte[] content, OssEntity oss, CannedAccessControlList permission) {
        // 创建OSSClient实例。
        return upload(fileRoute, content, oss, permission);
    }

    /**
     * 上传图片，传入字节数组
     *
     * @param fileRoute 文件路径
     * @param oss       oss对象
     * @return 返回上传之后的路径
     */
    public String uploadImage(String fileRoute, byte[] content, OssEntity oss) {
        try {
            // 图片上传
            return upload(fileRoute, content, oss, CannedAccessControlList.PublicRead);
        } catch (Exception e) {
            log.error("uploadImage --> error:{}", e.getMessage());
            throw new DataOperationErrorException("图片上传失败！");
        }
    }

    public void deleteMenu(OssEntity oss, String parent) {
        OSS ossClient = ossClient(oss);
        ObjectListing objectListing;
        String nextMarker = null;
        do {
            ListObjectsRequest listObjectsRequest = new ListObjectsRequest(oss.getBucketName()).withPrefix(parent).withMarker(nextMarker);
            objectListing = ossClient.listObjects(listObjectsRequest);
            if (objectListing.getObjectSummaries().size() > 0) {
                List<String> keys = new ArrayList<>();
                objectListing.getObjectSummaries().forEach(s -> keys.add(s.getKey()));
                DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(oss.getBucketName()).withKeys(keys).withEncodingType("url");
                ossClient.deleteObjects(deleteObjectsRequest);
            }
            nextMarker = objectListing.getNextMarker();
        } while (objectListing.isTruncated());
    }


    public void deleteFile(OssEntity oss, String objectName) {
        OSS ossClient = ossClient(oss);
        // 判断文件是否存在，存在则删除
        if (fileIsEmpty(ossClient, oss.getBucketName(), objectName)) {
            ossClient.deleteObject(oss.getBucketName(), objectName);
        }
    }


    // 删除目录
    public void deleteFiles(OssEntity oss, String parent) {
        String nextMarker = null;
        ObjectListing objectListing;
        OSS ossClient = ossClient(oss);
        do {
            ListObjectsRequest listObjectsRequest = new ListObjectsRequest(oss.getBucketName()).withPrefix(parent).withMarker(nextMarker);
            objectListing = ossClient.listObjects(listObjectsRequest);
            if (objectListing.getObjectSummaries().size() > 0) {
                List<String> keys = new ArrayList<>();
                objectListing.getObjectSummaries().forEach(s -> keys.add(s.getKey()));
                DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(oss.getBucketName()).withKeys(keys).withEncodingType("url");
                DeleteObjectsResult deleteObjectsResult = ossClient.deleteObjects(deleteObjectsRequest);
                List<String> deletedObjects = deleteObjectsResult.getDeletedObjects();
                deletedObjects.forEach(obj -> URLDecoder.decode(obj, StandardCharsets.UTF_8));
            }
            nextMarker = objectListing.getNextMarker();
        } while (objectListing.isTruncated());
    }

    private boolean fileIsEmpty(OSS ossClient, String bucketName, String objectName) {
        return ossClient.doesObjectExist(bucketName, objectName);
    }

    /**
     * 上传核心代码
     *
     * @param fileRoute  文件路径
     * @param content    上传的信息
     * @param oss        oss对象
     * @param permission 指定文件的访问权限
     * @return 返回上传之后的路径
     */
    private String upload(String fileRoute, byte[] content, OssEntity oss, CannedAccessControlList permission) {
        String bucket = oss.getBucketName();
        OSS ossClient = null;
        try {
            ossClient = ossClient(oss);
            ObjectMetadata meta = new ObjectMetadata();
            ByteArrayInputStream stream = new ByteArrayInputStream(content);
            // 设置为存储类型为标准
            meta.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard.toString());
            // 设置访问权限为公共
            meta.setObjectAcl(permission);
            // 设置上传的大小
            meta.setContentLength(content.length);
            // 设置文件在浏览器打开的状态
            meta.setContentDisposition("inline");
            ossClient.putObject(new PutObjectRequest(bucket, fileRoute, stream, meta));
            return oss.getEndpoint() + "/" + fileRoute;
        } catch (OSSException | ClientException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (ossClient != null) ossClient.shutdown();
        }
    }

    public OSS ossClient(OssEntity oss) {
        String endpoint = oss.getEndpoint();
        String accessKeyId = oss.getAccessKeyId();
        String accessKeySecret = oss.getAccessKeySecret();
        // 绑定了自定义域名必须加上
        // 创建ClientBuilderConfiguration实例，您可以根据实际情况修改默认参数。
        ClientBuilderConfiguration conf = new ClientBuilderConfiguration();
        // 设置是否支持CNAME。CNAME用于将自定义域名绑定到目标Bucket。
        conf.setSupportCname(true);
        return new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret, conf);
    }
}

