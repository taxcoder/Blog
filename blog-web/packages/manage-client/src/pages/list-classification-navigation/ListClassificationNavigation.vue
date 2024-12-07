<template>
  <div id="classification-navigation" class="h-screen">
    <table-search :formState="formState" :name="name" @dataSource="dataSource" @current="current" @add="add" />
    <list-classification-navigation-table :source="tableSource" @dataSource="dataSource" @current="current" />
  </div>
</template>

<script setup lang="ts">
import { provide, reactive, ref, watch, onMounted } from 'vue';

import TableSearch from '@/components/table-search/TableSearch.vue';
import ListClassificationNavigationTable from '@/pages/list-classification-navigation/ListClassificationNavigationTable.vue';
import { useRoute, useRouter } from 'vue-router';
import { useAuthClassificationNav } from '@tanxiang/apis';
import useRequestData from '@/hooks/useRequestData';
import { messageInfo } from '@tanxiang/common';
import { useClassificationNavigationStore } from '@/stores/classification-navigation';

const { messageError, messageSuccess } = messageInfo();

const router: any = useRouter();
const route: any = useRoute();
const auth = useAuthClassificationNav();
const classificationNavigation = useClassificationNavigationStore();

const { request } = useRequestData();

const tableSource = ref<any>({});
const tabCurrent = ref<number>(0);

const name = ref<string>('classificationNavigation');

const formState = reactive<any>({
  key: [
    { value: 'id', label: 'ID', placeholder: '请输入一个正整数，不能为0' },
    { value: 'name', label: '分类名', placeholder: '根据分类名模糊查询' },
    { value: 'link/gte', label: '网站数(大于等于)', placeholder: '请输入一个非负整数' },
    { value: 'link/lte', label: '网站数(小于等于)', placeholder: '请输入一个非负整数' },
    { value: 'link/equ', label: '网站数(等于)', placeholder: '请输入一个非负整数' },
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
    inputPlaceholder: '请输入分类名称',
    inputErrorMessage: '分类名长度不可以大于20个字符！',
  })
    .then(({ value }: { value: any }) => auth.addClassificationNav(value))
    .then((success: any) => {
      messageSuccess(success.message);
      return auth.classificationNavList(tabCurrent.value === 0 ? 1 : tabCurrent.value, 15);
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

const inputValidator = (value: string) => value.length <= 20;

const dataSource = (source: any) => (tableSource.value = source);

const current = (c: any) => (tabCurrent.value = c);

watch(
  () => formState,
  () => {
    classificationNavigation.setSearch({ name: name.value, value: formState.value, select: formState.select });
  },
  { deep: true }
);
</script>

<style scoped></style>