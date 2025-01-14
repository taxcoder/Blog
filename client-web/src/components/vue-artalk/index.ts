/*
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2025-01-05 10:26:09
 * @LastEditTime: 2025-01-07 09:22:43
 * @FilePath: /code/blog/client-web/src/components/vue-artalk/index.ts
 * @Description:
 * Copyright (c) 2025 by tanxiang, All Rights Reserved.
 */
import Artalk from 'artalk';

const init = (el: Ref<any>, theme: boolean) => {
  return Artalk.init({
    el: el.value,
    server: 'http://code.recall.top:9999',
    pageTitle: document.title,
    site: 'Shimmer 的博客',
    darkMode: theme,
    countEl: '.artalk-comment-count'
  });
};

const loadCountWidget = () => {
  Artalk.loadCountWidget({
    server: 'http://code.recall.top:9999',
    site: 'Shimmer 的博客',
    pvEl: '.artalk-pv-count',
    countEl: '.artalk-comment-count',
    statPageKeyAttr: 'data-page-key'
  });
};

export default Artalk;
export { init, loadCountWidget };
