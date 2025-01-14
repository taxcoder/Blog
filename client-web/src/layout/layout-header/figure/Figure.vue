<!--
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2024-12-20 12:29:02
 * @LastEditTime: 2025-01-10 08:39:54
 * @FilePath: /code/blog/client-web/src/layout/layout-header/figure/Figure.vue
 * @Description: 网站首页头部图片
 * Copyright (c) 2024 by tanxiang, All Rights Reserved. 
-->

<template>
  <div id="figure" class="w-full"></div>
  <figure class="relative flex-cc" :style="[figureHeight]">
    <el-image
      :src="props.imageUrl"
      :fit="props.fit"
      class="wh-full important-absolute image"
      :class="[filterBrightness]"
    >
      <template #placeholder>
        <slot name="placeholder"></slot>
      </template>
      <template #error>
        <span></span>
      </template>
    </el-image>
    <layout-header-figure-title v-show="embellishmentIsNotHidden"></layout-header-figure-title>
    <layout-header-figure-route-name v-show="!embellishmentIsNotHidden"></layout-header-figure-route-name>
    <!-- 滚到主体部分 -->
    <layout-header-figure-down></layout-header-figure-down>
    <!-- 波浪-->
    <div v-show="embellishmentIsNotHidden" class="w-full absolute bottom-0"><slot name="embellishment"></slot></div>
  </figure>
</template>

<script setup lang="ts">
const route = useRoute();

const props = defineProps({
  imageUrl: {
    type: String,
    required: true
  },
  fit: {
    type: String as PropType<'cover' | 'contain' | 'fill' | 'none' | 'scale-down'>,
    default: 'cover'
  },
  height: {
    type: String,
    default: 'auto'
  },
  theme: {
    type: Boolean,
    default: true
  }
});

const filterBrightness = computed(() => {
  return { 'filter-brightness-70': !props.theme };
});

const figureHeight = computed(() => {
  return /\/article\/\d+/.test(route.path) ? 'height: 0' : `height: max(18.75rem, var(${props.height}))`;
});

const embellishmentIsNotHidden = computed(() => {
  return route.name && route.path === '/';
});
</script>

<style lang="css" scoped>
figure {
  transition: height 0.2s;
}

.image::after {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  bottom: 0;
  right: 0;
  background: url('@/assets/svg/grid.svg');
}

#figure {
    background: url('@/assets/svg/grid.svg');
}
</style>
