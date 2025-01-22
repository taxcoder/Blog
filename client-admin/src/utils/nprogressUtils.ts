/*
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2025-01-15 07:41:17
 * @LastEditTime: 2025-01-15 08:02:39
 * @FilePath: /code/blog/client-admin/src/utils/nprogressUtils.ts
 * @Description:
 * Copyright (c) 2025 by tanxiang, All Rights Reserved.
 */
import 'nprogress/nprogress.css';
//@ts-ignore
import NProgress from 'nprogress';

const progress = {
  start() {
    NProgress.start();
  },
  close() {
    NProgress.done();
  }
};

export default progress;
