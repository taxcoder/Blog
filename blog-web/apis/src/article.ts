// @ts-ignore
import { service } from './http';

export const useArticle = (base: string = 'api') => {
  const articleList = async (current: number, size: number): Promise<any> => {
    return await service.get(`/${base}/article/list?current=${current}&size=${size}`);
  };

  const articleClassification = async (current: number, size: number, id: number) => {
    return await service.get(`/${base}/article/classification/${id}?current=${current}&size=${size}`);
  };

  const articleTag = async (current: number, size: number, id: number) => {
    return await service.get(`/${base}/article/tag/${id}?current=${current}&size=${size}`);
  };

  const archivedList = async (): Promise<any> => {
    return await service.get(`/${base}/article/archived/list`);
  };

  const findArticleContentById = async (id: any): Promise<any> => {
    return await service.get(`/${base}/article/content/${id}`);
  };

  const findArticleInfoById = async (id: any): Promise<any> => {
    return await service.get(`/${base}/article/info/${id}`);
  };

  return {
    articleList,
    archivedList,
    findArticleContentById,
    findArticleInfoById,
    articleClassification,
    articleTag,
  };
};

export const useAuthArticle = (base: string = 'auth') => {
  const articleList = async (current: number, size: number): Promise<any> => {
    return await service.get(`/${base}/article/info/limit?current=${current}&size=${size}`);
  };

  const changeArticleTop = async (id: number) => {
    return await service.put(`/${base}/article/update/top/${id}`);
  };
  // 存入回收站
  const deleteArticleById = async (id: number) => {
    return await service.delete(`/${base}/article/delete/${id}`);
  };
  // 在获取处于回收状态的文章
  const recoveryArticleList = async (current: number, size: number): Promise<any> => {
    return await service.get(`/${base}/article/recovery/limit?current=${current}&size=${size}`);
  };
  // 在回收站内彻底删除文章
  const realDeleteArticleById = async (id: number) => {
    return await service.delete(`/${base}/recovery/article/delete/${id}`);
  };

  const articleRestoreById = async (id: any) => {
    return await service.put(`/${base}/recovery/article/restore/${id}`);
  };

  const articleUpdateById = async (
    id: any,
    obj: { image: Blob; top: any; text: string; title: any; classificationId: any; tagsId: any; prefixContent: string }
  ) => {
    return await service.put(`/${base}/article/update/info/${id}`, upload(obj), {
      headers: { 'Content-Type': 'multipart/form-data' },
    });
  };

  const addArticle = async (obj: {
    text: string;
    classificationId: number;
    tagsId: number[];
    title: string;
    top: boolean;
    image: Blob;
    prefixContent: string;
  }) => {
    return await service.post(`/${base}/article/add/info`, upload(obj), {
      headers: { 'Content-Type': 'multipart/form-data' },
    });
  };

  const upload = (obj: {
    text: string;
    classificationId: number;
    tagsId: number[];
    title: string;
    top: boolean;
    image: Blob;
    prefixContent: string;
  }) => {
    let form = new FormData();
    form.append('image', obj.image);
    form.append('text', obj.text);
    form.append('classificationId', obj.classificationId.toString());
    form.append('tagsId', JSON.stringify(obj.tagsId));
    form.append('title', obj.title);
    form.append('top', obj.top.toString());
    form.append('prefixContent', obj.prefixContent);
    return form;
  };

  return {
    articleList,
    changeArticleTop,
    deleteArticleById,
    realDeleteArticleById,
    articleUpdateById,
    addArticle,
    recoveryArticleList,
    articleRestoreById,
  };
};