package com.tanx.blog.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tanx.blog.annotation.DeleteEmojiAnnotation;
import com.tanx.blog.constant.Constant;
import com.tanx.blog.entity.po.Emoji;
import com.tanx.blog.exception.DataOperationErrorException;
import com.tanx.blog.mapper.EmojiMapper;
import com.tanx.blog.service.EmojiService;
import com.tanx.blog.utils.PageUtils;
import com.tanx.blog.utils.RedisOperationUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author 谭翔
 * @description 针对表【d_emoji( emoji表情 )】的数据库操作Service实现
 * @createDate 2023-09-23 16:18:58
 */
@Service
public class EmojiServiceImpl extends ServiceImpl<EmojiMapper, Emoji>
        implements EmojiService {

    @Resource
    private EmojiMapper emojiMapper;

    @Resource
    private RedisOperationUtils operationUtils;

    @Override
    public PageUtils<Map<String, ?>> selectEmojiAll() {
        PageUtils<Map<String, ?>> utils = new PageUtils<>(0, Integer.MAX_VALUE);
        if (operationUtils.hasKey(Constant.EMOJI)) {
            Map<String, ?> map = JSON.parseObject((String) operationUtils.get(Constant.EMOJI), Map.class);
            List<?> values = Collections.singletonList(map.values());
            utils.setTotal(values.size());
            utils.setRecords(new ArrayList<>() {{
                add(map);
            }});
            return utils;
        }
        List<Emoji> emojis = list();
        utils.setTotal(emojis.size());
        // 存储的list的map，key是组名
        Map<String, List<Map<String, String>>> listMap = new HashMap<>();

        for (Emoji emoji : emojis) {
            listMap.computeIfAbsent(emoji.getGroupName(), k -> new ArrayList<>());

            List<Map<String, String>> mapList = listMap.get(emoji.getGroupName());
            mapList.add(new HashMap<>() {{
                put("id", emoji.getId().toString());
                put("icon", emoji.getIcon());
                put("address", emoji.getIcon());
                put("name", emoji.getName());
                put("createTime", String.valueOf(emoji.getCreateTime()));
            }});

            listMap.put(emoji.getGroupName(), mapList);
        }
        utils.setRecords(new ArrayList<>() {{
            add(listMap);
        }});
        operationUtils.set(Constant.EMOJI, JSON.toJSONString(listMap));
        return utils;
    }

    /**
     * 删除表情包
     *
     * @param id 表情包id
     * @return 是否删除成功
     */
    @Override
    @DeleteEmojiAnnotation
    public String deleteEmoji(long id) {
        if (emojiMapper.deleteById(id) != 1) {
            throw new DataOperationErrorException("删除失败！");
        }
        return "删除成功！";
    }

    @Override
    @DeleteEmojiAnnotation
    public String updateEmojiGroupNameById(long id, String name) {
        //  更新表情包组名
        UpdateWrapper<Emoji> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", id).set("group_name", name);
        if (emojiMapper.update(wrapper) != 1) {
            throw new DataOperationErrorException("更新失败！");
        }
        return "更新成功！";
    }

    @Override
    @DeleteEmojiAnnotation
    public String updateEmojiNameById(long id, String name) {
        //  更新表情包名称
        UpdateWrapper<Emoji> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", id).set("name", name);

        if (emojiMapper.update(wrapper) != 1) {
            throw new DataOperationErrorException("更新失败！");
        }
        return "更新成功！";
    }
}