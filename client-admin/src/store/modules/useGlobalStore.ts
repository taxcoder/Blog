/*
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2025-01-16 08:55:56
 * @LastEditTime: 2025-01-17 06:53:31
 * @FilePath: /code/blog/client-admin/src/store/modules/useGlobalStore.ts
 * @Description: 全局配置
 * Copyright (c) 2025 by tanxiang, All Rights Reserved.
 */
import { defineStore } from 'pinia';
let theme = 'auto';

const init = () => {
  const darkMode = localStorage.getItem('dark-mode-enabled');
  if (darkMode === 'true') {
    theme = 'dark';
  } else if (darkMode === 'false') {
    theme = 'light';
  } else {
    theme = 'auto';
  }
};

init();

export const useGlobalStore = defineStore('global', {
  state: () => ({
    // 全局配置
    globalConfig: {
      // 主题色
      themeColor: '#409eff',
      // 主题色
      theme: theme as 'dark' | 'light' | 'auto',
      // 侧边栏是否收起
      sidebarCollapse: false,
      // 侧边栏宽度
      sidebarWidth: 200
    }
  }),
  actions: {
    // 设置全局配置
    setGlobalConfig(config: any) {
      this.globalConfig = { ...this.globalConfig, ...config };
    },
    setTheme(theme: boolean | undefined) {
      this.globalConfig.theme = theme === void 0 ? 'auto' : theme ? 'dark' : 'light';
    }
  },
  getters: {
    // 获取全局配置
    getGlobalConfig: (state) => state.globalConfig,
    getTheme: (state) => state.globalConfig.theme
  },
  persist: true
});
