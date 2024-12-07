// @ts-ignore
import { service } from './http';

export const useChats = (base: string = 'api') => {
  return {
    chatAdd: async (eId: number, data: { author: string; webUrl: string; email: string; content: string }) => {
      return await service.post(`/${base}/chat/add`, getFormData(eId, data));
    },
    webChatAdd: async (eId: number, data: { author: string; webUrl: string; email: string; content: string }) => {
      return await service.post(`/admin/chat/add`, getFormData(eId, data));
    },
    chatList: async (eId: number) => {
      return await service.get(`/${base}/chat/list/${eId}`);
    },
  };
};

export const useAuthChats = (base: string = 'auth') => {
  return {
    chatDelete: async (id: string) => {
      return await service.delete(`/${base}/chat/delete/${id}`);
    },
    chatDeleteList: async (ids: string[]) => {
      return await service.delete(`/${base}/chat/delete/list`, {
        data: ids,
      });
    },
  };
};

function getFormData(eId: number, data: { author: string; webUrl: string; email: string; content: string }) {
  let form = new FormData();
  form.append('eId', eId.toString());
  form.append('author', data.author);
  form.append('webUrl', data.webUrl);
  form.append('email', data.email);
  form.append('content', data.content);
  return form;
}