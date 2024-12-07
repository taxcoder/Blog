import { defineStore } from 'pinia';

export const useRecoveryStore = defineStore('recovery', {
  state: () => {
    return {
      articleSearch: {
        name: '',
        value: '',
        select: '',
      },
      essaySearch: {
        name: '',
        value: '',
        select: '',
      },
    };
  },
  getters: {
    getArticleSearch(state) {
      return state.articleSearch;
    },
    getEssaySearch(state) {
      return state.essaySearch;
    },
  },
  actions: {
    setArticleSearch(obj: { name: string; value: any; select: string }) {
      this.articleSearch.name = obj.name;
      this.articleSearch.value = obj.value;
      this.articleSearch.select = obj.select;
    },
    setEssaySearch(obj: { name: string; value: any; select: string }) {
      this.essaySearch.name = obj.name;
      this.essaySearch.value = obj.value;
      this.essaySearch.select = obj.select;
    },
  },
});
