<template>
  <modal-modify ref="modal" :okClick="okModifyClick" :max-length="4" :width="getModalWidth">
    <template #other>
      <a-collapse v-model:activeKey="activeKey" accordion class="excerpt-collapse" @change="collapseChange">
        <a-collapse-panel key="content" header="更新文本">
          <a-textarea
            v-model:value="excerpts.content"
            placeholder="请输入摘抄内容"
            @change="change('content')"
            class="w-full important-rounded-0 important-min-h-[200px]"
          />
        </a-collapse-panel>
        <a-collapse-panel key="file" header="更新图片">
          <user-upload
            :list="excerpts.file"
            @change-file="changeFile"
            :class="{ 'important-h-[200px]': excerpts.file.length === 0 }"
          />
        </a-collapse-panel>
        <a-collapse-panel key="chats" header="删除信息">
          <a-spin :spinning="chatLoading" :delay="500">
            <a-table
              :row-selection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
              :columns="chatColumns"
              :row-key="(record) => record.id"
              :data-source="chats"
              size="small"
              :pagination="false"
              :scroll="{ y: 500 }"
            >
              <template #bodyCell="{ column, text, record }">
                <template v-if="column.dataIndex === 'status'" style="height: 100px">
                  <el-text>{{ text ? '博主' : '访客' }}</el-text>
                </template>
              </template>
            </a-table>
          </a-spin>
        </a-collapse-panel>
      </a-collapse>
    </template>
  </modal-modify>
  <modal-delete
    ref="del"
    :okClick="okDeleteClick"
    :width="300"
    title=""
    :bodyStyle="{ height: '40px', display: 'flex', alignItems: 'center', justifyContent: 'center' }"
    :cancelButtonProps="{ size: 'small' }"
    :okButtonProps="{ size: 'small' }"
    wrapClassName="excerpt-delete-modal"
    :notDisabled="true"
  >
    <template #content>
      <span style="width: 100%; display: block; text-align: center">是否确认删除摘抄?</span>
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
      <template v-if="column.dataIndex === 'content'" style="height: 100px">
        <div class="excerpt-content">
          <div v-if="text.startsWith('<img src=') && text.endsWith('/>')">
            <a-image
              :width="40"
              :src="getUrl(text)"
              fallback="data:image/png;base64,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"
            ></a-image>
          </div>
          <div v-else>
            <el-text truncated>{{ text || '' }}</el-text>
          </div>
        </div>
      </template>
      <template v-if="column.dataIndex === 'chatNumber'" style="height: 100px">
        <el-text>{{ text }}</el-text>
      </template>
      <template v-if="column.dataIndex === 'createTime'" style="height: 100px">
        <span>{{ shortTime(text, '/', true, false) }}</span>
      </template>
      <!-- operation格式自定义 -->
      <template v-else-if="column.dataIndex === 'operation'">
        <a-button type="primary" size="small" class="button-table modify" @click="onModify(record)">修改</a-button>
        <a-button type="primary" danger size="small" class="button-table ml-[5px]" @click="onDelete(record)">
          删除
        </a-button>
      </template>
    </template>
  </data-table>
</template>

<script lang="ts" setup>
import { computed, inject, onActivated, onMounted, reactive, ref, watch } from 'vue';

import { useRouter } from 'vue-router';
import { useDate } from '@tanxiang/utils';
//@ts-ignore
import useReload from '@/hooks/useReload';
import { useBaseStore } from '@/stores/base';
import { useAuthExcerpt, useAuthChats, useChats } from '@tanxiang/apis';
import useRequestData from '@/hooks/useRequestData';
//@ts-ignore
import { columns, innerColumns } from './tableData';
import { messageInfo } from '@tanxiang/common';
import ModalDelete from '@/components/modal-operation/ModalDelete.vue';
import ModalModify from '@/components/modal-operation/ModalModify.vue';
import { useExcerptStore } from '@/stores/excerpt';

const { commonOperation } = useReload();
const { messageError, messageSuccess } = messageInfo();

const { shortTime } = useDate();
const base = useBaseStore();
const auth = useAuthExcerpt();
const excerptStore = useExcerptStore();

const router = useRouter();
const currentData = ref<any>();
const del = ref<any>(null);
const modal = ref<any>(null);
const timer = ref<any>(null);
const current = ref<number>(1);
const loading = ref<boolean>(true);
const chatLoading = ref<boolean>(true);
const activeKey = ref<string>('content');
const selectedRowKeys = reactive<string[]>([]);

const tableSource = reactive<any>({
  current: 1,
  records: [],
  total: 0,
  size: 15,
});

const chats = reactive<any[]>([]);

const excerpts = reactive<any>({
  file: [] as File[],
  content: '' as string,
});

const chatColumns = reactive<any>([
  {
    dataIndex: 'content',
    title: '聊天内容',
    align: 'center',
  },
  {
    dataIndex: 'author',
    title: '作者',
    align: 'center',
    width: 100,
  },
  {
    dataIndex: 'status',
    title: '身份',
    align: 'center',
    width: 80,
  },
  {
    dataIndex: 'email',
    title: '邮箱',
    align: 'center',
    width: 200,
    responsive: ['sm'],
  },
  {
    dataIndex: 'webUrl',
    title: '作者网址',
    align: 'center',
    responsive: ['md'],
  },
]);
const status: any = inject('status');
const tabCurrent: any = inject('tabCurrent');

const props = defineProps({
  source: { type: Object, required: true },
});

const emit = defineEmits(['current', 'dataSource']);

const { request, getDataSource, search } = useRequestData(loading, auth.excerptList, emit);

onMounted(() => request());

onActivated(() => {
  if (base.getPreviousRoutePath !== '/refresh') return;
  loading.value = true;
  // 如果进入时，timer.value存在数据，则表示已经有一次的请求了，关闭当前请求，重新建立请求
  if (timer.value) clearTimeout(timer.value);
  timer.value = setTimeout(() => request(), 100);
});

const onSelectChange: any = (keys: any[]) => {
  selectedRowKeys.splice(0, selectedRowKeys.length, ...keys);
};

const collapseChange = (key: any) => {
  if (key === 'chats') {
    useChats()
      .chatList(currentData.value.id)
      .then((success: any) => chats.splice(0, chats.length, ...success.data))
      .catch((error: any) => messageError(!error || error.name ? '网络错误！' : error.message))
      .finally(() => (chatLoading.value = false));
  }
};

const excerptInfo = (key: any, link: any) => {
  currentData.value = key;
  if (selectedRowKeys.length > 0) {
    activeKey.value = 'chats';
  } else if (key.content.startsWith('<img src=') && key.content.endsWith('/>')) {
    activeKey.value = 'file';
    let url = getUrl(key.content);
    excerpts.file.splice(0, excerpts.file.length, {
      name: url.substring(url.lastIndexOf('/') + 1),
      url: url,
    });
  } else {
    activeKey.value = 'content';
    excerpts.content = key.content;
  }
  link.spinning = false;
};

const okModifyClick = (modify, d, list, cancel) => {
  if (activeKey.value === 'chats') {
    if (selectedRowKeys.length === 0) {
      messageError('没有需要删除的聊天内容！');
      return;
    }
    if (selectedRowKeys.length === 1) {
      useAuthChats()
        .chatDelete(selectedRowKeys[0])
        .then((success: any) => {
          commonOperation(success.message, list, cancel, request, current.value);
          selectedRowKeys.splice(0, selectedRowKeys.length);
          chats.splice(0, chats.length);
        })
        .catch((error: any) => messageError(!error || error.name ? '删除失败！' : error));
    } else {
      useAuthChats()
        .chatDeleteList(selectedRowKeys)
        .then((success: any) => {
          commonOperation(success.message, list, cancel, request, current.value);
          selectedRowKeys.splice(0, selectedRowKeys.length);
          chats.splice(0, chats.length);
        })
        .catch((error: any) => messageError(!error || error.name ? '删除失败！' : error));
    }
  } else if (activeKey.value === 'content') {
    if (excerpts.content.length === 0 || excerpts.content === '') {
      messageError('摘抄内容为空！');
      return;
    }
    if (excerpts.content.length > 300) {
      messageError('摘抄内容不能超过300个字！');
      return;
    }
    auth
      .excerptUpdateContent(modify.value.id, excerpts.content)
      .then((success: any) => {
        messageSuccess(success.message);
        tableSource.records.map((item: any) => {
          if (item.id === modify.value.id) {
            item.content = success.data;
          }
        });
        cancel();
      })
      .catch((error: any) => {
        messageError(!error || error.name ? '更新失败！' : error);
      });
  } else {
    if (excerpts.file.length === 0) {
      messageError('请上传图片！');
      return;
    }
    auth
      .excerptUpdateFile(modify.value.id, excerpts.file[0])
      .then((success: any) => {
        messageSuccess(success.message);
        tableSource.records.map((item: any) => {
          if (item.id === modify.value.id) {
            item.content = success.data;
          }
        });
        cancel();
      })
      .catch((error: any) => messageError(!error || error.name ? '更新失败！' : error));
  }
};

const okDeleteClick = (data: any, list: any, cancel: Function) => {
  useRequestData('正在删除！');
  auth
    .excerptDelete(data.value.id)
    .then((success: any) => commonOperation(success.message, list, cancel, request, current.value))
    .catch((error: any) => messageError(!error || error.name ? '删除失败！' : error));
};

const onDelete = (key: any) => del.value.showModal(key, excerptInfo, false);

const onModify = (key: any) => modal.value.showModal(key, excerptInfo, false);

const pageChange = (paging: any) => {
  if (excerptStore.getSearch.value === '' || excerptStore.getSearch.value.length === 0) {
    request(paging.current);
  } else {
    search(
      excerptStore.getSearch.name,
      excerptStore.getSearch.select,
      excerptStore.getSearch.value,
      emit,
      paging.current,
      15
    );
  }
};

const change = (data: string) => {
  if (data === 'content') {
    excerpts.file.splice(0, excerpts.file.length);
    selectedRowKeys.splice(0, selectedRowKeys.length);
  } else {
    excerpts.content = '';
    selectedRowKeys.splice(0, selectedRowKeys.length);
  }
};

const changeFile = (file: any) => {
  excerpts.file.splice(0, excerpts.file.length, ...file);
  change('file');
};

const getUrl = (text: string) => {
  return text.replace("<img src='", '').match('.*webp')[0];
};

const rowSelection: any['rowSelection'] = {
  onChange: (selectedRowKeys: string[], selectedRows: any[]) => {
    console.log(`selectedRowKeys: ${selectedRowKeys}`, 'selectedRows: ', selectedRows);
  },
  getCheckboxProps: (record: any) => {
    console.log(record);
    return {
      id: record.id + '',
      content: record.content,
    };
  },
};

const dataSource = computed(() => {
  return tableSource && ![undefined, null].includes(tableSource.total) ? tableSource : getDataSource.value;
});

const getModalWidth = computed(() => {
  return status.width <= 600 ? '70%' : 1000;
});

watch(
  () => tabCurrent.value,
  (newCurrent: number) => {
    current.value = newCurrent;
  },
  { deep: true }
);

watch(
  () => props.source,
  (newSource: any) => {
    if (!newSource || !newSource.records) return;
    tableSource.current = newSource.current;
    tableSource.total = newSource.total;
    tableSource.size = newSource.size;
    tableSource.records.splice(0, tableSource.records.length, ...newSource.records);
  },
  { deep: true, immediate: true }
);
</script>

<style scoped>
.button-table {
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
.excerpt-delete-modal .ant-modal-content .ant-modal-footer {
  display: flex;
  align-items: center;
  justify-content: center;
}

.excerpt-collapse .ant-collapse-content-box {
  padding: 0 !important;
}

.excerpt-content > img {
  width: 30px;
  height: 30px;
}

.excerpt-collapse .ant-checkbox-wrapper {
  padding: 8px;
}
</style>