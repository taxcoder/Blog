package com.tanx.blog.mapper;

import com.tanx.blog.entity.po.Target;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author 谭翔
* @description 针对表【b_target(目标表)】的数据库操作Mapper
* @createDate 2024-03-30 10:58:36
* @Entity com.tanx.blog.entity.po.Target
*/

@Mapper
public interface TargetMapper extends BaseMapper<Target> {

    @Select({"select id,content,is_success,year from blog.b_target where year = #{years}"})
    List<Target> selectTargets(@Param("years") int years);
}




