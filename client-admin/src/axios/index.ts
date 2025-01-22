/*
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2024-12-30 02:59:45
 * @LastEditTime: 2025-01-16 08:28:51
 * @FilePath: /code/blog/client-admin/src/axios/index.ts
 * @Description:
 * Copyright (c) 2025 by tanxiang, All Rights Reserved.
 */
import axios, { AxiosRequestConfig } from 'axios';
import serverConfig from './server.config';
//@ts-ignore
import qs from 'qs';

axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded;charset=UTF-8';

// 创建 axios 请求实例
const serviceAxios = axios.create({
  baseURL: serverConfig.baseURL,
  timeout: serverConfig.timeout,
  withCredentials: serverConfig.withCredentials
});

serviceAxios.interceptors.request.use(
  (config) => {
    // 如果开启 token 认证，设置请求头携带token
    if (serverConfig.useTokenAuthorization) config.headers['Authorization'] = localStorage.getItem('token');
    if (!config.headers['content-type'] && config.method === 'post') {
      // 如果类型请求头不存在，并且请求类型为POST，设置请求头为application/x-www-form-urlencoded
      config.headers['content-type'] = 'application/x-www-form-urlencoded';
      config.data = qs.stringify(config.data);
    } else if (!config.headers['content-type'] && config.method !== 'post') {
      // 如果类型请求头不存在，并且请求类型不是POST，设置请求头为application/json
      config.headers['content-type'] = 'application/json';
    }
    // 如果是本地环境，就显示请求配置信息
    if (import.meta.env.VITE_NODE_ENV === 'development') console.log('请求配置：', config);
    return config;
  },
  (error) => Promise.reject(error)
);

serviceAxios.interceptors.response.use(
  (response) => {
    // 如果是本地开发环境，显示响应配置信息
    if (import.meta.env.VITE_NODE_ENV === 'development') console.log('响应配置：', response);
    return response?.status === 200 ? Promise.resolve(response.data) : Promise.reject(response);
  },
  (error) => {
    let message = '';
    if (error && error.response) {
      switch (error.response.status) {
        case 400:
          message = '参数不正确';
          break;
        case 401:
          message = '身份信息已过期✿';
          break;
        case 403:
          message = '您没有权限操作';
          break;
        case 404:
          message = `请求地址出错, ${error.response.config.url}`;
          break;
        case 408:
          message = '请求超时';
          break;
        case 500:
          message = error.response.data.message || '服务器内部错误！';
          break;
        default:
          message = '异常问题，请联系管理员！';
          break;
      }
      return Promise.reject(message);
    }
    return Promise.reject(error);
  }
);

const request = <ResponseType = unknown>(url: string, options?: AxiosRequestConfig<unknown>): Promise<ResponseType> => {
  return new Promise((resolve, reject) => {
    serviceAxios({
      url,
      ...options
    })
      .then((res) => resolve(res?.data))
      .catch((err) => reject(err));
  });
};
export { serviceAxios, request };
