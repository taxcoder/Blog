<!--
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2025-01-04 10:51:27
 * @LastEditTime: 2025-01-04 12:43:00
 * @FilePath: /code/blog/client-web/src/common/echart/Index.vue
 * @Description: 
 * Copyright (c) 2025 by tanxiang, All Rights Reserved. 
-->
<template>
  <div ref="domEchart" style="width: 100%; height: 400px"></div>
</template>

<script setup lang="ts">
import echarts from './echarts';

const chart = ref<echarts.ECharts>();
const domEchart = ref<HTMLElement>();

const props = defineProps({
  series: {
    type: Object,
    default: () => {}
  },
  theme: {
    type: Boolean,
    default: true
  },
  update: {
    type: Boolean,
    default: false
  }
});

onMounted(() => {
  chart.value = echarts.init(domEchart.value, props.theme ? 'light' : 'dark');
  const option = {
    tooltip: { trigger: 'item' },
    series: [props.series]
  };
  chart.value.setOption(option);
  window.addEventListener('resize', () => chart.value?.resize());
});

watch(
  () => props.update,
  () => {
    chart.value?.resize();
  }
);
</script>

<style scoped></style>
