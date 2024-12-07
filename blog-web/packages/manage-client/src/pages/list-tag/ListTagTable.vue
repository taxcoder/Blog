<template>
  <modal-modify ref="modal" :okClick="okModifyClick" />
  <modal-delete ref="del" :okClick="okDeleteClick">
    <template #content>
      <div>删除后，该标签下的文章将不再显示该标签，是否继续？</div>
    </template>
  </modal-delete>

  <data-table
    :columns="columns"
    :loading="loading"
    :current="current"
    :dataSource="dataSource"
    @page-change="pageChange"
  >
    <template #out="{ column, text, record }">
      <!-- name格式自定义 -->
      <template v-if="column.dataIndex === 'id'" style="height: 100px">
        <div>{{ text || ' ' }}</div>
      </template>
      <!-- name格式自定义 -->
      <template v-if="column.dataIndex === 'name'" style="height: 100px">
        <span>{{ text || ' ' }}</span>
      </template>
      <!-- articleCount格式自定义 -->
      <template v-if="column.dataIndex === 'articleCount'">
        <a-button type="link">{{ text }}</a-button>
      </template>
      <!-- createTime格式自定义 -->
      <template v-if="column.dataIndex === 'createTime'">
        <span>{{ day.shortTime(text, '/', true, false) }}</span>
      </template>
      <!-- operation格式自定义 -->
      <template v-else-if="column.dataIndex === 'operation'">
        <a-button type="primary" size="small" class="button-table modify" @click="onModify(record)">修改</a-button>
        <a-button type="primary" danger size="small" class="button-table" @click="onDelete(record)">删除</a-button>
      </template>
    </template>
  </data-table>
</template>

<script setup lang="ts">
import { ref, onMounted, onActivated, computed, watch, inject } from 'vue';

import { useDate } from '@tanxiang/utils';
import ModalModify from '@/components/modal-operation/ModalModify.vue';
import ModalDelete from '@/components/modal-operation/ModalDelete.vue';
import { useRouter } from 'vue-router';
import useRequestData from '@/hooks/useRequestData';
import { useAuthTag } from '@tanxiang/apis';
//@ts-ignore
import { columns } from './tableData';
//@ts-ignore
import useReload from '@/hooks/useReload';
import { useTagStore } from '@/stores/tag';
import { useBaseStore } from '@/stores/base';
import { messageInfo } from '@tanxiang/common';

const { commonOperation } = useReload();
const { messageError, messageWarning } = messageInfo();

const day = useDate();
const auth = useAuthTag();
const base = useBaseStore();
const tagStore = useTagStore();

const del = ref<any>(null);
const modal = ref<any>(null);
const timer = ref<any>(null);
const loading = ref<boolean>(true);
const current = ref<number>(1);
const router = useRouter();
const tabCurrent: any = inject('tabCurrent');

const props = defineProps({
  source: { type: Object, required: true },
});

const emit = defineEmits(['current', 'dataSource']);

const { request, getDataSource, search } = useRequestData(loading, auth.tagLimit, emit);

onMounted(() => request());

onActivated(() => {
  if (base.getPreviousRoutePath !== '/refresh') return;
  loading.value = true;
  // 如果进入时，timer.value存在数据，则表示已经有一次的请求了，关闭当前请求，重新建立请求
  if (timer.value) clearTimeout(timer.value);
  timer.value = setTimeout(() => request(), 100);
});

const pageChange = (paging: any) => {
  if (tagStore.getSearch.value === '' || tagStore.getSearch.value.length === 0) {
    request(paging.current);
  } else {
    search(tagStore.getSearch.name, tagStore.getSearch.select, tagStore.getSearch.value, emit, paging.current, 15);
  }
};

const tagArticle = (key: any, link: any, status: boolean) => {
  link.spinning = false;
};

const okDeleteClick = (data: any, list: any, cancel: Function) => {
  auth
    .tagDelete(data.value.id)
    .then((success: any) => commonOperation(success.message, list, cancel, request, current.value))
    .catch((error: any) => messageError(!error || error.name ? '删除失败！' : error));
};

const okModifyClick = (modify: any, inputValue: any, list: any, cancel: Function) => {
  if (modify.value.name === inputValue) {
    return messageWarning('内容未发生改变！');
  }
  auth
    .tagUpdate(modify.value.id, inputValue)
    .then((success: any) => commonOperation(success.message, list, cancel, request, current.value))
    .catch((error: any) => messageError(!error || error.name ? '更新失败！' : error));
};

const onDelete = (key: any) => del.value.showModal(key, tagArticle);

const onModify = (key: any) => modal.value.showModal(key, tagArticle);

const dataSource = computed(() => {
  return ![undefined, null].includes(props.source.total) ? props.source : getDataSource.value;
});

watch(
  () => tabCurrent.value,
  (newCurrent: number) => {
    current.value = newCurrent;
  },
  { deep: true }
);
</script>

<style scoped>
.button-table {
  margin: 0 5px;
  font-size: 13px;
  border-radius: 4px;
}

.modify {
  background-color: #e6a23c !important;
}

.modify:hover {
  background-color: #eebe77 !important;
}
</style>