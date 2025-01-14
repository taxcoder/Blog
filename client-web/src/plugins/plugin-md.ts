/*
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2025-01-13 03:56:10
 * @LastEditTime: 2025-01-13 07:28:56
 * @FilePath: /code/blog/client-web/src/plugins/plugin-md.ts
 * @Description:
 * Copyright (c) 2025 by tanxiang, All Rights Reserved.
 */
import { config } from 'md-editor-v3';
//@ts-ignore
import MarkExtension from 'markdown-it-mark';
import { lineNumbers } from '@codemirror/view';
import { alert } from '@mdit/plugin-alert';
import { imgLazyload } from '@mdit/plugin-img-lazyload';
import { ins } from '@mdit/plugin-ins';

import '@vavt/v3-extension/lib/asset/Mark.css';
import '@mdit/plugin-alert/style';

config({
  markdownItConfig(md) {
    md.use(MarkExtension);
    md.use(alert, { deep: true });
    md.use(imgLazyload);
    md.use(ins);
  },
  codeMirrorExtensions(_theme, extensions) {
    return [...extensions, lineNumbers()];
  },
  markdownItPlugins(plugins) {
    return plugins.map((p) => {
      if (p.type === 'image') return { ...p, options: { ...p.options, classes: 'recall-img' } };
      return p;
    });
  }
});
