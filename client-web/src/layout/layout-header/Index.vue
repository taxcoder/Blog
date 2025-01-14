<!--
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2024-12-10 10:26:12
 * @LastEditTime: 2025-01-03 10:00:13
 * @FilePath: /code/blog/client-web/src/layout/layout-header/Index.vue
 * @Description: 
 * Copyright (c) 2024 by tanxiang, All Rights Reserved. 
-->
<template>
  <div id="layout-header" class="select-none" :class="[isOpenHeader]">
    <layout-header-bar></layout-header-bar>
    <layout-header-figure imageUrl="src/assets/1.jpg" :height="height" :theme="theme">
      <template #placeholder>
        <div class="bg-[#ccc] wh-full"></div>
      </template>
      <template #embellishment>
        <wave :theme="theme"></wave>
      </template>
    </layout-header-figure>
  </div>
</template>

<script setup lang="ts">
const route = useRoute();
const global = useGlobalStore();

const theme = computed(() => global.getTheme);

const isOpenHeader = computed(() => ({ hidden: route.name && route.path === '/404' }));

const height = computed(() => (route.name && route.path === '/' ? '--screen-height' : '--screen-height-not-full'));
</script>

<style lang="css" scoped></style>
