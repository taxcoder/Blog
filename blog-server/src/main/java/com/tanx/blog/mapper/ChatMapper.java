package com.tanx.blog.mapper;

import com.tanx.blog.entity.po.Chat;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 谭翔
* @description 针对表【b_chat(聊天表)】的数据库操作Mapper
* @createDate 2024-04-27 16:22:30
* @Entity com.tanx.blog.entity.po.Chat
*/

@Mapper
public interface ChatMapper extends BaseMapper<Chat> {

}




