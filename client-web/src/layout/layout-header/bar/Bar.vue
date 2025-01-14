<!--
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2024-12-20 08:06:25
 * @LastEditTime: 2025-01-10 06:13:53
 * @FilePath: /code/blog/client-web/src/layout/layout-header/bar/Bar.vue
 * @Description: 
 * Copyright (c) 2024 by tanxiang, All Rights Reserved. 
-->

<template>
  <div id="layout-header-bar">
    <layout-header-bar-left></layout-header-bar-left>
    <layout-header-bar-right></layout-header-bar-right>
    <!-- 顶部栏 -->
    <div
      id="layout-header-bar-item"
      @mouseenter="mouseEnter"
      @mouseleave="mouseLeave"
      class="bg-white dark:bg-[#252525] w-full h-[var(--height)] fixed z-100 top-[var(--layout-header-bar-top)]"
      :class="[addAnimation]"
    >
      <layout-header-bar-item v-if="!isPhone"></layout-header-bar-item>
    </div>
  </div>
</template>

<script setup lang="ts">
const route = useRoute();
const global = useGlobalStore();

const percentage: any = inject('percentage');
const isPhone: any = inject<boolean>('isPhone');

const previous = usePrevious(percentage);
// 监听鼠标移入和鼠标移出
const mouseEnter = (event: any) => {
  if (!openHeaderBar.value) event.target.classList.toggle('bar-active', true);
};
const mouseLeave = (event: any) => {
  if (!openHeaderBar.value) event.target.classList.toggle('bar-active', false);
};

const openHeaderBar = computed(() => {
  if (isPhone.value) return false;

  if (route.path === '/' && percentage.value > 0 && global.getAnchor) {
    return true;
  }

  if (previous.value && percentage.value > 0 && previous.value > percentage.value) {
    return true;
  }

  if (percentage.value === 0 && route.path !== '/' && (route.name || route.params.name || route.params.id)) {
    return true;
  }

  return false;
});

const addAnimation = computed(() => ({ 'bar-active': openHeaderBar.value }));
</script>

<style lang="css" scoped>
#layout-header-bar {
  --height: 4.375rem;
  --translateX-move: 0.625rem;
}

#layout-header-bar-item {
  opacity: 0;
  transition: opacity 0.65s;
  box-shadow: var(--layout-header-bar-box-shadow-light);
}

.dark#layout-header-bar-item {
  box-shadow: var(--layout-header-bar-box-shadow-dark);
}

.bar-active {
  opacity: 1 !important;
}
</style>
