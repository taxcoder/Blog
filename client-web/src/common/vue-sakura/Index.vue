<!--
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2025-01-07 10:40:25
 * @LastEditTime: 2025-01-07 13:37:03
 * @FilePath: /code/blog/client-web/src/common/vue-sakura/Index.vue
 * @Description: 
 * Copyright (c) 2025 by tanxiang, All Rights Reserved. 
-->
<template><div></div></template>

<script setup>
import { startSakura } from './sakura';

onMounted(() => {
  nextTick(() => {
    startSakura();
  });
});
</script>

<style scoped></style>
