<!--
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2025-01-15 07:34:48
 * @LastEditTime: 2025-01-17 06:53:46
 * @FilePath: /code/blog/client-admin/src/App.vue
 * @Description: 
 * Copyright (c) 2025 by tanxiang, All Rights Reserved. 
-->
<template>
  <n-config-provider
    :theme="global.getTheme === 'dark' ? darkTheme : lightTheme"
    :locale="zhCN"
    :date-locale="dateZhCN"
    class="wh-full"
    :theme-overrides="themeOverrides"
  >
    <router-view></router-view>
  </n-config-provider>
</template>

<script setup lang="ts">
import { zhCN, dateZhCN, darkTheme, lightTheme } from 'naive-ui';
import { GlobalThemeOverrides } from 'naive-ui';

const global = useGlobalStore();

const themeOverrides: GlobalThemeOverrides = {
  common: {
    primaryColor: '#409EFF',
    hoverColor: '#409EFF'
  },
  Input: {
    borderHover: '1px solid #409EFF',
    borderFocus: '1px solid #409EFF'
  }
};

watch(
  () => global.getTheme,
  (val: 'light' | 'dark' | 'auto') => {
    document.documentElement.classList.remove('dark');
    document.documentElement.classList.remove('light');
    document.documentElement.classList.remove('auto');
    document.documentElement.classList.add(val);
  },
  { immediate: true }
);
</script>

<style scoped lang="css"></style>
