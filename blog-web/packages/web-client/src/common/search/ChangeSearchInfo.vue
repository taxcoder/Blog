<template>
  <n-drawer
    v-model:show="sort"
    :height="isStatusSort ? 102 : 130"
    to="#search-data-info"
    placement="top"
    class="bg-white dark:bg-[#141414]"
  >
    <div v-if="isStatusSort" class="px-0 pt-[20px]">
      <div
        v-for="(item, index) in statusSort"
        :key="item.key"
        class="flex justify-between mb-[5px]"
        @click="changeStatusSort(index, item.key)"
      >
        <span class="dark:text-white" :class="{ 'important-text-[#409EFF]': item.active }">{{ item.name }}</span>
        <el-icon v-if="item.active" color="#409EFF"><Select /></el-icon>
      </div>
    </div>
    <div v-else class="px-0 pt-[20px]">
      <div
        v-for="(item, index) in timeSort"
        :key="item.key"
        class="flex justify-between mb-[5px]"
        @click="changeTimeSort(index, item.key)"
      >
        <span class="dark:text-white" :class="{ 'important-text-[#409EFF]': item.active }">{{ item.name }}</span>
        <el-icon v-if="item.active" color="#409EFF"><Select /></el-icon>
      </div>
    </div>
  </n-drawer>
</template>

<script setup lang="ts">
import { Select } from '@element-plus/icons-vue';

const sort = inject('sort');

defineProps({
  isStatusSort: {
    type: Boolean,
    required: true,
  },
  statusSort: {
    type: Array,
    required: true,
  },
  timeSort: {
    type: Array,
    required: true,
  },
});

const emit = defineEmits(['changeStatusSort', 'changeTimeSort']);

const changeStatusSort = (index: number, key: number) => emit('changeStatusSort', { index, key });
const changeTimeSort = (index: number, key: number) => emit('changeTimeSort', { index, key });
</script>

<style>
.n-drawer,
.n-drawer-container {
  box-shadow: none;
}

.n-drawer.n-drawer--native-scrollbar .n-drawer-content-wrapper {
  overflow: hidden;
}
</style>