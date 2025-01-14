/*
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2024-12-07 13:18:37
 * @LastEditTime: 2024-12-20 10:56:30
 * @FilePath: /code/blog/client-app/src/router/index.ts
 * @Description: 路由首页
 * Copyright (c) 2024 by ${git_name_email}, All Rights Reserved.
 */
import { createRouter, createWebHashHistory, RouteRecordRaw } from "vue-router";
/* 
  RouteRecordRaw是Vue Router的一个类型定义，它用于描述路由配置的对象。它包含以下属性：
  path：字符串，表示路由的路径。
  name：字符串，表示路由的名称。
  component：组件类型，表示路由所匹配的组件。
  children：子路由配置数组，用于描述嵌套路由。
  meta：对象，用于存储额外的路由元数据，例如需要验证用户权限的信息。
*/

const routes: Array<RouteRecordRaw> = [];

const router = createRouter({
  history: createWebHashHistory(),
  routes
});

export default router;
