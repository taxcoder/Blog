/*
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2024-12-08 10:08:15
 * @LastEditTime: 2024-12-31 02:59:17
 * @FilePath: /code/blog/client-web/vite.config.ts
 * @Description: vite配置页
 * Copyright (c) 2024 by tanxiang, All Rights Reserved.
 */
import { visualizer } from 'rollup-plugin-visualizer';
import { defineConfig } from 'vite';
import legacyPlugin from '@vitejs/plugin-legacy';
import viteCompression from 'vite-plugin-compression';
import vue from '@vitejs/plugin-vue';
// 导入自动导入插件
import AutoImport from 'unplugin-auto-import/vite';
// 导入自动注册组件的插件
import Components from 'unplugin-vue-components/vite';
// VueHooks
import { VueHooksPlusResolver } from '@vue-hooks-plus/resolvers';
// element-plus
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers';
import { VantResolver } from '@vant/auto-import-resolver';
import * as path from 'path';
import Unocss from 'unocss/vite';
import rollupOptionsConfig from './rollupOptions.config.js';

const resolveApp = (relativePath: string) => path.resolve(__dirname, relativePath);

export default defineConfig({
  //配置别名
  resolve: {
    // 需要在tsconfig.json的compilerOptions中配置paths
    alias: {
      // @代替src
      '@': resolveApp('src'),
      '@pages': resolveApp('src/pages')
    }
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
      targets: ['chrome 52'], // 需要兼容的目标列表，可以设置多个
      additionalLegacyPolyfills: ['regenerator-runtime/runtime'] // 面向IE11时需要此插件
    }),
    AutoImport({
      //plus按需引入
      resolvers: [ElementPlusResolver(), VueHooksPlusResolver(), VantResolver()],
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
      //引入vue 自动注册api插件
      imports: ['vue', 'vue-router', 'pinia', { 'element-plus': ['ElMessage', 'ElMessageBox', 'ElLoading'] }], // 配置需要自动导入的库
      dts: resolveApp('types/auto-import.d.ts'), // 自动引入生成api的地址
      eslintrc: {
        enabled: true, // 是否开启eslint
        filepath: resolveApp('types/.eslintrc-auto-import.json'), // eslint配置文件地址
        globalsPropValue: true // 是否开启全局变量
      }
    }),
    Components({
      resolvers: [
        // element-plus按需引入
        ElementPlusResolver(),
        VantResolver(),
        // icon-pack按需引入
        (ComponentName) => {
          if (ComponentName.startsWith('Icon')) return { name: ComponentName.slice(4), from: '@icon-park/vue-next' };
        }
      ],
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
  // 打包配置
  build: {
    minify: 'esbuild',
    // 构建后是否生成 source map 文件
    sourcemap: false,
    // 规定触发警告的 chunk(文件块) 大小
    chunkSizeWarningLimit: 1500,
    assetsDir: 'static',
    terserOptions: {
      compress: {
        //生产环境时移除console
        drop_console: true,
        drop_debugger: true
      }
    },

    rollupOptions: rollupOptionsConfig
  },
  // 跨域
  server: {
    host: '0.0.0.0',
    port: 7777,
    // 热更新
    hmr: true,
    //自定义代理规则
    proxy: {
      // 本地开发环境通过代理实现跨域，生产环境使用 nginx 转发
      // 正则表达式写法
      '^/api': {
        target: 'http://code.recall.ttop/8888', // 后端服务实际地址
        changeOrigin: true, //开启代理
        configure: (proxy) => {
          // 解决请求403问题：invalid CORS request。非常重要的代码！！
          proxy.on('proxyReq', function (proxyReq) {
            proxyReq.removeHeader('referer'); // 移除请求头
            proxyReq.removeHeader('origin'); // 移除请求头
          });
        },
        // path是请求方法axios配置的baseUr中去除 协议+域名+端口 剩下的部分。例如http://127.0.0.1:9000/api，这里的path就是/api
        rewrite: (path) => path.replace(/^\/api/, '') // 路径重写
      }
    }
  }
});
