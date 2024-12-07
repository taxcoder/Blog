import { defineStore } from 'pinia';

let size: any = localStorage.getItem('pageSize');
let current: any = localStorage.getItem('current');

export const useArticleStore = defineStore('article', {
  state: () => {
    return {
      articleList: {} as any,
      pageSize: !size ? 0 : size,
      current: (!isNaN(parseFloat(current)) && isFinite(current) ? parseInt(current) : 1) as number,
      directory: [] as any[],
    };
  },
  getters: {
    getArticleList(state) {
      return state.articleList;
    },
    getPageSize(state): number {
      return state.pageSize;
    },
    getCurrent(state) {
      return state.current;
    },
    getDirectory(state) {
      return state.directory;
    },
  },
  actions: {
    setArticleList(newArticleList: any) {
      this.articleList = newArticleList;
    },
    setPageSize(newPageSize: number) {
      this.pageSize = newPageSize;
    },
    setCurrent(newCurrent: number) {
      this.current = newCurrent;
    },
    setDirectory(newDirectory: any[]) {
      this.directory.splice(0, this.directory.length, ...newDirectory);
    },
  },
  persist: {
    key: 'article',
    paths: ['current', 'pageSize', 'directory'],
  },
});