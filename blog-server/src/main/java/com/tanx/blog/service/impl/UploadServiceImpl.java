package com.tanx.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tanx.blog.annotation.DeleteEmojiAnnotation;
import com.tanx.blog.api.OssOperationApi;
import com.tanx.blog.constant.Constant;
import com.tanx.blog.entity.common.OssEntity;
import com.tanx.blog.entity.po.Emoji;
import com.tanx.blog.entity.po.Images;
import com.tanx.blog.entity.po.WebStation;
import com.tanx.blog.exception.DataOperationErrorException;
import com.tanx.blog.exception.TokenVerifyErrorException;
import com.tanx.blog.mapper.WebStationMapper;
import com.tanx.blog.service.AliyunOssService;
import com.tanx.blog.service.EmojiService;
import com.tanx.blog.service.ImagesService;
import com.tanx.blog.service.UploadService;
import com.tanx.blog.utils.CommonUtils;
import com.tanx.blog.utils.RedisOperationUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @description:
 * @project_name: blog-server
 * @author: 谭翔
 * @date: 2023/9/28 17:26
 */

@Slf4j
@Service
public class UploadServiceImpl implements UploadService {

    private static final List<String> LIST = Arrays.asList("image/png", "image/jpeg", "image/jpg", "image/webp");
    @Resource
    private WebStationMapper webStationMapper;
    @Resource
    private RedisOperationUtils operationUtils;
    @Resource
    private ThreadPoolExecutor threadPoolExecutor;
    @Resource
    private EmojiService emojiService;
    @Resource
    private AliyunOssService aliyunOssService;

    @Override
    public String uploadHead(MultipartFile file, OssEntity oss) {
        String prefix = "profilePhoto/";
        OssEntity ossEntity = aliyunOssService.updateOss(oss, "tx-blog");
        WebStation webStation = isFileEmpty(file);
        // 删除文件
        String image = webStation.getHeadIcon().split(prefix)[1];
        log.info("image:{}", prefix + image);
        OssOperationApi.getInstance().deleteFile(oss, prefix + image);
        // 如果传递的元素为null或者是传递的blob元素的大小为0，则表示改blob对象有问题
        try {
            // 文件路径
            String fileName = prefix + "head" + System.currentTimeMillis() + "." + "webp";
            String fileRoute = OssOperationApi.getInstance().uploadImage(fileName, CommonUtils.saveImage(file), ossEntity);
            webStation.setHeadIcon(fileRoute);
            if (fileRoute == null || webStationMapper.updateById(webStation) != 1) {
                throw new DataOperationErrorException("上传失败！");
            }
            operationUtils.set(Constant.WEB_KEY, webStation);
            return fileRoute;
        } catch (IOException e) {
            throw new DataOperationErrorException("上传失败！");
        }
    }

    @Override
    public String uploadBackground(MultipartFile file, OssEntity oss) {
        String prefix = "background/";
        OssEntity ossEntity = aliyunOssService.updateOss(oss, "tx-blog");
        WebStation webStation = isFileEmpty(file);
        // 删除文件
        String image = webStation.getBackgroundImage().split(prefix)[1];
        log.info("image:{}", prefix + image);
        OssOperationApi.getInstance().deleteFile(oss, prefix + image);
        // 如果传递的元素为null或者是传递的blob元素的大小为0，则表示改blob对象有问题
        try {
            // 文件路径
            String fileName = prefix + "background" + System.currentTimeMillis() + "." + "webp";
            String fileRoute = OssOperationApi.getInstance().uploadImage(fileName, CommonUtils.saveImage(file), ossEntity);
            log.info("fileRoute:{}", fileRoute);
            webStation.setBackgroundImage(fileRoute);
            if (webStationMapper.updateById(webStation) != 1) {
                throw new DataOperationErrorException("上传失败！");
            }
            operationUtils.set(Constant.WEB_KEY, webStation);
            return fileRoute;
        } catch (IOException e) {
            throw new DataOperationErrorException("上传失败！");
        }
    }

    private WebStation isFileEmpty(MultipartFile file) {
        if (file.getSize() == 0) {
            throw new DataOperationErrorException("请选择图片！");
        }
        String type = file.getContentType();
        if (type == null || !LIST.contains(type)) {
            throw new DataOperationErrorException("参数错误！");
        }

        List<WebStation> webStations = webStationMapper.selectList(new QueryWrapper<>());
        if (webStations.size() != 1) {
            throw new TokenVerifyErrorException();
        }
        return webStations.get(0);
    }

    @Override
    @DeleteEmojiAnnotation
    public Map<String, Object> uploadEmoji(MultipartFile[] files, OssEntity oss, String groupName, String[] names) {
        Map<String, Object> dataInfo = new HashMap<>((int) Math.ceil(files.length + files.length * 0.75));
        OssEntity ossEntity = aliyunOssService.updateOss(oss, "tx-blog");
        OssOperationApi instance = OssOperationApi.getInstance();
        List<Future<Map<String, String>>> list = new ArrayList<>(files.length);
        for (int i = 0; i < files.length; i++) {
            int finalI = i;
            Future<Map<String, String>> submit = threadPoolExecutor.submit(() -> {
                String router = "emoji/emoji-" + System.currentTimeMillis() + "." + "webp";
                String image = instance.uploadImage(router, CommonUtils.saveImage(files[finalI]), ossEntity);
                Map<String, String> map = new HashMap<>();
                map.put("name", names[finalI]);
                map.put("icon", image);
                return map;
            });
            list.add(submit);
        }
        // 存储上传失败的图片信息
        List<String> error = new ArrayList<>(list.size());
        // 存储上传成功的图片信息
        List<Emoji> success = new ArrayList<>(list.size());
        list.forEach(l -> {
            try {
                Map<String, String> info = l.get();
                if (info.get("icon") == null) error.add(info.get("name"));
                else {
                    Emoji emoji = new Emoji();
                    emoji.setName(info.get("name"));
                    emoji.setIcon(info.get("icon"));
                    emoji.setGroupName(groupName);
                    success.add(emoji);
                }
            } catch (InterruptedException | ExecutionException e) {
                throw new DataOperationErrorException("上传错误！");
            }
        });
        //  存储上传结果
        boolean status = emojiService.saveBatch(success);
        dataInfo.put("code", status ? 200 : 500);
        dataInfo.put("data", status ? error : success.stream().map(Emoji::getIcon));
        return dataInfo;
    }


    @Override
    public List<Images> uploadAtlasImages(MultipartFile[] files, OssEntity oss, long timestamp) {
        OssEntity ossEntity = aliyunOssService.updateOss(oss, "tx-blog");
        OssOperationApi instance = OssOperationApi.getInstance();
        List<Images> images = new ArrayList<>();
        List<Future<Boolean>> list = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            int finalI = i;
            list.add(threadPoolExecutor.submit(() -> {
                try {
                    String router = "atlas/" + CommonUtils.currentDate() + "/atlas-" + System.currentTimeMillis() + "." + "webp";
                    String url = instance.uploadImage(router, CommonUtils.saveImage(files[finalI]), ossEntity);
                    if (url != null) images.add(new Images(url, new Timestamp(timestamp)));
                    return true;
                } catch (IOException e) {
                    return false;
                }
            }));
        }

        List<Boolean> bool = list.stream().map(l -> {
            try {
                return l.get();
            } catch (InterruptedException | ExecutionException e) {
                return false;
            }
        }).filter(f -> !f).toList();
        log.info("images:{}", images);
        //  如果存在上传失败的图片，则直接删除今天已经上传的图片
        if (bool.size() > 0) {
            instance.deleteFiles(ossEntity, "/atlas/" +  CommonUtils.currentDate() + "/");
            return null;
        } else {
            return images;
        }
    }

    @Override
    public String uploadOnceImage(MultipartFile file, OssEntity oss, String newBucketName, String prefix) {
        try {
            OssEntity ossEntity = aliyunOssService.updateOss(oss, newBucketName);
            OssOperationApi instance = OssOperationApi.getInstance();
            String router = prefix + System.currentTimeMillis() + "." + "webp";
            return instance.uploadImage(router, CommonUtils.saveImage(file), ossEntity);
        } catch (IOException e) {
            throw new DataOperationErrorException("上传失败！");
        }
    }
}
