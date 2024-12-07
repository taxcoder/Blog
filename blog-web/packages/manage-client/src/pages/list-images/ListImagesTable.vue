<template>
  <modal-delete
    ref="del"
    :okClick="okDeleteClick"
    :width="300"
    title=""
    :bodyStyle="{ height: '40px', display: 'flex', alignItems: 'center', justifyContent: 'center' }"
    :cancelButtonProps="{ size: 'small' }"
    :okButtonProps="{ size: 'small' }"
    wrapClassName="images-delete-modal"
    :notDisabled="true"
  >
    <template #content>
      <span style="width: 100%; display: block; text-align: center">是否确认删除该图片，请谨慎选择?</span>
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
      <template v-if="column.dataIndex === 'id'" style="height: 100px">
        <div>{{ text || ' ' }}</div>
      </template>
      <template v-if="column.dataIndex === 'url'" style="height: 100px">
        <a-image
          :width="50"
          :src="text"
          fallback="data:image/png;base64,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"
        />
      </template>
      <template v-if="column.dataIndex === 'uploadTime'" style="height: 100px">
        <span>{{ day.shortTime(text, '/', true, false) }}</span>
      </template>
      <template v-if="column.dataIndex === 'createTime'">
        <span>{{ day.shortTime(text, '/', true, false) }}</span>
      </template>
      <!-- operation格式自定义 -->
      <template v-else-if="column.dataIndex === 'operation'">
        <a-button type="primary" danger class="button-table" @click="onDelete(record)">删除</a-button>
      </template>
    </template>
  </data-table>
</template>

<script lang="ts" setup>
import { ref, onMounted, onActivated, watch, computed, inject } from 'vue';

import { useImagesStore } from '@/stores/images';
import { useDate } from '@tanxiang/utils';
import ModalDelete from '@/components/modal-operation/ModalDelete.vue';
import { useAuthImages } from '@tanxiang/apis';
import { useRouter } from 'vue-router';

import useRequestData from '@/hooks/useRequestData';
//@ts-ignore
import { columns } from './tableData';
//@ts-ignore
import useReload from '@/hooks/useReload';
import { useBaseStore } from '@/stores/base';
import { messageInfo } from '@tanxiang/common';
import { innerColumns } from '@/pages/list-article/tableData';

const { commonOperation } = useReload();

const { messageError } = messageInfo();

const day = useDate();
const auth = useAuthImages();
const base = useBaseStore();
const imagesStore = useImagesStore();

const size = ref<number>(15);
const table = ref<any>(null);
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

const { request, getDataSource, search } = useRequestData(loading, auth.imagesList, emit);

onMounted(() => request());

onActivated(() => {
  if (base.getPreviousRoutePath !== '/refresh') return;
  loading.value = true;
  // 如果进入时，timer.value存在数据，则表示已经有一次的请求了，关闭当前请求，重新建立请求
  if (timer.value) clearTimeout(timer.value);
  timer.value = setTimeout(() => request(), 100);
});

const pageChange = (paging: any) => {
  if (imagesStore.getSearch.value === '' || imagesStore.getSearch.value.length === 0) {
    request(paging.current);
  } else {
    search(
      imagesStore.getSearch.name,
      imagesStore.getSearch.select,
      imagesStore.getSearch.value,
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
    .deleteImages(data.value.id)
    .then((success: any) => commonOperation(success.message, list, cancel, request, current.value))
    .catch((error: any) => messageError(!error || error.name ? '删除失败！' : error));
};

const onDelete = (key: any) => del.value.showModal(key, tagArticle, false);

const dataSource = computed(() => {
  return ![undefined, null].includes(props.source['total']) ? props.source : getDataSource.value;
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
  font-size: 13px;
  border-radius: 4px;
}
</style>

<style>
.images-delete-modal .ant-modal-content .ant-modal-footer {
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>