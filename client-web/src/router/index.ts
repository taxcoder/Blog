/*
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 1824-12-07 13:18:37
 * @LastEditTime: 2025-01-14 05:41:05
 * @FilePath: /code/blog/client-web/src/router/index.ts
 * @Description: 路由首页
 * Copyright (c) 1824 by ${git_name_email}, All Rights Reserved.
 */
import { close, start } from '@/utils/nprogressUtils';

import {
  HomeTwo,
  Sunny,
  AllApplication,
  Label,
  Hourglass,
  StockMarket,
  MoreFour,
  Pic,
  FolderOpen,
  BookOne
} from '@icon-park/vue-next';

import { markRaw } from 'vue';
import Home from '@/views/home/Home.vue';
import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router';
/* 
  RouteRecordRaw是Vue Router的一个类型定义，它用于描述路由配置的对象。它包含以下属性：
  path：字符串，表示路由的路径。
  name：字符串，表示路由的名称。
  component：组件类型，表示路由所匹配的组件。
  children：子路由配置数组，用于描述嵌套路由。
  meta：对象，用于存储额外的路由元数据，例如需要验证用户权限的信息。
*/
const Tags = () => import('@/views/tags/Tags.vue');
const TagInfo = () => import('@/views/tags/TagInfo.vue');
const Categorys = () => import('@/views/categorys/Categorys.vue');
const CategoryInfo = () => import('@/views/categorys/CategoryInfo.vue');
const Feelings = () => import('@/views/feelings/Feelings.vue');
const FeelingInfo = () => import('@/views/feelings/FeelingInfo.vue');
const NotFound = () => import('@/views/404/404.vue');
const Archives = () => import('@/views/archives/Archives.vue');
const Photos = () => import('@/views/photos/Photos.vue');
const Article = () => import('@/views/article/Article.vue');

const setIcon: any = (component: any, isChildren = false, fontSize = '1.125rem') => {
  return {
    icon: markRaw(component),
    size: fontSize,
    fill: '#282828',
    darkFill: '#868888',
    activeFill: '#f5a623',
    activeDarkFill: '#d38900',
    isChildren: isChildren
  };
};

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    name: '首页',
    component: Home,
    meta: {
      ...setIcon(HomeTwo),
      order: 1
    }
  },
  {
    path: '/feelings',
    name: '心情',
    component: Feelings,
    meta: {
      ...setIcon(Sunny),
      order: 2
    }
  },
  {
    path: '/list',
    name: '清单',
    meta: {
      ...setIcon(FolderOpen),
      order: 3
    },
    children: [
      {
        path: '/categorys',
        name: '分类',
        component: Categorys,
        meta: setIcon(AllApplication, true, '1rem')
      },

      {
        path: '/tags',
        name: '标签',
        component: Tags,
        meta: setIcon(Label, true, '1rem')
      }
    ]
  },
  {
    path: '/archives',
    name: '时光轴',
    component: Archives,
    meta: {
      ...setIcon(Hourglass),
      order: 4
    }
  },
  {
    path: '/photos',
    name: '图集',
    component: Photos,
    meta: {
      ...setIcon(Pic),
      order: 5
    }
  },
  {
    path: '/more',
    name: '更多',
    meta: {
      ...setIcon(MoreFour),
      order: 6
    },
    children: [
      {
        path: '/replay',
        name: '复盘',
        meta: setIcon(StockMarket, true, '1rem'),
        redirect: '/pages/replay/'
      },
      {
        path: '/node',
        name: '笔记',
        meta: setIcon(BookOne, true, '1rem'),
        redirect: '/pages/note/'
      }
    ]
  },
  { path: '/home', redirect: '/' },
  { path: '/404', name: '404', component: NotFound },
  { path: '/:pathMatch(.*)', redirect: '/404' },
  {
    path: '/category/:id',
    component: CategoryInfo
  },
  {
    path: '/tag/:id(\\d+)',
    component: TagInfo
  },
  {
    path: '/feeling/:id(\\d+)',
    component: FeelingInfo
  },
  {
    path: '/article/:id(\\d+)',
    component: Article
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

const init = (global: any) => {
  if (global.getRoutes.length > 0) return;
  let routes = router.getRoutes();
  let array = filterArray(routes.filter((f: any) => f.name && !f.meta.isChildren && !['404'].includes(f.name))).map(
    (m: any) => itemObj(m)
  );
  global.setRoutes(array);
};

const filterArray = (arr: any[]): any[] => {
  /// 递归判断数组对象内是否存在name属性，不存在则从数组中删除掉当前的对象，存在则返回自身
  return arr.map((m: any) => {
    if (m.children && m.children.length > 0) {
      m.children = m.children.map((p: any) => {
        if (p.children && p.children.length > 0) p.children = void 0;
        return p;
      });
    }
    return m;
  });
};

const itemObj = (m: any) => ({
  name: m.name,
  url: m.path,
  icon: m.meta.icon,
  fill: m.meta.fill,
  darkFill: m.meta.darkFill,
  activeFill: m.meta.activeFill,
  activeDarkFill: m.meta.activeDarkFill,
  size: m.meta.size,
  redirect: m.redirect,
  children: m.children && m.children.length > 0 ? m.children.map((c: any) => itemObj(c)) : []
});

// 路由前置后卫
router.beforeEach(() => {
  const global = useGlobalStore();
  global.setChangeRouter(true);
  init(global);
  start();
});
// 路由后置后卫
router.afterEach(() => {
  const global = useGlobalStore();
  useTimeout(() => global.setChangeRouter(false), 500);
  close();
});

export default router;
