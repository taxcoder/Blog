package com.tanx.blog.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description: 增加文字
 * @project_name: blog-server
 * @author: 谭翔
 * @date: 2023/11/18 10:40
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AddTextCountAnnotation {
}
