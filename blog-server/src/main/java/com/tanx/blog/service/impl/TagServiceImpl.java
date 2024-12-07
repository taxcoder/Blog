package com.tanx.blog.service.impl;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tanx.blog.annotation.RedisArticleAnnotation;
import com.tanx.blog.constant.Constant;
import com.tanx.blog.entity.dto.TagDto;
import com.tanx.blog.entity.po.ArticleTag;
import com.tanx.blog.entity.po.Tag;
import com.tanx.blog.entity.vo.TagVo;
import com.tanx.blog.exception.DataOperationErrorException;
import com.tanx.blog.mapper.ArticleTagMapper;
import com.tanx.blog.mapper.TagMapper;
import com.tanx.blog.service.TagService;
import com.tanx.blog.utils.FunctionUtils;
import com.tanx.blog.utils.PageUtils;
import com.tanx.blog.utils.RedisOperationUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 谭翔
 * @description 针对表【b_tag( 标签表 )】的数据库操作Service实现
 * @createDate 2023-07-13 15:23:48
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>
        implements TagService {

    @Resource
    private TagMapper tagMapper;

    @Resource
    private ArticleTagMapper articleTagMapper;

    @Resource
    private RedisOperationUtils operationUtils;

    @Override
    public List<TagDto> tagList() {
        if (operationUtils.hasKey(Constant.TAG_LIST)) {
            return (List<TagDto>) operationUtils.lRange(Constant.TAG_LIST, 0, -1);
        }
        return tagMapper.tagsListDto();
    }

    @Override
    public PageUtils<TagDto> tagListLimit(int current, int size) {
        PageUtils<TagDto> utils = new PageUtils<>(current, size);
        long data = utils.getCurrent() * utils.getSize();
        if (operationUtils.hasKey(Constant.TAG_LIST)) {
            utils.setRecords((List<TagDto>) operationUtils.lRange(Constant.TAG_LIST, data, data + size - 1));
            utils.setTotal(operationUtils.lLen(Constant.TAG_LIST));
            return utils;
        }

        List<TagDto> tagDtoList = tagMapper.tagsListLimitDto();
        utils.setRecords(tagDtoList.stream().skip(data).limit(utils.getSize()).toList());
        utils.setTotal(tagDtoList.size());
        operationUtils.lRightPushAll(Constant.TAG_LIST, tagDtoList);
        return utils;
    }

    @Override
    public List<Map<String, String>> selectTagArticleById(int id) {
        return tagMapper.tagArticles(id);
    }

    @Override
    @RedisArticleAnnotation
    @Transactional(rollbackFor = {Exception.class, DataOperationErrorException.class})
    public String updateTagById(int id, String name) {
        Tag selectById = tagMapper.selectById(id);
        if(selectById == null) {
            throw new DataOperationErrorException("数据异常！");
        }
        if(selectById.getName().equals("默认") || selectById.getId() == 1) {
            throw new DataOperationErrorException("改标签不允许修改！");
        }
        if(tagMapper.selectCount(new QueryWrapper<Tag>().eq("name", name)) != 0) {
            throw new DataOperationErrorException("标签名已存在！");
        }
        selectById.setName(name);
        if(tagMapper.updateById(selectById) != 1) {
            throw new DataOperationErrorException("标签名更新失败！");
        }
        return "更新成功！";
    }

    @Override
    @RedisArticleAnnotation
    @Transactional(rollbackFor = {Exception.class, DataOperationErrorException.class})
    public String deleteTagById(int id) {
        List<ArticleTag> articleTags = articleTagMapper.selectList(new QueryWrapper<ArticleTag>().eq("tag_id", id));
        // 关联的文章不是只对应一个标签时
        List<Integer> quite = new ArrayList<>();
        // 关联的文章只对应一个标签时
        List<Integer> only = new ArrayList<>();
        //  在删除标签的时候，如果删除标签对应的文章只关联当前的标签，则将其默认关联的标签换成默认
        articleTags.forEach(tag -> FunctionUtils
                .isTrueOrFalse(articleTagMapper.selectCount(new QueryWrapper<ArticleTag>().eq("article_id", tag.getArticleId())) == 1)
                .trueOrFalseHandle(() -> only.add(tag.getId()), () -> quite.add(tag.getId()))
        );
        UpdateWrapper<ArticleTag> wrapper = new UpdateWrapper<ArticleTag>().in("article_id", only).set("tag_id", 1);

        FunctionUtils.isError(only.size() > 0 && articleTagMapper.update(null, wrapper) != only.size()).throwFunction("关联更新失败！");
        FunctionUtils.isError(quite.size() > 0 && articleTagMapper.deleteBatchIds(quite) != quite.size()).throwFunction("关联删除失败！");
        FunctionUtils.isError(!tagMapper.selectById(id).getIsRemove()).throwFunction("该标签不允许删除！");
        FunctionUtils.isError(tagMapper.deleteById(id) == 0).throwFunction("标签删除失败！");
        return "删除成功！";
    }

    @Override
    public PageUtils<TagDto> selectSearchDataById(int searchValue, int current, int size) {
        PageUtils<TagDto> utils = new PageUtils<>(current, size);
        List<TagDto> tagDtoList = tagMapper.searchById(searchValue);
        utils.setRecords(tagDtoList.stream().skip(utils.getCurrent() * utils.getSize()).limit(utils.getSize()).toList());
        utils.setTotal(tagDtoList.size());
        return utils;
    }

    @Override
    public PageUtils<TagDto> selectSearchDataByArticleCountEqualOrGreaterThan(int searchValue, int current, int size) {
        PageUtils<TagDto> utils = new PageUtils<>(current, size);
        List<TagDto> tagDtoList = tagMapper.searchByArticleCountEqualOrGreaterThan(searchValue);
        utils.setRecords(tagDtoList.stream().skip(utils.getCurrent() * utils.getSize()).limit(utils.getSize()).toList());
        utils.setTotal(tagDtoList.size());
        return utils;
    }

    @Override
    public PageUtils<TagDto> selectSearchDataByArticleCountEqualOrLessThan(int searchValue, int current, int size) {
        PageUtils<TagDto> utils = new PageUtils<>(current, size);
        List<TagDto> tagDtoList = tagMapper.searchByArticleCountEqualOrLessThan(searchValue);
        utils.setRecords(tagDtoList.stream().skip(utils.getCurrent() * utils.getSize()).limit(utils.getSize()).toList());
        utils.setTotal(tagDtoList.size());
        return utils;
    }

    @Override
    public PageUtils<TagDto> selectSearchDataByArticleCountEqual(int searchValue, int current, int size) {
        PageUtils<TagDto> utils = new PageUtils<>(current, size);
        List<TagDto> tagDtoList = tagMapper.searchByArticleCountEqual(searchValue);
        utils.setRecords(tagDtoList.stream().skip(utils.getCurrent() * utils.getSize()).limit(utils.getSize()).toList());
        utils.setTotal(tagDtoList.size());
        return utils;
    }

    @Override
    public List<TagDto> addTags(TagVo tagVo) {

        if (tagVo.getName().size() > 3) {
            throw new DataOperationErrorException("请传入正确的参数");
        }

        // 1. 查询携带的数据是否再数据库内已存在
        List<Tag> tags = tagMapper.selectList(new QueryWrapper<Tag>().in("name", tagVo.getName()));
        if (tags.size() > 0) {
            throw new DataOperationErrorException(tags.get(0).getName() + ",已存在！");
        }

        // 2. 创建一个集合，用于存储需要返回的数据的集合
        List<TagDto> tagDtoList = new ArrayList<>(tagVo.getName().size());

        // 3.执行添加操作
        tagVo.getName().forEach(name -> {

            if (StrUtil.trim(name).length() > 20) {
                // 3.1 如果添加失败，则直接抛出异常
                throw new DataOperationErrorException("添加失败`" + StrUtil.trim(name) + "`长度超出20个字符！");
            }

            if (tagMapper.insert(new Tag(name, true)) == 0) {
                // 3.2 如果添加失败，则直接抛出异常
                throw new DataOperationErrorException("标签添加失败！");
            } else {
                // 3.3 添加完成之后，通过标签名在去查询一次，如果没有查询到，或者是返回数据不止一条，则表示携带的数据，有存在多个相同的名称，抛出异常
                List<Tag> tagList = tagMapper.selectByMap(new HashMap<>() {{
                    put("name", name);
                }});
                if (tagList.size() != 1) {
                    throw new DataOperationErrorException("标签添加失败！");
                }
                // 3.4 将获取的数据传入集合内，用于返回
                TagDto tagDto = new TagDto();
                tagDto.setId(tagList.get(0).getId());
                tagDto.setName(tagList.get(0).getName());
                tagDtoList.add(tagDto);
            }
        });

        operationUtils.delete(Constant.TAG_LIST);
        return tagDtoList;
    }

    @Override
    public long allCount() {
        if (operationUtils.hasKey(Constant.TAG_LIST)) {
            return operationUtils.lLen(Constant.TAG_LIST);
        }
        return tagMapper.selectCount(new QueryWrapper<>());
    }

    @Override
    public PageUtils<TagDto> selectTagByName(String searchValue, long current, long size) {
        PageUtils<TagDto> utils = new PageUtils<>(current * size, size);
        List<TagDto> tagDtoList = tagMapper.selectTagName(searchValue);
        utils.setRecords(tagDtoList.stream().skip(current * size).limit(size).toList());
        utils.setTotal(tagDtoList.size());
        return utils;
    }

    @Override
    public PageUtils<TagDto> selectSearchDataLikeName(String searchValue, int current, int size) {
        PageUtils<TagDto> utils = new PageUtils<>(current, size);
        List<TagDto> tagDtoList = tagMapper.searchLikeName(searchValue);
        utils.setRecords(tagDtoList.stream().skip(utils.getCurrent() * utils.getSize()).limit(utils.getSize()).toList());
        utils.setTotal(tagDtoList.size());
        return utils;
    }

    @Override
    public PageUtils<TagDto> selectTagSearchData(String value, long current, int size) {
        PageUtils<TagDto> utils = new PageUtils<>(current, size);
        List<TagDto> tagDtoList = tagMapper.searchByName(value);
        utils.setTotal(tagDtoList.size());
        utils.setRecords(tagDtoList.stream().skip(utils.getCurrent() * utils.getSize()).limit(utils.getSize()).toList());
        return utils;
    }
}




