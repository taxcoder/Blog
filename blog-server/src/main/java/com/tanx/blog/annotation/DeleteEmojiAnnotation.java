package com.tanx.blog.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description: 删除redis内的emoji
 * @project_name: blog-server
 * @author: 谭翔
 * @date: 2023/12/27 18:25
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DeleteEmojiAnnotation {
}
