/*
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2025-01-17 08:32:41
 * @LastEditTime: 2025-01-17 08:32:43
 * @FilePath: /code/blog/client-admin/src/constant/menu/system.ts
 * @Description: 菜单项-系统管理
 * Copyright (c) 2025 by tanxiang, All Rights Reserved.
 */

const LoginLogs = () => import('@/views/system/login-logs/Index.vue');
const TimingLogs = () => import('@/views/system/timing-logs/Index.vue');
const Notice = () => import('@/views/system/notice/Index.vue');
const Personage = () => import('@/views/system/personage/Index.vue');

import { Sunny } from '@vicons/carbon';

export default {
  path: '/system',
  name: '系统管理',
  icon: markRaw(Sunny),
  meta: {
    order: 1
  },
  children: [
    {
      path: '/system/login-logs',
      name: '登录日志',
      component: LoginLogs,
      meta: {
        order: 1,
        requestAuth: true
      }
    },
    {
      path: '/system/timing-logs',
      name: '定时日志',
      component: TimingLogs,
      meta: {
        order: 2,
        requestAuth: true
      }
    },
    {
      path: '/system/notice',
      name: '通知公告',
      component: Notice,
      meta: {
        order: 3,
        requestAuth: true
      }
    },
    {
      path: '/system/personage',
      name: '个人中心',
      component: Personage,
      meta: {
        order: 4,
        requestAuth: true
      }
    }
  ]
};
