/*
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2024-12-31 09:48:22
 * @LastEditTime: 2025-01-14 06:35:03
 * @FilePath: /code/blog/client-web/src/store/modules/useCategoryStore.ts
 * @Description:
 * Copyright (c) 2024 by tanxiang, All Rights Reserved.
 */

import { Category } from '@/api/categorys/category.d';
import { defineStore } from 'pinia';

export const useCategoryStore = defineStore('category', {
  state: () => ({
    categorys: [] as Array<Category>,
    categoryArticleList: [] as Array<{ id: number; name: string; allCount: number; list: Array<any> }>
  }),
  getters: {
    getCategorys: (state) => state.categorys,
    getCategoriyArticleList: (state) => state.categoryArticleList,
    getCategoriyArticleListById: (state) => (id: number) => {
      return state.categoryArticleList.find((item) => item.id == id);
    }
  },
  actions: {
    setCategorys(categorys: Category[]) {
      this.categorys.splice(0, this.categorys.length);
      this.categorys.push(...categorys);
    },
    setCategoriyArticleList(categoryArticleList: { id: number; name: string; allCount: number; list: Array<any> }) {
      // 判断是否为空
      if (this.categoryArticleList.length == 0) {
        return this.categoryArticleList.push(categoryArticleList);
      }
      // 判断当前分类的文章列表是否存在
      let find = this.categoryArticleList.find((item: any) => item.id == categoryArticleList.id);
      if (!find) {
        return this.categoryArticleList.push(categoryArticleList);
      }
      // 判断当前的分类文章是否已经存储了
      let f = find.list.find((item: any) => item.id == categoryArticleList.list[0].id);
      if (!f) {
        find.list.push(...categoryArticleList.list);
      }
    }
  }
});
