<template>
  <div class="table-search" :class="{ 'input-clear': !props.show && props.formState.select !== 'id' }">
    <!-- 搜索 -->
    <a-form layout="inline" :model="formState">
      <a-form-item>
        <el-input
          v-model="props.formState.value"
          :placeholder="placeholder"
          :clearable="clearable"
          :input-style="!props.show && props.formState.select !== 'id' ? 'display: none' : ''"
        >
          <template #prepend>
            <el-select v-model="props.formState.select">
              <el-option
                v-for="item in props.formState.key"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </template>
          <template #suffix>
            <slot name="select"></slot>
          </template>
        </el-input>
      </a-form-item>
      <a-form-item>
        <slot name="other">
          <el-button type="primary" :icon="h(PlusCircleTwoTone)" @click="add">添加</el-button>
        </slot>
      </a-form-item>
    </a-form>

    <!-- 添加 -->
  </div>
</template>

<script setup lang="ts">
import { computed, h, ref, watch } from 'vue';

import { PlusCircleTwoTone } from '@ant-design/icons-vue';

import { useAuthSearch } from '@tanxiang/apis';

import useRequestData from '@/hooks/useRequestData';

const complete = ref<any>();
const auth = useAuthSearch();

const emit = defineEmits(['dataSource', 'current', 'add', 'local']);

const { coverDataSource, getDataSource, search } = useRequestData();

const props = defineProps({
  formState: { type: Object, required: true },
  name: { type: String, required: true },
  local: { type: Boolean, default: false },
  clearable: { type: Boolean, default: true },
  show: { type: Boolean, default: true },
});

const placeholder = computed(() => {
  let f = props.formState.key.filter((k: any) => k.value === props.formState.select);
  return f.length > 0 ? f[0].placeholder : '搜索';
});

const add = () => emit('add');

watch(
  () => [props.formState.value, props.formState.select],
  ([newValue, newSelect], [oldValue, oldSelect]) => {
    // 如果数据没变，同时选择的类型没变，那肯定请求过一次的，不执行请求
    if (newValue === oldValue && newSelect === oldSelect) return;
    // 如果数据是空，或者是是一个对象，则不请求
    if ((newValue === '' || newValue.length === 0) && newSelect === oldSelect && newValue === oldValue) return;
    // 判断是否使用通用的请求，还是使用自定义请求
    if (!props.local) {
      search(props.name, props.formState.select, props.formState.value, emit);
    } else {
      emit('local', { value: newValue, select: newSelect });
    }
  }
);
</script>

<style scoped>
.table-search {
  width: 100%;
  padding: 0 0 6px 0;
  z-index: 3;
  top: 0;
  height: 40px;
  position: sticky;
  background: #f5f5f5;
}
</style>

<style>
.table-search .el-input__wrapper {
  border-radius: 6px;
}

.table-search .ant-form-item {
  margin: 0;
}

.table-search .ant-form-item:first-child .el-select {
  min-width: 50px;
  max-width: 120px;
  height: 100%;
  border-top-right-radius: 0 !important;
  border-bottom-right-radius: 0 !important;
}

.table-search .ant-form-item:first-child {
  flex: 1;
}

.table-search .ant-form-item:first-child .el-input__wrapper {
  max-width: 300px;
  margin-right: 10px;
  border-top-left-radius: 0 !important;
  border-bottom-left-radius: 0 !important;
}

.input-clear .ant-form-item:first-child .el-input__wrapper {
  display: flow;
}

@media all and (max-width: 600px) {
  .input-clear .ant-form-item:first-child .el-input__wrapper {
    display: block;
    flex-grow: 0;
  }
}

.table-search .ant-form-inline {
  justify-content: space-between;
  align-items: center;
}

.table-search .el-input .el-input--suffix,
.table-search .select-trigger {
  height: 100%;
}

.table-search .el-input .el-input--suffix .el-input__wrapper {
  border-top-left-radius: 5px !important;
  border-bottom-left-radius: 5px !important;
}

.table-search .el-input__suffix-inner > :first-child {
  margin-left: 0;
}

.input-clear .el-input__wrapper {
  box-shadow: none !important;
  padding: 0 !important;
}

.input-clear .el-input .el-input--suffix .el-input__wrapper {
  padding: 1px 11px !important;
}

.input-clear .el-input-group--prepend {
  display: inline-flex !important;
}

.table-search .el-input-group--prepend .el-input-group__prepend .el-select .el-input.is_focus .el-input__wrapper,
.table-search .el-input-group--prepend .el-input-group__prepend .el-select:hover .el-input__wrapper,
.table-search .el-input-group--prepend .el-input-group__prepend .el-select .el-input.is-focus .el-input__wrapper,
.table-search .el-select .el-input__wrapper.is-focus {
  box-shadow: none !important;
  border: none !important;
}
</style>