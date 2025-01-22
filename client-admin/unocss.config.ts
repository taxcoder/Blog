/*
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2025-01-16 05:59:49
 * @LastEditTime: 2025-01-17 06:17:26
 * @FilePath: /code/blog/client-admin/unocss.config.ts
 * @Description:
 * Copyright (c) 2025 by tanxiang, All Rights Reserved.
 */
/*
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2024-12-08 10:05:34
 * @LastEditTime: 2024-12-28 06:13:14
 * @FilePath: /code/blog/client-web/unocss.config.ts
 * @Description:
 * Copyright (c) 2024 by taxcoder, All Rights Reserved.
 */
import { transformerVariantGroup, transformerDirectives, defineConfig, presetMini, presetUno } from 'unocss';

export default defineConfig({
  presets: [presetMini({ dark: undefined }), presetUno()],
  transformers: [transformerDirectives(), transformerVariantGroup()],
  shortcuts: {
    'wh-full': 'w-full h-full',
    'flex-cs': 'flex justify-center items-start',
    'flex-ac': 'flex justify-around items-center',
    'flex-bc': 'flex justify-between items-center',
    'flex-cc': 'flex justify-center items-center',
    'flex-ec': 'flex justify-end items-center',
    'grid-cc': 'grid justify-center items-center',
    'grid-ec': 'grid justify-end items-center'
  },
  theme: {
    fontFamily: {
      default: '寒蝉全圆体, fangsong, sans-serif'
    }
  }
});
