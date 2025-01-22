/*
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2025-01-08 03:31:32
 * @LastEditTime: 2025-01-15 02:33:40
 * @FilePath: /code/blog/client-web/src/api/photos/photo.ts
 * @Description:
 * Copyright (c) 2025 by tanxiang, All Rights Reserved.
 */
import { request } from '@/axios';
interface Result {
  list: { imageId: number; name: string; url: string }[];
  total: number;
}

interface PhotoCategory {
  id: number;
  name: string;
}

export const getPhotoInit = async () => {
  return request<Array<PhotoCategory>>(`/photos`);
};

export const getPhotosResult = async (index: number, limit: number) => {
  return request<Result>(`/photos?index=${index}&limit=${limit}`);
};

export const getPhotoById = async (phoneId: number, index: number, limit: number) => {
  return request<Result>(`/photos/${phoneId}?index=${index}&limit=${limit}`);
};
