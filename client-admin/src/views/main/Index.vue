<!--
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2025-01-15 08:09:06
 * @LastEditTime: 2025-01-17 08:05:42
 * @FilePath: /code/blog/client-admin/src/views/main/Index.vue
 * @Description: 
 * Copyright (c) 2025 by tanxiang, All Rights Reserved. 
-->
<template>
  <n-layout has-sider class="wh-full">
    <n-layout-sider class="max-w-[12.5rem]"> <layout-silder></layout-silder> </n-layout-sider>
    <n-layout>
      <n-layout-header class="h-[6.25rem] bg-cyan"> <layout-header></layout-header></n-layout-header>
      <n-layout-content class="h-[calc(100%-6.25rem)] bg-fuchsia">
        <layout-main>
          <router-view></router-view>
        </layout-main>
      </n-layout-content>
    </n-layout>
  </n-layout>
</template>

<script setup lang="ts">
import LayoutSilder from '@/layout/silder/Index.vue';
import LayoutHeader from '@/layout/header/Index.vue';
import LayoutMain from '@/layout/main/Index.vue';
</script>

<style lang="css" scoped></style>
