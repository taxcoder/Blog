package com.tanx.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tanx.blog.entity.po.PrintText;
import com.tanx.blog.entity.vo.PrintTextVo;

import java.util.List;

/**
 * @author 谭翔
 * @description 针对表【d_print_text( 文本打印表 )】的数据库操作Service
 * @createDate 2023-07-06 15:29:16
 */
public interface PrintTextService extends IService<PrintText> {

    List<String> findAll();

    String updatePrintText(List<PrintTextVo> voList);
}
