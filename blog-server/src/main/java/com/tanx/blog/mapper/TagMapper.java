package com.tanx.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tanx.blog.entity.dto.TagDto;
import com.tanx.blog.entity.po.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @author 谭翔aliyun_oss
 * @description 针对表【b_tag( 标签表 )】的数据库操作Mapper
 * @createDate 2023-07-13 15:23:48
 * @Entity com.tanx.blog.entity.po.Tag
 */

@Mapper
public interface TagMapper extends BaseMapper<Tag> {

    List<TagDto> tagsListDto();

    List<TagDto> tagsListLimitDto();

    @Select({"select ba.id,title from b_tag bt,r_article_tag rat,b_article ba where bt.id = rat.tag_id and ba.id = rat.article_id and bt.id = #{id} and ba.recovery = false"})
    List<Map<String, String>> tagArticles(int id);

    @Select({"""
            select b_tag.*, IFNULL(b.articleCount, 0) as articleCount
            from b_tag
            left join
            (select bt.id, bt.name, bt.is_remove, bt.create_time, bt.update_time, count(bt.id) as articleCount
            from b_tag bt,
            r_article_tag rat,
            b_article ba
            where bt.id = rat.tag_id
            and ba.id = rat.article_id
            and ba.recovery = false
            group by bt.id, bt.name, bt.is_remove, bt.create_time, bt.update_time) as b
            on b_tag.id = b.id
            where IFNULL(b.articleCount, 0) >= #{searchValue}
            """})
    List<TagDto> searchByArticleCountEqualOrGreaterThan(int searchValue);

    @Select({"""
            select b_tag.*, IFNULL(b.articleCount, 0) as articleCount
            from b_tag
            left join
            (select bt.id, bt.name, bt.is_remove, bt.create_time, bt.update_time, count(bt.id) as articleCount
            from b_tag bt,
            r_article_tag rat,
            b_article ba
            where bt.id = rat.tag_id
            and ba.id = rat.article_id
            and ba.recovery = false
            group by bt.id, bt.name, bt.is_remove, bt.create_time, bt.update_time) as b
            on b_tag.id = b.id
            where IFNULL(b.articleCount, 0) <= #{searchValue}
            """})
    List<TagDto> searchByArticleCountEqualOrLessThan(int searchValue);

    @Select({"""
             select b_tag.*, IFNULL(b.articleCount, 0) as articleCount
             from b_tag
             left join
             (select bt.id, bt.name, bt.is_remove, bt.create_time, bt.update_time, count(bt.id) as articleCount
             from b_tag bt,
             r_article_tag rat,
             b_article ba
             where bt.id = rat.tag_id
             and ba.id = rat.article_id
             and ba.recovery = false
             group by bt.id, bt.name, bt.is_remove, bt.create_time, bt.update_time) as b
             on b_tag.id = b.id
             where IFNULL(b.articleCount, 0) = #{searchValue}
             """})
    List<TagDto> searchByArticleCountEqual(int searchValue);

    @Select({"""
            select b_tag.*, IFNULL(b.articleCount, 0) as articleCount
            from b_tag
            left join
            (select bt.id, bt.name, bt.is_remove, bt.create_time, bt.update_time, count(bt.id) as articleCount
            from b_tag bt,
            r_article_tag rat,
            b_article ba
            where bt.id = rat.tag_id
            and ba.id = rat.article_id
            and ba.recovery = false
            group by bt.id, bt.name, bt.is_remove, bt.create_time, bt.update_time) as b
            on b_tag.id = b.id
            where LOCATE(#{searchValue}, b_tag.name) > 0
            """})
    List<TagDto> searchLikeName(String searchValue);

    @Select({"""
           select b_tag.*, IFNULL(b.articleCount, 0) as articleCount
            from b_tag
            left join
            (select bt.id, bt.name, bt.is_remove, bt.create_time, bt.update_time, count(bt.id) as articleCount
            from b_tag bt,
            r_article_tag rat,
            b_article ba
            where bt.id = rat.tag_id
            and ba.id = rat.article_id
            and ba.recovery = false
            group by bt.id, bt.name, bt.is_remove, bt.create_time, bt.update_time) as b
            on b_tag.id = b.id
            where LOCATE(#{searchValue}, b_tag.id) > 0
             """})
    List<TagDto> searchById(int searchValue);

    @Select({"""
              select b_tag.*, IFNULL(b.articleCount, 0) as articleCount
              from b_tag
              left join
              (select bt.id, bt.name, bt.is_remove, bt.create_time, bt.update_time, count(bt.id) as articleCount
              from b_tag bt,
              r_article_tag rat,
              b_article ba
              where bt.id = rat.tag_id
              and ba.id = rat.article_id
              and ba.recovery = false
              group by bt.id, bt.name, bt.is_remove, bt.create_time, bt.update_time) as b
              on b_tag.id = b.id
              where LOCATE('Mi', b_tag.name)
              """})
    List<TagDto> selectTagName(String searchValue);

    List<TagDto> searchByName(String value);
}




