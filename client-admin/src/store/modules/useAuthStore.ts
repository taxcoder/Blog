/*
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2025-01-16 06:16:48
 * @LastEditTime: 2025-01-17 03:34:47
 * @FilePath: /code/blog/client-admin/src/store/modules/useAuthStore.ts
 * @Description: 用户管理
 * Copyright (c) 2025 by tanxiang, All Rights Reserved.
 */
import { User } from '@/api/user/user.d';
import { defineStore } from 'pinia';

export const useAuthStore = defineStore('auth', {
  state: () => ({
    userInfo: {} as User
  }),
  getters: {
    getUserInfo: (state) => state.userInfo
  },
  actions: {
    setUserInfo(userInfo: any) {
      this.userInfo.name = userInfo.name;
      this.userInfo.avatar = userInfo.avatar;
      this.userInfo.level = userInfo.level;
      this.userInfo.id = userInfo.id;
      this.userInfo.email = userInfo.email;
    }
  },
  persist: true
});
