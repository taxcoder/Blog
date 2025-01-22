/*
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2025-01-17 08:20:22
 * @LastEditTime: 2025-01-17 08:35:06
 * @FilePath: /code/blog/client-admin/src/constant/menu.ts
 * @Description:
 * Copyright (c) 2025 by tanxiang, All Rights Reserved.
 */
import blog from './menu/blog';
import system from './menu/system';

const menus: { key: string; value: string; children: any[] }[] = [];
const childrenRouters = [blog, system];

export { childrenRouters, menus };
