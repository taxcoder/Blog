<template>
  <modal-modify
    ref="modal"
    :okClick="okModifyClick"
    :max-length="20"
    :columns="cNavigationColumn"
    :options="select"
    is-transfer
  ></modal-modify>
  <modal-delete ref="del" :okClick="okDeleteClick" :columns="cNavigationColumn" :options="select" is-transfer>
    <template #content>
      <strong>注意：</strong>
      是否确认删除该网站导航分类 -> {{ currentData.name }}
    </template>
  </modal-delete>

  <data-table :columns="columns" :loading="loading" :current="current" :dataSource="result" @page-change="pageChange">
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
      <template v-if="column.dataIndex === 'navigationCount'">
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
import { useAuthClassificationNav, useAuthNavigation } from '@tanxiang/apis';
import { useClassificationNavigationStore } from '@/stores/classification-navigation';
//@ts-ignore
import { columns } from './tableData';
import { useBaseStore } from '@/stores/base';
import { messageInfo } from '@tanxiang/common';

const { messageError, messageSuccess, messageLoading, messageWarning } = messageInfo();

const { shortTime } = useDate();
const router = useRouter();
const base = useBaseStore();
const auth = useAuthClassificationNav();
const { commonOperation } = useReload();
const classification = useClassificationNavigationStore();

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
const cNavigationColumn = reactive<any[]>([
  {
    dataIndex: 'title',
    title: '网站导航名称',
    width: 200,
    align: 'center',
  },
]);

const tabCurrent: any = inject('tabCurrent');

const props = defineProps({
  source: { type: Object, required: true },
});

const emit = defineEmits(['current', 'dataSource']);

const { request, getDataSource, search } = useRequestData(loading, auth.classificationNavList, emit);

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

const classificationNavigation = (key: any, link: any, status: boolean) => {
  select.splice(
    0,
    select.length,
    ...result.value.records.map((s: any) => ({ label: s.name, value: s.id })).filter((s: any) => s.value !== key.id)
  );
  useAuthNavigation()
    .selectNavigationByClassificationNavigation(key.id)
    .then((success: any) => {
      currentData.value = key;
      modalData.splice(
        0,
        modalData.length,
        ...success.data.records.map((s: any) => ({
          id: s.id,
          title: s.title,
        }))
      );
      link.list.datas.push(...modalData);
      link.list.checkList.push(...modalData.map((s: any) => s.id));
    })
    .catch((error: any) => messageError(!error || error.name ? '获取失败！' : error))
    .finally(() => (link.spinning = false));
};

const okDeleteClick = (modify: any, list: any, cancel: Function) => {
  if (list.datas.length !== 0 && list.newLinkDatas.filter((s: any) => s.datas.length > 0).length === 0) {
    return messageWarning('请更新该分类下面的所有网站！');
  }
  messageLoading('正在删除中');
  auth
    .deleteClassificationNavList(
      currentData.value.id,
      list.newLinkDatas.filter((s: any) => s.datas.length > 0)
    )
    .then((success: any) => commonOperation(success.data, null, del.value.cancel, request, current.value))
    .catch((error: any) => messageError(!error || error.name ? '删除失败！' : error));
};

const okModifyClick = (modify: any, inputValue: any, list: any, cancel: Function) => {
  auth
    .updateClassificationNav(modify.value.id, inputValue, list.newLinkDatas)
    .then((success: any) => commonOperation(success.data, list, cancel, request, current.value))
    .catch((error: any) => messageError(!error || error.name ? '更新失败！' : error));
};

const onDelete = (key: any) => del.value.showModal(key, classificationNavigation, true);

const onModify = (key: any) => modal.value.showModal(key, classificationNavigation, true);

const dataSource = (records: any) => {
  return records.map((value: any) => ({
    id: value.id,
    name: value.name,
    navigationCount: value.navigationCount,
    updateTime: value.updateTime,
    createTime: value.createTime,
  }));
};

const getTotal = computed(() => {
  return ![undefined, null].includes(props.source.total) ? props.source.total : getDataSource.value.total;
});

const result = computed(() => {
  let r = ![undefined, null].includes(props.source.total)
    ? dataSource(props.source.records)
    : dataSource(getDataSource.value.records);
  return {
    total: getTotal.value,
    records: r,
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

.classification-navigation-name {
  text-overflow: ellipsis;
  overflow: hidden;
  white-space: nowrap;
}

.change-link::-webkit-scrollbar {
  display: none;
}
</style>