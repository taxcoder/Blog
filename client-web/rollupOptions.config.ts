/*
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2024-12-27 07:26:35
 * @LastEditTime: 2025-01-14 07:23:52
 * @FilePath: /code/blog/client-web/rollupOptions.config.ts
 * @Description:
 * Copyright (c) 2024 by tanxiang, All Rights Reserved.
 */
//@ts-ignore
import fs from 'fs';
//@ts-ignore
import path from 'path';
//@ts-ignore
const resolveApp = (relativePath: string) => path.resolve(__dirname, relativePath);

const entryPath = () => {
  //最后生成的多页面配置项
  const map: { [key: string]: string } = {};
  //指定要查询的目录
  const PAGE_PATH = resolveApp('pages');
  //获取到指定目录下的所有文件名
  const entryFiles = fs.readdirSync(PAGE_PATH);
  entryFiles.forEach((filePath) => {
    //遍历处理每个子页面的入口
    map[filePath] = resolveApp(`pages/${filePath}/index.html`);
  });
  return map;
};

export default {
  input: {
    //入口文件
    index: 'index.html',
    //多入口文件
    ...entryPath()
  },
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
