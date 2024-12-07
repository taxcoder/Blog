<!--
 * @Author: tanxiang 1571922819@qq.com
 * @Date: 2023-06-15 10:07:04
 * @Description:
 * @LastEditTime: 2023-12-10 13:06:37
 * @LastEditors: tanxiang 1571922819@qq.com
 * @FilePath: \blog\packages\web-client\src\components\home-page\nav\PageNavigation.vue
 * @copyright: Copyright (c) 2023 by 1571922819@qq.com, All Rights Reserved.
-->
<template>
  <div
    id="page-nav"
    :class="getNavClass"
    @mouseover="info.hover = true"
    @mouseleave="info.hover = false"
    class="flex fixed w-full z-1000 box-border min-w-[360px] translate-y-0 important-transition-transform hover:important-translate-y-0"
  >
    <left-navigation :isActive="juiceIsActive" />
    <right-navigation />
  </div>
</template>

<script setup lang="ts">
import { minWidth } from '@/config';

import { computed } from 'vue';

import { useGlobalStore } from '@/stores/global';
import { useRoute } from 'vue-router';

import LeftNavigation from '@/components/home-page/nav/LeftNavigation.vue';
import RightNavigation from '@/components/home-page/nav/RightNavigation.vue';

import { usePage } from '@tanxiang/utils';
import { count } from '@tanxiang/apis';

const route = useRoute();

const info = reactive<{
  show: number;
  count: number;
  before: number | null;
  hover: boolean;
  isExceed: boolean;
  isStop: boolean;
}>({
  show: 0,
  count: 0,
  before: null,
  hover: false,
  isExceed: false,
  isStop: false,
});

const timer = ref<any>();

const global = useGlobalStore();
const status: any = inject('status');

// 判断是否需要激活
const juiceIsActive = computed(() => {
  if (status.scroll !== 0 && info.count === 0) info.count++;
  return status.scroll > 0;
});

const juiceIsPhone = computed(() => {
  return status.width <= minWidth || usePage().device();
});
// 判断是否移除了激活
const juiceIsRemoveActive = computed(() => count.value > 0);

const getNavClass = computed(() => {
  return {
    'important-translate-y-[-70px]': info.isExceed && !info.hover && !info.isStop && !juiceIsPhone.value,
    active: juiceIsActive.value,
    'pb-[10px] bg-[var(--current-color)] backdrop-blur-[50px]': juiceIsActive.value && juiceIsPhone.value,
    'pc-active': juiceIsActive.value && !juiceIsPhone.value,
    'remove-active': !juiceIsActive.value && juiceIsRemoveActive.value && !juiceIsPhone.value,
    'backdrop-opacity-none bg-white/0': !juiceIsActive.value && juiceIsRemoveActive.value && juiceIsPhone.value,
    'px-[40px] pt-[20px] pb-[10px]': !juiceIsRemoveActive.value && !juiceIsPhone.value,
    'drop-shadow-[0_1px_5px_rgba(204,204,204,1)] dark:drop-shadow-[0_1px_5px_rgba(0,0,0,1)]':
      info.isExceed && (juiceIsPhone.value || info.hover || info.isStop),
  };
});

watch(
  () => route.name,
  () => (count.value = 0),
  { immediate: true }
);

watch(
  () => status.scroll,
  (newValue: number) => {
    info.isStop = true;
    let start = document.getElementById('start');
    if (start) {
      info.show = newValue - usePage().getOffsetTop(start);
      info.isExceed = info.show > 0;
    }
    if (timer.value) clearTimeout(timer.value);
    timer.value = setTimeout(() => (info.isStop = false), 500);
  },
  { deep: true, immediate: true }
);
</script>

<style scoped>
#page-nav {
  &.pc-active {
    will-change: transform;
    animation: useActive 0.3s linear forwards;
  }
  &.remove-active {
    will-change: transform;
    animation: removeActive 0.3s linear forwards;
  }
}

.dark #page-nav {
  &.pc-active {
    background-color: var(--dark-current-color) !important;
  }
  &.remove-active {
    background-color: rgba(255, 255, 255, 0) !important;
  }
}

@keyframes removeActive {
  100% {
    padding: 20px 40px 10px 40px;
    backdrop-filter: none;
    background-color: rgba(255, 255, 255, 0);
  }
  0% {
    padding: 0 0 10px 0;
    background-color: var(--current-color);
    backdrop-filter: blur(50px);
  }
}

@keyframes useActive {
  0% {
    padding: 20px 40px 10px 40px;
    background-color: rgba(255, 255, 255, 0);
  }
  100% {
    padding: 0 0 10px 0;
    background-color: var(--current-color);
  }
}
</style>