/*
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2024-12-31 10:01:44
 * @LastEditTime: 2024-12-31 10:08:15
 * @FilePath: /code/blog/client-web/src/api/topping/topping.ts
 * @Description:
 * Copyright (c) 2024 by tanxiang, All Rights Reserved.
 */
import { request } from '@/axios';
import { Topping } from './topping.d';

export const getToppings = async () => {
  return request<Array<Topping>>(`/api/toppings`);
};
