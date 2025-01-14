<!--
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2024-12-24 10:58:45
 * @LastEditTime: 2025-01-08 15:27:53
 * @FilePath: /code/blog/client-web/src/views/feelings/Feelings.vue
 * @Description: 
 * Copyright (c) 2024 by tanxiang, All Rights Reserved. 
-->

<template>
  <div>
    <van-list
      v-model:loading="loading"
      v-model:error="error"
      :finished="finished"
      error-text="任务出现异常，请重新开始执行"
      loading-text="正在捕获中..."
      finished-text="已到达宇宙的最深处，只剩下一片虚无"
      :onLoad="onload"
    >
      <div v-for="(item, index) in feelingStore.getFeelings.list" :id="`feeling-${item.id}`">
        <feelings-feeling-list
          :feeling="item"
          :class="{ 'important-border-b-none': feelingStore.getFeelings.allCount === index + 1 }"
          class="border-none border-b-[#f7f7ff] border-b-solid border-b-[0.0625rem]"
        >
        </feelings-feeling-list>
      </div>
      <el-empty v-if="feelingStore.getFeelings.list.length === 0 && !isLoading && !loading" />
      <van-skeleton
        class="important-pt-[2rem] max-md:flex-col"
        title
        avatar
        avatar-size="3.125rem"
        avatar-shape="square"
        :row="9"
        :loading="loading"
      />
    </van-list>
  </div>
</template>

<script setup lang="ts">
import { loadCountWidget } from '@/components/vue-artalk';

const feelingStore = useFeelingStore();

const loading = ref<boolean>(false);
const error = ref<boolean>(false);
const finished = ref<boolean>(false);
const index = ref<number>(0);
const limit = ref<number>(10);
const isLoading = ref<boolean>(true);

onMounted(() => {
  loadCountWidget();
});
onActivated(() => {
  loadCountWidget();
});

const feeling = useRequest(() => api.getFeelings(index.value, limit.value), {
  manual: true,
  onSuccess: () => {
    if (!feeling.data.value) return;
    feelingStore.setFeelings(feeling.data.value);
    loading.value = false;
    error.value = false;
    if (Math.ceil(feeling.data.value.allCount / limit.value) <= index.value) {
      finished.value = true;
    }
  },
  onError: (error: any) => {
    loading.value = false;
    error.value = true;
  },
  onFinally: () => {
    isLoading.value = false;
  }
});

const onload = () => {
  index.value += 1;
  feeling.runAsync();
};

watch(
  () => error.value,
  (newVal: boolean) => {
    if (newVal) index.value -= 1;
  }
);
</script>

<style lang="css" scoped></style>
