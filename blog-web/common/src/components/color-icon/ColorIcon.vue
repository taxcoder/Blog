<template>
  <svg aria-hidden="true" :style="setIconStyle" class="relative">
    <use :xlink:href="getIconName" />
  </svg>
</template>

<script setup lang="ts">
/**
 * @author: tanxiang
 *
 * @projectName: client-blog
 * @createTime: 2022/10/16 15:22
 * @description: 通用加载iconfont多彩图标
 */
import { computed } from 'vue';

const props = defineProps({
  size: { type: Number, default: 20 },
  iconClass: { type: String, default: '' },
  left: { type: Number, default: 0 },
  top: { type: Number, default: 0 },
  padding: { type: String, default: '0' },
});

const getIconName = computed(() => {
  return `#${props.iconClass}`;
});

const setIconStyle = computed(() => {
  return {
    width: `${props.size}px`,
    height: `${props.size}px`,
    left: `${props.left}px`,
    top: `${props.top}px`,
    padding: props.padding,
  };
});
</script>

<style scoped></style>