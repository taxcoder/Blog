import 'md-editor-v3/lib/style.css';

import { config } from 'md-editor-v3';

import screenfull from 'screenfull';

import katex from 'katex';
import 'katex/dist/katex.min.css';

import Cropper from 'cropperjs';
import 'cropperjs/dist/cropper.css';

import mermaid from 'mermaid';

import highlight from 'highlight.js';
import 'highlight.js/styles/atom-one-dark.css';

import mark from 'markdown-it-mark';
import * as prettier from 'prettier';

import { lineNumbers } from '@codemirror/view';

import anchor from 'markdown-it-anchor';

config({
  editorConfig: {
    renderDelay: 0,
  },
  codeMirrorExtensions(_theme, extensions) {
    return [...extensions, lineNumbers()];
  },
  markdownItConfig(edit) {
    edit.use(mark, {
      permalink: true,
    });
    edit.use(anchor, {
      permalink: true,
    });
  },
  editorExtensions: {
    prettier: {
      prettierInstance: prettier,
    },
    screenfull: {
      instance: screenfull,
    },
    highlight: {
      instance: highlight,
    },
    katex: {
      instance: katex,
    },
    cropper: {
      instance: Cropper,
    },
    mermaid: {
      instance: mermaid,
    },
  },
});
