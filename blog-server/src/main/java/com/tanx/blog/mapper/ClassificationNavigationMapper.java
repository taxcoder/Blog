package com.tanx.blog.mapper;

import com.tanx.blog.entity.po.ClassificationNavigation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 谭翔
* @description 针对表【d_classification_navigation(存储导航分类信息)】的数据库操作Mapper
* @createDate 2024-04-21 11:37:30
* @Entity com.tanx.blog.entity.po.ClassificationNavigation
*/

@Mapper
public interface ClassificationNavigationMapper extends BaseMapper<ClassificationNavigation> {

}




