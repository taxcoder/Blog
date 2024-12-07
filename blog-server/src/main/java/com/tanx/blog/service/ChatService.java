package com.tanx.blog.service;

import com.tanx.blog.entity.po.Chat;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.ArrayList;
import java.util.List;

/**
* @author 谭翔
* @description 针对表【b_chat(聊天表)】的数据库操作Service
* @createDate 2024-04-27 16:22:30
*/
public interface ChatService extends IService<Chat> {
    /**
     * 添加一条聊天记录
     * @param id 摘抄ID
     * @param content 聊天的内容
     * @return 返回添加的结果
     */
    Chat addChat(long id, String content, String author, String email, String webUrl, boolean status);

    /**
     * 删除一条聊天的内容
     * @param id 聊天ID
     * @return 返回删除的结果
     */
    String deleteChat(long id);

    /**
     * 获取对应摘抄下面的聊天内容
     * @param id 摘抄ID
     * @return 返回查询到的数据
     */
    List<Chat> selectChat(long id);

    /**
     * 删除多个聊天
     * @param ids 信息ID
     * @return 返回删除的结果
     */
    String deleteChats(ArrayList<Long> ids);
}
