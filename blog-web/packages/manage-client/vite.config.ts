import vue from '@vitejs/plugin-vue';
import Unocss from 'unocss/vite';
import AutoImport from 'unplugin-auto-import/vite';
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers';
import Components from 'unplugin-vue-components/vite';
import { defineConfig } from 'vite';
import viteCompression from 'vite-plugin-compression';
import legacy from '@vitejs/plugin-legacy';

//@ts-ignore
import { fileURLToPath } from 'url';
//@ts-ignore
import currentPackage from './package.json';
//@ts-ignore
import parentPackage from '../../package.json';

const dependencies = currentPackage.dependencies;
const globalDependencies = parentPackage.dependencies;

// @ts-nocheck
const config = defineConfig({
  base: '/',
  plugins: [
    vue(),
    Unocss({
      configFile: './uno.config.ts',
    }),
    legacy({ targets: ['defaults', 'not IE 11'] }),
    viteCompression({
      deleteOriginFile: false,
      algorithm: 'gzip',
      ext: '.gz',
      threshold: 10240,
    }),
    AutoImport({
      imports: ['vue', 'vue-router', '@vueuse/core'],
      resolvers: [ElementPlusResolver()],
      dts: './auto-imports.d.ts',
    }),
    Components({
      dts: true,
      dirs: ['src/components', 'src/common', 'src/stores', 'src/hooks'],
      resolvers: [ElementPlusResolver()],
    }),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url)),
    },
  },
  server: {
    port: 5138,
  },
  build: {
    chunkSizeWarningLimit: 500,
    minify: 'terser',
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
        manualChunks: (id) => {
          if (id.includes('node_modules')) {
            // 指定需要拆分的第三方库或模块
            const dependenciesKeys = Object.keys(dependencies);
            const globalDependenciesKeys = Object.keys(globalDependencies);
            dependenciesKeys.push(...globalDependenciesKeys);
            const match = dependenciesKeys.find((item) => {
              return id.includes(item);
            });
            console.info('match', match);
            const notSplit = ['vue', 'ant-design-vue', 'element-plus'];
            if (match && !notSplit.includes(match)) {
              return match;
            }
          }
        },
      },
    },
  },
});

export default config;