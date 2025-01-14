/*
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2025-01-03 15:06:49
 * @LastEditTime: 2025-01-03 15:06:51
 * @FilePath: /code/blog/client-web/src/api/tags/tag.d.ts
 * @Description:
 * Copyright (c) 2025 by tanxiang, All Rights Reserved.
 */
export type Tag = {
  id: number;
  name: string;
  count: number;
};

export type TagArticle = {
  id: number;
  name: string;
  allCount: number;
  list: Article[];
};
