package com.tanx.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tanx.blog.entity.po.ArticleTag;
import com.tanx.blog.mapper.ArticleTagMapper;
import com.tanx.blog.service.ArticleTagService;
import org.springframework.stereotype.Service;

/**
 * @author 谭翔
 * @description 针对表【r_article_tag( 文章表和标签表的中间表 )】的数据库操作Service实现
 * @createDate 2023-07-13 15:30:28
 */
@Service
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag>
        implements ArticleTagService {

}




