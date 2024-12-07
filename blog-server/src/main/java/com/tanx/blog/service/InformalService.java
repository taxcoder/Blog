package com.tanx.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tanx.blog.entity.po.Informal;
import com.tanx.blog.entity.vo.AddEssayVo;
import com.tanx.blog.utils.PageUtils;

import java.util.HashMap;
import java.util.List;

/**
 * @author 谭翔
 * @description 针对表【b_informal( 随笔表 )】的数据库操作Service
 * @createDate 2023-09-02 19:23:38
 */
public interface InformalService extends IService<Informal> {

    /**
     * 随笔分页
     *
     * @param current  当前页
     * @param size     每页条数
     * @param recovery 是否处于回收站
     * @return 返回执行的结果
     */
    PageUtils<Informal> selectInformalList(int current, int size, boolean recovery);

    /**
     * 根据ID为该随笔点赞
     *
     * @param id 随笔ID
     * @return 返回执行的结果
     */
    int addLike(int id);

    /**
     * 将随笔还原，从回收站内去除
     *
     * @param id 随笔ID
     * @return 返回执行的结果
     */
    String restore(int id);

    /**
     * 根据ID将随笔存入回收站
     *
     * @param id 随笔ID
     * @return 返回执行的结果
     */
    String removeEssayById(int id);

    /**
     * 根据ID将随笔彻底删除
     *
     * @param id 随笔ID
     * @return 返回执行的结果
     */
    String realRemoveEssayById(int id);

    /**
     * 根据随笔ID查询对应的随笔信息
     *
     * @param id 随笔ID
     * @return 返回执行的结果
     */
    Informal selectInformalById(int id);

    /**
     * 根据ID修改随笔内容
     *
     * @param content 随笔内容
     * @param id      随笔ID
     * @return 返回执行的结果
     */
    String updateContentById(String content, int id);

    /**
     * 增加一条随笔
     *
     * @param addEssayVo 随笔信息
     * @param map        天气信息
     * @return 返回添加的结果
     */
    String addInformal(AddEssayVo addEssayVo, HashMap<String, Integer> map, String province);

    /**
     * 根据随笔ID获取对应数据，大于等于
     *
     * @param searchValue 查询条件
     * @param current     当前页
     * @param size        每页条数
     * @param recovery    是否处于回收站
     * @return 返回查询到的数据
     */
    PageUtils<Informal> selectSearchDataById(int searchValue, int current, int size, boolean recovery);

    /**
     * 根据随笔内容查询对应数据，大于等于
     *
     * @param searchValue 查询条件
     * @param current     当前页
     * @param size        每页条数
     * @param recovery    是否处于回收站
     * @return 返回查询到的数据
     */
    PageUtils<Informal> selectSearchDataLikeContent(String searchValue, int current, int size, boolean recovery);

    /**
     * 根据查询条件获取对应数据，大于等于
     *
     * @param searchValue 查询条件
     * @param current     当前页
     * @param size        每页条数
     * @param recovery    是否处于回收站
     * @return 返回查询到的数据
     */
    PageUtils<Informal> selectSearchDataByLikeCountEqualOrGreaterThan(int searchValue, int current, int size, boolean recovery);

    /**
     * 根据查询条件获取对应数据，小于等于
     *
     * @param searchValue 查询条件
     * @param current     当前页
     * @param size        每页条数
     * @param recovery    是否处于回收站
     * @return 返回查询到的数据
     */
    PageUtils<Informal> selectSearchDataByLikeCountEqualOrLessThan(int searchValue, int current, int size, boolean recovery);

    /**
     * 根据查询条件获取对应数据，等于
     *
     * @param searchValue 查询条件
     * @param current     当前页
     * @param size        每页条数
     * @param recovery    是否处于回收站
     * @return 返回查询到的数据
     */
    PageUtils<Informal> selectSearchDataByLikeCountEqual(int searchValue, int current, int size, boolean recovery);

    PageUtils<Informal> selectSearchDataByProvince(String searchValue, int current, Integer size, boolean recovery);

    List<? extends HashMap<String, ?>> selectProvinceCount(boolean recovery);

    long allCount();
}
