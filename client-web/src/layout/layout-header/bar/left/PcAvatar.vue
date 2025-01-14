<!--
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2024-12-25 10:40:08
 * @LastEditTime: 2024-12-31 18:25:43
 * @FilePath: /code/blog/client-web/src/layout/layout-header/bar/left/PcAvatar.vue
 * @Description: 
 * Copyright (c) 2024 by tanxiang, All Rights Reserved. 
-->
<template>
  <div class="wh-full">
    <!-- 回到首页 -->
    <div
      class="flex-cc wh-full absolute rounded-50% bg-[var(--theme-global-color)] transition-transform-350 translate-x-[-2.5rem] top-0 go-home z-999"
    >
      <icon-home-two theme="filled" size="1.5rem" :fill="global.getTheme ? '#fff' : '#333'" @click="goHome()" />
    </div>
    <!-- 头像 -->
    <el-avatar class="important-w-full">
      <template #default>
        <img v-show="isOpen" :src="global.getInfo.avatar" class="absolute top-0 rounded-50%" />
      </template>
      <template #error>
        <icon-application-effect theme="filled" size="24" fill="#333" />
      </template>
    </el-avatar>
  </div>
</template>

<script setup lang="ts">
const router = useRouter();
const global = useGlobalStore();
const goHome = () => router.push('/');

const isOpen = computed(() => global.getInfo && !!global.getInfo.avatar);
</script>

<style lang="css" scoped></style>
