package com.tanx.blog.config;

import com.tanx.blog.annotation.ProgramInitAnnotation;
import com.tanx.blog.constant.Constant;
import com.tanx.blog.service.*;
import com.tanx.blog.utils.RedisOperationUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @description: redis 数据预热
 * @project_name: blog-server
 * @author: 谭翔
 * @date: 2023/11/21 21:19
 */

@Slf4j
@Order(1)
@Component
public class RedisDataConfig implements CommandLineRunner {

    @Resource
    private InformalService informalService;
    @Resource
    private RedisOperationUtils operationUtils;
    @Resource
    private ArticleService articleService;
    @Resource
    private WebStationService webStationService;
    @Resource
    private PrintTextService printTextService;
    @Resource
    private EmojiService emojiService;
    @Resource
    private ClassificationService classificationService;
    @Resource
    private TagService tagService;
    @Resource
    private AliyunOssService aliyunOssService;
    @Resource
    private ThreadPoolExecutor threadPoolExecutor;

    @Override
    @ProgramInitAnnotation
    public void run(String... args) throws Exception {
        log.info("开始redis数据预热操作:{}", "-----------------------------------------------");
        threadPoolExecutor.execute(() -> informalService.selectInformalList(0, 15, false));
        threadPoolExecutor.execute(() -> informalService.selectInformalList(0, 15, true));
        threadPoolExecutor.execute(() -> articleService.selectArticleList(0, 15, false, false));
        threadPoolExecutor.execute(() -> articleService.selectArticleList(0, 15, true, false));
        threadPoolExecutor.execute(() -> webStationService.findWebStation());
        threadPoolExecutor.execute(() -> printTextService.findAll());
        threadPoolExecutor.execute(() -> tagService.tagListLimit(0, 15));
        threadPoolExecutor.execute(() -> classificationService.selectClassificationLimit(0, 15));
        threadPoolExecutor.execute(() -> emojiService.selectEmojiAll());
        threadPoolExecutor.execute(() -> aliyunOssService.init());
    }
}
