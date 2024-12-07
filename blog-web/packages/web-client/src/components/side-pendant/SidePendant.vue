<!--
 * @Author: tanxiang 1571922819@qq.com
 * @Date: 2023-06-20 15:08:58
 * @Description:
 * @LastEditTime: 2023-12-04 19:49:00
 * @LastEditors: tanxiang 1571922819@qq.com
 * @FilePath: \blog\packages\web-client\src\components\side-pendant\SidePendant.vue
 * @copyright: Copyright (c) 2023 by 1571922819@qq.com, All Rights Reserved.
-->
<template>
  <div id="pendant" class="fixed bottom-[25px] flex items-end flex-col z-100 right-[17px] opacity-100">
    <div v-if="isActiveDirectory && isDirectory" :class="setAnimationClass" class="button-menu">
      <directory-sider />
    </div>
    <div v-if="isActive" class="button-menu-item flex items-end flex-col relative" :class="setAnimationClass">
      <change-page />
      <change-theme />
    </div>
    <div :class="setAnimationClass" class="button-menu flex items-end flex-col relative">
      <back-home v-if="getCurrentRoute" />
      <open-menu />
      <back-top />
    </div>
  </div>
</template>

<script setup lang="ts">
import { useGlobalStore } from '@/stores/global';
import { useArticleStore } from '@/stores/article';

import { routeName } from '@/enum';

const global = useGlobalStore();
const articleStore = useArticleStore();
const route = useRoute();

const status: any = inject('status');
// 侧边栏菜单激活状态
const isActive = ref<boolean>(false);

provide('isBack', () => setBack());
provide('isMenuItemActive', () => setMenuItemActive());
// 收缩侧边栏
const setBack = () => (isActive.value = false);
// 侧边栏菜单激活
const setMenuItemActive = () => (isActive.value = !isActive.value);
// 当前页面不是首页
const getCurrentRoute = computed(() => route.meta.name !== routeName.home);
// 页面宽度是否超过1200
const juiceExceedWidth = computed(() => status.width > 1200);
// 设置动画类名
const setAnimationClass = computed(() => {
  return {
    scroll: global.getIsContract,
  };
});

const isDirectory = computed(() => {
  return articleStore.getDirectory?.length > 0;
});

const isActiveDirectory = computed(() => {
  return !juiceExceedWidth.value && !route.meta.isDraw && !route.meta.showUserInfo && route.name !== 'ArticleList';
});
</script>

<style scoped lang="css">
@import '@/styles/dark.css';
.button-menu-item,
.button-menu {
  opacity: 1;
  transition: transform 0.25s linear, opacity 0.25s linear;
  transform: translateX(0);
}

.scroll {
  transform: translateX(35px);
  opacity: 0.7;
}
</style>