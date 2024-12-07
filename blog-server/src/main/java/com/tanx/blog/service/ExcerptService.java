package com.tanx.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tanx.blog.entity.po.Excerpt;
import com.tanx.blog.entity.vo.ExcerptVo;
import com.tanx.blog.utils.PageUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 谭翔
 * @description 针对表【b_excerpt(优美语句摘抄表)】的数据库操作Service
 * @createDate 2024-04-27 13:54:35
 */
public interface ExcerptService extends IService<Excerpt> {
    /**
     * 增加一条文本摘抄
     * @param content 摘抄内容
     * @return 返回添加的结果
     */
    String addExcerpt(String content);
    /**
     * 增加一条图片摘抄
     * @param file 摘抄内容
     * @return 返回添加的结果
     */
    String addExcerpt(MultipartFile file);

    /**
     * 删除一条摘抄
     * @param id 摘抄ID
     * @return 返回删除的结果
     */
    String deleteExcerpt(int id);

    /**
     * 更新一条摘抄
     * @param id 摘抄ID
     * @param content 文本内容
     * @return 返回更新的结果
     */
    String updateExcerpt(int id, String content);

    /**
     * 更新一条摘抄
     * @param id 摘抄ID
     * @param file 图片内容
     * @return 返回更新的结果
     */
    String updateExcerpt(int id, MultipartFile file);

    /**
     * 分页查询摘抄的内容
     * @param current 当前页
     * @param size 每页条数
     * @return 返回查询到的数据
     */
    PageUtils<ExcerptVo> selectList(int current, int size);

    /**
     * 根据ID模糊查询摘抄数据
     * @param id 摘抄ID
     * @param current 当前页
     * @param size 每页条数
     * @return 返回模糊查询到的数据
     */
    PageUtils<ExcerptVo> selectListById(int id, int current, int size);
}
