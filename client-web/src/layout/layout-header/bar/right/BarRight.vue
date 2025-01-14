<!--
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2024-12-23 09:34:21
 * @LastEditTime: 2025-01-07 14:19:15
 * @FilePath: /code/blog/client-web/src/layout/layout-header/bar/right/BarRight.vue
 * @Description: 
 * Copyright (c) 2024 by tanxiang, All Rights Reserved. 
-->
<template>
  <div class="right-active fixed right-0 z-999">
    <!-- 搜索 -->
    <div class="grid-cc grid-cols-4 h-[var(--height)] px-[0.9375rem] max-md:pr-2 box-border">
      <layout-header-bar-right-return-top class="col-span-2" v-if="!isPhone"></layout-header-bar-right-return-top>
      <layout-header-bar-right-search :theme="theme" class="col-start-3"></layout-header-bar-right-search>
      <layout-header-bar-right-theme :theme="theme" class="col-start-4"></layout-header-bar-right-theme>
    </div>
  </div>
</template>

<script setup lang="ts">
const isPhone = inject('isPhone');
const global = useGlobalStore();

const theme = computed(() => global.getTheme);
</script>

<style lang="css" scoped>
.right-active {
  animation: right-active 0.5s ease-in-out forwards;
}

@-webkit-keyframes right-active {
  0% {
    opacity: 0;
    transform: translateX(var(--translateX-move));
  }

  100% {
    opacity: 1;
    transform: translateX(0);
  }
}

@keyframes right-active {
  0% {
    opacity: 0;
    transform: translateX(var(--translateX-move));
  }

  100% {
    opacity: 1;
    transform: translateX(0);
  }
}
</style>
