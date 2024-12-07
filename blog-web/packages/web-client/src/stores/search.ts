import { defineStore } from 'pinia';

export const useSearchStore = defineStore('search', {
  state: () => {
    return {};
  },
  getters: {},
  actions: {},
  persist: {
    key: 'search',
    paths: [],
  },
});
