/*
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2025-01-16 06:24:59
 * @LastEditTime: 2025-01-17 03:05:57
 * @FilePath: /code/blog/client-admin/src/api/user/user.ts
 * @Description:
 * Copyright (c) 2025 by tanxiang, All Rights Reserved.
 */
import { request } from '@/axios';
import { User } from './user.d';
// 获取文章列表，根据当前页和每页条数
export const authInfo = async () => {
  return request<User>('/auth/info');
};

export const login = async (loginInfo: { username: string; password: string }) => {
  return request<User>('/auth/login', {
    method: 'POST',
    data: loginInfo
  });
};
