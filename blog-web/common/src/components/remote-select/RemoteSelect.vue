<template>
  <el-select
    v-model="selectValue"
    placeholder="请选择导航分类"
    @visible-change="visibleChange"
    :loading="loading"
    @change="change"
  >
    <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value" />
  </el-select>
</template>

<script setup lang="ts">
import { computed, reactive, ref, watchEffect, watch } from 'vue';

import messageInfo from '../../config/message';

const { messageError } = messageInfo();

const state = reactive<{ value: number; label: string }[]>([]);

const loading = ref<boolean>(false);
const selectValue = ref<number>(0);

const props = defineProps({
  record: {
    type: Object,
    default: () => {
      return {};
    },
  },
  fn: {
    type: Function,
    required: true,
  },
  text: {
    type: String,
    default: '',
  },
  list: {
    type: Array as { value: number; label: string }[],
    default: () => [],
  },
});

const visibleChange = (visible) => {
  if (visible && state.length === 0) {
    loading.value = true;
    props
      .fn()
      .then((res) => {
        state.push(...res.data);
        selectValue.value = state.find((item) => item.label === props.text)?.value || 0;
      })
      .catch((error: any) => messageError(!error || error.name ? '网络错误' : error))
      .finally(() => (loading.value = false));
  }
};

const options = computed(() => {
  if (state.length === 0) {
    return [{ value: 0, label: props.text }];
  } else {
    return state;
  }
});

const change = (data: any) => {
  emit('change', {
    id: props.record.id,
    value: data,
    list: state,
  });
};

const emit = defineEmits(['change']);

defineExpose({ state });

watch(
  () => props.text,
  (newText: string) => {
    if (state && state.length > 0) {
      selectValue.value = state.find((item) => item.label === newText)?.value || 0;
    }
  }
);

watchEffect(() => {
  if (props.list.length > 0 && state.length === 0) {
    state.push(...props.list);
  }
  if (selectValue.value === 0) {
    selectValue.value = state.find((item) => item.label === props.text)?.value || 0;
  }
});
</script>

<style scoped></style>