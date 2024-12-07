/*
 * @Author: tanxiang 1571922819@qq.com
 * @Date: 2023-06-10 14:11:50
 * @Description: vite 配置文件
 * @LastEditTime: 2023-12-02 17:09:15
 * @LastEditors: tanxiang 1571922819@qq.com
 * @FilePath: \blog\packages\web-client\vite.config.ts
 * @copyright: Copyright (c) 2023 by ${git_name_email}, All Rights Reserved.
 */
import vue from '@vitejs/plugin-vue';
import AutoImport from 'unplugin-auto-import/vite';
import ElementPlus from 'unplugin-element-plus/vite';
import { ElementPlusResolver, NaiveUiResolver, VantResolver } from 'unplugin-vue-components/resolvers';
import Components from 'unplugin-vue-components/vite';
import { defineConfig } from 'vite';

import Unocss from 'unocss/vite';
import viteCompression from 'vite-plugin-compression';
import legacy from '@vitejs/plugin-legacy';
import { fileURLToPath, URL } from 'node:url';

const dependencies = require('./package.json').dependencies;
const globalDependencies = require('../../package.json').dependencies;

//@ts-nocheck
/** @type {import('vite').UserConfig} */
const config: any = defineConfig({
  base: '/',
  plugins: [
    vue(),
    Unocss({
      configFile: './uno.config.ts',
    }),
    legacy({
      targets: ['defaults', 'not IE 11', 'chrome 52'],
      additionalLegacyPolyfills: ['regenerator-runtime/runtime'],
    }),
    viteCompression({
      deleteOriginFile: false,
      algorithm: 'gzip',
      ext: '.gz',
      threshold: 10240,
    }),
    AutoImport({
      imports: ['vue', 'vue-router', '@vueuse/core'],
      dts: './auto-imports.d.ts',
      resolvers: [ElementPlusResolver(), VantResolver(), NaiveUiResolver()],
    }),
    Components({
      dts: true,
      dirs: ['src/components', 'src/common', 'src/views'],
      resolvers: [ElementPlusResolver(), NaiveUiResolver(), VantResolver()],
    }),
    ElementPlus({}),
  ],
  resolve: {
    alias: {
      //@ts-ignore
      '@': fileURLToPath(new URL('./src', import.meta.url)),
    },
  },
  server: {
    port: 5137,
    open: true,
  },
  build: {
    // 关闭生成map文件
    sourcemap: false,
    chunkSizeWarningLimit: 500,
    minify: 'terser',
    target: ['edge90', 'chrome90', 'firefox90', 'safari15'],
    terserOptions: {
      compress: {
        drop_console: true,
        drop_debugger: true,
      },
      output: {
        // 去掉注释内容
        comments: true,
      },
    },
    rollupOptions: {
      treeshake: true, // 开启 Tree Shaking，消除未使用的代码，减小最终的包大小
      output: {
        chunkFileNames: 'js/[name]-[hash].js', // 引入文件名的名称
        entryFileNames: 'js/[name]-[hash].js', // 包的入口文件名称
        assetFileNames: '[ext]/[name]-[hash].[ext]', // 资源文件像 字体，图片等
        manualChunks: (id: any) => {
          if (id.includes('node_modules')) {
            // 指定需要拆分的第三方库或模块
            console.info('id', id);
            const dependenciesKeys = Object.keys(dependencies);
            const globalDependenciesKeys = Object.keys(globalDependencies);
            dependenciesKeys.push(...globalDependenciesKeys);
            const match = dependenciesKeys.find((item) => {
              return id.includes(item);
            });
            console.info('match', match);
            const notSplit = ['vue', 'element-plus', 'naive-ui', 'vant'];
            if (match && !notSplit.includes(match)) {
              return match;
            }
          }
        },
      },
    },
  },
});
//@ts-ignore
export default config;