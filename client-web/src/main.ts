/*
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2024-12-08 10:08:15
 * @LastEditTime: 2025-01-14 07:11:52
 * @FilePath: /code/blog/client-web/src/main.ts
 * @Description:
 * Copyright (c) 2024 by tanxiang, All Rights Reserved.
 */
import initMock from '@/mock';
import '@vant/touch-emulator';
import '@/plugins/plugin-css';
import '@/plugins/plugin-md';
import '@/styles/global.css';

import App from './App.vue';
import Router from '@/router';
import pinia from '@/store';
import { createApp } from 'vue';

import vuetyped from 'vue3typed';
import VueViewer from 'v-viewer';
import { Lazyload, ImagePreview } from 'vant'; // 引入组件
import vue3VideoPlay from 'vue3-video-play';
const app = createApp(App);
app.use(pinia);
app.use(Router);
app.use(vuetyped);
app.use(Lazyload);
app.use(ImagePreview);
app.use(vue3VideoPlay);
app.use(VueViewer);
app.mount('#app');

export default app;

if (import.meta.env.VITE_APP_REQUEST_MOCK == 'true') {
  initMock();
}

useTitle(import.meta.env.VITE_APP_TITLE);

console.info('==>项目以halo的樱花主题为模版进行制作<==');
