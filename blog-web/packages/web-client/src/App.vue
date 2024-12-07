<!--
 * @Author: tanxiang 1571922819@qq.com
 * @Date: 2023-06-10 14:11:50
 * @Description:
 * @LastEditTime: 2023-12-02 16:25:49
 * @LastEditors: tanxiang 1571922819@qq.com
 * @FilePath: \blog\packages\web-client\src\App.vue
 * @copyright: Copyright (c) 2023 by 1571922819@qq.com, All Rights Reserved.
-->

<template>
  <div v-show="juiceCanShowContainer" class="w-full h-full">
    <n-config-provider :theme="base.getTheme ? darkTheme : null">
      <el-config-provider :locale="getLocale">
        <home-page :title="getTitle" />
        <!-- 专用于滚动 -->
        <div id="start" />
        <div v-show="juiceCanShowContent" class="data-container">
          <content-main :position="juicePosition" :show-user-info="juiceUserInfo" :isDraw="juiceIsDraw">
            <router-view />
          </content-main>
          <side-pendant />
          <side-drawer />
          <page-footer v-if="juiceCanShowContent" class="data-container" />
        </div>
      </el-config-provider>
    </n-config-provider>
  </div>
  <global-loading :hidden-loading="juiceCanShowContainer" />
</template>

<script setup lang="ts">
import { useArticleStore } from '@/stores/article';
import { useBaseStore } from '@/stores/base';
import { useGlobalStore } from '@/stores/global';

import { pagingCount } from '@/config';
import { Status } from '@/interface/base';
import { init } from '@tanxiang/apis';
import { GlobalLoading } from '@tanxiang/common';
import { darkTheme } from 'naive-ui';
//@ts-ignore
import en from 'element-plus/dist/locale/en.mjs';
//@ts-ignore
import zhCn from 'element-plus/dist/locale/zh-cn.mjs';

const ContentMain = defineAsyncComponent(() => import('@/components/ContentMain.vue'));
const HomePage = defineAsyncComponent(() => import('@/components/home-page/HomePage.vue'));

const route = useRoute();
const base = useBaseStore();
const global = useGlobalStore();
const article = useArticleStore();

// 全局loading的状态
const loadingTimer = ref<NodeJS.Timeout>();
// 侧边按钮的状态，让页面在滚动时，侧边按钮不会回弹到页面外，如果为null，则表示没有在滚动
const timer = ref<NodeJS.Timeout>();
// 用于解决切换主题导致的滚动异常，如果是-1，则表示是第一次滚动
const themeCurrent = ref<number>(-1);
// 设置组件的显示语言
const language = ref<string>('zh-cn');
// 页面背景图的高度
const imgHeight = ref<number>(-1);
// 当前的主题
const theme = ref<boolean>(false);

// 当前滚动的状态，存储着当前距离顶部的距离和上一次滚动距离顶部的距离
const status = reactive<Status>({
  scroll: 0,
  // 窗口的宽度，随着页面的拉伸也会变化
  width: 0,
  // 窗口的高度，随着页面的拉伸也会变化
  height: 0,
});

provide('status', status);
provide('imgHeight', imgHeight);

// 当数据加载完成，调用此方法可以关闭加载状态
provide('updateDataLoading', (timeout = 750) => {
  if (loadingTimer.value) clearTimeout(loadingTimer.value);
  loadingTimer.value = setTimeout(() => base.setIsLoadingShow(true), timeout);
});

onMounted(() => {
  themeInit();
  configInit();

  let time = setInterval(() => {
    let app = document.getElementById('app');
    if (app.clientHeight !== 0) {
      clearInterval(time);
      appInit();
    }
  }, 100);

  webSiteInit((success: any) => {
    success.data.text = success.text.data;
    success.data.count = success.count.data;
    global.setWebSite(success.data);
  });

  addListenerResize((e: any) => {
    // 每当窗口的大小发生变化时，获取到当前的宽度和高度，重新进行赋值
    status.width = document.documentElement.clientWidth;
    status.height = document.documentElement.clientHeight;
    appInit();
  });

  addListenerScroll(() => (status.scroll = document.documentElement.scrollTop));

  status.width = window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth;
  status.height = window.innerHeight || document.documentElement.clientHeight || document.body.clientHeight;
});

const appInit = () => {
  let app = document.getElementById('app');
  let body = document.body;
  if (!app) return;
  app.style.height = 'auto';
  let flag = app.clientHeight < body.clientHeight;
  if (app.clientHeight !== 0 && flag) {
    app.style.height = body.clientHeight + 'px';
  }

  let footer = document.getElementById('page-footer');
  if (!footer) return;
  footer.style.position = flag ? 'fixed' : 'relative';
  footer.style.bottom = '0';
};

// 获取网站信息
const webSiteInit = (fn: Function) => {
  init()
    .then((success: any) => fn(success))
    .catch((error: any) => {
      // 提示网站异常
      console.error(error);
    });
};
// 初始化主题
const themeInit = () => useToggle(useDark({ storageKey: 'theme', valueDark: 'dark', valueLight: 'light' }));
// 初始化配置
const configInit = () => {
  if (!pagingCount.includes(article.getPageSize)) article.setPageSize(pagingCount[0]);
};
// 监听窗口的大小变化
const addListenerResize = (fn: Function) => {
  window.addEventListener('resize', (e: any) => fn(e), { passive: false });
};

const addListenerScroll = (fn: Function) => {
  window.addEventListener('scroll', (e: any) => {
    let flag = base.getTheme ? 1 : 0;
    // 第一次进来，将当前的主题保存
    if (themeCurrent.value === -1) themeCurrent.value = flag;
    else if (flag !== themeCurrent.value) {
      // 如果后面进来了，主题不匹配，则表示是由于切换主题导致的滚动，直接取消执行
      themeCurrent.value = flag;
      return;
    }
    if (timer.value) {
      clearTimeout(timer.value);
      global.setIsContract(true);
    }

    timer.value = setTimeout(() => global.setIsContract(false), 650);
    fn();
  });
};
// 获取当前的语言
const getLocale = computed(() => (language.value === 'zh-cn' ? zhCn : en));
// 判断content内容部分是否可以显示
const juiceCanShowContent = computed(() => global.getWebSite.id && base.getIsLoadingShow);
// 判断container内容部分是否可以显示
const juiceCanShowContainer = computed(() => base.getIsLoadingShow);
// 判断是否显示用户信息
const juiceUserInfo = computed((): boolean => ![void 0, false].includes(getMeta.value.showUserInfo));
// 设置边栏的值，如果为true，则表示边栏的宽度为0
const juiceIsDraw = computed(() => getMeta.value.isDraw);
// 判断用户信息是在左边显示还是再右边显示，true在右边，false在左边
const juicePosition = computed(() => ([void 0].includes(getMeta.value.position) ? true : getMeta.value.position));
// 网站标题
const getTitle = computed((): string => (!base.getTitle ? '' : base.getTitle));
const getMeta = computed((): any => route.meta);
// 监听主题模式的变化
watch(
  () => base.getTheme,
  (newValue: boolean) => {
    let status = newValue ? 'dark' : 'light';
    localStorage.setItem('theme', status);
    document.getElementsByTagName('html')[0].setAttribute('class', status);
  },
  { deep: true, immediate: true }
);

watch(
  () => getMeta.value.name,
  (newValue: any) => base.setTitle(newValue ? newValue : ''),
  { deep: true, immediate: true }
);

watch(
  () => route.name,
  () => (status.scroll = 0),
  { immediate: true }
);
</script>

<style scoped></style>