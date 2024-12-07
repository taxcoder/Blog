<template>
  <div id="recovery-article-table">
    <a-table
      sticky
      :data-source="result"
      :columns="articleColumns"
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
        <!-- name格式自定义 -->
        <template v-if="column.dataIndex === 'name'" style="height: 100px">
          <span>{{ text || ' ' }}</span>
        </template>
        <!-- classificationName格式自定义 -->
        <template v-if="column.dataIndex === 'classificationName'" style="height: 100px">
          <span>{{ text || ' ' }}</span>
        </template>
        <!-- image格式自定义 -->
        <template v-if="column.dataIndex === 'image'" style="height: 100px">
          <a-image
            :src="text"
            fallback="data:image/png;base64,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"
          />
        </template>
        <!-- contentUrl格式自定义 -->
        <template v-if="column.dataIndex === 'contentUrl'">
          <span>{{ decodeURIComponent(text) }}</span>
        </template>
        <!-- tags格式自定义 -->
        <template v-if="column.dataIndex === 'removeTime'" style="height: 100px">
          <span>{{ day.headwayBoard(new Date(text).getTime()) }}</span>
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
import { computed, inject, onActivated, onMounted, reactive, ref, watch } from 'vue';

import { useDate } from '@tanxiang/utils';
import { useRouter } from 'vue-router';

import { useAuthArticle } from '@tanxiang/apis';
import useRequestData from '@/hooks/useRequestData';
//@ts-ignore
import { articleColumns } from './tableData';
//@ts-ignore
import { useRecoveryStore } from '@/stores/recovery';
import { useBaseStore } from '@/stores/base';
import { messageInfo } from '@tanxiang/common';

const { messageError, messageSuccess } = messageInfo();

const day = useDate();
const router = useRouter();
const base = useBaseStore();
const auth = useAuthArticle();
const recovery = useRecoveryStore();

const timer = ref<any>(null);
const current = ref<number>(1);
const loading = ref<boolean>(true);

const color = reactive<string[]>(['blue', 'success', 'magenta']);

const tabSource: any = inject('tableArticleSource');
const tabCurrent: any = inject('tabArticleCurrent');

const emit = defineEmits(['dataSource', 'current']);
//@ts-ignore
const { request, getDataSource, search } = useRequestData(loading, auth.recoveryArticleList, emit);

onMounted(() => request());

onActivated(() => {
  if (base.getPreviousRoutePath !== '/refresh') return;
  loading.value = true;
  // 如果进入时，timer.value存在数据，则表示已经有一次的请求了，关闭当前请求，重新建立请求
  if (timer.value) clearTimeout(timer.value);
  timer.value = setTimeout(() => request(), 100);
});

const pageChange = (paging: any) => {
  if (recovery.getArticleSearch.value === '' || recovery.getArticleSearch.value.length === 0) {
    request(paging.current);
  } else {
    search(
      recovery.getArticleSearch.name,
      recovery.getArticleSearch.select,
      recovery.getArticleSearch.value,
      emit,
      paging.current,
      15
    );
  }
};

const onRestore = (key: any) => {
  //@ts-ignore
  ElMessageBox.confirm('是否还原该文章？', '是否确认还原', {
    confirmButtonText: '确认',
    cancelButtonText: '取消',
    type: 'warning',
  })
    .then(() => {
      return auth.articleRestoreById(key.id);
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
  ElMessageBox.confirm('是否从回收站彻底移除该文章？', '是否确认删除', {
    confirmButtonText: '确认',
    cancelButtonText: '取消',
    type: 'warning',
  })
    .then(() => {
      return auth.realDeleteArticleById(key.id);
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

const source = (value: any) => ({
  id: value.id,
  name: value.title,
  classificationName: value.classification.name,
  image: value.image,
  removeTime: value.removeTime,
  tags: value.tags,
  top: value.top,
  address: value.address,
  likeCount: value.likeCount,
  contentUrl: value.contentUrl,
  updateTime: value.updateTime,
});

const result = computed(() => {
  return ![undefined, null].includes(tabSource.value.total)
    ? tabSource.value.records.map((value: any) => source(value))
    : getDataSource.value.records.map((value: any) => source(value));
});

const total = computed(() => {
  return ![undefined, null].includes(tabSource.value.total) ? tabSource.value.total : getDataSource.value.total;
});

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

.image {
  width: 100%;
  height: 100%;
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

<style>
.el-message-box__title {
  font-weight: 700;
}
</style>