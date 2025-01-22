/*
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2025-01-16 06:26:04
 * @LastEditTime: 2025-01-16 06:29:52
 * @FilePath: /code/blog/client-admin/src/api/USER/system.d.ts
 * @Description: 系统基本信息接口
 * Copyright (c) 2025 by tanxiang, All Rights Reserved.
 */
export interface User {
  id: number;
  name: string;
  avatar: string;
  level: number;
  email: string;
}
