/*
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2025-01-14 03:43:19
 * @LastEditTime: 2025-01-14 03:44:23
 * @FilePath: /code/blog/client-web/src/utils/nprogressUtils.ts
 * @Description:
 * Copyright (c) 2025 by tanxiang, All Rights Reserved.
 */
import NProgress from 'nprogress';
//全局进度条的配置
NProgress.configure({
  easing: 'ease', // 动画方式
  speed: 300, // 递增进度条的速度
  showSpinner: false, // 是否显示加载ico
  trickleSpeed: 200, //自动递增间隔
  minimum: 0.3, // 更改启动时使用的最小百分比
  parent: 'body' //指定进度条的父容器
});
// 打开进度条
export const start = () => {
  NProgress.start();
};
// 关闭进度条
export const close = () => {
  NProgress.done();
};
