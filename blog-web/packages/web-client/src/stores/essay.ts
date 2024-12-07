import { defineStore } from 'pinia';

export const useEssayStore = defineStore('essay', {
  state: () => {
    return {
      likes: [] as string[],
    };
  },
  getters: {
    getLikes(state) {
      return state.likes;
    },
  },
  actions: {
    setLikes(newLikes): void {
      this.likes.push(newLikes);
    },
  },
  persist: {
    key: 'essay',
    paths: ['likes'],
  },
});
