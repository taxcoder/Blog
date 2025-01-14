/*
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2025-01-04 15:38:33
 * @LastEditTime: 2025-01-04 15:38:34
 * @FilePath: /code/blog/client-web/src/mock/topping.ts
 * @Description:
 * Copyright (c) 2025 by tanxiang, All Rights Reserved.
 */
import { Topping } from '@/api/topping/topping.d';

let toppings: Array<Topping> = [
  {
    id: 1,
    img: 'src/assets/img/image.png',
    title: '测试1的标题',
    description: '测试1的描述'
  },
  {
    id: 2,
    img: 'src/assets/img/image.png',
    title: '测试2的标题',
    description: '测试2的描述'
  }
];

export const topping = (mock: any) => {
  mock.onGet('/api/toppings').reply(200, {
    data: toppings
  });
};
