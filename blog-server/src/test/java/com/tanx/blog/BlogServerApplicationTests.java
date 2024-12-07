package com.tanx.blog;

import com.tanx.blog.entity.common.OssEntity;
import com.tanx.blog.handler.JwtHandler;
import com.tanx.blog.mapper.ArticleMapper;
import com.tanx.blog.mapper.EmojiMapper;
import com.tanx.blog.mapper.ExcerptMapper;
import com.tanx.blog.mapper.PrintTextMapper;
import com.tanx.blog.service.*;
import com.tanx.blog.utils.RedisOperationUtils;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@SpringBootTest
@EnableEncryptableProperties
class BlogServerApplicationTests {
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private EmojiMapper emojiMapper;

    @Resource
    private RedisOperationUtils operationUtils;

    @Resource
    private JwtHandler jwtHandler;

    @Resource
    private EmojiService emojiService;

    @Resource
    private ArticleService articleService;

    @Resource
    private StringEncryptor encryptor;

    @Resource
    private PrintTextService printTextService;

    @Resource
    private PrintTextMapper printTextMapper;

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private TargetService targetService;
    @Resource
    private ImagesService imagesService;
    @Resource
    private OssEntity oss;
    @Resource
    private AliyunOssService aliyunOssService;
    @Resource
    private ExcerptService excerptService;
    @Resource
    private ExcerptMapper excerptMapper;
    @Resource
    private ChatService chatService;

    @Test
    void contextLoads() {
    }
}