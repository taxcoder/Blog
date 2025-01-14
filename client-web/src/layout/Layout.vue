<!--
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2024-12-10 10:25:49
 * @LastEditTime: 2025-01-13 12:28:15
 * @FilePath: /code/blog/client-web/src/layout/Layout.vue
 * @Description: 
 * Copyright (c) 2024 by tanxiang, All Rights Reserved. 
-->
<template>
  <div class="wh-full dark:bg-[rgb(40,40,40)]">
    <layout-header></layout-header>
    <layout-main>
      <router-view v-slot="{ Component }">
        <keep-alive :exclude="['TagInfo', 'CategoryInfo', 'Article']">
          <component :is="Component" />
        </keep-alive>
      </router-view>
    </layout-main>
    <layout-footer></layout-footer>
  </div>
</template>

<script setup lang="ts"></script>

<style lang="css" scoped></style>
