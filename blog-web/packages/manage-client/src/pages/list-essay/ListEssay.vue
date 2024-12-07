<template>
  <div id="essay" class="h-screen">
    <table-search :formState="formState" :name="name" @dataSource="dataSource" @current="current" @add="add" />
    <list-essay-table :source="tableSource" @dataSource="dataSource" @current="current" />
  </div>
</template>

<script setup lang="ts">
import ListEssayTable from '@/pages/list-essay/ListEssayTable.vue';
import TableSearch from '@/components/table-search/TableSearch.vue';
import { useEssayStore } from '@/stores/essay';
import { provide, reactive, ref, watch, onActivated, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';

const route: any = useRoute();
const essay = useEssayStore();
const router: any = useRouter();

const tableSource = ref<any>({});
const tabCurrent = ref<number>(0);

const name = ref<string>('essay');

const formState = reactive<any>({
  key: [
    { value: 'id', label: 'ID', placeholder: '请输入一个正整数，不能为0' },
    { value: 'content', label: '内容', placeholder: '根据内容模糊查询' },
    { value: 'province', label: '上传省份', placeholder: '根据上传省份获取信息' },
    { value: 'like/gte', label: '点赞数(大于等于)', placeholder: '请输入一个非负整数' },
    { value: 'like/lte', label: '点赞数(小于等于)', placeholder: '请输入一个非负整数' },
    { value: 'like/equ', label: '点赞数(等于)', placeholder: '请输入一个非负整数' },
  ],
  select: 'id',
  value: '',
});

onMounted(() => {
  if (route.query.key && formState.key.find((c: any) => c.value === route.query.key))
    formState.select = route.query.key;
  if (route.query.value !== void 0) formState.value = route.query.value;
});

provide('tabCurrent', tabCurrent);

const add = () => router.push('/editor/essay');

const dataSource = (source: any) => (tableSource.value = source);

const current = (c: any) => (tabCurrent.value = c);

watch(
  () => formState,
  () => {
    essay.setSearch({ name: name.value, value: formState.value, select: formState.select });
  },
  { deep: true }
);
</script>

<style scoped></style>