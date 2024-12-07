package com.tanx.blog.service.impl;

import cn.hutool.core.util.StrUtil;
import com.aliyun.oss.OSS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tanx.blog.annotation.AddArticleLookCountAnnotation;
import com.tanx.blog.annotation.AddTextCountAnnotation;
import com.tanx.blog.annotation.RedisArticleAnnotation;
import com.tanx.blog.api.OssOperationApi;
import com.tanx.blog.constant.Constant;
import com.tanx.blog.entity.common.OssEntity;
import com.tanx.blog.entity.dto.ArticleDto;
import com.tanx.blog.entity.po.Article;
import com.tanx.blog.entity.po.ArticleTag;
import com.tanx.blog.entity.po.Tag;
import com.tanx.blog.entity.vo.AddArticleVo;
import com.tanx.blog.exception.DataOperationErrorException;
import com.tanx.blog.mapper.ArticleMapper;
import com.tanx.blog.mapper.ArticleTagMapper;
import com.tanx.blog.mapper.ClassificationMapper;
import com.tanx.blog.mapper.TagMapper;
import com.tanx.blog.service.AliyunOssService;
import com.tanx.blog.service.ArticleService;
import com.tanx.blog.service.ArticleTagService;
import com.tanx.blog.utils.CommonUtils;
import com.tanx.blog.utils.FunctionUtils;
import com.tanx.blog.utils.PageUtils;
import com.tanx.blog.utils.RedisOperationUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


/**
 * @author 谭翔
 * @description 针对表【b_article( 文章表 )】的数据库操作Service实现
 * @createDate 2023-07-13 15:23:28
 */
@Slf4j
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article>
        implements ArticleService {

    @Resource
    private ArticleTagService articleTagService;
    @Resource
    private OssEntity oss;
    @Resource
    private ArticleMapper articleMapper;
    @Resource
    private ClassificationMapper classificationMapper;
    @Resource
    private ArticleTagMapper articleTagMapper;
    @Resource
    private TagMapper tagMapper;
    @Resource
    private RedisOperationUtils operationUtils;
    @Resource
    private AliyunOssService aliyunOssService;

    private List<ArticleDto> sort(List<ArticleDto> dtos) {
        return dtos.stream().sorted(Comparator.comparing(ArticleDto::isTop, Comparator.reverseOrder()).thenComparing(ArticleDto::getCreateTime, Comparator.reverseOrder())).toList();
    }

    private List<ArticleDto> sort(List<ArticleDto> dtos, boolean top) {
        return top ? sort(dtos).stream().peek(articleDto -> articleDto.setContentUrl(null)).toList() : dtos.stream().sorted(Comparator.comparing(ArticleDto::getCreateTime).reversed()).toList();
    }

    private PageUtils<ArticleDto> getArticleDtoPageUtils(long current, int size, PageUtils<ArticleDto> pageUtils, String articleTag, List<ArticleDto> sortArrayList) {
        return FunctionUtils.isTrueOrNull(sortArrayList.size() > 0, pageUtils).trueHandle(handle -> {
            pageUtils.setRecords(sortArrayList.stream().skip(current * size).limit(size).toList());
            pageUtils.setTotal(sortArrayList.size());
            operationUtils.lRightPushAll(articleTag, sortArrayList);
            return handle;
        });
    }

    @Override
    public PageUtils<ArticleDto> selectArticleList(int current, int size, boolean recovery, boolean top) {
        log.info("current:{},size:{},recovery:{},top:{}", current, size, recovery, top);
        PageUtils<ArticleDto> pageUtils = new PageUtils<>(current, size);
        String key = StrUtil.format(Constant.ARTICLE_DTO_LIST, recovery);
        return FunctionUtils.isTrueOrFalse(operationUtils.hasKey(key), pageUtils).trueOrFalseHandle(trueHandle -> {
            List<ArticleDto> list = sort((List<ArticleDto>) operationUtils.hValues(key), top);
            if (Math.ceil((double) list.size() / size) < current + 1) {
                pageUtils.setRecords(list.stream().skip(0).limit(size).toList());
            } else {
                pageUtils.setRecords(list.stream().skip((long) current * size).limit(size).toList());
            }
            pageUtils.setTotal(list.size());
            return pageUtils;
        }, falseHandle -> {
            List<ArticleDto> dtos = articleMapper.limitArticleDto(recovery);
            List<ArticleDto> list = sort(dtos, top);
            if (Math.ceil((double) list.size() / size) < current + 1) {
                // 将数据库的内容的指定部分切割
                pageUtils.setRecords(list.stream().skip(0).limit(size).toList());
            } else {
                // 将数据库的内容的指定部分切割
                pageUtils.setRecords(list.stream().skip((long) current * size).limit(size).toList());
            }

            // 将查询到的数据的个数存入total内
            pageUtils.setTotal(list.size());
            operationUtils.hPutAll(key, dtos.stream().collect(Collectors.toMap(ArticleDto -> String.valueOf(ArticleDto.getId()), ArticleDto -> ArticleDto)));
            return pageUtils;
        });
    }

    @Override
    public ArticleDto selectArticleById(int id) {
        String key = StrUtil.format(Constant.ARTICLE_DTO_LIST, Boolean.FALSE);
        if (operationUtils.hasKey(key)) {
            List<ArticleDto> collect = sort((List<ArticleDto>) operationUtils.hValues(key)).stream().filter(l -> l.getId() == id).toList();
            if (collect.size() == 1) return collect.get(0);
            // 如果过滤后的数据不等于1，则表示数据肯定是有问题的，直接删除数据，从数据库内获取
            operationUtils.delete(key);
        }

        return FunctionUtils.isTrue(articleMapper.selectArticleById(id)).trueOrFalseHandle("数据获取失败！", (article) -> article);
    }

    @Override
    @AddArticleLookCountAnnotation
    public String selectArticleContentById(int id) {
        String ret;
        if (operationUtils.hasKey(Constant.ARTICLE_CONTENT_ID) && (ret = (String) operationUtils.hGet(Constant.ARTICLE_CONTENT_ID, id)) != null) {
            return ret;
        }
        OssEntity ossEntity = aliyunOssService.updateOss(oss, "tanxiang-typora-images");
        String content = OssOperationApi.getInstance().downloadMarkDown(articleMapper.selectById(id).getContentUrl(), ossEntity);
        return FunctionUtils.isTrue(content).trueOrFalseHandle("文章获取失败！", (c) -> {
            operationUtils.hPut(Constant.ARTICLE_CONTENT_ID, id, c);
            return c;
        });
    }

    @Override
    public List<ArticleDto> selectArticleArchivedList() {
        String key = StrUtil.format(Constant.ARTICLE_DTO_LIST, false);
        return FunctionUtils.isTrueOrFalse(operationUtils.hasKey(key), (List<ArticleDto>) null).trueOrFalseHandle(
                (dtos) -> sort((List<ArticleDto>) operationUtils.hValues(key)),
                (dtos) -> {
                    dtos = sort(articleMapper.limitArchivedDto());
                    operationUtils.hPutAll(key, dtos.stream().collect(Collectors.toMap(ArticleDto -> String.valueOf(ArticleDto.getId()), ArticleDto -> ArticleDto)));
                    return dtos;
                });
    }

    @Override
    public PageUtils<ArticleDto> selectArticleTagListById(int current, int size, int id) {
        PageUtils<ArticleDto> pageUtils = new PageUtils<>(current, size);
        String articleTag = StrUtil.format(Constant.ARTICLE_TAG, id);
        if (operationUtils.hasKey(articleTag)) {
            pageUtils.setRecords((List<ArticleDto>) operationUtils.lRange(articleTag, (long) current * size, current * size + size - 1));
            pageUtils.setTotal(operationUtils.lLen(articleTag));
            return pageUtils;
        }

        String key = StrUtil.format(Constant.ARTICLE_DTO_LIST, false);
        return FunctionUtils.isTrueOrFalse(operationUtils.hasKey(key), pageUtils).trueOrFalseHandle(
                (util) -> getArticleDtoPageUtils(current, size, pageUtils, articleTag, sort(((List<ArticleDto>) operationUtils.hValues(key)).stream().filter(k -> k.getTag().stream().filter(tag -> tag.getId() == id).toList().size() > 0).toList())),
                (util) -> getArticleDtoPageUtils(current, size, util, Constant.ARTICLE_TAG, sort(articleMapper.tagArticleList(id)))
        );
    }


    @Override
    public PageUtils<ArticleDto> selectArticleClassificationListById(Integer current, Integer size, int id) {
        PageUtils<ArticleDto> pageUtils = new PageUtils<>(current, size);
        String articleClassification = StrUtil.format(Constant.ARTICLE_CLASSIFICATION, id);
        if (operationUtils.hasKey(articleClassification)) {
            List<?> list = operationUtils.lRange(articleClassification, (long) current * size, (long) current * size + size - 1);
            pageUtils.setRecords((List<ArticleDto>) list);
            pageUtils.setTotal(operationUtils.lLen(articleClassification));
            return pageUtils;
        }
        String key = StrUtil.format(Constant.ARTICLE_DTO_LIST, false);
        if (operationUtils.hasKey(key)) {
            List<ArticleDto> dtos = (List<ArticleDto>) operationUtils.hValues(key);
            List<ArticleDto> sortArrayList = sort(dtos).stream().filter(k -> k.getClassification().getId() == id).toList();
            return getArticleDtoPageUtils(current, size, pageUtils, articleClassification, sortArrayList);
        }

        List<ArticleDto> articles = articleMapper.limitClassificationArticle(id);
        return FunctionUtils.isTrue(articles.size() == 0, pageUtils).trueOrFalseHandle("分类获取失败！", (utils) -> getArticleDtoPageUtils(current, size, utils, articleClassification, sort(articles)));
    }


    @Override
    @RedisArticleAnnotation
    public boolean updateArticleTop(int id) {
        Article article = articleMapper.selectById(id);
        // 如果查询到的数据为null
        FunctionUtils.isNull(article).throwFunction("请传入正确的参数！");
        article.setTop(!article.isTop());
        // 如果更新失败了
        FunctionUtils.isError(articleMapper.updateById(article) == 0).throwFunction("更新置顶失败！");
        return !article.isTop();
    }

    @Override
    @AddTextCountAnnotation
    @RedisArticleAnnotation
    public String articleDeleteById(int id) {
        if(operationUtils.hExists(Constant.ARTICLE_CONTENT_ID, String.valueOf(id)) &&
                operationUtils.hDelete(Constant.ARTICLE_CONTENT_ID, String.valueOf(id)) != 1)
        {
            throw new DataOperationErrorException("缓存清除失败！");
        }
        Article article = articleMapper.selectById(id);
        FunctionUtils.isError(article.getRecovery() == Boolean.TRUE).throwFunction("删除失败！");
        article.setRecovery(true);
        article.setRemoveTime(new Timestamp(System.currentTimeMillis()));
        return FunctionUtils.isTrue(articleMapper.updateById(article) == 1, "删除成功！").trueOrFalseHandle("删除失败！", (content) -> content);
    }

    @Override
    public PageUtils<ArticleDto> selectSearchDataById(int searchValue, long current, int size, boolean flag) {
        PageUtils<ArticleDto> utils = new PageUtils<>(current, size);
        utils.setRecords(articleMapper.searchDataById(searchValue, current, size, flag));
        QueryWrapper<Article> wrapper = new QueryWrapper<>();
        wrapper.eq("recovery", flag);
        wrapper.last("and LOCATE('" + searchValue + "', id) > 0");
        utils.setTotal(articleMapper.selectCount(wrapper));
        return utils;
    }

    @Override
    public PageUtils<ArticleDto> selectSearchDataLikeTitle(String searchValue, long current, long size, boolean flag) {
        PageUtils<ArticleDto> utils = new PageUtils<>(current, size);
        List<ArticleDto> articleDto = articleMapper.searchDataLikeTitle(searchValue, current * size, size, flag);
        QueryWrapper<Article> wrapper = new QueryWrapper<>();
        wrapper.eq("recovery", flag);
        wrapper.last("and LOCATE('" + searchValue + "', title) > 0");
        utils.setTotal(articleMapper.selectCount(wrapper));
        utils.setRecords(articleDto);
        return utils;
    }

    @Override
    public PageUtils<ArticleDto> selectSearchDataLikeClassification(String searchValue, long current, long size, boolean flag) {
        PageUtils<ArticleDto> utils = new PageUtils<>(current, size);
        return FunctionUtils.isTrueOrFalse(CommonUtils.isPureDigital(searchValue), utils).trueOrFalseHandle(
                (util) -> {
                    // 纯数字，查询分类ID
                    util.setRecords(articleMapper.searchDataLikeClassificationId(searchValue, current, size, flag));
                    util.setTotal(articleMapper.searchDataLikeClassificationIdCount(searchValue, flag));
                    return util;
                },
                // 非纯数字，查询标签名称
                (util) -> {
                    // 非纯数字，查询分类名称
                    util.setRecords(articleMapper.searchDataLikeClassificationName(searchValue, current, size, flag));
                    util.setTotal(articleMapper.searchDataLikeClassificationNameCount(searchValue, flag));
                    return util;
                }
        );
    }

    @Override
    public PageUtils<ArticleDto> selectSearchDataPosition(String searchValue, long current, long size, boolean flag) {
        PageUtils<ArticleDto> utils = new PageUtils<>(current, size);
        utils.setRecords(articleMapper.searchDataPosition(searchValue, current, size, flag));

        QueryWrapper<Article> wrapper = new QueryWrapper<>();
        wrapper.eq("recovery", flag);
        wrapper.last("and LOCATE('" + searchValue + "', address) > 0");
        utils.setTotal(articleMapper.selectCount(wrapper));
        return utils;
    }

    @Override
    public PageUtils<ArticleDto> selectSearchDataTag(String searchValue, long current, long size, boolean flag) {
        PageUtils<ArticleDto> utils = new PageUtils<>(current, size);
        List<ArticleDto> articleDtos = new ArrayList<>();
        FunctionUtils.isTrueOrFalse(CommonUtils.isPureDigital(searchValue)).trueOrFalseHandle(
                // 纯数字，查询标签ID
                () -> articleDtos.addAll(articleMapper.searchDataByTagId(searchValue, flag)),
                // 非纯数字，查询标签名称
                () -> articleDtos.addAll(articleMapper.searchDataByTagName(searchValue, flag))
        );
        utils.setRecords(articleDtos.stream().skip(current * size).limit(size).toList());
        utils.setTotal(articleDtos.size());
        return utils;
    }

    @Override
    public PageUtils<ArticleDto> selectSearchDataByLikeCountEqualOrGreaterThan(int searchValue, long current, long size, boolean flag) {
        PageUtils<ArticleDto> utils = new PageUtils<>(current, size);
        utils.setRecords(articleMapper.searchDataByLikeCountEqualOrGreaterThan(searchValue, current, size, flag));
        QueryWrapper<Article> wrapper = new QueryWrapper<>();
        wrapper.eq("recovery", flag);
        wrapper.ge("like_count", searchValue);
        utils.setTotal(articleMapper.selectCount(wrapper));
        return utils;
    }

    @Override
    public PageUtils<ArticleDto> selectSearchDataByLikeCountEqualOrLessThan(int searchValue, long current, long size, boolean flag) {
        PageUtils<ArticleDto> utils = new PageUtils<>(current, size);

        utils.setRecords(articleMapper.searchDataByLikeCountEqualOrLessThan(searchValue, current, size, flag));
        QueryWrapper<Article> wrapper = new QueryWrapper<>();
        wrapper.eq("recovery", flag);
        wrapper.le("like_count", searchValue);
        utils.setTotal(articleMapper.selectCount(wrapper));
        return utils;
    }

    @Override
    public PageUtils<ArticleDto> selectSearchDataByLikeCountEqual(int searchValue, long current, long size, boolean flag) {
        PageUtils<ArticleDto> utils = new PageUtils<>(current, size);

        utils.setRecords(articleMapper.searchDataByLikeCountEqual(searchValue, current, size, flag));
        QueryWrapper<Article> wrapper = new QueryWrapper<>();
        wrapper.eq("recovery", flag);
        wrapper.eq("like_count", searchValue);
        utils.setTotal(articleMapper.selectCount(wrapper));
        return utils;
    }

    @Override
    @RedisArticleAnnotation
    @Transactional(rollbackFor = {Exception.class, DataOperationErrorException.class})
    public String updateArticleInfo(int id, AddArticleVo article) {
        // 删除redis缓存
        if(operationUtils.hExists(Constant.ARTICLE_CONTENT_ID, String.valueOf(id)) && operationUtils.hDelete(Constant.ARTICLE_CONTENT_ID, String.valueOf(id))!= 1) {
            throw new DataOperationErrorException("缓存清除失败！");
        }
        Article articleInfo = articleMapper.selectById(id);
        // 判断articleInfo是不是null
        FunctionUtils.isNull(articleInfo).throwFunction("请传入正确的参数！");
        // 判断classificationId是不是null
        FunctionUtils.isNull(classificationMapper.selectById(article.getClassificationId())).throwFunction("请传入正确的分类ID！");

        articleInfo.setClassificationId(article.getClassificationId());
        articleInfo.setTop(article.getTop());
        articleInfo.setPrefixContent(article.getPrefixContent());
        // 判断传入的标签不存在数据库内
        FunctionUtils.isError(tagMapper.selectCount(new QueryWrapper<Tag>().in("id", article.getTagsId())) != article.getTagsId().size()).throwFunction("请传入正确的标签ID");

        QueryWrapper<ArticleTag> wrapper = new QueryWrapper<ArticleTag>().eq("article_id", id);
        // 先删除所有对应的tagId，然后在添加
        articleTagMapper.delete(wrapper);
        // 如果删除后还是可以查询到数据，则表示删除失败了
        FunctionUtils.isError(articleTagMapper.selectList(wrapper).size() != 0).throwFunction("标签更新失败！");
        // 将文章的封面上传到 tx-blog 的bucket内
        OssEntity ossEntity = aliyunOssService.updateOss(oss, "tx-blog");
        // 判断图片是否上传
        FunctionUtils.isNullAVoid(CommonUtils.uploadImage(article.getImage(), articleInfo, ossEntity)).trueOrFalseHandle("图片上传失败！", articleInfo::setImage);
        try {
            ossEntity = aliyunOssService.updateOss(oss, "tanxiang-typora-images");
            OssOperationApi instance = OssOperationApi.getInstance();
            OSS ossClient = instance.ossClient(ossEntity);
            // 存储路径
            String fileRouter = "note/" + article.getTitle() + ".md";
            // 更新了标题
            FunctionUtils.isTrue(!article.getTitle().equals(articleInfo.getTitle())).trueHandle(() -> {
                // 如果文件已存在，则抛出异常
                FunctionUtils.isError(ossClient.doesObjectExist(oss.getBucketName(), fileRouter)).throwFunction("文件已存在，请更换标题！");
                articleInfo.setTitle(article.getTitle());
            });
            // 是否上传成功！
            FunctionUtils.isNullAVoid(instance.uploadContent(fileRouter, article.getText(), ossEntity)).trueOrFalseHandle("文章更新失败！", articleInfo::setContentUrl);
        } catch (Exception e) {
            throw new DataOperationErrorException("文章更新失败！");
        }
        // 判断是否更新成功
        FunctionUtils.isError(articleMapper.updateById(articleInfo) != 1).throwFunction("文章更新失败！");
        // 判断是否关联成功
        FunctionUtils.isError(!articleTagService.saveBatch(article.getTagsId().stream().map(tag -> new ArticleTag(id, tag)).toList())).throwFunction("标签ID异常！");
        return "更新成功！";
    }

    @Override
    @AddTextCountAnnotation
    @RedisArticleAnnotation
    @Transactional(rollbackFor = {Exception.class, DataOperationErrorException.class})
    public Integer addArticleInfo(AddArticleVo article, String province) {
        Article articleInfo = new Article();
        // 查询分类ID是否存在
        FunctionUtils.isNull(classificationMapper.selectById(article.getClassificationId())).throwFunction("请传入正确的分类ID！");
        // 查询当前的标题是否存在
        FunctionUtils.isError(articleMapper.selectCount(new QueryWrapper<Article>().eq("title", article.getTitle())) != 0).throwFunction("文章标题已存在，请更换标题！");
        // 查询标签ID是否存在
        FunctionUtils.isError(tagMapper.selectList(new QueryWrapper<Tag>().in("id", article.getTagsId())).size() != article.getTagsId().size()).throwFunction("请输入正确的标签ID！");

        articleInfo.setAddress(province);
        articleInfo.setTop(article.getTop());
        articleInfo.setTitle(article.getTitle());
        articleInfo.setPrefixContent(article.getPrefixContent());
        articleInfo.setClassificationId(article.getClassificationId());
        try {
            OssEntity ossEntity = aliyunOssService.updateOss(oss, "tx-blog");
            // 上传封面
            log.info("addArticleInfo -- article.getImage():{}", article.getImage());
            FunctionUtils.isNullAVoid(CommonUtils.uploadImage(article.getImage(), null, ossEntity)).trueOrFalseHandle("图片上传失败！", articleInfo::setImage);
            // 上传文章
            ossEntity = aliyunOssService.updateOss(oss, "tanxiang-typora-images");
            OSS ossClient = OssOperationApi.getInstance().ossClient(ossEntity);
            String fileRouter = "note/" + article.getTitle() + ".md";
            // 判断是否在oss内存在同名文件
            FunctionUtils.isError(ossClient.doesObjectExist(ossEntity.getBucketName(), fileRouter)).throwFunction("文件已存在，请更换标题！");
            // 判断文章是否上传成功
            FunctionUtils.isNullAVoid(OssOperationApi.getInstance().uploadContent(fileRouter, article.getText(), ossEntity)).trueOrFalseHandle("文章上传服务器失败！", articleInfo::setContentUrl);
            // 上传成功则把标题也放入
            articleInfo.setTitle(article.getTitle());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("错误：{}", e.getMessage());
            throw new DataOperationErrorException("文章上传失败！");
        }
        // 判断文章信息是否增加成功
        FunctionUtils.isError(articleMapper.insert(articleInfo) != 1).throwFunction("文章增加失败！");
        // 关联标签和文章
        FunctionUtils.isError(!articleTagService.saveBatch(article.getTagsId().stream().map(tag -> new ArticleTag(articleInfo.getId(), tag)).toList())).throwFunction("标签与文章关联失败！");
        return articleInfo.getId();
    }

    @Override
    @RedisArticleAnnotation
    public String restore(int id) {
        return FunctionUtils
                .isTrue(articleMapper.update(null, new UpdateWrapper<Article>().eq("id", id).set("recovery", false)) == 1, "还原成功！")
                .trueOrFalseHandle("还原失败！", (data) -> data);
    }

    @Override
    @RedisArticleAnnotation
    public String articleRealDeleteById(int id) {
        return FunctionUtils
                .isTrue(articleMapper.delete(new QueryWrapper<Article>().eq("id", id).eq("recovery", true)) == 1, "删除成功！")
                .trueOrFalseHandle("删除失败！", (data) -> data);
    }

    @Override
    public long allCount() {
        String key = StrUtil.format(Constant.ARTICLE_DTO_LIST, false);
        return FunctionUtils.isTrueOrFalse(operationUtils.hasKey(key), 0).trueOrFalseHandle(
                (count) -> Math.toIntExact(operationUtils.hSize(key)),
                (count) -> Math.toIntExact(articleMapper.selectCount(new QueryWrapper<Article>().eq("recovery", false))));
    }

    @Override
    public PageUtils<ArticleDto> selectSearchByContent(String searchKey, long current, long size) {
        log.info("searchKey:{}", searchKey);
        log.info("current:{}", current);
        log.info("size:{}", size);

        PageUtils<ArticleDto> utils = new PageUtils<>(current * size, size);

        AtomicReference<String> tag = new AtomicReference<>();
        AtomicReference<String> content = new AtomicReference<>();
        AtomicReference<String> classification = new AtomicReference<>();
        Arrays.stream(searchKey.trim().split(" ")).forEach(c -> {
            if (c.contains("tag:")) {
                tag.set(c.replace("tag:", ""));
                return;
            }
            if (c.contains("classification:")) {
                classification.set(c.replace("classification:", ""));
                return;
            }

            content.set(c);
        });
        log.info("tag.get():{}", tag.get());
        log.info("classification.get():{}", classification.get());

        boolean flag = StrUtil.isBlank(tag.get()) && StrUtil.isBlank(classification.get());
        return FunctionUtils
                .isTrueOrFalse(flag, utils)
                .trueOrFalseHandle(u -> {
                    // 普通的查询
                    u.setRecords(articleMapper.selectArticle(searchKey, u.getCurrent(), size));
                    u.setTotal(articleMapper.selectArticleTotal(searchKey));
                    return u;
                }, u -> {
                    u.setRecords(articleMapper.selectArticleCompositeSearch(content.get(), tag.get(), classification.get(), u.getCurrent(), size));
                    u.setTotal(articleMapper.selectArticleCompositeTotal(content.get(), tag.get(), classification.get()));
                    // 需要查询标签或者是分类的查询！
                    return u;
                });
    }

    @Override
    public PageUtils<ArticleDto> selectArticleSearchData(String value, int sort, int time, long current, int size) {
        PageUtils<ArticleDto> utils = new PageUtils<>(current, size);
        List<ArticleDto> list = articleMapper.searchDataList(value, sort, time);
        utils.setTotal(list.size());
        utils.setRecords(list.stream().skip(utils.getCurrent() * utils.getSize()).limit(utils.getSize()).toList());
        return utils;
    }
}




