/*
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2024-12-08 10:08:15
 * @LastEditTime: 2024-12-10 02:20:26
 * @FilePath: /code/blog/client-web/vite.config.ts
 * @Description: vite配置页
 * Copyright (c) 2024 by tanxiang, All Rights Reserved.
 */
import { defineConfig } from 'vite';
import vue from '@vitejs/plugin-vue';
// 导入自动导入插件
import AutoImport from 'unplugin-auto-import/vite';
// 导入自动注册组件的插件
import Components from 'unplugin-vue-components/vite';
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers';
import * as path from 'path';
import fs from 'fs';
import Unocss from 'unocss/vite';

const appDirectory = fs.realpathSync(process.cwd());
const resolveApp = (relativePath) => path.resolve(appDirectory, relativePath);

// https://vite.dev/config/
export default defineConfig({
  // 配置打包后的相对路径
  base: './',
  //配置别名
  resolve: {
    // 需要在tsconfig.json的compilerOptions中配置paths
    alias: {
      // @代替src
      '@': resolveApp('src'),
      common: resolveApp('common'),
      components: resolveApp('components'),
      router: resolveApp('router'),
      store: resolveApp('store'),
      api: resolveApp('api'),
      assets: resolveApp('assets'),
      hooks: resolveApp('hooks'),
      utils: resolveApp('utils'),
      pages: resolveApp('pages'),
      'element-plus': path.resolve(__dirname, 'node_modules/element-plus')
    }
  },
  plugins: [
    vue(),
    Unocss(),
    AutoImport({
      //plus按需引入
      resolvers: [ElementPlusResolver()],
      //引入vue 自动注册api插件
      imports: ['vue', 'vue-router', 'pinia'], // 配置需要自动导入的库
      dts: 'types/auto-import.d.ts', // 自动引入生成api的地址
      eslintrc: {
        enabled: true, // 是否开启eslint
        filepath: 'types/.eslintrc-auto-import.json', // eslint配置文件地址
        globalsPropValue: true // 是否开启全局变量
      }
    }),
    Components({
      //plus按需引入
      resolvers: [ElementPlusResolver()],
      // 配置需要将哪些后缀类型的文件进行自动按需引入
      extensions: ['vue', 'md'],
      dts: 'types/components.d.ts' //自动引入生成的组件的地址
    })
  ],
  // 打包配置
  build: {
    minify: 'terser',
    terserOptions: {
      compress: {
        //生产环境时移除console
        drop_console: true,
        drop_debugger: true
      }
    }
  },
  // 跨域
  server: {
    host: '0.0.0.0',
    port: 7777,
    // 热更新
    hmr: true
    //自定义代理规则
  }
});
