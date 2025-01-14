<!--
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2025-01-02 09:59:41
 * @LastEditTime: 2025-01-11 09:19:47
 * @FilePath: /code/blog/client-web/src/components/articles-list/PcContent.vue
 * @Description:
 * Copyright (c) 2025 by tanxiang, All Rights Reserved. 
-->
<template>
  <div class="flex wh-full box-border" :class="[setOrder]">
    <div class="flex-[1.25] overflow-hidden" :class="[setClipPath]">
      <el-image :src="props.content.cover" fit="cover" class="cursor-pointer wh-full article-cover">
        <template #placeholder>
          <page-loading-light></page-loading-light>
        </template>
      </el-image>
    </div>
    <div class="p-[0.75rem] flex-1 flex flex-col" :class="[setItems]">
      <div class="flex w-full" :class="[setJustify]">
        <div class="mr-[0.5rem] flex items-center">
          <icon-calendar-dot theme="outline" size="0.7rem" fill="#999999" />
          <span class="font-size-[0.775rem] color-[#999999] ml-[0.15rem]">
            {{ timeToStr(props.content.createTime) }}
          </span>
        </div>
      </div>
      <div class="flex-[1] w-full flex items-center justify-start">
        <h3 class="hover:color-[#f5a623] dark:important-color-[#ccc] cursor-pointer m-0 truncate">
          {{ props.content.title }}
        </h3>
      </div>
      <div class="flex-[4] w-full">
        <el-text
          line-clamp="8"
          class="important-color-[#282828] dark:important-color-[#b3b3b3] leading-5"
          style="font-variant: full-width"
        >
          {{ props.content.description }}
        </el-text>
      </div>
      <div class="flex justify-between w-full items-center" :class="[setOrder]">
        <a class="category-name [&_span]:hover:color-[#f5a623] cursor-pointer" @click.stop="toJump()">
          <icon-mark theme="filled" size="0.95rem" fill="#999999" />
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
            <icon-like theme="outline" size="0.825rem" fill="#999999" />
            <span class="font-size-[0.85rem] color-[#999999] ml-[0.15rem]">{{ props.content.likeCount }}</span>
          </div>
          <div class="mr-[0.5rem] flex items-center">
            <icon-comment class="relative top-[1px]" theme="outline" size="0.775rem" fill="#999999" />
            <span class="font-size-[0.85rem] color-[#999999] ml-[0.15rem]">
              <span class="artalk-comment-count" :data-page-key="`/article/${props.content.id}`">0</span>
            </span>
          </div>
          <div class="flex items-center">
            <span class="font-size-[0.9rem] color-[#999999] ml-[0.15rem]">{{ props.content.author }}</span>
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
  return dateUtils.format(time, 'YYYY-MM-DD');
};

const toJump = () => {
  router.push(`/category/${props.content.category.id}`);
};

const setOrder = computed(() => {
  let status = props.index % 2 === 0;
  return { 'flex-row-reverse': !status, 'flex-row': status };
});

const setItems = computed(() => {
  let status = props.index % 2 === 0;
  return { 'items-end': status, 'items-start': !status };
});

const setJustify = computed(() => {
  let status = props.index % 2 === 0;
  return { 'justify-end': status, 'justify-start': !status };
});

const setClipPath = computed(() => {
  let status = props.index % 2 === 0;
  return {
    'clip-left': status,
    'clip-right': !status
  };
});
</script>

<style lang="css">
.category-name:hover > .i-icon-mark > svg > path {
  stroke: #f5a623 !important;
  fill: #f5a623 !important;
}

.article-cover {
  transition: all 0.5s ease-in-out;
}

li:nth-child(odd) .article-cover:hover {
  transform: scale(1.1) rotateZ(3.5deg);
}

li:nth-child(even) .article-cover:hover {
  transform: scale(1.1) rotateZ(-3.5deg);
}

.clip-left {
  clip-path: polygon(0 0, 92% 0%, 100% 100%, 0% 100%);
}

.clip-right {
  clip-path: polygon(0 0%, 100% 0%, 100% 100%, 8% 100%);
}
</style>
