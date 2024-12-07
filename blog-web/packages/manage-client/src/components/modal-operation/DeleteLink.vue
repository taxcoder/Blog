<template>
  <a-spin :spinning="spinning" v-if="props.isDisplay">
    <slot name="input" />
    <div v-if="list.datas.length > 0" class="mt-[30px]">
      <div class="info">取消文章关联：</div>
      <el-transfer
        v-if="props.isTransfer"
        v-model="list.newLinkDatas.find((link) => link.select === selectOptions).datas"
        :data="transferData"
        class="style-transfer"
        filterable
        filter-placeholder="输入关键字搜索数据"
        :titles="['来源', '目的']"
        :button-texts="['回上面', '去下面']"
      >
        <template #right-footer>
          <el-select v-model="selectOptions" class="w-full">
            <el-option v-for="item in props.options" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </template>
      </el-transfer>
      <a-table
        v-else
        :row-selection="{ selectedRowKeys: list.checkList, onChange: onSelectChange }"
        :columns="columns"
        :row-key="(record) => record.value"
        :data-source="options"
        size="small"
        :pagination="false"
        :scroll="{ y: 500 }"
      >
        <template #bodyCell="{ column, text, record }">
          <template v-if="column.dataIndex === 'id'">{{ record.value }}</template>
          <template v-if="column.dataIndex === 'title'">{{ record.label }}</template>
        </template>
      </a-table>
    </div>
  </a-spin>
</template>

<script setup lang="ts">
import { computed, reactive, ref, watch } from 'vue';

const spinning = ref<boolean>(true);
const data = ref<{ key: string; label: string }[]>([]);
const selectOptions = ref<string>('');

let list = reactive<{
  checkList: number[];
  datas: { id: number; title: string }[];
  newLinkDatas: {
    select: string;
    datas: { value: string; label: string }[];
  }[];
}>({
  checkList: [],
  datas: [],
  newLinkDatas: [],
});

const transferData = reactive<{ label: string; key: string }[]>([]);

const props = defineProps({
  isDisplay: { type: Boolean, required: true },
  columns: { type: Array, required: true },
  isTransfer: { type: Boolean, default: true },
  options: { type: Array, required: true },
});

const onSelectChange: any = (keys: any[]) => {
  list.checkList.splice(0, list.checkList.length, ...keys);
};

const options = computed(() => {
  console.log(list.datas);
  return list.datas.map((data: any) => ({ label: data.title, value: data.id }));
});

defineExpose({ spinning, list });

watch(
  () => list.datas,
  (newData: any[]) => {
    data.value.splice(0, data.value.length, ...newData.map((d: any) => ({ label: d.title, key: d.id })));
    transferData.splice(0, transferData.length, ...data.value);
  },
  { deep: true }
);

watch(
  () => props.isDisplay,
  (newVal: boolean) => {
    if (!newVal) spinning.value = false;
  },
  { immediate: true }
);

watch(
  () => selectOptions.value,
  (newOptions: string) => {
    let temp = [];
    list.newLinkDatas.forEach((value: any) => temp.push(...value.datas));
    // 当前选中的select的数据
    let l = list.newLinkDatas.find((d) => d.select === newOptions).datas;
    // 排除掉自己选中的，这样可以让其显示出来
    temp.splice(0, temp.length, ...temp.filter((t) => !l.includes(t)));
    transferData.splice(0, transferData.length, ...data.value.filter((d: any) => !temp.includes(d.key)));
  }
);

watch(
  () => props.options,
  (newOptions: any[]) => {
    if (newOptions.length > 0) {
      selectOptions.value = newOptions[0].value;
      list.newLinkDatas.splice(
        0,
        list.newLinkDatas.length,
        ...newOptions.map((option: any) => ({ select: option.value, datas: [] }))
      );
    }
  },
  { deep: true }
);
</script>

<style scoped>
.info {
  font-weight: 700;
  padding: 6px 3px;
}
.group-list {
  max-height: calc(100vh / 2.5);
  overflow: auto;
}

.group-list::-webkit-scrollbar {
  display: none;
}
</style>

<style>
.style-transfer .el-transfer-panel {
  width: 100% !important;
}

.style-transfer .el-transfer__buttons {
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 10px 0;
}

.group-list .ant-checkbox-group {
  display: grid;
}

.group-list .ant-checkbox-group .ant-checkbox-group-item {
  white-space: nowrap;
  text-overflow: ellipsis;
  overflow: hidden;
}

.group-list .ant-checkbox-group .ant-checkbox-group-item span:last-child {
  white-space: nowrap;
  text-overflow: ellipsis;
  overflow: hidden;
}
</style>