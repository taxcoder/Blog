package com.tanx.blog.service;

import com.tanx.blog.entity.po.ClassificationNavigation;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tanx.blog.entity.vo.ClassificationNavigationVo;
import com.tanx.blog.entity.vo.LinkDataVo;
import com.tanx.blog.utils.PageUtils;

import java.util.List;

/**
* @author 谭翔
* @description 针对表【d_classification_navigation(存储导航分类信息)】的数据库操作Service
* @createDate 2024-04-21 11:37:30
*/
public interface ClassificationNavigationService extends IService<ClassificationNavigation> {

    String add(String name);

    String deleteById(long id);

    String deleteByIdAndCloseLink(long id, List<LinkDataVo> voList);

    String updateInfoById(long id, String name, List<LinkDataVo> list);

    PageUtils<ClassificationNavigationVo> select(int current, int size);

    PageUtils<ClassificationNavigationVo> selectById(int current, int size,long id);

    PageUtils<ClassificationNavigationVo> selectByName(int current, int size,String name);

    PageUtils<ClassificationNavigationVo> selectSearchDataByLinkCountEqualOrGreaterThan(int searchValue, int current, int size);

    PageUtils<ClassificationNavigationVo> selectSearchDataByLinkCountEqualOrLessThan(int searchValue, int current, int size);

    PageUtils<ClassificationNavigationVo> selectSearchDataByLinkCountEqual(int searchValue, int current ,int size);
}
