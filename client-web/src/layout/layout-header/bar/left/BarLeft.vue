<!--
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2024-12-23 09:31:48
 * @LastEditTime: 2025-01-03 17:45:59
 * @FilePath: /code/blog/client-web/src/layout/layout-header/bar/left/BarLeft.vue
 * @Description: 
 * Copyright (c) 2024 by tanxiang, All Rights Reserved. 
-->
<template>
  <div
    class="transition-all-300 transform-translate-x-0 md:max-lg:important-transform-translate-x-[-4.375rem] z-1000 absolute"
  >
    <div
      class="left-active fixed left-0 z-999 grid grid-cols-1 min-w-[var(--height)] w-[var(--height)] h-[var(--height)] p-[0.9375rem] max-md:pl-0 box-border"
    >
      <!-- 头像 -->
      <div
        class="flex-cc wh-full rounded-50% cursor-pointer overflow-hidden [&_div.go-home]:hover:translate-x-0 relative"
      >
        <transition name="fade" mode="out-in">
          <layout-header-bar-left-pc-avatar v-if="!isPhone"></layout-header-bar-left-pc-avatar>
          <layout-header-bar-left-phone-menu v-else></layout-header-bar-left-phone-menu>
        </transition>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
const isPhone = inject('isPhone');
</script>

<style lang="css" scoped>
.left-active {
  animation: left-active 0.5s ease-in-out forwards;
}

@keyframes left-active {
  0% {
    opacity: 0;
    transform: translateX(calc(-1 * var(--translateX-move)));
  }

  100% {
    opacity: 1;
    transform: translateX(0);
  }
}

/* 下面会解释这些 class 是做什么的 */
.fade-enter-active,
.fade-leave-active {
  transition: all 0.5s ease;
}
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
  transform: translateX(-30px);
}
</style>
