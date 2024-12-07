package com.tanx.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tanx.blog.entity.po.ArticleTag;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 谭翔
 * @description 针对表【r_article_tag( 文章表和标签表的中间表 )】的数据库操作Mapper
 * @createDate 2023-07-13 15:30:28
 * @Entity com.tanx.blog.entity.po.ArticleTag
 */
@Mapper
public interface ArticleTagMapper extends BaseMapper<ArticleTag> {

}




