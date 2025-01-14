/*
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2024-12-30 04:05:38
 * @LastEditTime: 2025-01-05 10:47:53
 * @FilePath: /code/blog/client-web/src/api/articles/article.d.ts
 * @Description:
 * Copyright (c) 2024 by tanxiang, All Rights Reserved.
 */
export type Article = {
  id: number;
  title: string;
  description: string;
  category: { id: number; name: string };
  tags: Array<{ id: number; name: string }>;
  likeCount: number;
  content: string;
  cover: string;
  author: string;
  createTime: number;
  updateTime: number;
};

export type ArticleInfo = {
  allCount: number;
  list: Array<Article>;
};
