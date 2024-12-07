<!--
 * @Author: tanxiang 1571922819@qq.com
 * @Date: 2023-06-17 21:55:42
 * @Description:
 * @LastEditTime: 2023-12-03 18:07:10
 * @LastEditors: tanxiang 1571922819@qq.com
 * @FilePath: \blog\packages\web-client\src\components\user-info\WebSiteInfo.vue
 * @copyright: Copyright (c) 2023 by ${git_name_email}, All Rights Reserved.
-->
<template>
  <user-container :isDefer="true" :loading="loading" title="网站资讯" iconClass="icon-tongzhi" :size="33" :left="-6">
    <template v-slot:content>
      <p
        v-for="(item, index) of node"
        class="text-[0.825rem] lh-[30px] inline-flex w-full first-of-type:pt-[8px]"
        :key="item.id"
      >
        <span class="flex-1" v-text="item.title" />
        <span>{{ getInfo(index) }}</span>
      </p>
    </template>
  </user-container>
</template>

<script setup lang="ts">
import { useGlobalStore } from '@/stores/global';
import { useDate } from '@tanxiang/utils';

const global: any = useGlobalStore();

const { days, headway } = useDate();

const loading = ref<boolean>(true);

const node = reactive<{ id: number; title: string }[]>([
  { id: 1, title: '文章数目' },
  { id: 2, title: '已运行天数' },
  { id: 3, title: '在线状态' },
  { id: 4, title: '本站总字数' },
  { id: 5, title: '本站总访问量' },
]);
// 获取已运行时间
const getRunTime = computed(() => days(global.getWebSite.createTime) + '天');
// 获取上次登录时间
const getLastUpdateTime = computed(() => {
  return juiceIsActive.value ? '当前在线' : headway(global.getWebSite.loginUpdateTime + 1000 * 60 * 60, false) + '前';
});
// 获取网站总访问量
const getAllVisit = computed(() => {
  let visit = global.getWebSite.totalVisits;
  return visit >= 10000 ? visit / 10000 + ' w' : visit;
});
// 获取文章数目
const getArticleSize = computed(() => {
  let count = global.getWebSite.count;
  return count ? count.find((article: { name: string; num: number }) => article.name === '文章').num : 0;
});
// 获取网站总字数
const getAllText = computed(() => {
  let total = global.getWebSite.totalTextQuantity;
  return total >= 10000 ? total / 10000 + ' w' : total;
});
// 获取网站信息
const getInfo = computed(() => (index: number) => {
  return {
    0: () => getArticleSize.value,
    1: () => getRunTime.value,
    2: () => getLastUpdateTime.value,
    3: () => getAllText.value,
    4: () => getAllVisit.value,
  }[index]();
});
// 判断登录时间是否超过了一小时
const juiceIsActive = computed(() => new Date().getTime() - global.getWebSite.loginUpdateTime <= 1000 * 60 * 60);

watch(
  () => global.getWebSite.id,
  (newValue: any) => {
    // 如果网站信息存在就不加载
    if (newValue) loading.value = false;
  },
  { immediate: true }
);
</script>

<style scoped></style>
