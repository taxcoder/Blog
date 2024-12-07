<template>
  <list-article
    :articleList="articleList"
    :loading="loading"
    @sizeChange="sizeChange"
    :page-size="size"
    :total="articleList.total"
    :current="current"
    layout="total, prev, pager, next, jumper"
  />
</template>

<script setup lang="ts">
import { useArticleStore } from '@/stores/article';
import { useBaseStore } from '@/stores/base';
import { useArticle, useClassification } from '@tanxiang/apis';

const articleApi = useArticle();
const classification: any = useClassification();
const route: any = useRoute();
const router: any = useRouter();
const base: any = useBaseStore();
const article: any = useArticleStore();

const current = ref<number>(1);
const size = ref<number>(6);
const articleList = ref<any>({});
const loading = ref<boolean>(true);

const updateDataLoading: any = inject('updateDataLoading');

/**
 * 在页面挂载之前，请求后台服务器，获取第一页的数据
 */
onMounted(() => {
  if (!(!isNaN(parseFloat(route.params.id)) && isFinite(route.params.id))) return router.push('/');
  articleApi
    .articleClassification(current.value, size.value, route.params.id)
    .then((success: any) => {
      if (success.data.total === 0) return router.push('/');
      loading.value = false;
      articleList.value = success.data;
      nextTick(updateDataLoading());
      base.setTitle(success.data.records[0].classification.name);
    })
    .catch(() => {})
    .finally(() => {
      nextTick(() => updateDataLoading());
    });
});

/**
 * 切换每页显示个数
 * @param obj 包含当前页和每页条数的对象
 */
const sizeChange = (obj: { current: number; size: number }) => {
  loading.value = true;
  classification
    .classificationArticle(obj.current, obj.size, route.params.id)
    .then((success: any) => {
      size.value = obj.size;
      current.value = obj.current;
      articleList.value = success.data;
    })
    .catch(() => {})
    .finally(() => {
      loading.value = false;
    });
};
</script>

<style scoped></style>
