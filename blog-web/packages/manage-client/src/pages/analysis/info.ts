import { useAuthClassification, useAuthEssay, useTag, count } from '@tanxiang/apis';
//@ts-ignore
import { messageInfo } from '@tanxiang/common';
//@ts-ignore
import { useBaseStore } from '@/stores/base';

const base = useBaseStore();

const { messageError } = messageInfo();

export const size = ref<number>(0);

export const classificationAll = (data: any[]) => {
  useAuthClassification()
    .classificationAll()
    .then((success: any) => {
      data.splice(0, data.length);
      data.push(...success.data);
      base.setRoses('classification', success.data);
      size.value = size.value + 1;
    })
    .catch((error: any) => messageError(!error || error.name ? '网络错误' : error));
};

export const tagAll = (data: any[]) => {
  useTag()
    .tags()
    .then((success: any) => {
      data.splice(0, data.length);
      data.push(...success.data);
      base.setRoses('tag', success.data);
      size.value = size.value + 1;
    })
    .catch((error: any) => messageError(!error || error.name ? '网络错误！' : error));
};

export const essayAddress = (data: any[]) => {
  useAuthEssay()
    .essayProvince()
    .then((success: any) => {
      data.splice(0, data.length);
      data.push(...success.data);
      base.setRoses('essay', success.data);
      size.value = size.value + 1;
    })
    .catch((error: any) => messageError(!error || error.name ? '网络错误！' : error));
};

export const webInfo = (data: any[]) => {
  count()
    .then((success: any) => {
      data[2].num = success.data.find((c: any) => c.name === '文章').num;
      data[3].num = success.data.find((c: any) => c.name === '说说').num;
      size.value = size.value + 1;
    })
    .catch((error: any) => messageError(!error || error.name ? '网络错误！' : error));
};