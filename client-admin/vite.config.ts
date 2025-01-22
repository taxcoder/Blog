/*
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2025-01-15 07:34:48
 * @LastEditTime: 2025-01-17 07:52:35
 * @FilePath: /code/blog/client-admin/vite.config.ts
 * @Description:
 * Copyright (c) 2025 by tanxiang, All Rights Reserved.
 */
import { defineConfig } from 'vite';
import vue from '@vitejs/plugin-vue';
import AutoImport from 'unplugin-auto-import/vite';
import { NaiveUiResolver } from 'unplugin-vue-components/resolvers';
import Components from 'unplugin-vue-components/vite';

import { visualizer } from 'rollup-plugin-visualizer';
import legacyPlugin from '@vitejs/plugin-legacy';
import viteCompression from 'vite-plugin-compression';
// VueHooks
import { VueHooksPlusResolver } from '@vue-hooks-plus/resolvers';
//@ts-ignore
import * as path from 'path';
import Unocss from 'unocss/vite';
import rollupOptionsConfig from './rollupOptions.config';
//@ts-ignore
const resolveApp = (relativePath: string) => path.resolve(__dirname, relativePath);

export default defineConfig({
  //配置别名
  resolve: {
    alias: { '@': resolveApp('src') }
  },
  plugins: [
    vue(),
    Unocss(),
    visualizer({
      open: true,
      emitFile: false,
      filename: 'stats.html',
      sourcemap: false
    }),
    legacyPlugin({
      // 需要兼容的目标列表，可以设置多个
      targets: ['chrome 52'],
      // 面向IE11时需要此插件
      additionalLegacyPolyfills: ['regenerator-runtime/runtime']
    }),
    AutoImport({
      imports: [
        'vue',
        'vue-router',
        'pinia',
        { 'naive-ui': ['useDialog', 'useMessage', 'useNotification', 'useLoadingBar'] }
      ],
      resolvers: [VueHooksPlusResolver(), NaiveUiResolver()],
      include: [/\.[tj]sx?$/, /\.vue$/, /\.vue\?vue/, /\.md$/],
      dirs: [
        'src/common',
        'src/components',
        'src/store/modules',
        'src/hooks',
        'src/views',
        'src/layout',
        'src/api',
        'src/utils'
      ],
      // 自动引入生成api的地址
      dts: resolveApp('types/auto-import.d.ts'),
      eslintrc: {
        // 是否开启eslint
        enabled: true,
        // eslint配置文件地址
        filepath: resolveApp('types/.eslintrc-auto-import.json'),
        // 是否开启全局变量
        globalsPropValue: true
      }
    }),
    Components({
      resolvers: [NaiveUiResolver()],
      deep: true,
      dirs: ['src/layout', 'src/components', 'src/common', 'src/views'],
      directoryAsNamespace: true,
      collapseSamePrefixes: true,
      dts: resolveApp('types/components.d.ts') //自动引入生成的组件的地址
    }),
    // gzip压缩 生产环境生成 .gz 文件
    viteCompression({
      verbose: true,
      disable: false,
      threshold: 10240,
      algorithm: 'gzip',
      ext: '.gz'
    })
  ],
  build: {
    minify: 'esbuild',
    // 构建后是否生成 source map 文件
    sourcemap: false,
    // 规定触发警告的 chunk(文件块) 大小
    chunkSizeWarningLimit: 1500,
    assetsDir: 'static',
    terserOptions: {
      //生产环境时移除console
      compress: { drop_console: true, drop_debugger: true }
    },
    rollupOptions: rollupOptionsConfig
  },
  server: {
    host: '0.0.0.0',
    port: 8888,
    open: true,
    hmr: true
  }
});
