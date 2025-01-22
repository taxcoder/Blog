/*
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2025-01-17 08:31:42
 * @LastEditTime: 2025-01-17 08:34:10
 * @FilePath: /code/blog/client-admin/src/constant/menu/blog.ts
 * @Description: 菜单项-博客管理
 * Copyright (c) 2025 by tanxiang, All Rights Reserved.
 */
const BlogArticles = () => import('@/views/blog/articles/Index.vue');
const BlogCategroys = () => import('@/views/blog/categroys/Index.vue');
const BlogTags = () => import('@/views/blog/tags/Index.vue');
const BlogFeelings = () => import('@/views/blog/feelings/Index.vue');

import { Sunny } from '@vicons/carbon';

export default {
  path: '/blog',
  name: '博客管理',
  icon: markRaw(Sunny),
  meta: {
    order: 2
  },
  children: [
    {
      path: '/blog/articles',
      name: '文章管理',
      meta: {
        requestAuth: true,
        order: 1
      },
      component: BlogArticles
    },
    {
      path: '/blog/categroys',
      name: '分类管理',
      meta: {
        requestAuth: true,
        order: 3
      },
      component: BlogCategroys
    },
    {
      path: '/blog/tags',
      name: '标签管理',
      meta: {
        requestAuth: true,
        order: 4
      },
      component: BlogTags
    },
    {
      path: '/blog/feelings',
      name: '心情管理',
      meta: {
        requestAuth: true,
        order: 2
      },
      component: BlogFeelings
    }
  ]
};
