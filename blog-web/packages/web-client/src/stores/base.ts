import { defineStore } from 'pinia';

let current: any = localStorage.getItem('current');
let theme: any = localStorage.getItem('theme');
let openActive: any = localStorage.getItem('openActive');
let openUserActive: any = localStorage.getItem('openUserActive');

export const useBaseStore = defineStore('base', {
  state: () => {
    return {
      theme: (theme === 'dark') as boolean,
      current: (!isNaN(parseFloat(current)) && isFinite(current) ? parseInt(current) : 0) as number,
      openActive: (openActive === 'true') as boolean,
      openUserActive: (openUserActive !== 'false') as boolean,
      title: '',
      isLoadingShow: false,
      reload: false,
    };
  },
  getters: {
    getTheme(state) {
      return state.theme;
    },
    getCurrent(state) {
      return state.current;
    },
    getOpenActive(state) {
      return state.openActive;
    },
    getOpenUserActive(state) {
      return state.openUserActive;
    },
    getTitle(state) {
      return state.title;
    },
    getIsLoadingShow(state) {
      return state.isLoadingShow;
    },
    getReload(state) {
      return state.reload;
    },
  },
  actions: {
    setTheme(newTheme: boolean) {
      this.theme = newTheme;
    },
    setCurrent(newCurrent: number) {
      this.current = newCurrent;
    },
    setOpenActive(newOpenActive: boolean) {
      this.openActive = newOpenActive;
    },
    setOpenUserActive(newOpenUserActive: boolean) {
      this.openUserActive = newOpenUserActive;
    },
    setTitle(newTitle: string) {
      this.title = newTitle;
    },
    setIsLoadingShow(newIsLoadingShow: boolean) {
      this.isLoadingShow = newIsLoadingShow;
    },
    setReload(newReload: boolean) {
      this.reload = newReload;
    },
  },
  persist: {
    key: 'base',
    paths: ['current', 'openActive', 'openUserActive'],
  },
});