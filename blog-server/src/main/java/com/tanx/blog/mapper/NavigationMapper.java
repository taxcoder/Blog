package com.tanx.blog.mapper;

import com.tanx.blog.entity.po.Navigation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tanx.blog.entity.vo.NavigationVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author 谭翔
* @description 针对表【b_navigation(导航网站信息)】的数据库操作Mapper
* @createDate 2024-04-21 11:37:09
* @Entity com.tanx.blog.entity.po.Navigation
*/

@Mapper
public interface NavigationMapper extends BaseMapper<Navigation> {

    @Select("""
    select bn.*,dcn.name as classificationNavigationName
    from d_classification_navigation dcn, b_navigation bn
    where dcn.id = bn.classification_navigation_id
    """)
    List<NavigationVo> selectDataList();
}




