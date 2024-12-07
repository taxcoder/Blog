// @ts-ignore
import vue from '@vitejs/plugin-vue';
// @ts-ignore
import { defineConfig } from 'vite';

/** @type {import('vite').UserConfig} */
export default defineConfig({
  plugins: [vue()],
  resolve: {
    extensions: ['.mjs', '.js', '', '.jsx', 'x', '.json'],
  },
});