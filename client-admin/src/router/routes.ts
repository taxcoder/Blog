/*
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2025-01-15 07:59:40
 * @LastEditTime: 2025-01-17 07:46:05
 * @FilePath: /code/blog/client-admin/src/router/routes.ts
 * @Description:
 * Copyright (c) 2025 by tanxiang, All Rights Reserved.
 */
/*
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2025-01-15 07:59:40
 * @LastEditTime: 2025-01-15 07:59:43
 * @FilePath: /code/blog/client-admin/src/router/routes.ts
 * @Description:
 * Copyright (c) 2025 by tanxiang, All Rights Reserved.
 */
import Main from '@/views/main/Index.vue';
import Login from '@/views/login/Index.vue';

import { childrens } from './children';

import { RouteRecordRaw } from 'vue-router';

export const routes: RouteRecordRaw[] = [
  {
    path: '/',
    name: 'Main',
    meta: {
      requiresAuth: true
    },
    component: Main
  },
  {
    path: '/login',
    name: 'Login',
    meta: {
      requiresAuth: false
    },
    component: Login
  }
];
