/**
 * @Date: 2023-06-30 18:49:24
 * @Description:
 * @LastEditTime: 2023-11-30 00:10:26
 * @LastEditors: tanxiang 1571922819@qq.com
 * @FilePath: \blog\packages\web-client\src\stores\global
 * @copyright: Copyright (c) 2023 by ${git_name_email}, All Rights Reserved.
 */
// @ts-ignore
import { WebSite } from '@/interface/WebSite';
import { defineStore } from 'pinia';
let token: any = localStorage.getItem('token');

export const useGlobalStore = defineStore('global', {
  state: () => {
    return {
      isOpenDrawer: false,
      isCardLoading: false as boolean,
      webSite: {} as WebSite,
      isContract: false as boolean,
      searchInput: '' as string,
      target: [] as any[],
      token: token as string,
    };
  },
  getters: {
    getIsOpenDrawer(state) {
      return state.isOpenDrawer;
    },
    getIsCardLoading(state) {
      return state.isCardLoading;
    },
    getWebSite(state): WebSite {
      return state.webSite;
    },
    getIsContract(state): boolean {
      return state.isContract;
    },
    getSearchInput(state): string {
      return state.searchInput;
    },
    getTarget(state): any[] {
      return state.target;
    },
    getToken(state): string {
      return state.token;
    },
  },
  actions: {
    setIsOpenDrawer(newIsOpenDrawer: boolean) {
      this.isOpenDrawer = newIsOpenDrawer;
    },
    setIsCardLoading(newIsCardLoading: boolean) {
      this.isCardLoading = newIsCardLoading;
    },
    setWebSite(newWebSite: WebSite) {
      let tags: { id: number; name: string }[] = this.webSite.tags;
      this.webSite = newWebSite;
      this.webSite.tags = tags;
    },
    setWebSiteTags(newWebSiteTags: { id: number; name: string }[]) {
      this.webSite.tags = newWebSiteTags;
    },
    setIsContract(newIsContract: boolean) {
      this.isContract = newIsContract;
    },
    setSearchInput(newSearchInput: string) {
      this.searchInput = newSearchInput;
    },
    setTarget(newTarget: any[]) {
      this.target.splice(0, this.target.length);
      this.target.push(...newTarget);
    },
    setToken(newToken: string) {
      this.token = newToken;
      localStorage.setItem('token', newToken);
    },
  },
});