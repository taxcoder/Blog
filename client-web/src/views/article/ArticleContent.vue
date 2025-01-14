<!--
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2025-01-11 06:52:40
 * @LastEditTime: 2025-01-13 10:07:47
 * @FilePath: /code/blog/client-web/src/views/article/ArticleContent.vue
 * @Description: 
 * Copyright (c) 2025 by tanxiang, All Rights Reserved. 
-->
<template>
  <el-card class="wh-full" shadow="always" body-class="important-p-0 important-pb-3 rounded-[0.3125rem]">
    <div
      class="update-time py-[0.85rem] px-[1.25rem] mb-[0.625rem] mt-[1.25rem] mx-[1.25rem] box-border font-size-[0.8rem] relative bg-[rgba(0,0,0,0.05)] overflow-hidden"
    >
      本文最后更新于：{{ dateUtils.format(props.articleInfo.updateTime, 'YYYY年MM月DD日 A') }}
    </div>
    <div
      v-show="textCount > 0"
      class="text-count py-[0.85rem] px-[1.25rem] mb-[0.625rem] mt-[0.25rem] mx-[1.25rem] box-border font-size-[0.8rem] relative bg-[rgba(0,0,0,0.05)] overflow-hidden"
    >
      文章共 {{ textCount }} 字，阅读完预计需要 {{ readTime }}
    </div>
    <article-content-preview :content="props.articleInfo.content" ref="preview"></article-content-preview>
    <div class="h-[10rem] flex-cc flex-col select-none">
      <div
        class="p-4 rounded-full mb-3 cursor-pointer border-solid border-[0.0625rem] border-[rgba(213,0213,213,0.75)]"
        @click="addLike"
      >
        <icon-like v-show="!isLike" theme="outline" size="1.75rem" fill="#f56c6c" />
        <icon-like v-show="isLike" theme="filled" size="1.75rem" fill="#f56c6c" />
      </div>
      <div class="font-size-[0.75rem] color-[#282828] dark:color-[#ccc]">{{ count }}</div>
    </div>
    <div class="wh-full px-3">
      <div
        v-for="item in props.articleInfo.tags"
        :key="item.id"
        class="inline-flex mr-[0.45rem] capitalize rounded-[1.25rem] flex-cc border pr-3 bg-[#eeeeee] dark:bg-[#282828] cursor-pointer"
        @click="toJump(item.id)"
      >
        <icon-label
          theme="filled"
          class="p-2 rounded-full bg-[#ffc45a] dark:bg-[#bf7b00]"
          size="0.75rem"
          :fill="global.getTheme ? '#282828' : '#ffffff'"
        />
        <span class="font-size-[0.625rem] ml-1 dark:color-[#f1f1f1]">{{ item.name }}</span>
      </div>
    </div>
  </el-card>
</template>

<script setup lang="ts">
import ArticleContentPreview from './ArticleContentPreview.vue';
import notifyUtils from '@/utils/notifyUtils';
import dateUtils from '@/utils/dateUtils';

const router = useRouter();
const global = useGlobalStore();
const notify = notifyUtils();

const preview = ref();

const [state, setState] = useCookieState('article_like');

const likes = reactive<number[]>([]);
const count = ref<number>(0);

const props = defineProps({
  articleInfo: {
    type: Object,
    required: true
  }
});

const toJump = (id: number) => {
  router.push('/tag/' + id);
};

const isLike = computed(() => {
  return likes.includes(props.articleInfo.id);
});

const addLikeCount = useRequest(() => api.addArticleLikeCount(props.articleInfo.id), {
  manual: true,
  onBefore: () => {
    notify.loading('请稍后');
    likes.push(props.articleInfo.id);
    count.value++;
  },
  onSuccess: () => {
    notify.success('点赞成功!');
  },
  onError: (error) => {
    console.log(error);
    likes.splice(likes.indexOf(props.articleInfo.id), 1);
    count.value--;
  }
});

const addLike = () => {
  if (notify.messageLoading.value === true) return;
  if (likes.includes(props.articleInfo.id)) return;
  addLikeCount.runAsync();
};

const textCount = computed(() => {
  return preview.value && preview.value.textCount ? preview.value.textCount : 0;
});

const readTime = computed(() => {
  let data = textCount.value / 400;
  let time = data - Math.floor(data);
  // 秒的英文
  let second = Math.round(parseFloat(((time * 100) / 60).toFixed(2)) * 100);
  return second > 0 ? `${Math.round(data)}分钟${second}秒` : `${Math.round(data)}分钟`;
});

watch(
  () => state.value,
  (newVal: any) => {
    try {
      likes.splice(0, likes.length);
      likes.push(...JSON.parse(newVal));
    } catch (error) {
      setState(JSON.stringify(likes));
    }
  },
  { deep: true, immediate: true }
);

watch(
  () => props.articleInfo.likeCount,
  () => (count.value = props.articleInfo.likeCount),
  { deep: true, immediate: true }
);
</script>

<style lang="css" scoped>
.update-time:before {
  content: '';
  height: 100%;
  width: 0.2rem;
  background: orange;
  position: absolute;
  top: 0;
  left: 0;
}

.text-count:before {
  content: '';
  height: 100%;
  width: 0.2rem;
  background: #60a5fa;
  position: absolute;
  top: 0;
  left: 0;
}
</style>
