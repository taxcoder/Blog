<!--
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2024-12-07 12:14:20
 * @LastEditTime: 2025-01-14 03:57:33
 * @FilePath: /code/blog/client-web/src/App.vue
 * @Description: 入口页
 * Copyright (c) 2024 by tanxiang, All Rights Reserved. 
-->
<template>
  <div v-if="!global.getInfo || !global.getInfo?.title">
    <page-loading-dark v-if="!global.getTheme" class="important-h-[100vh] z-9999"> </page-loading-dark>
    <page-loading-light v-else class="important-h-[100vh] z-9999" :scale="1"> </page-loading-light>
  </div>
  <el-config-provider v-else :locale="zhCn">
    <el-scrollbar ref="scrollbar" :native="true" @scroll="scroll">
      <el-progress
        v-if="isPhone && !global.getChangeRouter"
        :percentage="percentage"
        status="warning"
        :show-text="false"
        :striped="true"
        stroke-linecap="square"
        class="important-fixed top-0 z-999 w-full"
        :stroke-width="3"
      />
      <vue-sakura></vue-sakura>
      <van-back-top
        right="7vw"
        bottom="5vh"
        class="important-w-[3.75rem] important-h-[3.75rem] important-bg-[orange]"
        v-if="isPhone"
      >
        <icon-down theme="outline" size="2.25rem" fill="#fff" class="transform-rotate-z-180" />
      </van-back-top>
      <transition name="page" mode="default" appear>
        <van-config-provider :theme="global.getTheme ? 'light' : 'dark'">
          <layout v-if="!!global.getInfo.title"></layout>
        </van-config-provider>
      </transition>
    </el-scrollbar>
  </el-config-provider>
</template>

<script setup lang="ts">
import zhCn from 'element-plus/es/locale/lang/zh-cn';

const route = useRoute();
const global = useGlobalStore();
const size = useSize(document.querySelector('body'));

const scrollbar = ref();
let percentage = ref<number>(0);
const isPhone = ref<boolean>(false);

provide('isPhone', isPhone);
provide('percentage', percentage);

const { data } = useRequest(() => api.getWebInfo(), {
  retryCount: 4,
  onSuccess: () => global.setInfo(data.value)
});

onMounted(() => {
  global.initThemeMode();
});

const scroll = ({ scrollTop }: { scrollTop: number }) => {
  let bodyHeight = document.body.scrollHeight ?? document.body.offsetHeight;
  let pageHeight = scrollbar.value.wrapRef.lastChild.scrollHeight ?? scrollbar.value.wrapRef.lastChild.offsetHeight;
  percentage.value = scrollTop === 0 ? 0 : Math.round((scrollTop / (pageHeight - bodyHeight)) * 100);
};

const change = () => {
  let width = 0;
  let changeWidth = getComputedStyle(document.documentElement).getPropertyValue('--change-width').trim();
  if (changeWidth.endsWith('rem')) width = parseInt(changeWidth.replace('rem', '')) * 16;
  else if (changeWidth.endsWith('px')) width = parseInt(changeWidth.replace('px', ''));
  else width = 768;
  isPhone.value = width >= size.width.value;
};

watch(size.width, () => change());

watch(
  () => route.path,
  () => {
    if (!route.name && !route.params.name) return;
    // 切换路由，就回到顶部
    if (scrollbar.value) scrollbar.value?.setScrollTop(0);
    percentage.value = 0;
  }
);
</script>

<style lang="css" scoped>
.page-enter-active,
.page-leave-active {
  transition: opacity 0.55s ease;
}
.page-enter-from,
.page-leave-to {
  opacity: 0;
}
</style>
