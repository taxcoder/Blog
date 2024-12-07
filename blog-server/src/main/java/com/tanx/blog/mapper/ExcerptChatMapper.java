package com.tanx.blog.mapper;

import com.tanx.blog.entity.po.ExcerptChat;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 谭翔
* @description 针对表【r_excerpt_chat(摘抄链接聊天表)】的数据库操作Mapper
* @createDate 2024-04-27 16:34:37
* @Entity com.tanx.blog.entity.po.ExcerptChat
*/

@Mapper
public interface ExcerptChatMapper extends BaseMapper<ExcerptChat> {

}




