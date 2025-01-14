/*
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2024-12-30 03:56:53
 * @LastEditTime: 2025-01-14 07:12:54
 * @FilePath: /code/blog/client-web/src/axios/server.config.ts
 * @Description:
 * Copyright (c) 2024 by tanxiang, All Rights Reserved.
 */

const serverConfig = {
  baseURL: import.meta.env.VITE_APP_API_BASE_URL, // 请求基础地址,可根据环境自定义
  useTokenAuthorization: true, // 是否开启 token 认证
  timeout: 5000 // 请求超时时间
};
export default serverConfig;
