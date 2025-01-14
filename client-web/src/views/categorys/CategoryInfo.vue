<!--
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2025-01-03 14:40:10
 * @LastEditTime: 2025-01-14 06:15:52
 * @FilePath: /code/blog/client-web/src/views/categorys/CategoryInfo.vue
 * @Description: 
 * Copyright (c) 2025 by tanxiang, All Rights Reserved. 
-->

<template>
  <div>
    <articles-list :activeFunction="activeFunction" :fn="fn" :dataArray="array"></articles-list>
  </div>
</template>

<script setup lang="ts">
import api from '@/api';

const route = useRoute();
const global = useGlobalStore();
const category = useCategoryStore();

const categoryArticleList = reactive<{ allCount: number; list: any[] }>({
  allCount: -1,
  list: []
});

const fn = (data: any) => {
  category.setCategoriyArticleList(data);
  global.setFigureName(data.name);
};

const activeFunction = (index: number, limit: number) => {
  let articles = category.getCategoriyArticleListById(parseInt(route.params.id as string));
  let size = 0;
  // 如果没有存储，则直接请求后台数据，否则从仓库中取数据
  if (!articles || articles.list.length === 0) {
    return api.getArticleByCategoryId(parseInt(route.params.id as string), index, limit);
  }
  // 获取存储了多少的数据
  if (articles && articles.list.length > 0) {
    // 从仓库中取的数据的长度
    size = Math.ceil(articles.list.length / limit);
  }
  // 当前的索引小于等于存储的长度，则直接从仓库中取数据，否则请求后台数据
  if (index <= size) {
    return {
      id: articles.id,
      name: articles.name,
      allCount: articles.allCount,
      list: articles.list.slice((index - 1) * limit, index * limit)
    };
  }
  return api.getArticleByCategoryId(parseInt(route.params.id as string), index, limit);
};

const array = computed(() => {
  let articles = category.getCategoriyArticleListById(parseInt(route.params.id as string));
  return articles && articles.list.length > 0 ? articles : categoryArticleList;
});
</script>

<style lang="css" scoped></style>
