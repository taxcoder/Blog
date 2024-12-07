// @ts-ignore
import { service } from './http';

export const useExcerpt = (base: string = 'api') => {
  return {
    excerptList: async (current: number, size: number) => {
      return await service.get(`/${base}/excerpt/list?current=${current}&size=${size}`);
    },
  };
};

export const useAuthExcerpt = (base: string = 'auth') => {
  return {
    excerptInsertContent: async (content: string) => {
      let form = new FormData();
      form.append('content', content);
      return await service.post(`/${base}/excerpt/add/content`, form);
    },
    excerptInsertFile: async (file: any) => {
      let form = new FormData();
      form.append('file', file.originFileObj ? file.originFileObj : file);
      return await service.post(`/${base}/excerpt/add/file`, form);
    },
    excerptDelete: async (id: number) => {
      return await service.delete(`/${base}/excerpt/delete/${id}`);
    },
    excerptUpdateContent: async (id: number, content: string) => {
      let form = new FormData();
      form.append('content', content);
      return await service.put(`/${base}/excerpt/${id}/update/content`, form);
    },
    excerptUpdateFile: async (id: number, file: any) => {
      let form = new FormData();
      form.append('file', file.originFileObj ? file.originFileObj : file);
      return await service.put(`/${base}/excerpt/${id}/update/file`, form);
    },
    excerptList: async (current: number, size: number) => {
      return await service.get(`/${base}/excerpt/list?current=${current}&size=${size}`);
    },
  };
};