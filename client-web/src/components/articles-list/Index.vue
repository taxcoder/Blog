<!--
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2025-01-01 10:25:02
 * @LastEditTime: 2025-01-15 02:47:01
 * @FilePath: /code/blog/client-web/src/components/articles-list/Index.vue
 * @Description: 首页文章列表
 * Copyright (c) 2025 by tanxiang, All Rights Reserved. 
-->
<template>
  <div class="mt-[0.5rem]">
    <prefix-info :icon="Bookshelf" title="文章"></prefix-info>
    <articles-list-loading :phone="isPhone" :loading="loading || request.loading.value || isLoading">
    </articles-list-loading>
    <van-list
      v-model:loading="loading"
      v-model:error="error"
      :finished="finished"
      error-text="任务出现异常，请重新开始执行"
      loading-text="正在捕获中..."
      finished-text="已到达宇宙的最深处，只剩下一片虚无"
      :onLoad="onload"
      :immediate-check="false"
    >
      <div
        v-if="index === index"
        :id="`article-${item.id}`"
        v-for="(item, index) in props.dataArray.list"
        :key="item.id"
        :data-index="index"
        @click="toJump(item.id)"
        class="wh-full cursor-pointer"
      >
        <el-card
          shadow="always"
          body-class="wh-full important-p-0"
          class="mb-[0.625rem] wh-full"
          :class="{ 'h-[17.5rem]': !isPhone, 'h-[20rem]': isPhone }"
        >
          <articles-list-pc-content v-if="!isPhone" :content="item" :index="index"></articles-list-pc-content>
          <articles-list-phone-content v-else :content="item" :index="index"></articles-list-phone-content>
        </el-card>
      </div>
      <el-empty v-if="isEmpty"></el-empty>
    </van-list>
  </div>
</template>

<script setup lang="ts">
import { Bookshelf } from '@icon-park/vue-next';

const isPhone = inject('isPhone') as Ref<boolean>;

import ArticlesListPcContent from './PcContent.vue';
import ArticlesListPhoneContent from './PhoneContent.vue';
import ArticlesListLoading from './Loading.vue';

const router = useRouter();

const index = ref(0);
const limit = ref(10);
const error = ref(false);
const loading = ref(false);
const finished = ref(false);
const isLoading = ref(true);
const active = reactive<Array<number>>([]);

const props = defineProps({
  activeFunction: {
    type: Function as any,
    require: true
  },
  fn: {
    type: Function as any,
    default: () => {}
  },
  dataArray: {
    type: Object,
    default: () => {
      allCount: 0;
      list: [];
    }
  }
});
onMounted(() => {
  onload();
  index.value = 0;
});

const request = useRequest(() => props.activeFunction(index.value, limit.value), {
  manual: true,
  onSuccess: (data) => {
    props.fn(data);
    loading.value = false;
    error.value = false;
    // 当前页大于或等于总页数时，则认为数据加载完毕
    if (Math.ceil(props.dataArray.allCount / limit.value) <= index.value) finished.value = true;
  },
  onError: () => {
    error.value = true;
    loading.value = false;
    index.value--;
  },
  onFinally: () => {
    isLoading.value = false;
  }
});

const toJump = (id: number) => {
  // 跳转到文章详情页
  router.push(`/articles/${id}`);
};

const onload = () => {
  if (props.dataArray.allCount === -1) {
    index.value++;
    request.runAsync();
  } else if (props.dataArray.allCount === 0) {
    finished.value = true;
    loading.value = false;
    error.value = false;
  } else if (Math.ceil(props.dataArray.allCount / limit.value) > index.value) {
    index.value++;
    request.runAsync();
  } else {
    finished.value = true;
    loading.value = false;
    error.value = false;
  }
};

const isEmpty = computed(() => {
  return !loading.value && !isLoading.value && props.dataArray.list.length === 0;
});
</script>

<style lang="css" scoped></style>
