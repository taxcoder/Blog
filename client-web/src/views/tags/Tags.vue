<!--
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2024-12-20 10:49:49
 * @LastEditTime: 2025-01-09 06:12:43
 * @FilePath: /code/blog/client-web/src/views/tags/Tags.vue
 * @Description: 
 * Copyright (c) 2024 by tanxiang, All Rights Reserved. 
-->
<template>
  <el-card body-class="min-h-[15rem] flex-cc important-p-0" class="important-rounded-[0.4rem]">
    <page-loading-light
      v-if="loading"
      class="important-h-[20rem] dark:bg-[rgb(140 117 75)] rounded-[0.4375rem]"
    ></page-loading-light>
    <div class="wh-full p-4 py-8 place-self-start inline-block text-center select-none" v-else>
      <div
        v-for="(item, index) in data"
        :key="item.id"
        class="inline-flex p-2.5 mr-[0.5rem] mb-[0.5rem] last:mr-0 rounded-[5px] font-size-[0.9rem] font-bold color-white cursor-pointer shadow-xl hover:opacity-60 transition-all duration-150 whitespace-pre dark:filter-brightness-90"
        :style="`background-color: ${index >= color.length ? color[Math.floor(index % color.length)] : color[index]}`"
        @click="toJump(item)"
      >
        {{ item.name }} {{ item.count }}
      </div>
    </div>
  </el-card>
</template>

<script setup lang="ts">
import { color } from '@/constant/color';

const router = useRouter();
const tag = useTagStore();

const { data, loading } = useRequest(() => api.getTags(), {
  cacheKey: 'tag-list',
  staleTime: 50000,
  onSuccess: () => {
    if (!data.value) return;
    tag.setTags(data.value);
  }
});

const toJump = (item: any) => {
  router.push(`/tag/${item.id}`);
};
</script>

<style lang="css" scoped></style>
