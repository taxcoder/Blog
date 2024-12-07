<template>
  <modal-modify ref="modal" />
  <modal-delete
    ref="del"
    :okClick="okDeleteClick"
    :width="300"
    title=""
    :bodyStyle="{ height: '40px', display: 'flex', alignItems: 'center', justifyContent: 'center' }"
    :cancelButtonProps="{ size: 'small' }"
    :okButtonProps="{ size: 'small' }"
    wrapClassName="essay-delete-modal"
    :notDisabled="true"
  >
    <template #content>
      <span style="width: 100%; display: block; text-align: center">是否确认删除该随笔，请谨慎选择?</span>
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
      <template v-if="column.dataIndex === 'content'" style="height: 100px">
        <span v-html="text || ''"></span>
      </template>
      <template v-if="column.dataIndex === 'mood'" style="height: 100px">
        <img :src="text" alt="" class="w-[25px]" />
      </template>
      <!-- articleCount格式自定义 -->
      <template v-if="column.dataIndex === 'likeCount'">
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

<script lang="ts" setup>
import { ref, onMounted, onActivated, watch, computed, inject } from 'vue';

import { useEssayStore } from '@/stores/essay';
import { useDate } from '@tanxiang/utils';
import ModalModify from '@/components/modal-operation/ModalModify.vue';
import ModalDelete from '@/components/modal-operation/ModalDelete.vue';
import { useAuthEssay, useEssay } from '@tanxiang/apis';
import { useRouter } from 'vue-router';

import useRequestData from '@/hooks/useRequestData';
//@ts-ignore
import { columns } from './tableData';
//@ts-ignore
import useReload from '@/hooks/useReload';
import { useBaseStore } from '@/stores/base';
import { messageInfo } from '@tanxiang/common';

const { commonOperation } = useReload();

const { messageError } = messageInfo();

const day = useDate();
const auth = useAuthEssay();
const base = useBaseStore();
const essay = useEssay();
const essayStore = useEssayStore();

const size = ref<number>(17);
const table = ref<any>(null);
const modal = ref<any>(null);
const del = ref<any>(null);

const current = ref<number>(1);
const timer = ref<any>(null);
const loading = ref<boolean>(true);
const router = useRouter();

const tabCurrent: any = inject('tabCurrent');

const props = defineProps({
  source: { type: Object, required: true },
});

const emit = defineEmits(['current', 'dataSource']);

const { request, getDataSource, search } = useRequestData(loading, essay.essayList, emit);

onMounted(() => request());

onActivated(() => {
  if (base.getPreviousRoutePath === '/refresh' || essayStore.getIsRefer) {
    loading.value = true;
    // 如果进入时，timer.value存在数据，则表示已经有一次的请求了，关闭当前请求，重新建立请求
    if (timer.value) clearTimeout(timer.value);
    timer.value = setTimeout(() => request(), 100);
    essayStore.setIsRefer(false);
  }
});

const pageChange = (paging: any) => {
  if (essayStore.getSearch.value === '' || essayStore.getSearch.value.length === 0) {
    request(paging.current);
  } else {
    search(
      essayStore.getSearch.name,
      essayStore.getSearch.select,
      essayStore.getSearch.value,
      emit,
      paging.current,
      15
    );
  }
};

const tagArticle = (key: any, link: any, status: boolean) => {
  if (!status) return router.push(`/editor/essay/${key.id}`);
  link.spinning = false;
};

const okDeleteClick = (data: any, list: any, cancel: Function) => {
  useRequestData('正在删除！');
  auth
    .essayDelete(data.value.id)
    .then((success: any) => commonOperation(success.message, list, cancel, request, current.value))
    .catch((error: any) => messageError(!error || error.name ? '删除失败！' : error));
};

const onDelete = (key: any) => del.value.showModal(key, tagArticle, false);

const onModify = (key: any) => modal.value.showModal(key, tagArticle, true, false);

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
  background-color: #eebe77;
}
</style>

<style>
.essay-delete-modal .ant-modal-content .ant-modal-footer {
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>