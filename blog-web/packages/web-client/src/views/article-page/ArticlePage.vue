<template>
  <div id="article-page">
    <div class="article-breadcrumb">
      <el-breadcrumb :separator-icon="ArrowRight">
        <el-breadcrumb-item to="/">
          <i data-v-90bfc037="" class="icon-home iconfont"></i>
          <span>首页</span>
        </el-breadcrumb-item>
        <el-breadcrumb-item>
          {{ articleData?.title }}
        </el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div class="article-item-content">
      <article-page-content :markdownContent="content" />
    </div>
  </div>
</template>

<script setup lang="ts">
//@ts-ignore
import { ArrowRight } from '@element-plus/icons-vue';

import { useArticleStore } from '@/stores/article';
import { useArticle } from '@tanxiang/apis';
import { messageInfo } from '@tanxiang/common';

const ArticlePageContent = defineAsyncComponent(() => import('@/views/article-page/ArticlePageContent.vue'));

const article = useArticleStore();
const router = useRouter();
const articleApi = useArticle();
//@ts-ignore
const updateDataLoading: Function = inject('updateDataLoading');
const articleData = ref<any>({});
const content = ref<string>('');

const route = useRoute();

const { messageError } = messageInfo();

provide('articleData', articleData);

onMounted(() => queryInfo(article.getArticleList.records));

const queryInfo = (isArraysEmpty: boolean) => {
  if (!isArraysEmpty || !selectArticleId.value) queryArticleInfo();
  else if (!!selectArticleId.value) queryArticleUrl();
};

const queryArticleInfo = () => {
  articleApi
    .findArticleInfoById(route.params.id)
    .then((success: any) => {
      articleData.value = success.data;
      // 当请求完成之后，再显示下面的内容
      queryArticleUrl();
    })
    .catch((error: any) => {
      let container: any = document.querySelectorAll('.data-container');
      container.forEach((item: any) => item.classList.add('hidden'));
      messageError(error, 750, () => {
        router.replace('/list/article');
        container.forEach((item: any) => item.classList.remove('hidden'));
      });
    })
    .finally(() => {
      nextTick(() => updateDataLoading());
    });
};

const queryArticleUrl = () => {
  // 只请求url
  articleApi
    .findArticleContentById(route.params.id)
    .then((success: any) => {
      // 返回文章的个数
      content.value = success.data.replace(/^# .*([\s\S]*?)(\r?\n\s*\r?\n)?/, '');
      if (!articleData.value.id) articleData.value = selectArticleId.value;
    })
    .catch((error: any) => messageError(!error || error.name ? '网络错误' : error))
    .finally(() => {
      nextTick(() => updateDataLoading());
    });
};

/**
 * 如果是从首页点击进入的，会传递文章数组，从数组内查询到当前文章信息
 */
const selectArticleId = computed(() => {
  return article.getArticleList.records && article.getArticleList.records.length > 0
    ? article.getArticleList.records.find((record: any) => record.id == route.params.id)
    : null;
});
</script>

<style scoped>
#article-page {
  width: 100%;
  box-sizing: border-box;
  padding-top: 15px;
}

.article-item-content {
  margin-top: 15px;
  background-color: white;
  width: 100%;
  height: 100%;
  border-radius: 10px;
  box-shadow: 0 3px 8px 6px rgba(7, 17, 27, 0.05);
}

.icon-home {
  font-size: 0.9rem;
  margin-right: 3px;
}

.article-breadcrumb {
  background-color: white;
  width: 100%;
  box-sizing: border-box;
  padding: 15px 20px;
  border-radius: 7px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  box-shadow: 0 3px 8px 6px rgba(7, 17, 27, 0.05);
}
</style>