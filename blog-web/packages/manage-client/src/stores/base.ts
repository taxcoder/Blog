import { defineStore } from 'pinia';

let token = localStorage.getItem('token');

export const useBaseStore = defineStore('base', {
  state: () => {
    return {
      open: false,
      breakpoint: false,
      clickRouterList: [] as { name: string; url: string }[],
      fullScreen: false,
      currentListIndex: 0,
      emojis: {},
      webStation: {} as any,
      weather: {
        pushTime: null,
        future: [] as any[],
      },
      roses: {
        classification: [],
        tag: [],
        essay: [],
      },
      loading: false,
      isLogin: !!token,
      isSuccess: false,
      previousRoutePath: '' as string,
    };
  },
  getters: {
    getOpen(state) {
      return state.open;
    },
    getBreakpoint(state) {
      return state.breakpoint;
    },
    getClickRouterList(state) {
      return state.clickRouterList;
    },
    getFullScreen(state) {
      return state.fullScreen;
    },
    getCurrentListIndex(state) {
      return state.currentListIndex;
    },
    getEmojis(state) {
      return state.emojis;
    },
    getWebStation(state) {
      return state.webStation;
    },
    getWeather(state) {
      return state.weather;
    },
    getRoses(state) {
      return state.roses;
    },
    getLoading(state) {
      return state.loading;
    },
    getIsLogin(state) {
      return state.isLogin;
    },
    getIsSuccess(state) {
      return state.isSuccess;
    },
    getPreviousRoutePath(state) {
      return state.previousRoutePath;
    },
  },
  actions: {
    setOpen(newOpen: boolean) {
      this.open = newOpen;
    },
    setBreakpoint(newBreakpoint: boolean) {
      this.breakpoint = newBreakpoint;
    },
    pushClickRouterList(newRouter: { name: string | symbol; url: string }) {
      let list = this.clickRouterList.filter((list: { name: string; url: string }) => list.name === newRouter.name);
      if (list.length === 0) this.clickRouterList.push(newRouter);
    },
    removeClickRouterList(index: number) {
      this.clickRouterList.splice(index, 1);
    },
    initClickRouterList(newRouter: { name: string | symbol; url: string }) {
      this.clickRouterList = [newRouter];
    },
    setFullScreen(newFullScreen: boolean) {
      this.fullScreen = newFullScreen;
    },
    setCurrentListIndex(newIndex: number) {
      this.currentListIndex = newIndex;
    },
    setEmojis(newEmojis: any) {
      this.emojis = newEmojis;
    },
    setWebStation(newWebStation: any) {
      this.webStation = newWebStation;
    },
    setWeather(key: string, newWeather: any) {
      this.weather[key] = newWeather;
    },
    setRoses(key: string, newRoses: any) {
      this.roses[key].splice(0, this.roses[key].length);
      this.roses[key].push(...newRoses);
    },
    setLoading(newLoading: boolean) {
      this.loading = newLoading;
    },
    initWeather() {
      this.weather = {
        pushTime: null,
        future: [] as any[],
      };
    },
    setIsLogin(newIsLogin: boolean) {
      this.isLogin = newIsLogin;
    },
    setIsSuccess(newIsSuccess: boolean) {
      this.isSuccess = newIsSuccess;
    },
    setPreviousRoutePath(newPreviousRoutePath: string) {
      this.previousRoutePath = newPreviousRoutePath;
    },
  },
  persist: {
    key: 'base',
    paths: ['open', 'clickRouterList', 'fullScreen', 'currentListIndex', 'weather'],
  },
});