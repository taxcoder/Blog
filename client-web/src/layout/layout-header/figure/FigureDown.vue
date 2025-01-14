<!--
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2024-12-24 06:49:25
 * @LastEditTime: 2024-12-28 07:45:28
 * @FilePath: /code/blog/client-web/src/layout/layout-header/figure/FigureDown.vue
 * @Description: 
 * Copyright (c) 2024 by tanxiang, All Rights Reserved. 
-->
<template>
  <div v-show="status" class="scroll-down absolute bottom-10 cursor-pointer" @click="scrollTo()">
    <icon-down theme="filled" size="75" fill="#FFF" />
  </div>
</template>

<script setup lang="ts">
const route = useRoute();
const isPhone: any = inject('isPhone');

const status = computed(() => route.name && route.path === '/' && !isPhone.value);
const scrollTo = () => {
  document.querySelector('#layout-main')?.scrollIntoView({ behavior: 'smooth' });
};
</script>

<style lang="css" scoped>
.scroll-down {
  animation: downBounce 2s infinite;
}

@keyframes downBounce {
  0%,
  100% {
    transform: translateY(-25%);
    animation-timing-function: cubic-bezier(0.8, 0, 1, 1);
  }
  50% {
    transform: translateY(0);
    animation-timing-function: cubic-bezier(0, 0, 0.2, 1);
  }
}
</style>
