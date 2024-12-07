package com.tanx.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tanx.blog.entity.po.Emoji;
import com.tanx.blog.entity.po.Images;
import com.tanx.blog.utils.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * @author 谭翔
 * @description 针对表【d_emoji( emoji表情 )】的数据库操作Service
 * @createDate 2023-09-23 16:18:58
 */
public interface EmojiService extends IService<Emoji> {

    /**
     * 查询所有表情包
     * @return 返回查询结果
     */
    PageUtils<Map<String, ?>> selectEmojiAll();

    /**
     *  删除表情包
     * @param id 表情ID
     * @return 返回删除的结果
     */
    String deleteEmoji(long id);

    /**
     * 更新表情包组名
     *
     * @param id 表情ID
     * @param name 分组名
     * @return 返回修改的结果
     * @author 谭翔
     * @date 2023/09/23 16:20
     */
    String updateEmojiGroupNameById(long id, String name);

    /**
     * 更新表情包名称
     * @param id 表情ID
     * @param name 表情名称
     * @return 返回修改的结果
     * @author 谭翔
     *  @date 2023/09/23 16:20
     */
    String updateEmojiNameById(long id, String name);

}
