<template>
  <div class="px-[15px]">
    <n-input
      placeholder="搜索"
      v-model:value="modelValue"
      @input="handleInput"
      size="large"
      :round="true"
      type="text"
      :clearable="true"
    >
      <template #prefix>
        <el-icon>
          <Search />
        </el-icon>
      </template>
    </n-input>
  </div>
</template>
<script setup lang="ts">
import { Search } from '@element-plus/icons-vue';
import { useGlobalStore } from '@/stores/global';

const global = useGlobalStore();

let isLoading = ref<boolean>(false);
const inputData = ref<any>();
const modelValue = ref<any>('');

const handleInput = (value: string) => global.setSearchInput(value);

watch(
  () => global.getSearchInput,
  (value: string) => (modelValue.value = value),
  { immediate: true, deep: true }
);
</script>

<style></style>
