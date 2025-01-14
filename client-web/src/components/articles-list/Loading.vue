<!--
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2025-01-01 10:25:38
 * @LastEditTime: 2025-01-08 16:06:16
 * @FilePath: /code/blog/client-web/src/components/articles-list/Loading.vue
 * @Description: 文章列表加载动画
 * Copyright (c) 2025 by tanxiang, All Rights Reserved. 
-->
<template>
  <el-card shadow="always" v-show="props.loading">
    <el-skeleton v-if="!props.phone" animated class="h-[15rem]">
      <template #template>
        <div class="flex flex-row gap-4">
          <el-skeleton-item variant="image" class="important-w-[15rem] important-h-[15rem]" />
          <div class="w-full h-full flex-1">
            <el-skeleton-item
              variant="p"
              v-for=" in [1, 2, 3, 4, 5, 6, 7]"
              class="important-h-[1.16rem] mb-[1rem] important-w-80% important-w-full [&]:first:important-w-80% [&]:last:important-w-60% [&]:last:important-mb-0"
            />
          </div>
        </div>
      </template>
    </el-skeleton>
    <el-skeleton v-else animated class="h-[18.75rem]">
      <template #template>
        <div class="flex flex-col gap-3">
          <el-skeleton-item variant="image" class="important-w-full important-h-[12rem]" />
          <div class="w-full h-full flex-1">
            <el-skeleton-item variant="p" v-for=" in [1,2,3,4]" class="important-h-[1rem] mb-[0.6rem] [&]:last:mb-0" />
          </div>
        </div>
      </template>
    </el-skeleton>
  </el-card>
</template>

<script setup lang="ts">
const props = defineProps({
  phone: {
    type: Boolean,
    default: false
  },
  loading: {
    type: Boolean,
    default: false
  }
});
</script>

<style lang="css" scoped></style>
