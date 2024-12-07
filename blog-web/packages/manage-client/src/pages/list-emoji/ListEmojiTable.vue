<template>
  <modal-delete ref="del" :okClick="okDeleteClick" :notDisabled="true">
    <template #content>
      <span>是否确认删除该表情?</span>
    </template>
  </modal-delete>

  <div id="tag-table" class="sticky top-0">
    <a-table
      sticky
      :data-source="dataSource.records"
      :columns="columns"
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
        total: dataSource.total,
      }"
    >
      <template #bodyCell="{ column, text, record }">
        <!-- name格式自定义 -->
        <template v-if="column.dataIndex === 'id'" style="height: 100px">
          <div>{{ text || ' ' }}</div>
        </template>
        <!-- name格式自定义 -->
        <template v-if="column.dataIndex === 'icon'" style="height: 100px">
          <img :src="text" alt="" class="w-[25px]" />
        </template>
        <template v-if="column.dataIndex === 'name'">
          <div class="editable-cell">
            <div v-if="editableData[record.id]" class="editable-cell-input-wrapper flex items-center justify-between">
              <a-input v-model:value="editableData[record.id].name" size="small" @pressEnter="save(record.id)" />
              <check-outlined class="editable-cell-icon-check ml-[10px]" @click="save(record.id)" />
            </div>
            <div v-else class="editable-cell-text-wrapper flex items-center justify-between">
              <span>{{ text || ' ' }}</span>
              <edit-outlined class="editable-cell-icon" @click="edit(record.id)" />
            </div>
          </div>
        </template>
        <!-- articleCount格式自定义 -->
        <template v-if="column.dataIndex === 'address'">
          <span>{{ text || ' ' }}</span>
        </template>
        <template v-if="column.dataIndex === 'groupName'">
          <el-select :model-value="text" placeholder="Select" @change="(data:string) => handleChange(record,data)">
            <el-option v-for="item in cities" :key="item.value" :label="item.label" :value="item.value" />
            <template #footer>
              <el-button
                v-if="isAdding"
                type="primary"
                @click="onAddOption"
                v-text="'增加分组'"
                class="w-full rounded-[8px] flex-center"
              />
              <el-button
                v-if="isAdding"
                type="danger"
                @click="clearGroupName"
                v-text="'清除自定义分组'"
                class="w-full rounded-[8px] flex-center important-ml-0 mt-[5px]"
              />
              <template v-else>
                <el-input v-model="optionName" class="rounded-lg" placeholder="输入分组名" />
                <div class="mt-[7px] text-center">
                  <el-button type="primary" @click="onConfirm(record)" v-text="'提交'" />
                  <el-button @click="clear" v-text="'取消'" />
                </div>
              </template>
            </template>
          </el-select>
        </template>
        <!-- createTime格式自定义 -->
        <template v-if="column.dataIndex === 'createTime'">
          <span>{{ day.shortTime(text, '/', true, true) }}</span>
        </template>
        <!-- operation格式自定义 -->
        <template v-else-if="column.dataIndex === 'operation'">
          <a-button type="primary" danger size="small" class="button-table" @click="onDelete(record)">删除</a-button>
        </template>
      </template>
    </a-table>
  </div>
</template>

<script setup lang="ts">
import { useDate } from '@tanxiang/utils';
import useRequestData from '@/hooks/useRequestData';
import { useAuthEmoji } from '@tanxiang/apis';
//@ts-ignore
import { columns } from './tableData';
//@ts-ignore
import useReload from '@/hooks/useReload';
import { useEmojiStore } from '@/stores/emoji';
import { messageInfo } from '@tanxiang/common';
import { useBaseStore } from '@/stores/base';
import { UnwrapRef } from 'vue';
import { CheckOutlined, EditOutlined } from '@ant-design/icons-vue';

const { commonOperation } = useReload();
const { messageError, messageSuccess, messageLoading } = messageInfo();

const router = useRouter();
const day: any = useDate();
const auth = useAuthEmoji();
const base = useBaseStore();
const emojiStore = useEmojiStore();

const items = ref(['jack', 'lucy']);
const del = ref<any>(null);
const timer = ref<any>(null);
const loading = ref<boolean>(true);
const current = ref<number>(1);
const isAdding = ref<boolean>(true);
const optionName = ref<string>('');
const editableData: UnwrapRef<any> = reactive({});

const tabCurrent: any = inject('tabCurrent');
const deleteEmoji: Function = inject<Function>('deleteEmoji');
const updateEmoji: Function = inject<Function>('updateEmoji');
const source = reactive<any>({
  total: 0,
  records: [],
});

const props = defineProps({
  source: { type: Object, required: true },
});

const emit = defineEmits(['current', 'dataSource']);

const { request, getDataSource, search } = useRequestData(loading, auth.emojiList, emit);

onMounted(() => request());

onActivated(() => {
  if (base.getPreviousRoutePath !== '/refresh') return;
  loading.value = true;
  // 如果进入时，timer.value存在数据，则表示已经有一次的请求了，关闭当前请求，重新建立请求
  if (timer.value) clearTimeout(timer.value);
  timer.value = setTimeout(() => request(), 100);
});

const edit = (key: string) => {
  editableData[key] = getDataSource.value.records.filter((item) => key === item.id)[0];
};

const save = (key: string) => {
  auth
    .updateEmojiName(key, editableData[key].name)
    .then((success: any) => {
      Object.assign(getDataSource.value.records.filter((item) => key === item.id)[0], editableData[key]);
      delete editableData[key];
      messageSuccess(success.message);
    })
    .catch((error: any) => messageError(!error || error.name ? '更新失败！' : error));
};

const clearGroupName = () => {
  emojiStore.clearGroup();
  emojiStore.setGroup(emojiStore.getInitGroup);
};

const pageChange = (paging: any) => (current.value = paging.current);

const okDeleteClick = (data: any, list: any, cancel: Function) => {
  messageLoading('正在删除中');
  auth
    .deleteEmoji(data.value.id)
    .then((success: any) => {
      messageSuccess(success.message);
      deleteEmoji(data.value.id);
      cancel();
    })
    .catch((error: any) => messageError(!error || error.name ? '删除失败！' : error));
};

const onDelete = (key: any) => del.value.showModal(key, null, false);

const handleChange = (record: any, value: any) => updateEmoji(record.id, value);

const onAddOption = () => (isAdding.value = false);

const onConfirm = (record: any) => {
  if (optionName.value) {
    emojiStore.addGroup(optionName.value);
    updateEmoji(record.id, optionName.value);
    clear();
  }
};

const clear = () => {
  optionName.value = '';
  isAdding.value = true;
};

const dataSource = computed(() => {
  //@ts-ignore
  return ![undefined, null].includes(props.source.total) ? source : getDataSource.value;
});

const cities = computed(() => {
  return emojiStore.getGroup.map((item: string) => ({ label: item, value: item }));
});

watch(
  () => props.source,
  (newSource: any) => {
    source.records.splice(0, source.records.length, ...newSource.records);
    source.total = newSource.total;
  },
  { deep: true }
);

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