import { request } from '@/axios';
/*
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2025-01-17 07:45:29
 * @LastEditTime: 2025-01-17 07:45:31
 * @FilePath: /code/blog/client-admin/src/router/children.ts
 * @Description: home的子路由
 * Copyright (c) 2025 by tanxiang, All Rights Reserved.
 */
const Home = () => import('@/views/home/Index.vue');

export const childrens = [
  {
    path: '/home',
    name: 'Home',
    component: Home,
    meta: {
      requestAuth: true
    }
  }
];
