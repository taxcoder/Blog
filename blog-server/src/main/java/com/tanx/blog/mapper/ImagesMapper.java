package com.tanx.blog.mapper;

import com.tanx.blog.entity.po.Images;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 谭翔
* @description 针对表【b_images(记录生活照片)】的数据库操作Mapper
* @createDate 2024-04-18 10:43:54
* @Entity com.tanx.blog.entity.po.Images
*/

@Mapper
public interface ImagesMapper extends BaseMapper<Images> {
    List<Images> selectVagueById(@Param("id") String id);

    List<Images> selectVagueByTime(@Param("start") String start,@Param("end") String end);
}




