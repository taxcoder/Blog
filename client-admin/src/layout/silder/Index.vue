<!--
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2025-01-17 07:39:39
 * @LastEditTime: 2025-01-17 08:48:35
 * @FilePath: /code/blog/client-admin/src/layout/silder/Index.vue
 * @Description: 
 * Copyright (c) 2025 by tanxiang, All Rights Reserved. 
-->

<template>
  <n-menu accordion :collapsed="collapsed" :options="menus" />
</template>

<script setup lang="ts">
import { menus } from '@/constant/menu';

const collapsed = ref(false);
</script>

<style lang="css" scoped></style>
