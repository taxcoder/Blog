<!--
 * @Author: tanxiang 1571922819@qq.com
 * @Date: 2023-06-14 09:58:06
 * @Description:
 * @LastEditTime: 2023-12-03 20:51:59
 * @LastEditors: tanxiang 1571922819@qq.com
 * @FilePath: \blog\packages\web-client\src\views\article-list\ArticleList.vue
 * @copyright: Copyright (c) 2023 by 1571922819@qq.com, All Rights Reserved.
-->
<template>
  <list-article
    :articleList="articleList"
    :loading="loading"
    @sizeChange="sizeChange"
    :current="article.getCurrent"
    :page-size="article.getPageSize"
  />
</template>

<script lang="ts" setup>
import { useArticleStore } from '@/stores/article';
import { useArticle } from '@tanxiang/apis';
import listArticle from '@/components/list-article/ListArticle.vue';

const list = useArticle();
const article: any = useArticleStore();

const articleList = ref<any>({});
const loading = ref<boolean>(true);

const updateDataLoading: any = inject('updateDataLoading');

/**
 * 在页面挂载之前，请求后台服务器，获取第一页的数据
 */
onBeforeMount(() => {
  list
    .articleList(article.getCurrent === null ? 1 : article.getCurrent, article.getPageSize)
    .then((success: any) => {
      let index = Math.ceil(+success.data.total / +success.data.size);
      article.setArticleList(success.data);
      articleList.value = article.getArticleList;
      article.setCurrent(+success.data.current + 1);
    })
    .catch(() => {})
    .finally(() => {
      loading.value = false;
      nextTick(() => updateDataLoading());
    });
});

/**
 * 切换每页显示个数
 * @param obj 包含当前页和每页条数的对象
 */
const sizeChange = (obj: { current: number; size: number }) => {
  loading.value = true;
  list
    .articleList(obj.current, obj.size)
    .then((success: any) => {
      article.setPageSize(obj.size);
      article.setCurrent(obj.current);
      article.setArticleList(success.data);
      articleList.value = article.getArticleList;
    })
    .catch((error: any) => {
      console.error(error);
    })
    .finally(() => {
      loading.value = false;
    });
};
</script>

<style scoped></style>
