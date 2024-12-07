<template>
  <div id="page" class="mt-[40px] mb-[10px]">
    <el-pagination
      v-if="juiceIsSmallWidth"
      :page-size="pageSize"
      :current-page="current"
      @currentChange="currentChange"
      :total="props.total === -1 ? getTotal : props.total"
      :pager-count="pagerCount"
      :background="background"
      :hide-on-single-page="true"
      layout="prev, pager, next"
    />
    <el-pagination
      v-else
      :current-page="current"
      :page-size="pageSize"
      @sizeChange="sizeChange"
      @currentChange="currentChange"
      :total="props.total === -1 ? getTotal : props.total"
      :pager-count="pagerCount"
      :page-sizes="pageSizes"
      :background="background"
      :hide-on-single-page="true"
      :layout="props.layout"
    />
  </div>
</template>

<script setup lang="ts">
import { useArticleStore } from '@/stores/article';

const currentPage = ref<number>(1);
const article = useArticleStore();

const props = defineProps({
  widthScreen: { type: Number, default: 0 },
  pageSize: { type: Number, required: true },
  pageSizes: { type: Array, default: () => [6, 10] },
  current: { type: Number, required: true },
  total: { type: Number, required: true },
  layout: { type: String, required: true },
  background: { type: Boolean, default: true },
  pagerCount: { type: Number, default: 9 },
});

const emit = defineEmits(['sizeChange', 'currentChange']);
// 变更每页显示的数量
const sizeChange = (size: number) => emit('sizeChange', { current: props.current, size: size });
// 变更当前页
const currentChange = (current: number) => emit('currentChange', { current: current, size: props.pageSize });
// 小于860的宽度，将多余的功能清除
const juiceIsSmallWidth = computed(() => props.widthScreen <= 860);
// 小于600的宽度，直接算移动端
const getTotal = computed(() => (article.getArticleList.total ? article.getArticleList.total : 0));
watch(
  () => currentPage.value,
  () => sizeChange(props.pageSize)
);
</script>

<style scoped></style>
