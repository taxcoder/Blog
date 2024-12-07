package com.tanx.blog.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tanx.blog.api.OssOperationApi;
import com.tanx.blog.constant.Constant;
import com.tanx.blog.entity.common.OssEntity;
import com.tanx.blog.entity.po.Images;
import com.tanx.blog.exception.DataOperationErrorException;
import com.tanx.blog.mapper.ImagesMapper;
import com.tanx.blog.service.ImagesService;
import com.tanx.blog.utils.CommonUtils;
import com.tanx.blog.utils.PageUtils;
import com.tanx.blog.utils.RedisOperationUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.List;

/**
 * @author 谭翔
 * @description 针对表【b_images(记录生活照片)】的数据库操作Service实现
 * @createDate 2024-04-18 10:43:54
 */
@Slf4j
@Service
public class ImagesServiceImpl extends ServiceImpl<ImagesMapper, Images>
        implements ImagesService {

    @Resource
    private ImagesMapper imagesMapper;
    @Resource
    private RedisOperationUtils operationUtils;
    @Resource
    private OssEntity oss;

    @Override
    @Transactional(rollbackFor = DataOperationErrorException.class)
    public String addImages(OssEntity oss, List<Images> images) {
        if (images == null) throw new DataOperationErrorException("上传失败！");
        for (var image : images) {
            if (imagesMapper.insert(image) != 1) {
                OssOperationApi instance = OssOperationApi.getInstance();
                instance.deleteFiles(oss, "/atlas/" + CommonUtils.currentDate() + "/");
                throw new DataOperationErrorException("上传失败！");
            }
        }
        clear();
        return "上传成功!";
    }

    @Override
    public String addImage(String url, Timestamp timestamp) {
        if (imagesMapper.insert(new Images(url, timestamp)) != 1) {
            throw new DataOperationErrorException("上传失败！");
        }
        clear();
        return "上传成功！";
    }

    @Override
    public PageUtils<Images> images(int current, int size) {
        PageUtils<Images> utils = new PageUtils<>(current - 1, size);
        if (operationUtils.hasKey(Constant.UPLOAD_IMAGE)) {
            utils.setRecords((List<Images>) operationUtils.lRange(Constant.UPLOAD_IMAGE, utils.getCurrent() * utils.getSize(), utils.getCurrent() * utils.getSize() + utils.getSize() - 1));
            utils.setTotal(operationUtils.lLen(Constant.UPLOAD_IMAGE));
        } else {
            List<Images> images = imagesMapper.selectList(new QueryWrapper<>());
            List<Images> list = images.stream().sorted(Comparator.comparingLong(o -> o.getUploadTime().getTime())).toList();
            operationUtils.lRightPushAll(Constant.UPLOAD_IMAGE, list);
            utils.setRecords(list.stream().skip(utils.getCurrent() * utils.getSize()).limit(utils.getSize()).toList());
            utils.setTotal(list.size());
        }
        return utils;
    }

    public List<Images> imageList() {
        if (operationUtils.hasKey(Constant.UPLOAD_IMAGE)) {
            return (List<Images>) operationUtils.lRange(Constant.UPLOAD_IMAGE, 0, -1);
        }
        List<Images> images = imagesMapper.selectList(new QueryWrapper<>());
        List<Images> list = images.stream().sorted(Comparator.comparingLong(o -> o.getUploadTime().getTime())).toList();
        operationUtils.lRightPushAll(Constant.UPLOAD_IMAGE, list);
        return list;
    }

    @Override
    public String delImages(long id) {
        if (imagesMapper.deleteById(id) != 1) {
            throw new DataOperationErrorException("删除失败！");
        }
        clear();
        return "删除成功！";
    }

    @Override
    public PageUtils<Images> selectImages(long id, int current, int size) {
        PageUtils<Images> utils = new PageUtils<>(current, size);
        List<Images> images;
        if (operationUtils.hasKey(Constant.UPLOAD_IMAGE)) {
            List<Images> list = (List<Images>) operationUtils.lRange(Constant.UPLOAD_IMAGE, 0, -1);
            images = list.stream().filter(l -> l.getId().toString().matches(".*" + id + ".*")).toList();
        } else {
            images = imagesMapper.selectVagueById("%" + id + "%");
        }
        utils.setSize(images.size());
        utils.setRecords(images.stream().skip(utils.getCurrent() * utils.getSize()).limit(utils.getSize()).toList());
        return utils;
    }

    @Override
    public PageUtils<Images> selectImagesByTime(long start, long end, int current, int size) {
        PageUtils<Images> utils = new PageUtils<>(current, size);
        List<Images> images;
        if (operationUtils.hasKey(Constant.UPLOAD_IMAGE)) {
            List<Images> list = (List<Images>) operationUtils.lRange(Constant.UPLOAD_IMAGE, 0, -1);
            images = list.stream().filter(l -> l.getUploadTime().getTime() >= start && l.getUploadTime().getTime() <= end).sorted(Comparator.comparingLong(o -> o.getUploadTime().getTime())).toList();
        } else {
            images = imagesMapper.selectVagueByTime(DateUtil.date(start).toString("yyyy-MM-dd HH:mm:ss"), DateUtil.date(end).toString("yyyy-MM-dd HH:mm:ss"));
        }
        utils.setTotal(images.size());
        utils.setRecords(images.stream().skip(utils.getCurrent() * utils.getSize()).limit(utils.getSize()).toList());
        return utils;
    }

    public void clear() {
        if (operationUtils.hasKey(Constant.UPLOAD_IMAGE)) operationUtils.delete(Constant.UPLOAD_IMAGE);
    }
}




