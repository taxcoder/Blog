/*
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2025-01-03 05:48:35
 * @LastEditTime: 2025-01-03 15:07:52
 * @FilePath: /code/blog/client-web/src/api/categorys/category.d.ts
 * @Description:
 * Copyright (c) 2025 by tanxiang, All Rights Reserved.
 */
export type Category = {
  id: number;
  name: string;
  count: number;
};

export type CategoryArticle = {
  id: number;
  namme: string;
  allCount: number;
  list: Article[];
};
