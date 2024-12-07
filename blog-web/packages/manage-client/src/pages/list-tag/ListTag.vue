<template>
  <div id="tag" class="h-screen">
    <table-search :formState="formState" :name="name" @dataSource="dataSource" @current="current" @add="add" />
    <list-tag-table :source="tableSource" @dataSource="dataSource" @current="current" />
  </div>
</template>

<script setup lang="ts">
import { reactive, ref, watch, provide, computed, onMounted, onActivated } from 'vue';

import ListTagTable from '@/pages/list-tag/ListTagTable.vue';
import TableSearch from '@/components/table-search/TableSearch.vue';

import { useTagStore } from '@/stores/tag';
import { useAuthTag } from '@tanxiang/apis';
import { useRoute, useRouter } from 'vue-router';
import { messageInfo } from '@tanxiang/common';

const { messageError, messageSuccess } = messageInfo();

const auth = useAuthTag();
const tag = useTagStore();

const cur = ref<number>(1);
const name = ref<string>('tag');
const tableSource = ref<any>({});
const tabCurrent = ref<number>(0);

const router: any = useRouter();
const route: any = useRoute();

provide('tabCurrent', tabCurrent);

const formState = reactive<any>({
  key: [
    { value: 'id', label: 'ID', placeholder: '请输入一个正整数，不能为0' },
    { value: 'name', label: '标签名', placeholder: '根据标签名模糊查询' },
    { value: 'like/gte', label: '文章数(大于等于)', placeholder: '请输入一个非负整数' },
    { value: 'like/lte', label: '文章数(小于等于)', placeholder: '请输入一个非负整数' },
    { value: 'like/equ', label: '文章数(等于)', placeholder: '请输入一个非负整数' },
  ],
  select: 'id',
  value: '',
});

onMounted(() => {
  if (route.query.key && formState.key.find((c: any) => c.value === route.query.key))
    formState.select = route.query.key;
  if (route.query.value !== void 0) formState.value = route.query.value;
});

const add = () => {
  //@ts-ignore
  ElMessageBox.prompt('', 'Tip', {
    title: '添加标签',
    inputValidator: inputValidator,
    cancelButtonText: '取消',
    confirmButtonText: '确定',
    inputPlaceholder: '请输入新增的标签',
    inputErrorMessage: '输入的内容长度不可以大于20个字符！',
  })
    .then(({ value }: { value: string }) => auth.tagAdd([value.trim()]))
    .then((success: any) => {
      messageSuccess(success.message);
      return auth.tagLimit(tabCurrent.value === 0 ? 1 : tabCurrent.value, 15);
    })
    .then((success: any) => {
      dataSource(success.data);
      setTimeout(() => messageSuccess(success.message), 1200);
    })
    .catch((error: any) => {
      if (error === 'cancel') return;
      messageError(!error || error.name ? '网络错误！' : error);
    });
};

const inputValidator = (value: string) => value.length <= 20;

const dataSource = (source: any) => {
  tableSource.value = source;
};
const current = (c: any) => {
  tabCurrent.value = c;
};

watch(
  () => formState,
  () => {
    tag.setSearch({ name: name.value, value: formState.value, select: formState.select });
  },
  { deep: true, immediate: true }
);
</script>

<style scoped>
#tag {
  width: 100%;
  display: flex;
  flex-direction: column;
}
</style>