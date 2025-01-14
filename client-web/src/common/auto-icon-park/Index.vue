<!--
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2024-12-27 10:23:03
 * @LastEditTime: 2024-12-27 14:04:43
 * @FilePath: /code/blog/client-web/src/common/auto-icon-park/Index.vue
 * @Description: 
 * Copyright (c) 2024 by tanxiang, All Rights Reserved. 
-->
<template>
  <component
    :is="props.name"
    :size="props.size"
    :theme="props.theme"
    :fill="props.fill"
    strokelinejoin="bevel"
    strokelinecap="square"
  ></component>
</template>

<script setup lang="ts">
const props = defineProps({
  name: {
    type: Object,
    required: true
  },
  size: {
    type: [Number, String],
    default: 24
  },
  theme: {
    type: String,
    default: 'outline',
    validator: (val: string) => ['outline', 'filled', 'two-tone', 'multi-color'].includes(val)
  },
  fill: {
    type: [String, Array<String>],
    default: '#ffffff',
    validator: function (value: String | Array<String>, props: any) {
      return Array.isArray(value)
        ? ['multi-color', 'two-tone'].includes(props.theme)
        : ['filled', 'outline'].includes(props.theme);
    }
  }
});
</script>

<style lang="css" scoped></style>
