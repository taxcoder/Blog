import { defineStore } from 'pinia';

export const useNavigationStore = defineStore('navigation', {
  state: () => {
    return {
      search: {
        name: '',
        value: '',
        select: '',
      },
      cnName: [] as { value: number; label: string }[],
    };
  },
  getters: {
    getSearch(state) {
      return state.search;
    },
    getCnName(state) {
      return state.cnName;
    },
  },
  actions: {
    setSearch(obj: { name: string; value: any; select: string }) {
      this.search.name = obj.name;
      this.search.value = obj.value;
      this.search.select = obj.select;
    },
    setCnName(arr: { value: number; label: string }[]) {
      this.cnName.push(...arr);
    },
    clearCnName() {
      this.cnName.splice(0, this.cnName.length);
    },
  },
});