package com.tanx.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tanx.blog.entity.dto.ArticleDto;
import com.tanx.blog.entity.po.Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.security.core.parameters.P;

import java.util.List;

/**
 * @author 谭翔
 * @description 针对表【b_article( 文章表 )】的数据库操作Mapper
 * @createDate 2023-07-13 15:23:28
 * @Entity com.tanx.blog.entity.po.Article
 */

@Mapper
public interface ArticleMapper extends BaseMapper<Article> {

    List<ArticleDto> limitArticleDto(@Param("recovery") boolean recovery);

    List<ArticleDto> limitArchivedDto();

    ArticleDto selectArticleById(@Param("id") int id);

    List<ArticleDto> tagArticleList(@Param("id") int id);

    List<ArticleDto> limitClassificationArticle(@Param("id") int id);

    List<ArticleDto> searchDataById(int searchValue, long current, long size, boolean recovery);

    List<ArticleDto> searchDataLikeTitle(String searchValue, long current, long size, boolean recovery);

    List<ArticleDto> searchDataByLikeCountEqual(int searchValue, long current, long size, boolean recovery);

    List<ArticleDto> searchDataByLikeCountEqualOrLessThan(int searchValue, long current, long size, boolean recovery);

    List<ArticleDto> searchDataByLikeCountEqualOrGreaterThan(int searchValue, long current, long size, boolean recovery);

    List<ArticleDto> searchDataLikeClassificationId(String searchValue, long current, long size, boolean recovery);

    List<ArticleDto> searchDataLikeClassificationName(String searchValue, long current, long size, boolean recovery);

    @Select("""
            select count(ba.id)
            from b_article ba,
                 b_classification bc
            where ba.classification_id = bc.id and  ba.recovery = #{recovery}
              and LOCATE(#{searchValue}, ba.classification_id) > 0
            """)
    long searchDataLikeClassificationIdCount(String searchValue, boolean recovery);

    @Select("""
            select count(ba.id)
            from b_article ba,
                 b_classification bc
            where ba.classification_id = bc.id and ba.recovery = #{recovery}
              and LOCATE(#{searchValue}, bc.name) > 0
            """)
    long searchDataLikeClassificationNameCount(String searchValue, boolean recovery);

    List<ArticleDto> searchDataPosition(String searchValue, long current, long size, boolean recovery);

    List<ArticleDto> searchDataByTagName(String searchValue, boolean recovery);

    List<ArticleDto> searchDataByTagId(String searchValue, boolean recovery);

    List<ArticleDto> selectArticle(String searchKey, long current, long size);

    long selectArticleTotal(String searchKey);

    List<ArticleDto> selectArticleCompositeSearch(String content,String tagName, String classificationName,long current, long size);

    long selectArticleCompositeTotal(String content,String tagName,String classificationName);

    List<ArticleDto> searchDataList(String value, int sort, int time);

}




