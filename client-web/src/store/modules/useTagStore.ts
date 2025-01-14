import { TagArticle } from './../../api/tags/tag.d';
/*
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2025-01-03 15:05:54
 * @LastEditTime: 2025-01-14 06:36:42
 * @FilePath: /code/blog/client-web/src/store/modules/useTagStore.ts
 * @Description:
 * Copyright (c) 2025 by tanxiang, All Rights Reserved.
 */
import { Tag } from '@/api/tags/tag.d';
import { defineStore } from 'pinia';

export const useTagStore = defineStore('tag', {
  state: () => ({
    tags: [] as Array<Tag>,
    tagArticleList: [] as Array<{ id: number; name: string; allCount: number; list: Array<any> }>
  }),
  getters: {
    getTags: (state) => state.tags,
    getTagArticleList: (state) => state.tagArticleList,
    getTagArticleListById: (state) => (id: number) => {
      return state.tagArticleList.find((item: any) => item.id == id);
    }
  },
  actions: {
    setTags(tags: Tag[]) {
      this.tags.splice(0, this.tags.length);
      this.tags.push(...tags);
    },
    setTagArticleList(TagArticle: { id: number; name: string; allCount: number; list: Array<any> }) {
      // 判断是否为空
      if (this.tagArticleList.length == 0) {
        return this.tagArticleList.push(TagArticle);
      }
      // 判断当前分类的文章列表是否存在
      let find = this.tagArticleList.find((item: any) => item.id == TagArticle.id);
      if (!find) {
        return this.tagArticleList.push(TagArticle);
      }
      // 判断当前的分类文章是否已经存储了
      let f = find.list.find((item: any) => item.id == TagArticle.list[0].id);
      if (!f) {
        find.list.push(...TagArticle.list);
      }
    }
  }
});
