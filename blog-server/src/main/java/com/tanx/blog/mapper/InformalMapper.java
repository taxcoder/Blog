package com.tanx.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tanx.blog.entity.po.Informal;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

/**
 * @author 谭翔
 * @description 针对表【b_informal( 随笔表 )】的数据库操作Mapper
 * @createDate 2023-09-06 21:52:06
 * @Entity com.tanx.blog.entity.po.Informal
 */

@Mapper
public interface InformalMapper extends BaseMapper<Informal> {

    List<HashMap<String, ?>> selectByProvinceList(boolean recovery);
}




