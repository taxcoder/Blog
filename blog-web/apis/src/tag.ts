// @ts-ignore
import { service } from './http';

export const useTag = (base: string = 'api') => {
  const tags = async () => {
    return await service.get(`/${base}/tag/all`);
  };
  return { tags };
};

export const useAuthTag = (base: string = 'auth') => {
  const tagLimit = async (current: number, size: number) => {
    return await service.get(`/${base}/tag/limit?current=${current}&size=${size}`);
  };

  const tagArticles = async (id: number) => {
    return await service.get(`/${base}/tag/article/${id}`);
  };

  const tagUpdate = async (id: number, name: string | any) => {
    let formData = new FormData();
    formData.append('name', name);
    return await service.put(`/${base}/tag/update/${id}`, formData);
  };

  const tagDelete = async (id: number) => {
    return await service.delete(`/${base}/tag/delete/${id}`);
  };

  const tagAdd = async (names: string[]) => {
    return await service.post(`/${base}/tag/add`, {
      name: names,
    });
  };

  return { tagArticles, tagUpdate, tagDelete, tagLimit, tagAdd };
};