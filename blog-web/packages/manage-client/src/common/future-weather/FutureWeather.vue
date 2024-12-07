<template>
  <div id="charts">
    <v-chart :option="option" autoresize></v-chart>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue';
import { usePage } from '@tanxiang/utils';

import * as echarts from 'echarts/core';

const props = defineProps({
  day: { type: Array, required: true },
  maxTemperature: { type: Array, required: true },
  minTemperature: { type: Array, required: true },
});

import {
  GridComponent,
  LegendComponent,
  MarkLineComponent,
  MarkPointComponent,
  TitleComponent,
  ToolboxComponent,
  TooltipComponent,
} from 'echarts/components';
import { LineChart } from 'echarts/charts';
import { UniversalTransition } from 'echarts/features';
import { CanvasRenderer } from 'echarts/renderers';

echarts.use([
  TitleComponent,
  ToolboxComponent,
  TooltipComponent,
  GridComponent,
  LegendComponent,
  MarkLineComponent,
  MarkPointComponent,
  LineChart,
  CanvasRenderer,
  UniversalTransition,
]);

const temp = ref<number>(0);
const current = ref<number>(1);

const option: any = reactive({
  tooltip: {
    trigger: 'axis',
  },
  grid: { left: '10%', right: '10%' },
  legend: {},
  toolbox: {
    right: 20,
    show: true,
    feature: {
      magicType: { type: ['line', 'bar'] },
    },
  },
  xAxis: {
    type: 'category',
    boundaryGap: false,
    data: props.day,
  },
  yAxis: {
    type: 'value',
    axisLabel: {
      formatter: '{value} °C',
    },
  },
  series: [
    {
      name: '最高温度',
      type: 'line',
      data: props.maxTemperature,
      markPoint: {
        data: [
          { type: 'max', name: 'Max' },
          { type: 'min', name: 'Min' },
        ],
      },
    },
    {
      name: '最低温度',
      type: 'line',
      data: props.minTemperature,
      markPoint: {
        data: [
          { type: 'max', name: 'Max' },
          { type: 'min', name: 'Min' },
        ],
      },
    },
  ],
});

onMounted(() => {
  window.onload = () => {
    option.grid.left = usePage().realWidth() <= 700 ? '15%' : '10%';
    option.grid.right = usePage().realWidth() <= 500 ? '7%' : '10%';
  };
  window.addEventListener('resize', (e: any) => {
    current.value = e.currentTarget.outerWidth <= 700 ? 1 : 0;
    if (current.value === temp.value) {
      option.grid.left = e.currentTarget.outerWidth <= 700 ? '15%' : '10%';
      option.grid.right = e.currentTarget.outerWidth <= 500 ? '5%' : '10%';
      temp.value = current.value === 1 ? 0 : 1;
    }
  });
});
</script>

<style scoped>
@media all and (max-width: 1000px) {
  #charts {
    min-height: 300px !important;
  }
}

#charts {
  height: 100%;
  min-height: 250px;
  width: 100%;
}
</style>
