/*
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2024-12-30 04:05:32
 * @LastEditTime: 2025-01-15 02:29:40
 * @FilePath: /code/blog/client-web/src/api/articles/article.ts
 * @Description:
 * Copyright (c) 2024 by tanxiang, All Rights Reserved.
 */
import { request } from '@/axios';
import { Article, ArticleInfo } from './article.d';
// 获取文章列表，根据当前页和每页条数
export const getArticles = async (index: number, limit: number) => {
  return request<Array<ArticleInfo>>(`/articles?index=${index}&limit=${limit}`);
};
// 返回哪些年份有上传文章
export const getArticlesCreateTime = async () => {
  return request<Array<String>>(`/articles/years`);
};
// 根据年份查询当年所有的文章
export const getArticlesByCreateTime = async (year: string) => {
  return request<Array<Article>>(`/articles/years/${year}`);
};

// 根据文章id查询文章
export const getArticleById = async (id: number) => {
  return request<Article>(`/articles/${id}`);
};

// 给文章点赞
export const addArticleLikeCount = async (articleId: number) => {
  return request<Article>(`/articles/${articleId}/like`, {
    method: 'POST'
  }).then((res) => {
    // 如果点赞成功，给当前的日记ID加入cookie，表示当前网页用户已经点赞过了
    const [state, setState] = useCookieState('article_like');
    try {
      // 如果没有，则添加一个
      if (!state.value) {
        setState(JSON.stringify([articleId]));
        return Promise.resolve(res);
      }
      //@ts-ignore
      const articleLike = JSON.parse(state.value);
      // 如果没有在里面，表示没有点过赞
      if (!articleLike.includes(articleId)) {
        articleLike.push(articleId);
        setState(JSON.stringify(articleLike));
      }
      return Promise.resolve(res);
    } catch (error) {
      setState(JSON.stringify([articleId]));
    }
  });
};
