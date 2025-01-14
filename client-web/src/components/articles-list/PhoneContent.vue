<!--
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2025-01-03 16:15:11
 * @LastEditTime: 2025-01-11 09:12:41
 * @FilePath: /code/blog/client-web/src/components/articles-list/PhoneContent.vue
 * @Description: 
 * Copyright (c) 2025 by tanxiang, All Rights Reserved. 
-->
<template>
  <div class="flex wh-full box-border flex-col">
    <div class="flex-[1.25] overflow-hidden">
      <el-image :src="props.content.cover" fit="cover" class="cursor-pointer wh-full wh-full">
        <template #placeholder>
          <page-loading-light class="h-full" width="10rem" height="10rem" :scale="0.5"></page-loading-light>
        </template>
      </el-image>
    </div>
    <div class="p-[0.75rem] flex-1 flex flex-col items-end">
      <div class="flex w-full justify-end">
        <div class="mr-[0.5rem] flex items-center">
          <icon-calendar-dot theme="outline" size="0.685rem" fill="#999999" />
          <span class="font-size-[0.85rem] color-[#999999] ml-[0.15rem]">
            {{ timeToStr(props.content.createTime) }}
          </span>
        </div>
      </div>
      <div class="flex-[2] w-full flex-cc">
        <h3 class="m-0 truncate dark:important-color-[#ccc]">{{ props.content.title }}</h3>
      </div>
      <div class="flex-[4] w-full">
        <el-text
          line-clamp="3"
          class="important-color-[#282828] dark:important-color-[#b3b3b3] leading-5"
          style="font-variant: full-width"
        >
          {{ props.content.description }}
        </el-text>
      </div>
      <div class="flex justify-between w-full items-center">
        <a @click="toJump()">
          <icon-mark theme="filled" size="0.9rem" fill="#999999" />
          <span class="font-size-[0.85rem] color-[#999999] ml-[0.15rem]">{{ props.content.category.name }} </span>
        </a>
        <div class="flex items-center">
          <div class="mr-[0.5rem] flex items-center">
            <icon-read-book theme="outline" size="0.85rem" fill="#999999" />
            <span class="font-size-[0.9rem] color-[#999999] ml-[0.15rem]">
              <span class="artalk-pv-count" :data-page-key="`/article/${props.content.id}`">0</span>
            </span>
          </div>
          <div class="mr-[0.5rem] flex items-center">
            <icon-like theme="outline" size="0.85rem" fill="#999999" />
            <span class="font-size-[0.85rem] color-[#999999] ml-[0.15rem]">{{ props.content.likeCount }}</span>
          </div>
          <div class="mr-[0.5rem] flex items-center">
            <icon-comment class="relative top-[1px]" theme="outline" size="0.8rem" fill="#999999" />
            <span class="font-size-[0.9rem] color-[#999999] ml-[0.15rem]">
              <span class="artalk-comment-count" :data-page-key="`/article/${props.content.id}`">0</span>
            </span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import dateUtils from '@/utils/dateUtils';
import { loadCountWidget } from '@/components/vue-artalk';

const router = useRouter();

const props = defineProps({
  content: { type: Object, default: () => {} },
  index: { type: Number, default: -1 }
});

onMounted(() => loadCountWidget());
onActivated(() => loadCountWidget());

const timeToStr = (time: number) => {
  return dateUtils.format(time, 'YYYY-MM-dd');
};

const toJump = () => {
  router.push(`/categorys/${props.content.category.name}`);
};
</script>

<style lang="css" scoped></style>
