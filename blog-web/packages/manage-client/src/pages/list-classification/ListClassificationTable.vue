<template>
  <modal-modify
    ref="modal"
    :okClick="okModifyClick"
    :max-length="4"
    :columns="classificationColumns"
    :is-transfer="true"
    :options="select"
  ></modal-modify>
  <modal-delete
    ref="del"
    :okClick="okDeleteClick"
    :is-transfer="true"
    :options="select"
    :columns="classificationColumns"
  >
    <template #content>
      <strong>注意：</strong>
      是否确认删除分类 -> {{ currentData.name }}
    </template>
  </modal-delete>

  <data-table :columns="columns" :loading="loading" :current="current" :dataSource="result" @page-change="pageChange">
    <template #out="{ column, text, record }">
      <!-- articleCount格式自定义 -->
      <template v-if="column.dataIndex === 'articleCount'">
        <a-button type="link">{{ text }}</a-button>
      </template>
      <!-- createTime格式自定义 -->
      <template v-if="column.dataIndex === 'createTime'">
        <span>{{ shortTime(text, '/', true, false) }}</span>
      </template>
      <!-- updateTime格式自定义 -->
      <template v-if="column.dataIndex === 'updateTime'">
        <span>{{ shortTime(text, '/', true, false) }}</span>
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
import ModalModify from '@/components/modal-operation/ModalModify.vue';
import ModalDelete from '@/components/modal-operation/ModalDelete.vue';

import { ref, onMounted, computed, reactive, onActivated, watch, inject } from 'vue';

import { useRouter } from 'vue-router';
import useReload from '@/hooks/useReload';
import { useDate } from '@tanxiang/utils';
import useRequestData from '@/hooks/useRequestData';
import { useAuthClassification } from '@tanxiang/apis';
import { useClassificationStore } from '@/stores/classification';
//@ts-ignore
import { columns } from './tableData';
import { useBaseStore } from '@/stores/base';
import { messageInfo } from '@tanxiang/common';

const { messageError, messageSuccess, messageLoading } = messageInfo();

const { shortTime } = useDate();
const router = useRouter();
const base = useBaseStore();
const auth = useAuthClassification();
const { commonOperation } = useReload();
const classification = useClassificationStore();

const del = ref<any>(null);
const modal = ref<any>(null);

const selectCurrent = ref<number>(1);
const timer = ref<any>(null);
const current = ref<number>(1);
const currentData = ref<any>({});
const loading = ref<boolean>(true);
const placeholder = ref<string>('');
const deleteOpen = ref<boolean>(false);

const select = reactive<any[]>([]);
const modalData = reactive<any[]>([]);
const checkBoxList = reactive<number[]>([]);

const classificationColumns = reactive<any[]>([
  {
    dataIndex: 'label',
    title: '文章名',
    align: 'center',
  },
]);

const tabCurrent: any = inject('tabCurrent');

const props = defineProps({
  source: { type: Object, required: true },
});

const emit = defineEmits(['current', 'dataSource']);

const { request, getDataSource, search } = useRequestData(loading, auth.classificationLimit, emit);

onMounted(() => request());

onActivated(() => {
  if (base.getPreviousRoutePath !== '/refresh') return;
  loading.value = true;
  // 如果进入时，timer.value存在数据，则表示已经有一次的请求了，关闭当前请求，重新建立请求
  if (timer.value) clearTimeout(timer.value);
  timer.value = setTimeout(() => request(), 100);
});

const pageChange = (paging: any) => {
  if (classification.getSearch.value === '' || classification.getSearch.value.length === 0) {
    request(paging.current);
  } else {
    search(
      classification.getSearch.name,
      classification.getSearch.select,
      classification.getSearch.value,
      emit,
      paging.current,
      15
    );
  }
};

const classificationArticle = (key: any, link: any, status: boolean) => {
  currentData.value = key;
  modalData.splice(
    0,
    modalData.length,
    ...getDataSource.value.records.filter((data: any) => data.id === key.id)[0].articles
  );

  select.splice(
    0,
    select.length,
    ...getDataSource.value.records
      .filter((record: any) => record.id !== key.id)
      .map((record: any) => ({ value: record.id, label: record.name }))
  );

  link.list.datas.push(...modalData);
  if (status) link.list.checkList.push(...modalData.map((s: any) => s.id));
  link.spinning = false;
};

const okDeleteClick = (d: any, list: any, cancel: Function) => {
  let temp: { key: number; articleId: number }[] = [];
  list.newLinkDatas.forEach((l) => l.datas.forEach((s) => temp.push({ key: l.select, articleId: s })));

  auth
    .classificationDelete(currentData.value.id, temp)
    .then((success: any) => {
      commonOperation(success.message, null, del.value.cancel, request, current.value);
    })
    .catch((error: any) => messageError(!error || error.name ? '删除失败！' : error));
};

const okModifyClick = (modify: any, inputValue: any, list: any, cancel: Function) => {
  auth
    .classificationUpdateInfo(modify.value.id, inputValue, list.newLinkDatas)
    .then((success: any) => commonOperation(success.message, list, cancel, request, current.value))
    .catch((error: any) => messageError(!error || error.name ? '更新失败！' : error));
};

const onDelete = (key: any) => del.value.showModal(key, classificationArticle, true);

const onModify = (key: any) => modal.value.showModal(key, classificationArticle, true);

const dataSource = (records: any) => {
  return records.map((value: any) => ({
    id: value.id,
    name: value.name,
    articleCount: value.articles.length,
    updateTime: value.updateTime,
    createTime: value.createTime,
  }));
};

const articleList = computed(() =>
  checkBoxList.map((list: number) => {
    let data = modalData.filter((d: { id: number; title: string }) => d.id === list)[0];
    return {
      id: data.id,
      name: data.title,
      classificationId: currentData.value.id,
      classificationName: currentData.value.name,
    };
  })
);

const result = computed(() => {
  return {
    records: ![undefined, null].includes(props.source['total'])
      ? dataSource(props.source['records'])
      : dataSource(getDataSource.value.records),
    total: props.source['total'] ? props.source['total'] : getDataSource.value.total,
  };
});

watch(
  () => tabCurrent.value,
  (newCurrent: number) => (current.value = newCurrent),
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

.change-link {
  max-height: calc(100vh / 2.5);
  overflow: auto;
}

.article-name {
  text-overflow: ellipsis;
  overflow: hidden;
  white-space: nowrap;
}

.change-link::-webkit-scrollbar {
  display: none;
}
</style>