/*
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2025-01-03 05:48:43
 * @LastEditTime: 2025-01-15 02:30:45
 * @FilePath: /code/blog/client-web/src/api/categorys/category.ts
 * @Description:
 * Copyright (c) 2025 by tanxiang, All Rights Reserved.
 */
import { request } from '@/axios';
import { Category, CategoryArticle } from './category.d';

export const getCategorys = async () => {
  return request<Array<Category>>(`/categorys`);
};

export const getArticleByCategoryId = async (id: number, index: number, limit: number) => {
  return request<Array<CategoryArticle>>(`/categorys/${id}?index=${index}&limit=${limit}`);
};
