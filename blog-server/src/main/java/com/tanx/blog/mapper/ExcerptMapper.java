package com.tanx.blog.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tanx.blog.entity.po.Excerpt;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tanx.blog.entity.vo.ExcerptVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 谭翔
* @description 针对表【b_excerpt(优美语句摘抄表)】的数据库操作Mapper
* @createDate 2024-04-27 13:54:35
* @Entity com.tanx.blog.entity.po.Excerpt
*/

@Mapper
public interface ExcerptMapper extends BaseMapper<Excerpt> {

    List<ExcerptVo> selectListGroup();

    List<ExcerptVo> selectListGroupLikeId(@Param("id") int id);
}




