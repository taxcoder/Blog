<template>
  <modal-delete ref="del" :okClick="okDeleteClick" :not-disabled="true">
    <template #content>
      <strong>注意：</strong>
      是否确认删除该文章，请谨慎选择！
    </template>
  </modal-delete>

  <data-table
    :columns="columns"
    :loading="loading"
    :current="current"
    :dataSource="tableSource"
    @page-change="pageChange"
    :inner-columns="innerColumns"
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
      <!-- classificationName格式自定义 -->
      <template v-if="column.dataIndex === 'classificationName'" style="height: 100px">
        <span>{{ text || ' ' }}</span>
      </template>
      <!-- tags格式自定义 -->
      <template v-if="column.dataIndex === 'tags'" style="height: 100px">
        <a-tag :color="color[index % color.length]" v-for="(item, index) in text" :key="item.id">
          {{ item.name }}
        </a-tag>
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
      <!-- operation格式自定义 -->
      <template v-else-if="column.dataIndex === 'operation'">
        <a-button type="primary" size="small" class="button-table modify" @click="onModify(record)">修改</a-button>
        <a-button type="primary" danger size="small" class="button-table" @click="onDelete(record)">删除</a-button>
      </template>
    </template>
    <template #inner="{ column, index, text, record }">
      <!-- likeCount格式自定义 -->
      <template v-if="column.dataIndex === 'likeCount'">
        <a-button type="link">{{ text }}</a-button>
      </template>
      <!-- address格式自定义 -->
      <template v-if="column.dataIndex === 'address'">
        <span>{{ text }}</span>
      </template>
      <!-- top格式自定义 -->
      <template v-if="column.dataIndex === 'top'">
        <a-switch
          :key="getChecked(record).id"
          :checked="getChecked(record).status"
          @click="switchClick(record)"
          :disabled="getChecked(record).loading"
          :loading="getChecked(record).loading"
        />
      </template>
      <!-- updateTime格式自定义 -->
      <template v-if="column.dataIndex === 'updateTime'">
        <div>{{ day.shortTime(text, '/', true, false) }}</div>
      </template>
    </template>
  </data-table>
</template>

<script setup lang="ts">
import { useDate } from '@tanxiang/utils';

import { useAuthArticle } from '@tanxiang/apis';
import useRequestData from '@/hooks/useRequestData';
//@ts-ignore
import { columns, innerColumns } from './tableData';

import useReload from '@/hooks/useReload';
//@ts-ignore
import { useArticleStore } from '@/stores/article';
import { useBaseStore } from '@/stores/base';
import { messageInfo } from '@tanxiang/common';

const { commonOperation } = useReload();

const { messageError, messageSuccess } = messageInfo();

const router = useRouter();
const route = useRoute();

const day: any = useDate();
const base = useBaseStore();
const auth = useAuthArticle();

const current = ref<number>(1);
const currentData = ref<any>({});
const del = ref<any>(null);
const loading = ref<boolean>(true);
const modal = ref<any>(null);
const timer = ref<any>(null);
const value = ref<string>('');
const status = ref<boolean>(false);

const select = reactive<any[]>([]);
const modalData = reactive<any[]>([]);
const color = reactive<string[]>(['blue', 'success', 'magenta']);
const values = reactive<{ key: number; articleId: number }[]>([]);
const checked = reactive<{ id: number; status: boolean; loading: boolean }[]>([]);

const tabCurrent: any = inject('tabCurrent');

const articleStore = useArticleStore();

const props = defineProps({
  source: { type: Object, required: true },
});

const emit = defineEmits(['dataSource', 'current']);

const { request, getDataSource, search } = useRequestData(loading, auth.articleList, emit);

onMounted(() => request());

onActivated(() => {
  if (articleStore.getIsRefer || base.getPreviousRoutePath === '/refresh') {
    loading.value = true;
    // 如果进入时，timer.value存在数据，则表示已经有一次的请求了，关闭当前请求，重新建立请求
    if (timer.value) clearTimeout(timer.value);
    timer.value = setTimeout(() => request(), 100);
  }
});

const pageChange = (paging: any) => {
  if (articleStore.getSearch.value === '' || articleStore.getSearch.value.length === 0) {
    request(paging.current);
  } else {
    search(
      articleStore.getSearch.name,
      articleStore.getSearch.select,
      articleStore.getSearch.value,
      emit,
      paging.current,
      15
    );
  }
};

const switchClick = (record: any) => {
  // 开启加载和禁用
  getChecked.value(record).loading = true;
  // 让加载动画最少执行一秒，提升观感
  setTimeout(() => {
    auth
      .changeArticleTop(record.id)
      .then((success: any) => {
        messageSuccess(success.message);
        request(current.value);
      })
      .catch((error: any) => {
        messageError(!error || error.name ? '网络错误，请重新尝试！' : error);
      })
      .finally(() => (getChecked.value(record).loading = false));
  }, 1000);
};

const listArticle = (key: any, link: any, status: boolean) => {
  if (!status) {
    currentData.value = key;
    modalData.splice(0, modalData.length);
    modalData.push(...getDataSource.value.records.filter((data: any) => data.id === key.id)[0].articles);
    link.list.datas.push(...modalData);
    if (status) link.list.checkList.push(...modalData.map((s: any) => s.id));
  }

  link.spinning = false;
};

const okDeleteClick = (modify: any, list: any, cancel: Function) => {
  auth
    .deleteArticleById(modify.value.id)
    .then((success: any) => commonOperation(success.message, list, cancel, request, current.value))
    .catch((error: any) => messageError(!error || error.name ? '网络错误' : error));
};

const onDelete = (key: any) => del.value.showModal(key, listArticle, true);

// 文章信息修改页
const onModify = (key: any) => router.push(`/editor/article/${key.id}`);

const dataSource = (value: any) => ({
  id: value.id,
  name: value.title,
  classificationName: value.classification.name,
  image: value.image,
  tags: value.tag,
  top: value.top,
  address: value.address,
  likeCount: value.likeCount,
  contentUrl: value.contentUrl,
  updateTime: value.updateTime,
});

const getChecked = computed(() => (record: any) => {
  return checked.filter((c: any) => c.id === record.id)[0];
});

const result = computed(() => {
  checked.splice(0, checked.length);
  //@ts-ignore
  return ![undefined, null].includes(props.source.total)
    ? props.source.records.map((value: any) => {
        checked.push({ id: value.id, status: value.top, loading: false });
        return dataSource(value);
      })
    : getDataSource.value.records.map((value: any) => {
        checked.push({ id: value.id, status: value.top, loading: false });
        return dataSource(value);
      });
});

const tableSource = computed(() => {
  return {
    total: props.source['total'],
    records: result.value,
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