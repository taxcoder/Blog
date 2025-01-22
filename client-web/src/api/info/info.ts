/*
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2024-12-30 09:17:40
 * @LastEditTime: 2025-01-15 02:32:02
 * @FilePath: /code/blog/client-web/src/api/info/info.ts
 * @Description:
 * Copyright (c) 2024 by tanxiang, All Rights Reserved.
 */
import { request } from '@/axios';
import { WebInfo } from './info.d';

export const getWebInfo = async () => {
  return request<Array<WebInfo>>(`/info`);
};
