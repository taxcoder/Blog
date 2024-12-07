<template>
  <div id="article">
    <table-search :formState="formState" :name="name" @dataSource="dataSource" @current="current" @add="add" />
    <list-article-table :source="tableSource" @dataSource="dataSource" @current="current" />
  </div>
</template>

<script setup lang="ts">
import ListArticleTable from '@/pages/list-article/ListArticleTable.vue';
import { onActivated, provide, reactive, ref, watch } from 'vue';
import TableSearch from '@/components/table-search/TableSearch.vue';
import { useRouter } from 'vue-router';
import { useArticleStore } from '@/stores/article';

const router = useRouter();
const article = useArticleStore();

const tableSource = ref<any>({});
const tabCurrent = ref<number>(0);

const name = ref<string>('article');

const formState = reactive<any>({
  key: [
    { value: 'id', label: 'ID', placeholder: '请输入一个正整数，不能为0' },
    { value: 'title', label: '标题', placeholder: '根据标题模糊查询' },
    { value: 'classification', label: '分类', placeholder: '根据分类ID和分类名查询' },
    { value: 'tag', label: '标签', placeholder: '根据标签ID和标签名查询' },
    { value: 'position', label: '位置', placeholder: '根据上传地查询' },
    { value: 'like/gte', label: '点赞数(大于等于)', placeholder: '请输入一个非负整数' },
    { value: 'like/lte', label: '点赞数(小于等于)', placeholder: '请输入一个非负整数' },
    { value: 'like/equ', label: '点赞数(等于)', placeholder: '请输入一个非负整数' },
  ],
  select: 'id',
  value: '',
});

provide('tabCurrent', tabCurrent);

const add = () => router.push('/editor/article');

const dataSource = (source: any) => (tableSource.value = source);

const current = (c: any) => (tabCurrent.value = c);

watch(
  () => formState,
  () => {
    article.setSearch({ name: name.value, value: formState.value, select: formState.select });
  },
  { deep: true }
);
</script>

<style scoped></style>