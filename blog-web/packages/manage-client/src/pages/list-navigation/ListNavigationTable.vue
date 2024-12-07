<template>
  <modal-modify ref="modal" :okClick="okModifyClick" :max-length="4">
    <template #other>
      <a-segmented v-model:value="value" :options="options" :block="true" class="rounded-[6px]" />
      <div class="w-full min-h-[200px] mt-[10px]">
        <div v-if="options[0] === value" class="h-full">
          <a-textarea
            v-model:value="data.url"
            maxLength="200"
            class="important-h-[200px] rounded-[5px] break-all"
            :show-word-limit="true"
          />
        </div>
        <div v-else-if="options[1] === value" class="h-full">
          <a-textarea
            v-model:value="data.description"
            maxLength="50"
            class="important-h-[200px] rounded-[5px]"
            :show-word-limit="true"
          />
        </div>
        <div v-else-if="options[2] === value" class="h-full">
          <user-upload
            @ChangeFile="changeFile"
            :list="data.fileList"
            class="h-full"
            :class="{
              'important-h-[200px]': data.fileList.length === 0,
            }"
          />
        </div>
        <div v-else class="h-full">
          <remote-select
            ref="rSelect"
            :text="data.classificationNavigationName"
            :record="currentData"
            class="w-full"
            :fn="auth.selectCnInfo"
            @change="change"
            :list="navigationStore.getCnName"
          />
        </div>
      </div>
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
    wrapClassName="navigation-delete-modal"
    :notDisabled="true"
  >
    <template #content>
      <span style="width: 100%; display: block; text-align: center">是否确认删除该导航网站，请谨慎选择?</span>
    </template>
  </modal-delete>

  <data-table
    :columns="columns"
    :loading="loading"
    :current="current"
    :dataSource="dataSource"
    @page-change="pageChange"
    :inner-columns="innerColumns"
  >
    <template #out="{ column, text, record }">
      <template v-if="column.dataIndex === 'id'" style="height: 100px">
        <div>{{ text || ' ' }}</div>
      </template>
      <template v-if="column.dataIndex === 'title'" style="height: 100px">
        <input-update
          :datas="dataList"
          :record="setRecord(record, 'title')"
          textarea
          condition="title"
          :fn="auth.updateNavigationTitle"
        ></input-update>
      </template>
      <template v-if="column.dataIndex === 'description'" style="height: 100px">
        <input-update
          :datas="dataList"
          :record="setRecord(record, 'description')"
          condition="description"
          textarea
          :fn="auth.updateNavigationDescription"
        ></input-update>
      </template>
      <template v-if="column.dataIndex === 'url'" style="height: 100px">
        <span class="break-all">{{ text }}</span>
      </template>
      <template v-if="column.dataIndex === 'classificationNavigationName'" style="height: 100px">
        <remote-select
          ref="rSelect"
          :text="text"
          :record="record"
          :fn="auth.selectCnInfo"
          @change="change"
          :list="navigationStore.getCnName"
        />
      </template>
      <!-- operation格式自定义 -->
      <template v-else-if="column.dataIndex === 'operation'">
        <a-button type="primary" size="small" class="button-table modify" @click="onModify(record)">修改</a-button>
        <a-button type="primary" danger size="small" class="button-table ml-[5px]" @click="onDelete(record)">
          删除
        </a-button>
      </template>
    </template>

    <template #inner="{ column, index, text, record }">
      <template v-if="column.dataIndex === 'cnName'" style="height: 100px">
        <span>{{ text || '' }}</span>
      </template>
      <template v-if="column.dataIndex === 'favicon'" style="height: 100px">
        <el-image
          :hide-on-click-modal="true"
          class="w-[20px] h-[20px]"
          :src="getImage(text)"
          fit="cover"
          lazy
          :z-index="9999"
          :preview-teleported="true"
          :preview-src-list="[text]"
        />
      </template>
      <template v-if="column.dataIndex === 'ok'">
        <span>{{ text ? '已联通' : '无法联通' }}</span>
      </template>
      <template v-else-if="column.dataIndex === 'createTime'">
        <span>{{ shortTime(text, '/', true, false) }}</span>
      </template>
    </template>
  </data-table>
</template>

<script lang="ts" setup>
import { computed, inject, onActivated, onMounted, ref, watch } from 'vue';

import { useRouter, useRoute } from 'vue-router';
import { useDate } from '@tanxiang/utils';
//@ts-ignore
import useReload from '@/hooks/useReload';
import { useBaseStore } from '@/stores/base';
import { useAuthNavigation } from '@tanxiang/apis';
import useRequestData from '@/hooks/useRequestData';
//@ts-ignore
import { columns, innerColumns } from './tableData';
import { useNavigationStore } from '@/stores/navigation';
import { InputUpdate, RemoteSelect, messageInfo } from '@tanxiang/common';
import ModalDelete from '@/components/modal-operation/ModalDelete.vue';
import ModalModify from '@/components/modal-operation/ModalModify.vue';
import UserUpload from '@/components/user-upload/UserUpload.vue';
import * as url from 'url';

const { commonOperation } = useReload();
const { messageError, messageSuccess } = messageInfo();

const { shortTime } = useDate();
const base = useBaseStore();
const auth = useAuthNavigation();
const navigationStore = useNavigationStore();

const route = useRoute();
const router = useRouter();
const del = ref<any>(null);
const modal = ref<any>(null);
const timer = ref<any>(null);
const current = ref<number>(1);
const loading = ref<boolean>(true);
const rSelect = ref<any>(null);
const currentData = ref<any>();
const base64 = ref<string>('');

const options = reactive<any[]>(['网址', '描述', '图标', '分类']);
const value = ref<any>(options[0]);

const data = reactive<{
  url: string;
  description: string;
  fileList: File[];
  classificationNavigationName: string;
}>({
  url: '',
  description: '',
  fileList: [],
  classificationNavigationName: '',
});

const dataList = reactive<
  {
    content: string;
    id: number;
    input: boolean;
    isSuccess: boolean;
    isClick: boolean;
    loading: boolean;
    oldContent: string;
  }[]
>([]);
const tableSource = reactive<any>({
  current: 1,
  records: [],
  total: 0,
  size: 15,
});

const status: any = inject('status');
const tabCurrent: any = inject('tabCurrent');

const props = defineProps({
  source: { type: Object, required: true },
});

const emit = defineEmits(['current', 'dataSource']);

const { request, getDataSource, search } = useRequestData(loading, auth.selectList, emit);

onMounted(() => request());

onActivated(() => {
  if (base.getPreviousRoutePath !== '/refresh') return;
  loading.value = true;
  // 如果进入时，timer.value存在数据，则表示已经有一次的请求了，关闭当前请求，重新建立请求
  if (timer.value) clearTimeout(timer.value);
  timer.value = setTimeout(() => request(), 100);
});

const navigationInfo = (key: any, link: any, status: boolean) => {
  currentData.value = key;
  data.url = key.url;
  data.classificationNavigationName = key.classificationNavigationName;
  data.description = key.description;
  changeFile([{ name: 'img.png', url: key.favicon }]);
  link.spinning = false;
};

const okModifyClick = (d: any, list: any, cancel: Function) => {
  let regex =
    /^(http:\/\/www\.|https:\/\/www\.|http:\/\/|https:\/\/)?[a-z0-9]+([\-.]{1}[a-z0-9]+)*\.[a-z]{2,5}(:[0-9]{1,5})?(\/.*)?$/;
  if (value.value === options[0]) {
    if (data.url === currentData.value.url) {
      messageError('数据未变化！');
      return;
    } else if (!data.url || data.url === '' || data.url.length === 0) {
      messageError('网址不能为空！');
      return;
    } else if (!regex.test(data.url)) {
      messageError('网址格式错误！');
    } else {
      auth
        .updateNavigationUrl(currentData.value.id, data.url)
        .then((success: any) => {
          messageSuccess(success.data);
          setTimeout(() => {
            router.replace({ path: '/refresh', query: { t: Date.now(), f: route.path } });
          }, 500);
        })
        .catch((error: any) => messageError(!error || error.name ? '修改失败！' : error));
    }
  } else if (value.value === options[1]) {
    if (data.description === currentData.value.description) {
      messageError('数据未变化！');
      return;
    } else if (!data.description || data.description === '' || data.description.length === 0) {
      messageError('描述不能为空！');
      return;
    } else {
      auth
        .updateNavigationDescription(currentData.value.id, data.description)
        .then((success: any) => {
          tableSource.records.map((item: any) => {
            if (item.id === currentData.value.id) item.description = data.description;
            return item;
          });
          messageSuccess(success.data);
          modal.value.cancel();
        })
        .catch((error: any) => messageError(!error || error.name ? '修改失败！' : error));
    }
  } else if (value.value === options[2]) {
    auth
      .updateNavigationFavicon(currentData.value.id, data.fileList[0])
      .then((success: any) => {
        tableSource.records.map((item: any) => {
          if (item.id === currentData.value.id) item.favicon = success.data;
          return item;
        });
        messageSuccess('更新成功！');
        modal.value.cancel();
      })
      .catch((error: any) => messageError(!error || error.name ? '网络异常' : error));
  } else {
    modal.value.cancel();
    messageSuccess('修改成功！');
  }
};

const okDeleteClick = (data: any, list: any, cancel: Function) => {
  useRequestData('正在删除！');
  auth
    .deleteNavigation(data.value.id)
    .then((success: any) => commonOperation(success.message, list, cancel, request, current.value))
    .catch((error: any) => messageError(!error || error.name ? '删除失败！' : error));
};

const onDelete = (key: any) => del.value.showModal(key, navigationInfo, false);

const onModify = (key: any) => modal.value.showModal(key, navigationInfo, false);

const pageChange = (paging: any) => {
  if (navigationStore.getSearch.value === '' || navigationStore.getSearch.value.length === 0) {
    request(paging.current);
  } else {
    search(
      navigationStore.getSearch.name,
      navigationStore.getSearch.select,
      navigationStore.getSearch.value,
      emit,
      paging.current,
      15
    );
  }
};

const setRecord = computed(() => (record: any, condition: string) => {
  if (!dataList.find((l) => l['condition'] === condition && l['id'] === record.id)) {
    dataList.push({
      id: record.id,
      condition: condition,
      loading: false,
      isClick: false,
      isSuccess: false,
      input: false,
      oldContent: record[condition],
      content: record[condition],
    });
  }
  return record;
});

const change = (obj: { id: number; value: number; list: any[] }) => {
  if (navigationStore.getCnName.length === 0) {
    navigationStore.setCnName(obj.list);
  }
  auth
    .updateNavigationCnId(obj.id, obj.value)
    .then((success: any) => {
      messageSuccess(success.data);
      setTimeout(() => {
        router.replace({ path: '/refresh', query: { t: Date.now(), f: route.path } });
      }, 500);
    })
    .catch((error: any) => messageError(!error || error.name ? '修改失败！' : error));
};

const cancel = () => {
  setTimeout(() => changeFile([{ name: 'image.png', url: currentData.value['favicon'] }]), 500);
};

const changeFile = (files: any[]) => {
  data.fileList.splice(0, data.fileList.length, ...files);
};

const getImage = computed(() => (text: string) => {
  return text;
});

const dataSource = computed(() => {
  return tableSource && ![undefined, null].includes(tableSource.total) ? tableSource : getDataSource.value;
});

watch(
  () => tabCurrent.value,
  (newCurrent: number) => {
    current.value = newCurrent;
  },
  { deep: true }
);

watch(
  () => status.width,
  (newWidth: number) => {
    if (newWidth <= 575 && innerColumns[0].dataIndex !== 'id') {
      innerColumns.unshift({
        title: 'ID',
        dataIndex: 'id',
        align: 'center',
        width: 60,
      } as any);
    } else if (newWidth > 575 && innerColumns[0].dataIndex === 'id') {
      innerColumns.shift();
    }
  },
  { deep: true, immediate: true }
);

watch(
  () => props.source,
  (newSource: any) => {
    if (!newSource || !newSource.records) return;
    tableSource.current = newSource.current;
    tableSource.total = newSource.total;
    tableSource.size = newSource.size;
    dataList.splice(0, dataList.length);
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
.navigation-delete-modal .ant-modal-content .ant-modal-footer {
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>