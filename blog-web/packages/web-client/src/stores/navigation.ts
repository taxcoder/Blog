import { defineStore } from 'pinia';

export const useNavigationStore = defineStore('navigation', {
  state: () => {
    return {
      datas: [] as string[],
    };
  },
  getters: {
    getDatas(): string[] {
      return this.datas;
    },
  },
  actions: {
    setDatas(datas: string[]) {
      this.datas.splice(0, this.datas.length, ...datas);
    },
  },
  persist: {
    key: 'navigation',
    paths: ['datas'],
  },
});