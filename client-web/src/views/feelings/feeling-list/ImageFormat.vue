<!--
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2025-01-05 11:59:51
 * @LastEditTime: 2025-01-05 12:54:55
 * @FilePath: /code/blog/client-web/src/views/feeling/ImageFormat.vue
 * @Description: 
 * Copyright (c) 2025 by tanxiang, All Rights Reserved. 
-->

<template>
  <div v-if="props.imageUrl.length === 1" class="pt-[0.75rem]">
    <van-image
      lazy-load
      :src="props.imageUrl[0]"
      fit="cover"
      class="rounded-md overflow-hidden"
      @click="previewImage"
    />
  </div>
  <div v-else-if="props.imageUrl.length % 3 === 0 || isPhone" class="grid gap-2 grid-cols-3 pt-[0.75rem] max-w-[30rem]">
    <van-image
      lazy-load
      v-for="item in props.imageUrl"
      :src="item"
      fit="cover"
      class="rounded-md overflow-hidden"
      @click="previewImage"
    />
  </div>
  <div v-else class="grid gap-2 grid-cols-4 pt-[0.75rem] max-w-[40rem]">
    <van-image
      lazy-load
      v-for="item in props.imageUrl"
      :src="item"
      fit="cover"
      class="rounded-md overflow-hidden"
      @click="previewImage"
    />
  </div>
</template>

<script setup lang="ts">
const isPhone = inject('isPhone');

const props = defineProps({
  imageUrl: {
    type: Array<string>,
    default: []
  }
});

const previewImage: any = () => {
  showImagePreview({
    images: props.imageUrl,
    showIndicators: true
  });
};
</script>

<style lang="css" scoped></style>
