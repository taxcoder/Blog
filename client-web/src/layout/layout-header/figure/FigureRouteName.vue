<!--
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2024-12-20 18:34:28
 * @LastEditTime: 2025-01-09 08:51:05
 * @FilePath: /code/blog/client-web/src/layout/layout-header/figure/FigureRouteName.vue
 * @Description: 
 * Copyright (c) 2024 by tanxiang, All Rights Reserved. 
-->

<template>
  <div class="z-99 font-size-[2.5rem] flex-cc w-full pt-[2rem] route-active">{{ name }}</div>
</template>

<script setup lang="ts">
const route = useRoute();
const global = useGlobalStore();

const name = computed(() => {
  return route.params.name ? route.params.name : route.name ? route.name : global.getFigureName;
});

watch(
  () => route,
  () => {
    const div: any = document.querySelector('.route-active');
    //div设置opacity为0
    div.style.opacity = '0';
    div.classList.remove('route-active');
    useTimeout(() => div.classList.add('route-active'), 100);
    // 每次切换路由，刷新figureName
    global.setFigureName('');
  },
  { deep: true }
);
</script>

<style lang="css" scoped></style>
