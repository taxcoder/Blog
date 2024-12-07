// @ts-ignore
import { service } from './http';

export const useImages = (base: string = 'api') => {
  return {
    imagesList: async () => {
      return await service.get(`/${base}/images/list`);
    },
  };
};

export const useAuthImages = (base: string = 'auth') => {
  return {
    deleteImages: async (id: string) => {
      return await service.delete(`/${base}/images/delete/${id}`);
    },
    imagesList: async (current: number, size: number) => {
      return await service.get(`/${base}/images/list?current=${current}&size=${size}`);
    },
    searchImageTime: async (current: number, size: number, start: number, end: string) => {
      return await service.get(`/${base}/images/search/time?current=${current}&size=${size}&start=${start}&end=${end}`);
    },
  };
};