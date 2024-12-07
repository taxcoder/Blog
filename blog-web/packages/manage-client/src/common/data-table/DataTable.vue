<template>
  <div id="data-table" class="sticky top-0">
    <a-table
      sticky
      :data-source="props.dataSource['records']"
      :columns="props.columns"
      :loading="props.loading"
      size="small"
      @change="pageChange"
      :pagination="{
        position: ['bottomCenter'],
        size: 'middle',
        defaultPageSize: props.size,
        hideOnSinglePage: true,
        current: props.current,
        showSizeChanger: false,
        total: dataSource.total,
      }"
      @expand="expand"
      :expandedRowKeys="expandedRowKeys"
      :row-key="(record: any) => record.id"
    >
      <template #bodyCell="{ column, text, record }">
        <slot name="out" :column="column" :text="text" :record="record"></slot>
      </template>
      <template #expandedRowRender="{ record, index }" v-if="props.innerColumns[0]">
        <a-table :columns="props.innerColumns" :data-source="[record]" :pagination="false">
          <template #bodyCell="{ column, index, text, record }">
            <slot name="inner" :column="column" :text="text" :record="record" :index="index"></slot>
          </template>
        </a-table>
      </template>
    </a-table>
  </div>
</template>

<script setup lang="ts">
const expandedRowKeys = reactive<String[]>([]);

const props = defineProps({
  dataSource: {
    type: Object as any,
    default: () => ({
      records: [],
      total: 0,
    }),
  },
  loading: {
    type: Boolean,
    default: false,
  },
  current: {
    type: Number,
    default: 1,
  },
  size: {
    type: Number,
    default: 15,
  },
  columns: {
    type: Array as any,
    required: true,
  },
  innerColumns: {
    type: Array as any[],
    default: () => [],
  },
});

const emit = defineEmits(['pageChange']);

const pageChange = (paging: any) => {
  emit('pageChange', paging);
};

const expand = (expanded: any, record: any) => {
  // 判断数组的长度是否为0，如果为0直接添加
  if (expandedRowKeys.length === 0) return expandedRowKeys.push(record.id);
  // 当数组长度不为0时，判断当前点击的行是否已经打开
  let number = expandedRowKeys.indexOf(record.id);
  // 如果已经打开，将该行的ID移除数组
  if (number !== -1) return expandedRowKeys.splice(number, 1);
  // 没有打开则移除掉其他打开的行，再打开当前行
  expandedRowKeys.splice(0, expandedRowKeys.length);
  expandedRowKeys.push(record.id);
};
</script>

<style scoped></style>