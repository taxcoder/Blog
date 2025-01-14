<!--
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2025-01-11 06:43:20
 * @LastEditTime: 2025-01-11 08:58:14
 * @FilePath: /code/blog/client-web/src/views/article/ArticleInfo.vue
 * @Description: 
 * Copyright (c) 2025 by tanxiang, All Rights Reserved. 
-->
<template>
  <teleport defer to="#figure" v-if="route.path.indexOf('/article/') !== -1">
    <div class="h-[22.5rem] max-md:h-[17.5rem] w-full relative">
      <el-image
        v-if="props.articleInfo && props.articleInfo.cover"
        :src="props.articleInfo.cover"
        fit="cover"
        class="wh-full filter-brightness-55"
      >
      </el-image>
      <div class="wh-full blur-sm bg-[#ccc]" v-else></div>
      <div
        class="wh-full absolute top-0 left-0 flex-cc important-color-white dark:color-[#f2f2f2]"
        v-show="props.articleInfo && props.articleInfo.title"
      >
        <div
          class="max-w-[55rem] wh-full flex items-end max-md:items-center md:justify-start max-md:justify-center md:pb-10 box-border md:pl-[1.5625rem]"
        >
          <div class="flex flex-col">
            <h1 class="color-white max-md:font-size-[2rem] font-size-[2.25rem] max-md:text-center max-md:px-10">
              {{ props.articleInfo.title }}
            </h1>
            <div
              class="max-md:font-size-[0.85rem] font-size-[0.9rem] inline-flex flex-wrap justify-center leading-6 max-md:px-10"
            >
              <span> {{ props.articleInfo.author || 'Author' }}</span>
              <span class="mx-[0.25rem]">·</span>
              <span class="whitespace-nowrap"
                >发布于{{ dateUtils.format(props.articleInfo.createTime, 'YYYY年MM月DD日') }}</span
              >
              <span class="mx-[0.25rem]">·</span>
              <div class="inline-flex whitespace-nowrap">
                <span class="artalk-pv-count" :data-page-key="`/article/${props.articleInfo.id}`">0</span>
                <span>次阅读</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </teleport>
</template>

<script setup lang="ts">
const route = useRoute();

import dateUtils from '@/utils/dateUtils';

const props = defineProps({
  articleInfo: { type: Object, required: true }
});
</script>

<style lang="css" scoped></style>
