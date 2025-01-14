<!--
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2024-12-10 10:26:23
 * @LastEditTime: 2025-01-03 14:54:38
 * @FilePath: /code/blog/client-web/src/layout/layout-main/Index.vue
 * @Description: 网页主体公共部分
 * Copyright (c) 2024 by tanxiang, All Rights Reserved. 
-->

<template>
  <div id="layout-main" class="flex-cs" :class="[setPadding]">
    <div class="max-w-[55rem] wh-full box-border px-[0.625rem]">
      <slot></slot>
    </div>
  </div>
</template>

<script setup lang="ts">
const route = useRoute();

const setPadding = computed(() => {
  return route.name && route.path !== '/404' ? 'pt-[3rem]' : 'p-0';
});
</script>

<style lang="css" scoped>
#layout-main {
  width: 100%;
  height: 100%;
  min-height: 45vh;
  box-sizing: border-box;
}
</style>
