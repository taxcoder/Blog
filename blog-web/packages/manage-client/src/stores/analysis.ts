import { defineStore } from 'pinia';

export const useAnalysisStore = defineStore('analysis', {
  state: () => {
    return {};
  },
  getters: {},
  actions: {},
  persist: {
    key: 'analysis',
    paths: [],
  },
});
