package com.tanx.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tanx.blog.entity.dto.ClassificationArticleDto;
import com.tanx.blog.entity.dto.ClassificationDto;
import com.tanx.blog.entity.po.Classification;
import com.tanx.blog.entity.vo.LinkDataVo;
import com.tanx.blog.utils.PageUtils;

import java.util.HashMap;
import java.util.List;

/**
 * @author 谭翔
 * @description 针对表【b_classification( 分类表 )】的数据库操作Service
 * @createDate 2023-07-13 15:22:35
 */
public interface ClassificationService extends IService<Classification> {

    /**
     * 获取分类ID获取分类信息
     * @param id 分类ID
     * @return 返回分类信息
     */
    ClassificationDto findById(int id);

    /**
     * 获取分类列表
     * @param current 当前页
     * @param size 每页显示数量
     * @return 返回分类列表
     */
    PageUtils<ClassificationArticleDto> selectClassificationLimit(long current,long size);

    /**
     * 删除分类
     * @param id 分类ID
     * @param maps 分类文章数量
     * @return 返回删除结果
     */
    String classificationDeleteById(int id, List<HashMap<String, Integer>> maps);

    String updateClassificationInfoById(int id, String name,List<LinkDataVo> list);

    PageUtils<ClassificationArticleDto> selectSearchDataById(int searchValue, long current, long size);

    PageUtils<ClassificationArticleDto> selectSearchDataByArticleCountEqualOrGreaterThan(int searchValue, long current, long size);

    PageUtils<ClassificationArticleDto> selectSearchDataByArticleCountEqualOrLessThan(int searchValue, long current, long size);

    PageUtils<ClassificationArticleDto> selectSearchDataByArticleCountEqual(int searchValue, long current, long size);

    PageUtils<ClassificationArticleDto> selectSearchDataLikeName(String searchValue, long current, long size);

    List<ClassificationDto> selectAll();

    ClassificationDto classificationAdd(String name);


}
