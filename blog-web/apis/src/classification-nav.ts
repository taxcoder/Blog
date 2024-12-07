// @ts-ignore
import { service } from './http';

export const useAuthClassificationNav = (base: string = 'auth') => {
  return {
    classificationNavList: async (current: number, size: number) => {
      return await service.get(`/${base}/classificationNavigation/list?current=${current}&size=${size}`);
    },
    addClassificationNav: async (name: string) => {
      let data = new FormData();
      data.append('name', name);
      return await service.post(`/${base}/classificationNavigation/add`, data);
    },
    deleteClassificationNav: async (id: string) => {
      return await service.delete(`/${base}/classificationNavigation/delete/${id}`);
    },
    deleteClassificationNavList: async (id: string, links: any[]) => {
      if (links.length === 0) {
        return useAuthClassificationNav().deleteClassificationNav(id);
      } else {
        let form = new FormData();
        form.append('list', JSON.stringify(links));
        return await service.delete(`/${base}/classificationNavigation/delete/list/${id}`, form);
      }
    },
    updateClassificationNav: async (id: string, name: string, newLinkDatas: any[]) => {
      let list = newLinkDatas.filter((d) => d.datas.length > 0);
      let data = new FormData();
      data.append('name', name);
      data.append('list', JSON.stringify(list));
      return await service.put(`/${base}/classificationNavigation/update/${id}`, data);
    },
  };
};