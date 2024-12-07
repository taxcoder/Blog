package com.tanx.blog.aspect;

import cn.hutool.core.util.StrUtil;
import com.tanx.blog.constant.Constant;
import com.tanx.blog.entity.vo.AddArticleVo;
import com.tanx.blog.entity.vo.AddEssayVo;
import com.tanx.blog.service.InformalService;
import com.tanx.blog.utils.RedisOperationUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * @description: 文章删除后置通知
 * @project_name: blog-server
 * @author: 谭翔
 * @date: 2023/11/15 19:44
 */


@Slf4j
@Aspect
@Component
public class RedisAspect {

    @Resource
    private RedisOperationUtils operationUtils;

    public void reload() {
        operationUtils.delete(StrUtil.format(Constant.ARTICLE_DTO_LIST, Boolean.TRUE));
        operationUtils.delete(StrUtil.format(Constant.ARTICLE_DTO_LIST, Boolean.FALSE));
        operationUtils.delete(Constant.CLASSIFICATION_LIST);
        operationUtils.delete(Constant.TAG_LIST);
        operationUtils.delete(operationUtils.keys(StrUtil.format(Constant.ARTICLE_TAG, "*")));
        operationUtils.delete(operationUtils.keys(StrUtil.format(Constant.CLASSIFICATION_LIST, "*")));
    }

    public void init() {
        operationUtils.delete(StrUtil.format(Constant.ARTICLE_DTO_LIST, Boolean.TRUE));
        operationUtils.delete(StrUtil.format(Constant.ARTICLE_DTO_LIST, Boolean.FALSE));
        operationUtils.delete(Constant.CLASSIFICATION_LIST);
        operationUtils.delete(Constant.TAG_LIST);
        operationUtils.delete(Constant.ALIYUN_OSS);
        operationUtils.delete(operationUtils.keys(StrUtil.format(Constant.ARTICLE_TAG, "*")));
        operationUtils.delete(operationUtils.keys(StrUtil.format(Constant.CLASSIFICATION_LIST, "*")));
        operationUtils.delete(Constant.UPLOAD_IMAGE);
        operationUtils.delete(Constant.TARGET);
        operationUtils.delete(Constant.EMOJI);
        operationUtils.delete(Constant.CLASS_NAV);
        operationUtils.delete(Constant.NAVIGATION);
        operationUtils.delete(Constant.EXCERPT);
        operationUtils.delete(Constant.CHAT);
        operationUtils.delete(Constant.WEB_KEY);
        operationUtils.delete(Constant.PRINT_TEXT);
        operationUtils.delete(StrUtil.format(Constant.ESSAY_LIST, Boolean.TRUE));
        operationUtils.delete(StrUtil.format(Constant.ESSAY_LIST, Boolean.FALSE));
    }

    @After("@annotation(com.tanx.blog.annotation.RedisArticleAnnotation) && execution(* *(..))")
    public void redisArticleDelete() {
        reload();
        new Thread(() -> {
            try {
                Thread.sleep(500);
                reload();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    @After("@annotation(com.tanx.blog.annotation.DeleteEmojiAnnotation) && execution(* *(..))")
    public void emojiDelete() {
        operationUtils.delete(Constant.EMOJI);
    }

    @After("@annotation(com.tanx.blog.annotation.DeleteClassificationNavAnnotation) && execution(* *(..))")
    public void classificationNavDelete() {
        operationUtils.delete(Constant.CLASS_NAV);
    }

    @After("@annotation(com.tanx.blog.annotation.DeleteNavigationAnnotation) && execution(* *(..))")
    public void navigationDelete() {
        operationUtils.delete(Constant.NAVIGATION);
    }

    @After("@annotation(com.tanx.blog.annotation.DeleteExcerptAnnotation) && execution(* *(..))")
    public void excerptDelete() {
        operationUtils.delete(Constant.EXCERPT);
    }
    @After("@annotation(com.tanx.blog.annotation.ProgramInitAnnotation) && execution(* *(..))")
    public void redisDataInit() {
        init();
    }

    @After("@annotation(com.tanx.blog.annotation.AddTextCountAnnotation) && execution(* *(..))")
    public void addTextCount(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = signature.getParameterNames();
        String textCount = Constant.TEXT_COUNT;
        switch (parameterNames[0]) {
            case "addEssayVo" -> operationUtils.incrBy(textCount, ((AddEssayVo) args[0]).getContent().length());
            case "article" -> operationUtils.incrBy(textCount, ((AddArticleVo) args[0]).getText().length());
            default -> {
            }
        }
    }

    @After("@annotation(com.tanx.blog.annotation.AddArticleLookCountAnnotation) && execution(* *(..))")
    public void addArticleCount(JoinPoint joinPoint) {
        operationUtils.hIncrBy(Constant.ARTICLE_LOOK_COUNT, String.valueOf(joinPoint.getArgs()[0]), 1);
    }
}
