<!--
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2024-12-25 07:13:17
 * @LastEditTime: 2025-01-10 12:00:42
 * @FilePath: /code/blog/client-web/src/layout/layout-header/bar/right/Theme.vue
 * @Description: 
 * Copyright (c) 2024 by tanxiang, All Rights Reserved. 
-->

<template>
  <div class="flex-cc w-[2.5rem] h-[2.5rem] rounded-50% cursor-pointer" @click="global.toggle()">
    <transition name="theme" mode="out-in">
      <div>
        <icon-sun-one
          v-show="props.theme"
          :theme="isPhone ? 'filled' : 'multi-color'"
          size="1.75rem"
          :fill="isPhone ? '#f5a623' : ['#333', '#fff', '#FFF', '#43CCF8']"
          strokeLinejoin="bevel"
          strokeLinecap="square"
        />
        <icon-moon
          v-show="!props.theme"
          :theme="isPhone ? 'filled' : 'multi-color'"
          size="1.65rem"
          :fill="isPhone ? '#f5a623' : ['#f5a623', '#f5a623', '#FFF', '#43CCF8']"
          strokeLinejoin="bevel"
          strokeLinecap="square"
        />
      </div>
    </transition>
  </div>
</template>

<script setup lang="ts">
const global = useGlobalStore();
const isPhone = inject('isPhone');

const props = defineProps({
  theme: { type: Boolean, default: false }
});
</script>

<style lang="css" scoped>
.theme-enter-active,
.theme-leave-active {
  transition: all 0.15s ease;
}
.theme-enter-from,
.theme-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}
</style>
