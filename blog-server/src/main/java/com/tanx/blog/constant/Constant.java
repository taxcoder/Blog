package com.tanx.blog.constant;

import java.util.Collection;

/**
 * @description: 常量
 * @project_name: blog-server
 * @author: 谭翔
 * @date: 2023/11/5 8:46
 */
public class Constant {
    public static final String REDIS_TOKEN_KEY = "login:{}:token";
    public static final String REDIS_TEMP_TOKEN_KEY = "temp_login:{}:token";
    public final static String ARTICLE_TAG = "article:tag:{}";
    public final static String ARTICLE_CLASSIFICATION = "article:classification:{}";
    public final static String ARTICLE_DTO_LIST = "article_dto_list:{}";

    public final static String ARTICLE_CONTENT_ID = "article_content";
    public final static String WEB_KEY = "web";
    public final static String PRINT_TEXT = "print";
    public final static String CLASSIFICATION_LIST = "classification_list";
    public final static String TAG_LIST = "tag_list";

    public final static String EMOJI = "emoji";
    public static final String ESSAY_LIST = "essay_list:{}";
    public static final String ESSAY_LIKE = "essay_like";
    public static final String VISIT = "visit";
    public static final String TEXT_COUNT = "text_count";

    public static final String ALIYUN_OSS = "aliyun_oss";
    public static final String LOGIN_TIME = "login_time";
    public static final String ARTICLE_LOOK_COUNT = "article_look_count";
    public static final String TARGET = "target";
    public static final String UPLOAD_IMAGE = "upload_image";
    public static final String CLASS_NAV = "nav_list";
    public static final String NAVIGATION = "navigation";
    public static final String EXCERPT = "excerpt";
    public static final String CHAT = "chat";
}
