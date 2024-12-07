import { defineStore } from 'pinia';

export const useClassificationNavigationStore = defineStore('classificationNavigation', {
  state: () => {
    return {
      search: {
        name: '',
        value: '',
        select: '',
      },
    };
  },
  getters: {
    getSearch(state) {
      return state.search;
    },
  },
  actions: {
    setSearch(obj: { name: string; value: any; select: string }) {
      this.search.name = obj.name;
      this.search.value = obj.value;
      this.search.select = obj.select;
    },
  },
});