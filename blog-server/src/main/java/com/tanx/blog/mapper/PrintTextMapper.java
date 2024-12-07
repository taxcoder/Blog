package com.tanx.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tanx.blog.entity.po.PrintText;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 谭翔
 * @description 针对表【d_print_text( 文本打印表 )】的数据库操作Mapper
 * @createDate 2023-07-06 15:29:16
 * @Entity com.tanx.blog.entity.po.PrintText
 */
@Mapper
public interface PrintTextMapper extends BaseMapper<PrintText> {

}




