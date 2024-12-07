<template>
  <div class="rose">
    <v-chart :option="options" autoresize @click="clickEChart" @restore="restore"></v-chart>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue';
import { usePage } from '@tanxiang/utils';

const props = defineProps({
  data: { type: Array, required: true },
  name: { type: String, required: true },
});

const top = ref<string>('67%');
const legendTop = ref<number>(0);

const emit = defineEmits(['change', 'restore']);

onMounted(() => {
  resize();
  window.addEventListener('resize', resize);
});

const clickEChart = (text: any) => {
  if (text.name === '其他') {
    if (props.name === '标签') emit('change', { name: 'tags', value: '', operation: false });
    else if (props.name === '分类') emit('change', { name: 'classifications', value: '', operation: false });
    else if (props.name === '随笔') emit('change', { name: 'essays', value: '', operation: false });
  } else {
    if (props.name === '标签') emit('change', { name: 'tags', value: text.name, operation: true });
    else if (props.name === '分类') emit('change', { name: 'classifications', value: text.name, operation: true });
    else if (props.name === '随笔') emit('change', { name: 'essays', value: text.name, operation: true });
  }
};

const resize = () => {
  let width = usePage().realWidth();
  top.value = width <= 1000 ? '60%' : '64%';
  legendTop.value = width <= 1000 ? 40 : 30;
};

const restore = () => {
  if (props.name === '标签') emit('restore', 'tags');
  if (props.name === '分类') emit('restore', 'classifications');
  if (props.name === '随笔') emit('restore', 'essays');
};

const options = computed(() => {
  return {
    title: {
      text: props.name,
      left: 'center',
      top: 5,
    },
    toolbox: {
      feature: {
        restore: { show: true },
      },
    },
    legend: {
      top: legendTop.value,
    },
    tooltip: {
      trigger: 'item',
    },
    series: [
      {
        name: props.name,
        type: 'pie',
        radius: [10, 90],
        center: ['50%', top.value],
        itemStyle: {
          borderRadius: 10,
        },
        data: props.data,
      },
    ],
  };
});
</script>

<style scoped>
.rose {
  flex: 1;
  height: 100%;
  min-height: 300px;
  background-color: white;
}
</style>
