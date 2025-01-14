/*
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2024-12-18 10:07:51
 * @LastEditTime: 2025-01-14 06:58:26
 * @FilePath: /code/blog/client-web/src/store/modules/useGlobalStore.ts
 * @Description:
 * Copyright (c) 2024 by tanxiang, All Rights Reserved.
 */
import { WebInfo } from '@/api/info/info.d';
import { Topping } from '@/api/topping/topping.d';
import { defineStore } from 'pinia';

export const useGlobalStore = defineStore('global', {
  state: () => ({
    loading: false,
    // true 白天 false 暗黑
    theme: true,
    info: {
      avatar: '',
      strings: [''],
      title: '',
      name: '',
      icpRecord: '',
      policeRecord: '',
      createTime: 0,
      articleCount: 0,
      tagCount: 0,
      feelingCount: 0
    } as WebInfo,
    topping: [] as Array<Topping>,
    routes: [] as any[],
    figureName: '',
    // 锚点的英文
    anchor: false,
    innerHtml: '',
    changeRouter: false
  }),
  getters: {
    getLoading: (state: any) => state.loading,
    getTheme: (state: any) => state.theme,
    getInfo: (state: any) => state.info,
    getRoutes: (state: any) => state.routes,
    getFigureName: (state: any) => state.figureName,
    getAnchor: (state: any) => state.anchor,
    getInnerHtml: (state: any) => state.innerHtml,
    getChangeRouter: (state: any) => state.changeRouter
  },
  actions: {
    setLoading(loading: boolean) {
      this.loading = loading;
    },
    setInfo(info: any) {
      this.info = info;
    },
    setRoutes(route: any | any[]) {
      if (Array.isArray(route)) {
        this.routes.push(...route);
      } else {
        this.routes.push(route);
      }
    },
    toggle() {
      this.theme = !this.theme;
      localStorage.setItem('theme', this.theme ? 'light' : 'dark');
      document.documentElement.className = this.theme ? 'light' : 'dark';
    },
    initThemeMode() {
      let theme = localStorage.getItem('theme');
      this.theme = !theme || theme !== 'dark' ? true : false;
    },
    setFigureName(name: string) {
      this.figureName = name;
    },
    setAnchor(anchor: boolean) {
      this.anchor = anchor;
    },
    setInnerHtml(innerHtml: string) {
      this.innerHtml = innerHtml;
    },
    setChangeRouter(changeRouter: boolean) {
      this.changeRouter = changeRouter;
    }
  }
});
