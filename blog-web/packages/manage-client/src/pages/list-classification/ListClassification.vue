<template>
  <div id="classification" class="h-screen">
    <table-search :formState="formState" :name="name" @dataSource="dataSource" @current="current" @add="add" />
    <list-classification-table :source="tableSource" @dataSource="dataSource" @current="current" />
  </div>
</template>

<script setup lang="ts">
import { provide, reactive, ref, watch, onMounted } from 'vue';

import { useClassificationStore } from '@/stores/classification';
import TableSearch from '@/components/table-search/TableSearch.vue';
import ListClassificationTable from '@/pages/list-classification/ListClassificationTable.vue';
import { useRoute, useRouter } from 'vue-router';
import { useAuthClassification } from '@tanxiang/apis';
import useRequestData from '@/hooks/useRequestData';
import { messageInfo } from '@tanxiang/common';

const { messageError, messageSuccess } = messageInfo();

const router: any = useRouter();
const route: any = useRoute();
const auth = useAuthClassification();
const classification = useClassificationStore();

const { request } = useRequestData();

const tableSource = ref<any>({});
const tabCurrent = ref<number>(0);

const name = ref<string>('classification');

const formState = reactive<any>({
  key: [
    { value: 'id', label: 'ID', placeholder: '请输入一个正整数，不能为0' },
    { value: 'name', label: '分类名', placeholder: '根据分类名模糊查询' },
    { value: 'like/gte', label: '文章数(大于等于)', placeholder: '请输入一个非负整数' },
    { value: 'like/lte', label: '文章数(小于等于)', placeholder: '请输入一个非负整数' },
    { value: 'like/equ', label: '文章数(等于)', placeholder: '请输入一个非负整数' },
  ],
  select: 'id',
  value: '',
});

provide('tabCurrent', tabCurrent);

onMounted(() => {
  if (route.query.key && formState.key.find((c: any) => c.value === route.query.key))
    formState.select = route.query.key;
  if (route.query.value !== void 0) formState.value = route.query.value;
});

const add = () => {
  //@ts-ignore
  ElMessageBox.prompt('', 'Tip', {
    title: '添加分类',
    inputValidator: inputValidator,
    cancelButtonText: '取消',
    confirmButtonText: '确定',
    inputPlaceholder: '请输入4个字符',
    inputErrorMessage: '输入的内容长度不可以大于4个字符！',
  })
    .then(({ value }: { value: any }) => auth.classificationAdd(value))
    .then((success: any) => {
      messageSuccess(success.message);
      return auth.classificationLimit(tabCurrent.value === 0 ? 1 : tabCurrent.value, 15);
    })
    .then((success: any) => {
      dataSource(success.data);
      setTimeout(() => messageSuccess(success.message), 500);
    })
    .catch((error: any) => {
      if (error === 'cancel') return;
      messageError(!error || error.name ? '网络错误！' : error);
    });
};

const inputValidator = (value: string) => value.length <= 4;

const dataSource = (source: any) => (tableSource.value = source);

const current = (c: any) => (tabCurrent.value = c);

watch(
  () => formState,
  () => {
    classification.setSearch({ name: name.value, value: formState.value, select: formState.select });
  },
  { deep: true }
);
</script>

<style scoped></style>