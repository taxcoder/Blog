package com.tanx.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tanx.blog.entity.dto.TagDto;
import com.tanx.blog.entity.po.Tag;
import com.tanx.blog.entity.vo.TagVo;
import com.tanx.blog.utils.PageUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 谭翔
 * @description 针对表【b_tag( 标签表 )】的数据库操作Service
 * @createDate 2023-07-13 15:23:48
 */
public interface TagService extends IService<Tag> {


    List<TagDto> tagList();

    PageUtils<TagDto> tagListLimit(int current, int size);

    List<Map<String, String>> selectTagArticleById(int id);

    String updateTagById(int id, String name);

    String deleteTagById(int id);

    PageUtils<TagDto> selectSearchDataById(int searchValue, int current, int size);

    PageUtils<TagDto> selectSearchDataLikeName(String searchValue, int current, int size);

    PageUtils<TagDto> selectSearchDataByArticleCountEqualOrGreaterThan(int searchValue, int current, int size);

    PageUtils<TagDto> selectSearchDataByArticleCountEqualOrLessThan(int searchValue, int current, int size);

    PageUtils<TagDto> selectSearchDataByArticleCountEqual(int searchValue, int current, int size);

    List<TagDto> addTags(TagVo tagVo);

    long allCount();

    PageUtils<TagDto> selectTagByName(String searchValue, long current, long size);

    PageUtils<TagDto> selectTagSearchData(String value, long current, int size);
}
