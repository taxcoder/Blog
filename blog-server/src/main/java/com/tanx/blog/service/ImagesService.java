package com.tanx.blog.service;

import com.tanx.blog.entity.common.OssEntity;
import com.tanx.blog.entity.po.Images;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tanx.blog.utils.PageUtils;
import org.springframework.security.core.parameters.P;

import java.sql.Timestamp;
import java.util.List;

/**
* @author 谭翔
* @description 针对表【b_images(记录生活照片)】的数据库操作Service
* @createDate 2024-04-18 10:43:54
*/
public interface ImagesService extends IService<Images> {

    String addImages(OssEntity oss, List<Images> images);

    String addImage(String url,Timestamp timestamp);

    PageUtils<Images> images(int current, int size);

    List<Images> imageList();

    String delImages(long id);

    PageUtils<Images> selectImages(long id,int current, int size);

    PageUtils<Images> selectImagesByTime(long start, long end, int current, int size);
}
