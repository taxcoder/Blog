package com.tanx.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tanx.blog.entity.dto.ClassificationArticleDto;
import com.tanx.blog.entity.dto.ClassificationDto;
import com.tanx.blog.entity.po.Classification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author 谭翔
 * @description 针对表【b_classification( 分类表 )】的数据库操作Mapper
 * @createDate 2023-07-13 15:22:35
 * @Entity com.tanx.blog.entity.po.Classification
 */

@Mapper
public interface ClassificationMapper extends BaseMapper<Classification> {

    List<ClassificationArticleDto> selectClassificationList();

    List<ClassificationArticleDto> searchById(@Param("searchValue") int searchValue);

    List<ClassificationArticleDto> searchByArticleCountEqualOrGreaterThan(int searchValue, long current, long size);

    List<ClassificationArticleDto> searchByArticleCountEqualOrLessThan(int searchValue, long current, long size);

    List<ClassificationArticleDto> searchByArticleCountEqual(int searchValue, long current, long size);

    List<ClassificationArticleDto> searchLikeName(String searchValue);

    @Select({"""
            select count(id)
            from (select IF(ISNULL(ANY_VALUE(ba.id)), 0, count(bc.id)) as count,
                         bc.id
                  from b_classification bc
                           left join b_article ba on ba.classification_id = bc.id
                  group by bc.id) as b
            where count >= #{searchValue}
            """})
    long selectSearchDataByArticleCountEqualOrGreaterThanCount(int searchValue);

    @Select({"""
            select count(id)
            from (select IF(ISNULL(ANY_VALUE(ba.id)), 0, count(bc.id)) as count,
                         bc.id
                  from b_classification bc
                           left join b_article ba on ba.classification_id = bc.id
                  group by bc.id) as b
            where count <= #{searchValue}
            """})
    long selectSearchDataByArticleCountEqualOrLessThanCount(int searchValue);

    @Select({"""
            select count(id)
            from (select IF(ISNULL(ANY_VALUE(ba.id)), 0, count(bc.id)) as count,
                         bc.id
                  from b_classification bc
                           left join b_article ba on ba.classification_id = bc.id
                  group by bc.id) as b
            where count = #{searchValue}
            """})
    long selectSearchDataByArticleCountEqualCount(int searchValue);

    List<ClassificationDto> selectListAll();
}




