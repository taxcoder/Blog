<!--
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2025-01-08 15:51:01
 * @LastEditTime: 2025-01-11 10:16:02
 * @FilePath: /code/blog/client-web/src/views/article/Article.vue
 * @Description: 
 * Copyright (c) 2025 by tanxiang, All Rights Reserved. 
-->

<template>
  <article-info :articleInfo="articleInfo"></article-info>
  <div class="mt-[1.5rem]">
    <page-loading-light class="important-h-[18rem] rounded-[0.4375rem]" v-if="loading"></page-loading-light>
    <el-empty v-else-if="articleInfo.id === -1" description="文章不存在"></el-empty>
    <div v-else class="wh-full">
      <article-content :articleInfo="articleInfo"></article-content>
      <article-agreement :articleInfo="articleInfo"></article-agreement>
      <el-card class="wh-full mt-4" shadow="always" body-class="important-p-3  rounded-[0.3125rem]">
        <vue-artalk></vue-artalk>
      </el-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import ArticleInfo from './ArticleInfo.vue';
import ArticleAgreement from './ArticleAgreement.vue';
import ArticleContent from './ArticleContent.vue';
import { Article } from '@/api/articles/article.d';

const route = useRoute();

const loading = ref<boolean>(true);
const articleInfo = reactive<Article>({
  id: -1,
  title: '',
  description: '',
  category: { id: -1, name: '' },
  tags: [],
  likeCount: 0,
  content: '',
  cover: '',
  author: '',
  createTime: 0,
  updateTime: 0
});

onMounted(() => {
  document.querySelector('#layout-header')?.scrollIntoView({ behavior: 'instant' });
});

useRequest(() => api.getArticleById(parseInt(route.params.id as string)), {
  onSuccess: (res) => {
    articleInfo.id = res.id;
    articleInfo.title = res.title;
    articleInfo.description = res.description;
    articleInfo.category = res.category;
    articleInfo.tags = res.tags;
    articleInfo.likeCount = res.likeCount;
    articleInfo.content = res.content;
    articleInfo.cover = res.cover;
    articleInfo.author = res.author;
    articleInfo.createTime = res.createTime;
    articleInfo.updateTime = res.updateTime;
  },
  onError: (err) => {
    console.log(err);
  },
  onFinally: () => {
    loading.value = false;
  }
});
</script>

<style lang="css" scoped></style>
