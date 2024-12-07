package com.tanx.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tanx.blog.entity.po.WebStation;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 谭翔
 * @description 针对表【s_web_station( 网站信息表 )】的数据库操作Mapper
 * @createDate 2023-07-06 15:29:16
 * @Entity com.tanx.blog.entity.po.WebStation
 */
@Mapper
public interface WebStationMapper extends BaseMapper<WebStation> {

}




