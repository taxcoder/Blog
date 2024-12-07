// @ts-ignore
import { service } from './http';

export const useNavigation = (base: string = 'api') => {
  return {
    selectAll: async () => {
      return await service.get(`/${base}/navigation/list`);
    },
  };
};

export const useAuthNavigation = (base: string = 'auth') => {
  return {
    selectNavigationByClassificationNavigation: async (id: string) => {
      return await service.get(`/${base}/navigation/select/classificationNavigation/${id}`);
    },
    selectList: async (current: number, size: number) => {
      return await service.get(`/${base}/navigation/list?current=${current}&size=${size}`);
    },
    deleteNavigation: async (id: number) => {
      return await service.delete(`/${base}/navigation/delete/${id}`);
    },
    updateNavigationCnId: async (id: number, cnId: number) => {
      let formData = new FormData();
      formData.append('cnId', cnId.toString());
      return await service.put(`/${base}/navigation/update/cnId/${id}`, formData);
    },
    updateNavigationTitle: async (id: number, title: string) => {
      let formData = new FormData();
      formData.append('title', title);
      return await service.put(`/${base}/navigation/update/title/${id}`, formData);
    },
    updateNavigationDescription: async (id: number, description: string) => {
      let formData = new FormData();
      formData.append('description', description);
      return await service.put(`/${base}/navigation/update/description/${id}`, formData);
    },
    updateNavigationUrl: async (id: number, url: string) => {
      let formData = new FormData();
      formData.append('url', url);
      return await service.put(`/${base}/navigation/update/url/${id}`, formData);
    },
    updateNavigationFavicon: async (id: number, favicon: any) => {
      let formData = new FormData();
      formData.append('file', favicon);
      return await service.put(`/${base}/navigation/update/favicon/${id}`, formData);
    },
    addNavigation: async (cnId: string, url: string, title: string, description: string, favicon: any) => {
      let formData = new FormData();
      formData.append('cnId', cnId);
      formData.append('url', url);
      formData.append('title', title);
      formData.append('description', description);
      formData.append('favicon', favicon ? (favicon.originFileObj ? favicon.originFileObj : favicon) : favicon);
      return await service.post(`/${base}/navigation/add`, formData);
    },
    selectCnInfo: async () => {
      return await service.get(`/${base}/navigation/select/cnInfo`);
    },
  };
};