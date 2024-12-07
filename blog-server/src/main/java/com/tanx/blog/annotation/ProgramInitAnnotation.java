package com.tanx.blog.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description:
 * @project_name: blog-server
 * @author: 谭翔
 * @date: 2023/11/21 21:24
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ProgramInitAnnotation {
}
