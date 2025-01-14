<!--
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2024-12-20 10:48:56
 * @LastEditTime: 2025-01-10 06:12:23
 * @FilePath: /code/blog/client-web/src/views/home/Home.vue
 * @Description: 
 * Copyright (c) 2024 by tanxiang, All Rights Reserved. 
-->
<template>
  <div class="wh-full">
    <div ref="anchors"></div>
    <home-topping :toppings="articles.getTopping"></home-topping>
    <articles-list :activeFunction="activeFunction" :fn="fn" :dataArray="articles.getArticles" </articles-list>
  </div>
</template>

<script setup lang="ts">
import api from '@/api';
const global = useGlobalStore();
const articles = useArticleStore();
const anchors = ref();

const [inViewport] = useInViewport(anchors);

const topping = useRequest(() => api.getToppings(), {
  onSuccess: () => articles.setTopping(topping.data.value)
});

const activeFunction = (index: number, limit: number) => {
  return api.getArticles(index, limit);
};

const fn = (data: any) => {
  articles.setArticles(data);
};

watchEffect(() => {
  global.setAnchor(inViewport.value ? inViewport.value : false);
});
</script>

<style lang="css" scoped></style>
