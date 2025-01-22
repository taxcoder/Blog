/*
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2024-12-27 07:26:35
 * @LastEditTime: 2025-01-15 08:24:09
 * @FilePath: /code/blog/client-admin/rollupOptions.config.ts
 * @Description:
 * Copyright (c) 2024 by tanxiang, All Rights Reserved.
 */
//@ts-ignore
import fs from 'fs';
//@ts-ignore
import path from 'path';
//@ts-ignore
const resolveApp = (relativePath: string) => path.resolve(__dirname, relativePath);

export default {
  output: {
    //静态文件输出的文件夹名称
    assetFileNames: '[ext]/[name]-[hash].[ext]',
    //chunk包输出的文件夹名称
    chunkFileNames: (chunkInfo) => {
      const facadeModuleId = chunkInfo.facadeModuleId ? chunkInfo.facadeModuleId.split('/') : [];
      const fileName = facadeModuleId[facadeModuleId.length - 2] || '[name]';
      return `js/${fileName}/[name].[hash].js`;
    },
    //入口文件输出的文件夹名称
    entryFileNames: 'js/[name]-[hash].js',
    compact: true,
    manualChunks: (id: string) => {
      if (id.includes('node_modules')) {
        // 拆分多个vendors
        return id.toString().split('node_modules/')[1].split('/')[0].toString();
      }
    }
  }
};
