import { defineStore } from 'pinia';

export const useArticleStore = defineStore('article', {
  state: () => {
    return {
      isRefer: false,
      search: {
        name: '',
        value: '',
        select: '',
      },
      formState: {
        classificationValue: '',
        classification: [],
        tagValue: [],
        tag: [],
        content: '',
        title: '',
        isTop: false,
        image: [],
        prefixContent: '',
      },
    };
  },
  getters: {
    getSearch(state) {
      return state.search;
    },
    getFormState(state) {
      return state.formState;
    },
    getIsRefer(state) {
      return state.isRefer;
    },
  },
  actions: {
    setIsRefer(isRefer: boolean) {
      this.isRefer = isRefer;
    },
    setSearch(obj: { name: string; value: any; select: string }) {
      this.search.name = obj.name;
      this.search.value = obj.value;
      this.search.select = obj.select;
    },
    setFormState(obj: {
      classificationValue: string;
      classification: any[];
      tagValue: string[];
      tag: any[];
      content: string;
      title: string;
      isTop: boolean;
      image: any[];
      prefixContent: string;
    }) {
      this.formState.classificationValue = obj.classificationValue;
      this.formState.content = obj.content;
      this.formState.title = obj.title;
      this.formState.prefixContent = obj.prefixContent;
      this.formState.isTop = obj.isTop;
      //@ts-ignore
      if (this.formState.image && obj.images) {
        this.formState.image.splice(0, this.formState.image.length);
        //@ts-ignore
        this.formState.image.push(...obj.images);
      } else {
        this.formState.image = [];
      }

      this.formState.tagValue.splice(0, this.formState.tagValue.length);
      this.formState.tagValue.push(...obj.tagValue);

      this.formState.classification.splice(0, this.formState.classification.length);
      this.formState.classification.push(...obj.classification);

      this.formState.tag.splice(0, this.formState.tag.length);
      this.formState.tag.push(...obj.tag);
    },

    initFormState() {
      this.formState = {
        classificationValue: '',
        classification: [],
        tagValue: [],
        tag: [],
        content: '',
        title: '',
        isTop: false,
        image: [],
        prefixContent: '',
      };
    },
  },
  persist: {
    key: 'article',
    paths: ['formState'],
  },
});