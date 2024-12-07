package com.tanx.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tanx.blog.entity.po.Emoji;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author 谭翔
 * @description 针对表【d_emoji( emoji表情 )】的数据库操作Mapper
 * @createDate 2023-09-23 16:18:58
 * @Entity com.tanx.blog.entity.po.Emoji
 */

@Mapper
public interface EmojiMapper extends BaseMapper<Emoji> {

}




