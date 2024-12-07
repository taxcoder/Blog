// @ts-ignore
import { service } from './http';

//@ts-ignore
export const useClassification = (base: string = 'api') => {
  return {};
};

export const useAuthClassification = (base: string = 'auth') => {
  const classificationLimit = async (current: number, size: number) => {
    return await service.get(`/${base}/classification/limit?current=${current}&size=${size}`);
  };

  const classificationUpdateInfo = async (id: number, name: string, l: any[]) => {
    let list = l.filter((item) => item.datas.length > 0);
    let from = new FormData();
    from.append('name', name);
    from.append('list', JSON.stringify(list));
    return await service.put(`/${base}/classification/update/info/${id}`, from, {
      contentType: 'application/json',
    });
  };

  const classificationDelete = async (id: number, newArticlesClassification: { key: number; articleId: number }[]) => {
    return await service.delete(`/${base}/classification/delete/${id}`, {
      data: newArticlesClassification,
    });
  };

  const classificationAll = async () => {
    return await service.get(`/${base}/classification/all`);
  };

  const classificationAdd = async (name: string) => {
    return await service.post(`/${base}/classification/add/${name}`);
  };

  return { classificationLimit, classificationUpdateInfo, classificationDelete, classificationAll, classificationAdd };
};