import { defineStore } from 'pinia';

export const useEmojiStore = defineStore('emoji', {
  state: () => {
    return {
      search: {
        name: '',
        value: '',
        select: '',
      },
      group: [] as string[],
      initGroup: [] as string[],
    };
  },
  getters: {
    getSearch(state) {
      return state.search;
    },
    getGroup(state) {
      return state.group;
    },
    getInitGroup(state) {
      return state.initGroup;
    },
  },
  actions: {
    setSearch(obj: { name: string; value: any; select: string }) {
      this.search.name = obj.name;
      this.search.value = obj.value;
      this.search.select = obj.select;
    },
    setGroup(group: string[]) {
      let temp = this.group
        .concat(group)
        .filter((item: string, index: number, arr: string[]) => arr.indexOf(item) === index);
      this.group.splice(0, this.group.length, ...temp);
    },
    addGroup(group: string) {
      if (this.group.indexOf(group) === -1) this.group.push(group);
    },
    clearGroup() {
      this.group.splice(0, this.group.length);
    },
    setInitGroup(group: string[]) {
      this.initGroup.splice(0, this.initGroup.length, ...group);
    },
  },
  persist: {
    key: 'emoji',
    paths: ['group'],
  },
});