/*
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2025-01-15 07:59:00
 * @LastEditTime: 2025-01-17 08:15:15
 * @FilePath: /code/blog/client-admin/src/router/index.ts
 * @Description: 路由配置文件
 * Copyright (c) 2025 by tanxiang, All Rights Reserved.
 */
import notify from '@/utils/notifyUtils';
import { createRouter, createWebHistory } from 'vue-router';
import { routes } from '@/router/routes';
import progress from '@/utils/nprogressUtils';
import { useAuthStore } from '@/store/modules/useAuthStore';

const router = createRouter({
  history: createWebHistory(),
  routes: routes
});

router.beforeEach((to, from, next) => {
  progress.start();
  // 如果token不存在，并且前面的页面不是login，则跳转到login
  const token = localStorage.getItem('token');
  const auth = useAuthStore();
  if (to.meta.requiresAuth && !token) {
    next('/login');
  } else if (token && !to.meta.requiresAuth && auth.getUserInfo.id) {
    next('/');
  } else {
    next();
  }
});

router.afterEach(() => {
  progress.close();
});

router.isReady().then(async () => {
  let token = localStorage.getItem('token');
  // 存在token，请求用户信息接口，获取用户信息
  if (token) {
    try {
      const auth = useAuthStore();
      const info = await api.authInfo();
      auth.setUserInfo(info);
    } catch (e) {
      // 如果获取失败，返回登陆页
      notify.warning(e as string);
      localStorage.removeItem('token');
      router.replace('/login');
    }
  }
});

export default router;
