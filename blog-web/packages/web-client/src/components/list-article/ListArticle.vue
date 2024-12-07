<template>
  <div class="card cursor-pointer pt-[15px] box-border pl-0 pr-0" :class="cardClass">
    <el-card
      @click="navToArticle(item.id)"
      v-if="articleList.records && articleList.records.length > 0"
      v-for="(item, index) in articleList.records"
      :body-style="setBodyStyle"
      :embedded="true"
      class="article-list-card-item mb-[20px] important-rounded-[7px]"
      shadow="hover"
    >
      <el-skeleton :animated="true" :loading="props.loading" :rows="6">
        <template #template>
          <el-skeleton :animated="true" :rows="getChange ? 10 : 8" />
        </template>
        <template #default>
          <card-content :change="getChange" :index="index" :item="item" />
        </template>
      </el-skeleton>
    </el-card>
    <el-card
      v-else
      class="article-list-card-item mb-[20px] important-rounded-[7px]"
      shadow="hover"
      :body-style="setBodyStyle"
    >
      <el-empty :image-size="200"></el-empty>
    </el-card>
    <format-paging
      :layout="layout"
      :total="props.total"
      :pageSizes="pagingCount"
      :pager-count="juiceIsPcAndPhone ? 9 : 5"
      :current="props.current === -1 ? article.getCurrent : props.current"
      :pageSize="props.pageSize === -1 ? article.getPageSize : props.pageSize"
      @sizeChange="sizeChange"
      @currentChange="currentChange"
    />
  </div>
</template>

<script setup lang="ts">
import { changeWidth, minWidth, pagingCount } from '@/config';
import { useArticleStore } from '@/stores/article';
import { useBaseStore } from '@/stores/base';

import { usePage } from '@tanxiang/utils';

const route = useRoute();
const router = useRouter();
const article = useArticleStore();
const base = useBaseStore();

const status: any = inject('status');

const emit = defineEmits(['sizeChange']);

const props = defineProps({
  articleList: { type: Object, default: () => {} },
  loading: { type: Boolean, required: true },
  pageSize: { type: Number, default: -1 },
  current: { type: Number, default: -1 },
  total: { type: Number, default: -1 },
  layout: { type: String, default: 'total, prev, pager, next, jumper, sizes' },
});
// 点击文章列表时，跳转路由
const navToArticle = (id: any) => router.push('/articles/' + id);
// 设置卡片的样式
const setBodyStyle: any = computed(() => {
  return {
    width: '100%',
    boxSizing: 'border-box',
    padding: props.loading ? 'var(--el-card-padding)' : '0',
  };
});
// 判断是否是移动端
const getChange = computed(() => status.width <= minWidth || usePage().device());
// 当前页更新
const currentChange = (obj: { current: number; size: number }) => sizeChange(obj);
// 改变分页大小
const sizeChange = (obj: { current: number; size: number }) => emit('sizeChange', obj);
// 判断是否是PC和手机
const juiceIsPcAndPhone = computed(() => status.width > changeWidth && !usePage().device());

const cardClass = computed(() => {
  let active = !base.getOpenActive;
  let meta = route.meta;
  return [
    active ? 'w-full' : 'w-auto',
    !juiceIsPcAndPhone.value ? 'mb-0' : 'mb-[20px]',
    meta.isDraw || active ? '2xl:pl-0' : !meta.position ? '2xl:pl-[10px]' : '2xl:pl-[30px]',
    meta.isDraw || active ? '2xl:pr-0' : meta.position ? '2xl:pr-[10px]' : '2xl:pr-[30px]',
  ];
});
</script>

<style scoped></style>