package com.tanx.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tanx.blog.annotation.DeleteExcerptAnnotation;
import com.tanx.blog.api.OssOperationApi;
import com.tanx.blog.constant.Constant;
import com.tanx.blog.entity.common.OssEntity;
import com.tanx.blog.entity.po.Chat;
import com.tanx.blog.entity.po.Excerpt;
import com.tanx.blog.entity.po.ExcerptChat;
import com.tanx.blog.entity.vo.ExcerptVo;
import com.tanx.blog.exception.DataOperationErrorException;
import com.tanx.blog.mapper.ChatMapper;
import com.tanx.blog.mapper.ExcerptChatMapper;
import com.tanx.blog.mapper.ExcerptMapper;
import com.tanx.blog.service.ExcerptService;
import com.tanx.blog.service.UploadService;
import com.tanx.blog.utils.PageUtils;
import com.tanx.blog.utils.RedisOperationUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 谭翔
 * @description 针对表【b_excerpt(优美语句摘抄表)】的数据库操作Service实现
 * @createDate 2024-04-27 13:54:35
 */
@Service
@Slf4j
public class ExcerptServiceImpl extends ServiceImpl<ExcerptMapper, Excerpt>
        implements ExcerptService {

    @Resource
    private ExcerptMapper excerptMapper;

    @Resource
    private RedisOperationUtils operationUtils;

    @Resource
    private UploadService uploadService;

    static OssOperationApi ossOperationApi;

    @Resource
    private OssEntity oss;

    @Resource
    private AliyunOssServiceImpl aliyunOssService;

    @Resource
    private ChatMapper chatMapper;

    @Resource
    private ExcerptChatMapper excerptChatMapper;

    @Override
    @DeleteExcerptAnnotation
    public String addExcerpt(String content) {
        Excerpt excerpt = new Excerpt();
        excerpt.setContent(content);
        if (excerptMapper.insert(excerpt) != 1) {
            throw new DataOperationErrorException("添加失败!");
        }
        return "添加成功！";
    }

    @Override
    @DeleteExcerptAnnotation
    public String addExcerpt(MultipartFile file) {
        String url = uploadService.uploadOnceImage(file, oss, "tx-blog", "excerpt/");
        if (url == null) {
            throw new DataOperationErrorException("图片上传失败！");
        }
        String content = "<img src='" + url + "' alt=''/>";
        Excerpt excerpt = new Excerpt();
        excerpt.setContent(content);
        if (excerptMapper.insert(excerpt) != 1) {
            throw new DataOperationErrorException("添加失败！");
        }
        return "添加成功！";
    }

    @Override
    @DeleteExcerptAnnotation
    @Transactional(rollbackFor = {DataOperationErrorException.class, Exception.class})
    public String deleteExcerpt(int id) {

        List<ExcerptChat> excerptChats = excerptChatMapper.selectList(new QueryWrapper<ExcerptChat>().eq("excerpt_id", id));

        if (excerptChats != null && excerptChats.size() > 0) {
            List<Long> chatIds = excerptChats.stream().map(ExcerptChat::getChatId).toList();
            List<Long> excerptChatIds = excerptChats.stream().map(ExcerptChat::getId).toList();

            long count = chatMapper.selectCount(new QueryWrapper<Chat>().in("id", chatIds));
            if (chatMapper.deleteBatchIds(chatIds) != count)
                throw new DataOperationErrorException("记录断开失败！");
            if (excerptChatMapper.deleteBatchIds(excerptChatIds) != excerptChatIds.size())
                throw new DataOperationErrorException("关系断开失败！");
        }

        if (excerptMapper.deleteById(id) != 1) {
            throw new DataOperationErrorException("删除失败！");
        }
        return "删除成功！";
    }

    @Override
    @DeleteExcerptAnnotation
    public String updateExcerpt(int id, String content) {
        Excerpt selectById = excerptMapper.selectById(id);
        if (selectById == null) {
            throw new DataOperationErrorException("请输入的参数！");
        }

        if (selectById.getContent().equals(content)) {
            throw new DataOperationErrorException("内容未变动！");
        }

        deleteFile(selectById);
        Excerpt excerpt = new Excerpt();
        excerpt.setContent(content);
        excerpt.setId(id);
        if (excerptMapper.updateById(excerpt) != 1) {
            throw new DataOperationErrorException("更新失败！");
        }

        return content;
    }

    @Override
    @DeleteExcerptAnnotation
    public String updateExcerpt(int id, MultipartFile file) {
        Excerpt selectById = excerptMapper.selectById(id);
        if (selectById == null) {
            throw new DataOperationErrorException("请输入的参数！");
        }

        String url = uploadService.uploadOnceImage(file, oss, "tx-blog", "excerpt/");
        if (url == null) {
            throw new DataOperationErrorException("图片上传失败！");
        }
        String content;
        deleteFile(selectById);
        content = "<img src='" + url + "' alt=''/>";
        Excerpt excerpt = new Excerpt();
        excerpt.setId(id);
        excerpt.setContent(content);
        if (excerptMapper.updateById(excerpt) != 1) {
            throw new DataOperationErrorException("更新失败！");
        }
        return content;
    }

    @Override
    public PageUtils<ExcerptVo> selectList(int current, int size) {
        PageUtils<ExcerptVo> utils = new PageUtils<>(current, size);
        if (operationUtils.hasKey(Constant.EXCERPT)) {
            utils.setRecords((List<ExcerptVo>) operationUtils.lRange(Constant.EXCERPT, (long) current * size, (long) current * size + size - 1));
            utils.setTotal(operationUtils.lLen(Constant.EXCERPT));
        } else {
            List<ExcerptVo> excerpts = excerptMapper.selectListGroup();
            utils.setRecords(excerpts.stream().skip((long) current * size).limit(size).toList());
            utils.setTotal(excerpts.size());
            operationUtils.lRightPushAll(Constant.EXCERPT, excerpts);
        }
        return utils;
    }

    @Override
    public PageUtils<ExcerptVo> selectListById(int id, int current, int size) {
        PageUtils<ExcerptVo> utils = new PageUtils<>(current, size);
        List<ExcerptVo> excerpts;
        if (operationUtils.hasKey(Constant.EXCERPT)) {
            List<ExcerptVo> list = (List<ExcerptVo>) operationUtils.lRange(Constant.EXCERPT, 0, -1);
            excerpts = list.stream().filter(l -> l.getId().toString().matches(".*" + id + ".*")).toList();
        } else {
            excerpts = excerptMapper.selectListGroupLikeId(id);
        }
        utils.setRecords(excerpts.stream().skip((long) current * size).limit(size).toList());
        utils.setTotal(excerpts.size());
        operationUtils.lRightPushAll(Constant.EXCERPT, excerpts);
        return utils;
    }

    private void deleteFile(Excerpt selectById) {
        if (selectById.getContent().startsWith("<img src=") && selectById.getContent().endsWith("/>")) {
            OssEntity ossEntity = aliyunOssService.updateOss(oss, "tx-blog");
            Pattern compile = Pattern.compile("(https://|http://).*.webp");
            Matcher matcher = compile.matcher("<img src='https://static.recall.top/profilePhoto/head1704523543423.webp' alt='' />");
            while (matcher.find()) {
                String replace = matcher.group().replaceAll("(http://static.recall.top/|https://static.recall.top/|static.recall.top/)", "");
                ossOperationApi.deleteFile(ossEntity, replace);
            }
        }
    }
}




