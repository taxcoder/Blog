/*
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2025-01-15 07:34:48
 * @LastEditTime: 2025-01-15 08:13:11
 * @FilePath: /code/blog/client-admin/types/env.d.ts
 * @Description: 
 * Copyright (c) 2025 by tanxiang, All Rights Reserved. 
 */
/// <reference types="vite/client" />
// 三斜线引用告诉编译器在编译过程中用types形式引入的额外的文件vite/client.d.ts,
// 此文件里面是vite帮我们定义的各种常用类型定义，比如css,图片等。

declare module '*.vue' {
  import type { DefineComponent } from 'vue';
  // eslint-disable-next-line @typescript-eslint/no-explicit-any, @typescript-eslint/ban-types
  const component: DefineComponent<{}, {}, any>;
  export default component;
}
