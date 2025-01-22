/*
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2025-01-04 15:37:24
 * @LastEditTime: 2025-01-15 03:09:58
 * @FilePath: /code/blog/client-web/src/mock/topping.ts
 * @Description:
 * Copyright (c) 2025 by tanxiang, All Rights Reserved.
 */
import { Topping } from '@/api/topping/topping.d';
import { articles } from './article';
import { Article } from '@/api/articles/article.d';

let toppings: Array<Topping> = articles.list.slice(7, 9).map((item: Article) => {
  return {
    id: item.id,
    title: item.title,
    description: item.description.substring(0, 30),
    img: item.cover
  };
});

export const topping = (mock: any) => {
  mock.onGet('/toppings').reply(200, {
    data: toppings
  });
};
