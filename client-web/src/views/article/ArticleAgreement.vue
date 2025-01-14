<!--
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2025-01-11 06:48:12
 * @LastEditTime: 2025-01-11 07:37:21
 * @FilePath: /code/blog/client-web/src/views/article/ArticleAgreement.vue
 * @Description: 
 * Copyright (c) 2025 by tanxiang, All Rights Reserved. 
-->

<template>
  <el-card class="wh-full mt-4" shadow="always" body-class="important-p-0 rounded-[0.3125rem]">
    <div
      class="color-[#7B7B7B] leading-6 font-size-[0.8125rem] border-l-[0.125rem] border-l-solid border-l-color-[#354b58] p-3 rounded-[0.3125rem]"
    >
      <div>
        本文由
        <a herf="/" class="color-blue cursor-pointer">{{ props.articleInfo.author }}</a>
        创作，如果您觉得本文不错，请随意赞赏
      </div>
      <div>
        采用
        <a href="https://creativecommons.org/licenses/by/4.0/" class="color-blue cursor-pointer"> 知识共享署名4.0 </a>
        国际许可协议进行许可
      </div>
      <span>本站文章除注明转载/出处外，均为本站原创或翻译，转载前请务必署名</span>
      <div>
        原文链接：<a :href="currentPath" class="color-blue cursor-pointer">{{ currentPath }}</a>
      </div>
      <div>最后更新于：{{ dateUtils.format(props.articleInfo.updateTime, 'YYYY-MM-DD hh:mm:ss') }}</div>
    </div>
  </el-card>
</template>

<script setup lang="ts">
import dateUtils from '@/utils/dateUtils';

const props = defineProps({
  articleInfo: {
    type: Object,
    required: true
  }
});

const currentPath = computed(() => {
  return window.location.href;
});
</script>

<style lang="css" scoped></style>
