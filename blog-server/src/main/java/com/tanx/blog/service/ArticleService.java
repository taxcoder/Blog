package com.tanx.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tanx.blog.entity.dto.ArticleDto;
import com.tanx.blog.entity.po.Article;
import com.tanx.blog.entity.vo.AddArticleVo;
import com.tanx.blog.utils.PageUtils;

import java.util.List;

/**
 * @author 谭翔
 * @description 针对表【b_article( 文章表 )】的数据库操作Service
 * @createDate 2023-07-13 15:23:28
 */

public interface ArticleService extends IService<Article> {

    /**
     * 分页搜索文章
     *
     * @param current 当前页
     * @param size    每页条数
     * @return 返回包含分页信息的文章集合
     */
    PageUtils<ArticleDto> selectArticleList(int current, int size, boolean recovery, boolean top);

    /**
     * 通过id获取文章
     *
     * @param id 文章ID
     * @return 返回查询到的数据
     */
    ArticleDto selectArticleById(int id);

    /**
     * 通过url获取文章对应的内容
     *
     * @param id 文章ID
     * @return 返回markdown文本
     */
    String selectArticleContentById(int id);

    /**
     * 获取所有未处于回收站的文章，组成档案
     *
     * @return 返回查询到的数据
     */
    List<ArticleDto> selectArticleArchivedList();

    /**
     * 根据标签ID查询文章和分类对应的数据
     *
     * @param current 当前页
     * @param size    每页条数
     * @param id      标签ID
     * @return 返回查询到的数据
     */
    PageUtils<ArticleDto> selectArticleTagListById(int current, int size, int id);

    /**
     * 根据分类ID查询文章和分类对应的数据
     *
     * @param current 当前页
     * @param size    每页条数
     * @param id      分类ID
     * @return 返回查询到的数据
     */
    PageUtils<ArticleDto> selectArticleClassificationListById(Integer current, Integer size, int id);


    /**
     * 更新文章置顶情况
     *
     * @param id 文章ID
     * @return 返回执行的情况
     */
    boolean updateArticleTop(int id);

    /**
     * 将文章放到回收站
     *
     * @param id 文章ID
     * @return 返回执行的情况
     */
    String articleDeleteById(int id);

    /**
     * 查询数据根据文章ID
     *
     * @param searchValue 查询条件
     * @param current     当前页
     * @param size        每页条数
     * @param flag        是否处于回收站
     * @return 返回查询的到的数据
     */
    PageUtils<ArticleDto> selectSearchDataById(int searchValue, long current, int size, boolean flag);

    /**
     * 查询数据根据文章标题
     *
     * @param searchValue 查询条件
     * @param current     当前页
     * @param size        每页条数
     * @param flag        是否处于回收站
     * @return 返回查询的到的数据
     */
    PageUtils<ArticleDto> selectSearchDataLikeTitle(String searchValue, long current, long size, boolean flag);

    /**
     * 查询数据根据分类
     *
     * @param searchValue 查询条件
     * @param current     当前页
     * @param size        每页条数
     * @param flag        是否处于回收站
     * @return 返回查询的到的数据
     */
    PageUtils<ArticleDto> selectSearchDataLikeClassification(String searchValue, long current, long size, boolean flag);

    /**
     * 查询数据根据上传时作者所处的位置
     *
     * @param searchValue 查询条件
     * @param current     当前页
     * @param size        每页条数
     * @param flag        是否处于回收站
     * @return 返回查询的到的数据
     */
    PageUtils<ArticleDto> selectSearchDataPosition(String searchValue, long current, long size, boolean flag);

    /**
     * 查询数据根据标签
     *
     * @param searchValue 查询条件
     * @param current     当前页
     * @param size        每页条数
     * @param flag        是否处于回收站
     * @return 返回查询的到的数据
     */
    PageUtils<ArticleDto> selectSearchDataTag(String searchValue, long current, long size, boolean flag);

    /**
     * 查询数据根据点赞数，大于等于时
     *
     * @param searchValue 查询条件
     * @param current     当前页
     * @param size        每页条数
     * @param flag        是否处于回收站
     * @return 返回查询的到的数据
     */
    PageUtils<ArticleDto> selectSearchDataByLikeCountEqualOrGreaterThan(int searchValue, long current, long size, boolean flag);

    /**
     * 查询数据根据点赞数，小于等于时
     *
     * @param searchValue 查询条件
     * @param current     当前页
     * @param size        每页条数
     * @param flag        是否处于回收站
     * @return 返回查询的到的数据
     */
    PageUtils<ArticleDto> selectSearchDataByLikeCountEqualOrLessThan(int searchValue, long current, long size, boolean flag);

    /**
     * 查询数据根据点赞数，等于时
     *
     * @param searchValue 查询条件
     * @param current     当前页
     * @param size        每页条数
     * @param flag        是否处于回收站
     * @return 返回查询的到的数据
     */
    PageUtils<ArticleDto> selectSearchDataByLikeCountEqual(int searchValue, long current, long size, boolean flag);

    /**
     * 更新文章信息
     *
     * @param id      文章ID
     * @param article 需要更新的内容
     * @return 返回更新的情况
     */
    String updateArticleInfo(int id, AddArticleVo article);

    /**
     * 增加文章
     *
     * @param article  文章信息
     * @param province 添加文章时作者所处的省份
     * @return 返回增加的ID
     */
    Integer addArticleInfo(AddArticleVo article, String province);

    /**
     * 还原文章
     *
     * @param id 文章ID
     * @return 返回还原的状态
     */
    String restore(int id);

    /**
     * 彻底删除文章
     *
     * @param id 文章ID
     * @return 返回删除的状态
     */
    String articleRealDeleteById(int id);

    long allCount();

    /**
     * 根据条件查询数据
     * @param searchKey 查询条件
     * @return 返回获取的数据
     */
    PageUtils<ArticleDto> selectSearchByContent(String searchKey, long current, long size);

    /**
     *  查询搜索数据
     * @param value 查询的数据
     * @param sort 排序
     * @param time 时间
     * @param current 当前页
     * @param size 每页条数
     * @return 返回分页的数据
     */
    PageUtils<ArticleDto> selectArticleSearchData(String value, int sort, int time, long current, int size);
}
