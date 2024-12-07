/*
 * @Author: tanxiang 1571922819@qq.com
 * @Date: 2023-12-02 12:11:29
 * @Description:
 * @LastEditTime: 2023-12-03 14:44:28
 * @LastEditors: tanxiang 1571922819@qq.com
 * @FilePath: \blog\packages\web-client\uno.config.ts
 * @copyright: Copyright (c) 2023 by 1571922819@qq.com, All Rights Reserved.
 */
// uno.config.ts
import {
  defineConfig,
  presetAttributify,
  presetIcons,
  presetTypography,
  presetUno,
  presetWebFonts,
  transformerDirectives,
  transformerVariantGroup,
} from 'unocss';

export default defineConfig({
  content: {
    filesystem: ['./src/**/*.{html,js,ts,jsx,tsx,vue}'],
  },
  theme: {
    colors: {},
    breakpoints: {
      sm: '400px',
      md: '600px',
      lg: '750px',
      xl: '950px',
      '2xl': '1200px',
    },
  },
  presets: [
    presetUno(),
    presetAttributify(),
    presetIcons(),
    presetTypography(),
    presetWebFonts({
      fonts: {},
    }),
  ],
  transformers: [transformerDirectives(), transformerVariantGroup()],
});
