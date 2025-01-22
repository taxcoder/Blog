/*
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2025-01-04 15:39:11
 * @LastEditTime: 2025-01-10 04:36:16
 * @FilePath: /code/blog/client-web/src/mock/category.ts
 * @Description:
 * Copyright (c) 2025 by tanxiang, All Rights Reserved.
 */
import { Category } from '@/api/categorys/category.d';
import { articles } from './article';

const categorys: Array<Category> = [
  {
    id: 1,
    name: '默认分类',
    count: 3
  },
  {
    id: 2,
    name: '前端',
    count: 22
  },
  {
    id: 3,
    name: '后端',
    count: 19
  },
  {
    id: 4,
    name: '我的复盘',
    count: 15
  },
  {
    id: 5,
    name: '工具使用',
    count: 10
  },
  {
    id: 6,
    name: '操作笔记',
    count: 12
  },
  {
    id: 7,
    name: 'Python',
    count: 11
  }
];

export const category = (mock: any) => {
  mock.onGet('/categorys').reply(200, {
    data: categorys
  });

  for (let i = 1; i < categorys.length; i++) {
    mock.onGet(`/categorys/${i}?index=1&limit=10`).reply(200, {
      data: {
        id: i,
        name: categorys[i].name,
        allCount: articles.list.filter((element) => element.category.id === i % 7).length,
        list: articles.list.filter((element) => element.category.id === i % 7).slice(0, 10)
      }
    });
  }
};
