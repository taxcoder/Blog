/*
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2025-01-03 15:06:42
 * @LastEditTime: 2025-01-09 09:16:43
 * @FilePath: /code/blog/client-web/src/api/tags/tag.ts
 * @Description:
 * Copyright (c) 2025 by tanxiang, All Rights Reserved.
 */
import { request } from '@/axios';
import { Tag, TagArticle } from './tag.d';

export const getTags = async () => {
  return request<Array<Tag>>(`/api/tags`);
};

export const getArticleByTagId = async (id: number, index: number, limit: number) => {
  return request<Array<TagArticle>>(`/api/tag/${id}/articles?index=${index}&limit=${limit}`);
};
