<!--
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2024-12-20 10:52:38
 * @LastEditTime: 2025-01-03 12:34:24
 * @FilePath: /code/blog/client-web/src/views/404/404.vue
 * @Description: 
 * Copyright (c) 2024 by tanxiang, All Rights Reserved. 
-->
<script setup lang="ts">
const router = useRouter();
const toHome = () => {
  router.replace('/');
};
</script>

<template>
  <div class="h-[80vh] flex-cc">
    <div class="w-full">
      <not-found></not-found>
      <animation-button :fn="toHome"></animation-button>
    </div>
  </div>
</template>

<style lang="css" scoped></style>
