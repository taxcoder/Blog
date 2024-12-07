// @ts-ignore
import { service } from './http';

export const useAuthEmoji = (base: string = 'auth') => {
  return {
    emojiList: async () => {
      return await service.get(`/${base}/emoji/list`);
    },
    deleteEmoji: async (id: string) => {
      return await service.delete(`/${base}/emoji/delete/${id}`);
    },
    updateGroupName: async (id: string, name: string) => {
      let data = new FormData();
      data.append('name', name);
      return await service.put(`/${base}/emoji/update/group/${id}`, data);
    },
    updateEmojiName: async (id: string, name: string) => {
      let data = new FormData();
      data.append('name', name);
      return await service.put(`/${base}/emoji/update/name/${id}`, data);
    },
  };
};