/*
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2025-01-15 08:36:37
 * @LastEditTime: 2025-01-16 08:59:11
 * @FilePath: /code/blog/client-admin/src/store/index.ts
 * @Description:
 * Copyright (c) 2025 by tanxiang, All Rights Reserved.
 */
import { createPinia } from 'pinia';
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate';

const pinia = createPinia();
// 使用持久化插件
pinia.use(piniaPluginPersistedstate);

export default pinia;
