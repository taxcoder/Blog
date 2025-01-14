<!--
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2024-12-25 07:14:10
 * @LastEditTime: 2025-01-10 11:59:01
 * @FilePath: /code/blog/client-web/src/layout/layout-header/bar/right/Search.vue
 * @Description: 
 * Copyright (c) 2024 by tanxiang, All Rights Reserved. 
-->

<template>
  <div class="flex-cc w-[2.5rem] h-[2.5rem] cursor-pointer" @click="() => {}">
    <icon-search theme="outline" size="1.75rem" :fill="isPhone ? '#f5a623' : props.theme ? '#333' : '#f5a623'" />
  </div>
</template>

<script setup lang="ts">
const isPhone = inject('isPhone');

const props = defineProps({
  theme: { type: Boolean, default: false }
});
</script>

<style lang="css" scoped></style>
