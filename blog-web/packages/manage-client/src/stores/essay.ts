import { defineStore } from 'pinia';

export const useEssayStore = defineStore('essay', {
  state: () => {
    return {
      essayById: {},
      content: '',
      isRefer: false,
      search: {
        name: '',
        value: '',
        select: '',
      },
    };
  },
  getters: {
    getEssayById(state) {
      return state.essayById;
    },
    getContent(state) {
      return state.content;
    },
    getSearch(state) {
      return state.search;
    },
    getIsRefer(state) {
      return state.isRefer;
    },
  },
  actions: {
    setEssayById(newEssayById: string) {
      this.essayById = newEssayById;
    },
    setContent(newContent: string) {
      this.content = newContent;
    },
    setSearch(obj: { name: string; value: any; select: string }) {
      this.search.name = obj.name;
      this.search.value = obj.value;
      this.search.select = obj.select;
    },
    setIsRefer(newIsRefer: boolean) {
      this.isRefer = newIsRefer;
    },
  },
  persist: {
    key: 'essay',
    paths: ['content'],
  },
});