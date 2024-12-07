package com.tanx.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tanx.blog.annotation.RedisArticleAnnotation;
import com.tanx.blog.constant.Constant;
import com.tanx.blog.entity.dto.ClassificationArticleDto;
import com.tanx.blog.entity.dto.ClassificationDto;
import com.tanx.blog.entity.po.Article;
import com.tanx.blog.entity.po.Classification;
import com.tanx.blog.entity.vo.LinkDataVo;
import com.tanx.blog.exception.DataOperationErrorException;
import com.tanx.blog.mapper.ArticleMapper;
import com.tanx.blog.mapper.ClassificationMapper;
import com.tanx.blog.service.ClassificationService;
import com.tanx.blog.utils.PageUtils;
import com.tanx.blog.utils.RedisOperationUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author 谭翔
 * @description 针对表【b_classification( 分类表 )】的数据库操作Service实现
 * @createDate 2023-07-13 15:22:35
 */
@Service
public class ClassificationServiceImpl extends ServiceImpl<ClassificationMapper, Classification>
        implements ClassificationService {

    @Resource
    private ClassificationMapper classificationMapper;

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private RedisOperationUtils operationUtils;

    public ClassificationDto findById(int id) {
        if (operationUtils.hasKey(Constant.CLASSIFICATION_LIST)) {
            List<ClassificationArticleDto> dtos = (List<ClassificationArticleDto>) operationUtils.lRange(Constant.CLASSIFICATION_LIST, 0, -1);
            List<ClassificationArticleDto> list = dtos.stream().filter(dto -> dto.getId() == id).toList();
            if (list.size() != 1) {
                operationUtils.delete(Constant.CLASSIFICATION_LIST);
            } else {
                return new ClassificationDto(list.get(0));
            }

        }
        return new ClassificationDto(classificationMapper.selectOne(new QueryWrapper<Classification>().eq("id", id)));
    }

    @Override
    public PageUtils<ClassificationArticleDto> selectClassificationLimit(long current, long size) {
        PageUtils<ClassificationArticleDto> utils = new PageUtils<>(current, size);

        if (operationUtils.hasKey(Constant.CLASSIFICATION_LIST)) {
            List<ClassificationArticleDto> list = (List<ClassificationArticleDto>) operationUtils.lRange(Constant.CLASSIFICATION_LIST, 0, -1);
            utils.setRecords(list);
            utils.setTotal(operationUtils.lLen(Constant.CLASSIFICATION_LIST));
            utils.setSize(utils.getTotal());
            return utils;
        }

        List<ClassificationArticleDto> classificationArticleDtos = classificationMapper.selectClassificationList();
        utils.setRecords(classificationArticleDtos);
        utils.setSize(classificationArticleDtos.size());

        if (utils.getRecords().size() == 0) {
            throw new DataOperationErrorException("分类获取失败！");
        }

        operationUtils.lRightPushAll(Constant.CLASSIFICATION_LIST, classificationArticleDtos);
        utils.setTotal(classificationArticleDtos.size());
        return utils;
    }

    @Override
    @RedisArticleAnnotation
    @Transactional(rollbackFor = {Exception.class, DataOperationErrorException.class})
    public String classificationDeleteById(int id, List<HashMap<String, Integer>> arrayList) {
        HashMap<Integer, ArrayList<Integer>> maps = new HashMap<>();
        // 遍历集合形成一个 map集合，key是分类ID，value是存储着文章的集合
        arrayList.forEach(list -> {
            Integer key = list.get("key");
            Integer articleId = list.get("articleId");
            // 如果传入的map集合不存在对应的key，则表示参数错误了
            if (key == null || articleId == null) throw new DataOperationErrorException("请传入正确的参数！");

            ArrayList<Integer> ret = maps.get(key);
            if (ret != null) {
                ret.add(articleId);
                return;
            }
            maps.put(key, new ArrayList<>() {{
                add(articleId);
            }});
        });

        // 获取map的所有key，进行遍历来更新数据
        maps.keySet().forEach(key -> {
            UpdateWrapper<Article> wrapper = new UpdateWrapper<>();
            wrapper.set("classification_id", key).in("id", maps.get(key));
            int update = articleMapper.update(null, wrapper);
            // 如果没有更新一条数据或者是更新的数据与传入数据数量不一致，则表示更新异常
            if (update == 0 || update != maps.get(key).size()) {
                throw new DataOperationErrorException("请传入正确的文章信息！");
            }
        });

        // 查询当前分类是否存在关联的文章
        QueryWrapper<Article> wrapper = new QueryWrapper<>();
        wrapper.eq("classification_id", id);
        // TODO: 如果文章进入了回收站，无视该文章，等到从回收站内恢复时，如果数据库内不存在该分类，在重新往数据库，添加该分类
        wrapper.eq("recovery", false);
        if (articleMapper.selectCount(wrapper) > 0) {
            throw new DataOperationErrorException("传入的文章信息不完全！");
        }

        Classification classification = classificationMapper.selectById(id);
        if (classification == null) {
            throw new DataOperationErrorException("请传入正确的ID！");
        }

        if (!classification.isRemove()) {
            throw new DataOperationErrorException("该分类不可删除！");
        }

        if (classificationMapper.deleteById(classification) != 1) {
            throw new DataOperationErrorException("分类ID：" + id + "，删除失败！");
        }
        return "删除成功！";
    }

    @Override
    @RedisArticleAnnotation
    @Transactional(rollbackFor = {DataOperationErrorException.class, Exception.class})
    public String updateClassificationInfoById(int id, String name, List<LinkDataVo> list) {
        Classification classification = classificationMapper.selectById(id);
        if (classification.getName().equals(name) && (list == null || list.size() == 0)) {
            throw new DataOperationErrorException("没有什么需要更新的！");
        }

        if (!classification.getName().equals(name)) {
            classification.setName(name);
            if (classificationMapper.updateById(classification) != 1) {
                throw new DataOperationErrorException("分类名修改失败,请检查是否符合规范！");
            }
        }

        if (list != null && list.size() != 0) {
            list.forEach(l -> {
                UpdateWrapper<Article> wrapper = new UpdateWrapper<>();
                wrapper.in("id", List.of(l.getDatas())).set("classification_id", l.getSelect());
                if (articleMapper.update(wrapper) != l.getDatas().length) {
                    throw new DataOperationErrorException("关系更新失败！");
                }
            });
        }

        return "更新成功！";
    }

    @Override
    public PageUtils<ClassificationArticleDto> selectSearchDataById(int searchValue, long current, long size) {
        PageUtils<ClassificationArticleDto> utils = new PageUtils<>(current, size);
        List<ClassificationArticleDto> classificationArticleDtos = classificationMapper.searchById(searchValue);
        utils.setRecords(classificationArticleDtos.stream().skip(current * size).limit(size).toList());
        utils.setTotal(classificationArticleDtos.size());
        return utils;
    }

    @Override
    public PageUtils<ClassificationArticleDto> selectSearchDataByArticleCountEqualOrGreaterThan(int searchValue, long current, long size) {
        PageUtils<ClassificationArticleDto> utils = new PageUtils<>(current, size);
        utils.setRecords(classificationMapper.searchByArticleCountEqualOrGreaterThan(searchValue, current * size, size));
        utils.setTotal(classificationMapper.selectSearchDataByArticleCountEqualOrGreaterThanCount(searchValue));
        return utils;
    }

    @Override
    public PageUtils<ClassificationArticleDto> selectSearchDataByArticleCountEqualOrLessThan(int searchValue, long current, long size) {
        PageUtils<ClassificationArticleDto> utils = new PageUtils<>(current, size);
        utils.setRecords(classificationMapper.searchByArticleCountEqualOrLessThan(searchValue, current * size, size));
        utils.setTotal(classificationMapper.selectSearchDataByArticleCountEqualOrLessThanCount(searchValue));
        return utils;
    }

    @Override
    public PageUtils<ClassificationArticleDto> selectSearchDataByArticleCountEqual(int searchValue, long current, long size) {
        PageUtils<ClassificationArticleDto> utils = new PageUtils<>(current, size);
        utils.setRecords(classificationMapper.searchByArticleCountEqual(searchValue, current * size, size));
        utils.setTotal(classificationMapper.selectSearchDataByArticleCountEqualCount(searchValue));
        return utils;
    }

    @Override
    public PageUtils<ClassificationArticleDto> selectSearchDataLikeName(String searchValue, long current, long size) {
        List<ClassificationArticleDto> classificationArticleDtos = classificationMapper.searchLikeName(searchValue);
        PageUtils<ClassificationArticleDto> utils = new PageUtils<>(current, size);
        utils.setRecords(classificationArticleDtos.stream().skip(current * size).limit(size).toList());
        utils.setTotal(classificationArticleDtos.size());
        return utils;
    }

    @Override
    public List<ClassificationDto> selectAll() {
        if (operationUtils.hasKey(Constant.CLASSIFICATION_LIST)) {
            return ((List<ClassificationArticleDto>) operationUtils.lRange(Constant.CLASSIFICATION_LIST, 0, -1)).stream().map(ClassificationDto::new).toList();
        }
        return classificationMapper.selectListAll();
    }

    @Override
    public ClassificationDto classificationAdd(String name) {
        Classification classification = new Classification();
        classification.setName(name);
        classification.setRemove(true);

        QueryWrapper<Classification> wrapper = new QueryWrapper<>();
        wrapper.eq("name", name);
        if (classificationMapper.selectCount(wrapper) > 0) {
            throw new DataOperationErrorException("分类已存在！");
        }

        if (classificationMapper.insert(classification) != 1) {
            throw new DataOperationErrorException("分类增加失败！");
        }

        operationUtils.delete(Constant.CLASSIFICATION_LIST);
        return new ClassificationDto(classification);
    }

}


