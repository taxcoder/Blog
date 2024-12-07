<template>
  <div id="recovery-essay-table">
    <a-table
      sticky
      :data-source="result"
      :columns="essayColumns"
      :loading="loading"
      size="small"
      @change="pageChange"
      :pagination="{
        position: ['bottomCenter'],
        size: 'middle',
        defaultPageSize: 15,
        hideOnSinglePage: true,
        current: current,
        showSizeChanger: false,
        total: total,
      }"
    >
      <template #bodyCell="{ column, text, record }">
        <!-- name格式自定义 -->
        <template v-if="column.dataIndex === 'id'" style="height: 100px">
          <div>{{ text || ' ' }}</div>
        </template>
        <template v-if="column.dataIndex === 'content'" style="height: 100px">
          <div v-html="text"></div>
        </template>
        <!-- name格式自定义 -->
        <template v-if="column.dataIndex === 'mood'" style="height: 100px">
          <img :src="text || ' '" alt="text" class="w-[25px]" />
        </template>
        <!-- articleCount格式自定义 -->
        <template v-if="column.dataIndex === 'likeCount'">
          <a-button type="link">{{ text }}</a-button>
        </template>
        <!-- createTime格式自定义 -->
        <template v-if="column.dataIndex === 'createTime'">
          <span>{{ shortTime(text, '/', true, false) }}</span>
        </template>
        <!-- removeTime格式自定义 -->
        <template v-if="column.dataIndex === 'removeTime'" style="height: 100px">
          <span>{{ headwayBoard(new Date(text).getTime()) }}</span>
        </template>
        <!-- operation格式自定义 -->
        <template v-else-if="column.dataIndex === 'operation'">
          <a-button type="primary" size="small" class="button-table modify" @click="onRestore(record)">恢复</a-button>
          <a-button type="primary" danger size="small" class="button-table" @click="onDelete(record)">删除</a-button>
        </template>
      </template>
    </a-table>
  </div>
</template>

<script setup lang="ts">
import { computed, inject, onActivated, onMounted, ref, watch } from 'vue';

import { useDate } from '@tanxiang/utils';
import { useRouter } from 'vue-router';

import { useAuthEssay } from '@tanxiang/apis';
import useRequestData from '@/hooks/useRequestData';
//@ts-ignore
import { essayColumns } from './tableData';
//@ts-ignore
import { useRecoveryStore } from '@/stores/recovery';
import { useBaseStore } from '@/stores/base';
import { messageInfo } from '@tanxiang/common';

const { messageError, messageSuccess } = messageInfo();

const { headwayBoard, shortTime } = useDate();
const base = useBaseStore();
const router = useRouter();
const auth = useAuthEssay();
const recovery = useRecoveryStore();

const timer = ref<any>(null);
const current = ref<number>(1);
const loading = ref<boolean>(true);

const tabSource: any = inject('tableEssaySource');
const tabCurrent: any = inject('tabEssayCurrent');

const emit = defineEmits(['dataSource', 'current']);
//@ts-ignore
const { request, getDataSource, search } = useRequestData(loading, auth.essayRecoveryList, emit);

onMounted(() => request());

onActivated(() => {
  if (base.getPreviousRoutePath !== '/refresh') return;
  loading.value = true;
  // 如果进入时，timer.value存在数据，则表示已经有一次的请求了，关闭当前请求，重新建立请求
  if (timer.value) clearTimeout(timer.value);
  timer.value = setTimeout(() => request(), 100);
});

const pageChange = (paging: any) => {
  if (recovery.getEssaySearch.value === '' || recovery.getEssaySearch.value.length === 0) {
    request(paging.current);
  } else {
    search(
      recovery.getEssaySearch.name,
      recovery.getEssaySearch.select,
      recovery.getEssaySearch.value,
      emit,
      paging.current,
      15
    );
  }
};

const onRestore = (key: any) => {
  //@ts-ignore
  ElMessageBox.confirm('是否还原该随笔？', '是否确认还原', {
    confirmButtonText: '确认',
    cancelButtonText: '取消',
    type: 'warning',
  })
    .then(() => {
      return auth.essayRestoreById(key.id);
    })
    .then((success: any) => {
      messageSuccess(success.message);
      setTimeout(() => request(), 1200);
    })
    .catch((error: any) => {
      if (error === 'cancel') return;
      messageError(!error || error.name ? '网络错误！' : error);
    });
};

const onDelete = (key: any) => {
  //@ts-ignore
  ElMessageBox.confirm('是否从回收站彻底移除该随笔？', '是否确认删除', {
    confirmButtonText: '确认',
    cancelButtonText: '取消',
    type: 'warning',
  })
    .then(() => {
      return auth.essayRestoreDeleteById(key.id);
    })
    .then((success: any) => {
      messageSuccess(success.message);
      setTimeout(() => request(), 1200);
    })
    .catch((error: any) => {
      if (error === 'cancel') return;
      messageError(!error || error.name ? '网络错误！' : error);
    });
};

const result = computed(() =>
  ![undefined, null].includes(tabSource.value.total) ? tabSource.value.records : getDataSource.value.records
);

const total = computed(() =>
  ![undefined, null].includes(tabSource.value.total) ? tabSource.value.total : getDataSource.value.total
);

watch(
  () => tabCurrent.value,
  (newCurrent: number) => (current.value = newCurrent)
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

.table-auto-complete {
  border-radius: 20px;
}

.modify:hover {
  background-color: #eebe77;
}
</style>

<style>
.el-message-box__title {
  font-weight: 700;
}
</style>