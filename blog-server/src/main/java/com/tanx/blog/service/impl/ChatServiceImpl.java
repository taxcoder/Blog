package com.tanx.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tanx.blog.annotation.DeleteExcerptAnnotation;
import com.tanx.blog.constant.Constant;
import com.tanx.blog.entity.po.Chat;
import com.tanx.blog.entity.po.Excerpt;
import com.tanx.blog.entity.po.ExcerptChat;
import com.tanx.blog.exception.DataOperationErrorException;
import com.tanx.blog.mapper.ChatMapper;
import com.tanx.blog.mapper.ExcerptChatMapper;
import com.tanx.blog.mapper.ExcerptMapper;
import com.tanx.blog.service.ChatService;
import com.tanx.blog.utils.RedisOperationUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 谭翔
 * @description 针对表【b_chat(聊天表)】的数据库操作Service实现
 * @createDate 2024-04-27 16:22:30
 */
@Slf4j
@Service
public class ChatServiceImpl extends ServiceImpl<ChatMapper, Chat>
        implements ChatService {

    @Resource
    private ChatMapper chatMapper;
    @Resource
    private ExcerptMapper excerptMapper;
    @Resource
    private ExcerptChatMapper excerptChatMapper;
    @Resource
    private RedisOperationUtils operationUtils;

    @Override
    @DeleteExcerptAnnotation
    @Transactional(rollbackFor = DataOperationErrorException.class)
    public Chat addChat(long id, String content, String author, String email, String webUrl, boolean status) {
        Excerpt excerpt = excerptMapper.selectById(id);
        if (excerpt == null) {
            throw new DataOperationErrorException("数据异常！");
        }

        Chat chat = new Chat();
        chat.setContent(content);
        chat.setStatus(status);
        chat.setEmail(email);
        chat.setAuthor(author);
        chat.setCreateTime(new Date());

        if (chatMapper.insert(chat) != 1) {
            throw new DataOperationErrorException("发布失败，请重新尝试！");
        }

        ExcerptChat excerptChat = new ExcerptChat();
        excerptChat.setChatId(chat.getId());
        excerptChat.setExcerptId(id);

        if (excerptChatMapper.insert(excerptChat) != 1) {
            throw new DataOperationErrorException("内容发布失败，请重新尝试！");
        }

        if (operationUtils.hExists(Constant.CHAT, String.valueOf(id)) &&
                operationUtils.hDelete(Constant.CHAT, String.valueOf(id)) != 1) {
            throw new DataOperationErrorException("缓存更新失败！");
        }

        return chatMapper.selectById(chat.getId());
    }

    @Override
    @DeleteExcerptAnnotation
    @Transactional(rollbackFor = DataOperationErrorException.class)
    public String deleteChat(long id) {
        QueryWrapper<ExcerptChat> wrapper = new QueryWrapper<>();
        wrapper.eq("chat_id", id);
        List<ExcerptChat> excerptChats = excerptChatMapper.selectList(wrapper);

        if (excerptChats == null || excerptChats.size() == 0) {
            throw new DataOperationErrorException("数据异常！");
        }

        if (excerptChatMapper.deleteBatchIds(excerptChats.stream().map(ExcerptChat::getId).toList()) == 0) {
            throw new DataOperationErrorException("无法与摘抄断开链接！");
        }

        if (chatMapper.deleteById(id) != 1) {
            throw new DataOperationErrorException("删除失败！");
        }

        if (operationUtils.hExists(Constant.CHAT, String.valueOf(excerptChats.get(0).getExcerptId())) &&
                operationUtils.hDelete(Constant.CHAT, String.valueOf(excerptChats.get(0).getExcerptId())) != 1) {
            throw new DataOperationErrorException("缓存更新失败！");
        }
        return "删除成功！";
    }

    @Override
    @DeleteExcerptAnnotation
    @Transactional(rollbackFor = {DataOperationErrorException.class, Exception.class})
    public String deleteChats(ArrayList<Long> ids) {
        QueryWrapper<ExcerptChat> wrapper = new QueryWrapper<ExcerptChat>().in("chat_id", ids);
        List<ExcerptChat> excerptChats = excerptChatMapper.selectList(wrapper);

        if (excerptChats == null || excerptChats.size() == 0) {
            throw new DataOperationErrorException("数据异常！");
        }

        if (excerptChats.size() != ids.size()) {
            throw new DataOperationErrorException("数据异常！");
        }

        if (excerptChatMapper.delete(wrapper) != ids.size()) {
            throw new DataOperationErrorException("关联删除失败！");
        }

        if (chatMapper.deleteBatchIds(ids) != ids.size()) {
            throw new DataOperationErrorException("信息删除失败！");
        }
        if (operationUtils.hExists(Constant.CHAT, String.valueOf(excerptChats.get(0).getExcerptId())) &&
                operationUtils.hDelete(Constant.CHAT, String.valueOf(excerptChats.get(0).getExcerptId())) != 1) {
            throw new DataOperationErrorException("缓存更新失败！");
        }
        return "删除成功！";
    }


    @Override
    public List<Chat> selectChat(long id) {
        log.info("id:{}", id);

        if (operationUtils.hExists(Constant.CHAT, String.valueOf(id))) {
            return (List<Chat>) operationUtils.hGet(Constant.CHAT, id);
        }

        Excerpt excerpt = excerptMapper.selectById(id);

        if (excerpt == null) {
            throw new DataOperationErrorException("聊天获取失败！");
        }

        QueryWrapper<ExcerptChat> wrapper = new QueryWrapper<>();
        wrapper.eq("excerpt_id", excerpt.getId());
        List<ExcerptChat> excerptChats = excerptChatMapper.selectList(wrapper);
        if (excerptChats == null || excerptChats.size() == 0) {
            return new ArrayList<>();
        }

        List<Long> list = excerptChats.stream().map(ExcerptChat::getChatId).toList();
        List<Chat> chats = chatMapper.selectBatchIds(list);
        operationUtils.hPut(Constant.CHAT, String.valueOf(id), chats);
        return chats;
    }
}




