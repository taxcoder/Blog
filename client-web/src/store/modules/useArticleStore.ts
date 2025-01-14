/*
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2024-12-31 09:48:22
 * @LastEditTime: 2025-01-13 12:29:22
 * @FilePath: /code/blog/client-web/src/store/modules/useArticleStore.ts
 * @Description:
 * Copyright (c) 2024 by tanxiang, All Rights Reserved.
 */
import { Topping } from '@/api/topping/topping.d';
import { Article, ArticleInfo } from '@/api/articles/article.d';
import { defineStore } from 'pinia';

export const useArticleStore = defineStore('article', {
  state: () => ({
    topping: [] as Array<Topping>,
    articles: {
      allCount: -1,
      list: []
    } as ArticleInfo
  }),
  getters: {
    getTopping: (state) => state.topping,
    getArticles: (state) => state.articles
  },
  actions: {
    setTopping(topping: any) {
      this.topping.splice(0, this.topping.length);
      this.topping.push(...topping);
    },
    setArticles(articles: any) {
      this.articles.allCount = articles.allCount;
      this.articles.list.push(...articles.list);
    }
  }
});
