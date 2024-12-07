package com.tanx.blog.mapper;

import com.tanx.blog.entity.po.Logger;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 谭翔
* @description 针对表【s_logger】的数据库操作Mapper
* @createDate 2024-04-26 19:24:42
* @Entity com.tanx.blog.entity.po.Logger
*/

@Mapper
public interface LoggerMapper extends BaseMapper<Logger> {

}




