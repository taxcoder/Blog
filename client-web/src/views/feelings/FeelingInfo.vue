<!--
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2025-01-07 07:46:30
 * @LastEditTime: 2025-01-08 02:20:09
 * @FilePath: /code/blog/client-web/src/views/feelings/FeelingInfo.vue
 * @Description: 
 * Copyright (c) 2025 by tanxiang, All Rights Reserved. 
-->

<template>
  <el-breadcrumb :separator-icon="Left" style="--el-color-primary: orange" class="pt-[1.5rem] select-none">
    <el-breadcrumb-item class="font-size-[1.05rem]" replace :to="{ path: '/' }"> 首页 </el-breadcrumb-item>
    <el-breadcrumb-item class="font-size-[1.05rem]" replace :to="{ path: '/feelings#feeling-3' }"
      >心情</el-breadcrumb-item
    >
    <el-breadcrumb-item class="font-size-[1.05rem]">{{ route.params.id }}</el-breadcrumb-item>
  </el-breadcrumb>
  <van-skeleton
    v-if="isLoading"
    class="important-pt-[2rem] max-md:flex-col"
    title
    avatar
    avatar-size="3.125rem"
    avatar-shape="square"
    :row="9"
  />
  <feelings-feeling-list v-else :feeling="fellingInfo">
    <div class="w-full py-3 px-2 bg-[#f6f7ff] box-border"><vue-artalk v-if="open"></vue-artalk></div>
  </feelings-feeling-list>
</template>

<script setup lang="ts">
import { Left } from '@icon-park/vue-next';
import { Feeling } from '@/api/feelings/feeling.d';

const route = useRoute();
const router = useRouter();
const open = ref<boolean>(false);
const feelingRequest = ref<any>();
const fellingInfo = reactive<Feeling>({
  id: 0,
  author: '',
  headImg: '',
  content: '',
  province: '',
  city: '',
  imagesUrl: [],
  videosUrl: '',
  likeCount: 0,
  createTime: 0
});
const feelingStore = useFeelingStore();

onBeforeMount(() => {
  if (feelingStore.feelings.allCount !== 0) {
    let filter = feelingStore.getFeelings.list.filter((f: Feeling) => f.id === parseInt(route.params.id as string));
    if (filter.length !== 0) return setFeeling(filter[0]);
  }
  feelingRequest.value = useRequest(() => api.getFeeling(parseInt(route.params.id as string)), {
    onSuccess: () => setFeeling(feelingRequest.value.data)
  });
});

onActivated(() => {
  open.value = true;
});
onDeactivated(() => {
  open.value = false;
});

const setFeeling = (feelingData: Feeling) => {
  fellingInfo.id = feelingData.id;
  fellingInfo.author = feelingData.author;
  fellingInfo.headImg = feelingData.headImg;
  fellingInfo.content = feelingData.content;
  fellingInfo.province = feelingData.province;
  fellingInfo.city = feelingData.city;
  fellingInfo.imagesUrl = feelingData.imagesUrl;
  fellingInfo.videosUrl = feelingData.videosUrl;
  fellingInfo.likeCount = feelingData.likeCount;
  fellingInfo.createTime = feelingData.createTime;
};

const isLoading = computed(() => !fellingInfo || !fellingInfo.id);
</script>

<style lang="css" scoped></style>
