<!--
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2024-12-31 13:42:06
 * @LastEditTime: 2024-12-31 14:33:06
 * @FilePath: /code/blog/client-web/src/views/home/ToppingItem.vue
 * @Description: 
 * Copyright (c) 2024 by tanxiang, All Rights Reserved. 
-->

<template>
  <div
    class="bg-[#b1b3b8] wh-full cursor-pointer relative"
    :class="{
      '[&_div.cover]:hover:bg-[rgba(0,0,0,0.7)] [&_div]:hover:transform-translate-x-0 [&_img]:hover:transform-scale-[1.2] [&_img]:transition-all-500 [&_div]:transition-all-350':
        !props.phone
    }"
  >
    <el-image :src="props.item.img" fill="cover" class="wh-full">
      <template #placeholder>
        <page-loading-light></page-loading-light>
      </template>
    </el-image>
    <div
      class="cover absolute wh-full top-0 left-0"
      :class="{ 'bg-[rgba(0,0,0,0)]': !props.phone, 'bg-[rgba(0,0,0,0.7)]': props.phone }"
    ></div>
    <div
      class="title absolute w-full top-30% left-0 color-white dark:color-[#cbcbcb] flex-cc h-[2.75rem] bg-[rgba(0,0,0)] font-size-[1.1rem]"
      :class="{ 'transform-translate-x-[-100%]': !props.phone }"
    >
      <span>{{ props.item.title }}</span>
    </div>
    <div
      class="title absolute w-full top-60% left-0 color-white flex-cc h-[2.75rem] font-size-[0.9rem] color-[#cbcbcb] dark:color-[#afafaf]"
      :class="{ 'transform-translate-x-[100%]': !props.phone }"
    >
      <span>{{ props.item.description }}</span>
    </div>
  </div>
</template>

<script setup lang="ts">

const props = defineProps({
  item: {
    type: Object,
    default: () => {
      return {
        id: 0,
        img: '',
        description: '',
        title: ''
      };
    }
  },
  phone: {
    type: Boolean,
    default: false
  }
});
</script>

<style lang="css" scoped></style>
