// @ts-ignore
import { service } from './http';

export const useEssay = (base: string = 'api') => {
  return {
    essayList: async (current: number, size: number) => {
      return await service.get(`/${base}/essay/limit?current=${current}&size=${size}`);
    },
    essayAddLike: async (id: number) => {
      return await service.get(`/${base}/essay/update/like/${id}`);
    },
  };
};

export const useAuthEssay = (base: string = 'auth') => {
  return {
    essayById: async (id: number) => {
      return await service.get(`/${base}/essay/${id}`);
    },
    essayDelete: async (id: number) => {
      return await service.delete(`/${base}/essay/delete/${id}`);
    },
    essayUpdateContentById: async (id: number, content: string) => {
      return await service.put(`/${base}/essay/update/content/${id}?content=${content}`);
    },
    essayRecoveryList: async (current: number, size: number) => {
      return await service.get(`/${base}/recovery/essay/limit?current=${current}&size=${size}`);
    },
    essayRestoreById: async (id: number) => {
      return await service.get(`/${base}/recovery/essay/restore/${id}`);
    },
    essayRestoreDeleteById: async (id: number) => {
      return await service.get(`/${base}/recovery/essay/delete/${id}`);
    },
    addEssay: async (content: string, emojiId: string) => {
      return await service.post(`/${base}/essay/add`, { content: content, emojiId: emojiId });
    },
    essayProvince: async () => {
      return await service.get(`/${base}/essay/province/info`);
    },
  };
};