<!--
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2025-01-04 06:45:46
 * @LastEditTime: 2025-01-04 06:50:29
 * @FilePath: /code/blog/client-web/src/components/prefix-info/Index.vue
 * @Description: 
 * Copyright (c) 2025 by tanxiang, All Rights Reserved. 
-->
<template>
  <div
    class="font-size-[0.95rem] pt-[0.3125rem] pb-[0.3125rem] border-b-[0.0625rem] border-[#f1f1f1] dark:border-[#555555] border-b-dashed mb-[0.4688rem]"
  >
    <auto-icon-park
      :name="icon"
      :size="props.size"
      :theme="props.theme"
      :fill="global.getTheme ? '#474747' : '#afafaf'"
    ></auto-icon-park>
    <span class="color-[#474747] dark:color-[#afafaf] ml-[0.15rem]"> {{ props.title }} </span>
  </div>
</template>

<script setup lang="ts">
const global = useGlobalStore();

const props = defineProps({
  title: {
    type: String,
    required: true
  },
  icon: {
    type: Object,
    required: true
  },
  theme: {
    type: String,
    default: 'outline'
  },
  size: {
    type: String,
    default: '0.95rem'
  }
});
</script>

<style lang="css" scoped></style>
