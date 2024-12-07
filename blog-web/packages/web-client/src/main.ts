/*
 * @Author: tanxiang 1571922819@qq.com
 * @Date: 2023-06-10 14:11:50
 * @Description:
 * @LastEditTime: 2023-12-02 17:02:21
 * @LastEditors: tanxiang 1571922819@qq.com
 * @FilePath: \blog\packages\web-client\src\main.ts
 * @copyright: Copyright (c) 2023 by 1571922819@qq.com, All Rights Reserved.
 */
import '@/config/icons';
import '@/config/markdown';
import '@/styles/uno.css';
// @ts-ignore
import '@tanxiang/common/style';
import 'uno.css';

import router from '@/router/index';
import { useBaseStore } from '@/stores/base';
import { createPinia } from 'pinia';
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate';
import vueTyped from 'vue3typed';
// @ts-ignore
import App from './App.vue';
import '@vant/touch-emulator';
import { createApp } from 'vue';
import { message } from 'ant-design-vue';
import { Loading } from 'vant';

// @ts-ignore
const pinia = createPinia();
pinia.use(piniaPluginPersistedstate);

const app = createApp(App);

// @ts-ignore
app.use(router);
// @ts-ignore
app.use(pinia);
app.use(vueTyped);
app.use(Loading);
app.mount('#app');

message.config({
  maxCount: 1,
});

router.beforeEach((to: any, from: any, next: () => void) => {
  const base = useBaseStore();
  base.setIsLoadingShow(false);
  setTimeout(() => {
    if (!base.getIsLoadingShow) base.setIsLoadingShow(true);
  }, 1000 * 10);
  next();
});

router.afterEach((to: any, from: any) => {
  const base = useBaseStore();
  document.title = !!to.meta.name ? to.meta.name : '我的博客';
});