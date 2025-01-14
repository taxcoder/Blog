/*
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2024-12-30 02:59:45
 * @LastEditTime: 2025-01-10 08:18:06
 * @FilePath: /code/blog/client-web/src/axios/index.ts
 * @Description:
 * Copyright (c) 2025 by tanxiang, All Rights Reserved.
 */
import axios, { AxiosRequestConfig } from 'axios';
import notify from '@/utils/notifyUtils';
import serverConfig from './server.config';
import qs from 'qs';

axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded;charset=UTF-8';

// 创建 axios 请求实例
const serviceAxios = axios.create({
  baseURL: serverConfig.baseURL, // 基础请求地址
  timeout: serverConfig.timeout, // 请求超时设置
  withCredentials: false // 跨域请求是否需要携带 cookie
});

serviceAxios.interceptors.request.use(
  (config) => {
    // 如果开启 token 认证
    if (serverConfig.useTokenAuthorization) {
      config.headers['Authorization'] = localStorage.getItem('token'); // 请求头携带 token
    }
    // 设置请求头
    if (!config.headers['content-type']) {
      // 如果没有设置请求头
      if (config.method === 'post') {
        config.headers['content-type'] = 'application/x-www-form-urlencoded'; // post 请求
        config.data = qs.stringify(config.data); // 序列化,比如表单数据
      } else {
        config.headers['content-type'] = 'application/json'; // 默认类型
      }
    }
    console.log('请求配置：', config);
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

serviceAxios.interceptors.response.use(
  (response) => {
    console.log('响应配置：', response);
    if (response?.status === 200) {
      let data = response.data;
      return Promise.resolve(data);
    } else {
      return Promise.reject(response);
    }
  },
  (error) => {
    let message = '';
    if (error && error.response) {
      switch (error.response.status) {
        case 302:
          message = '接口重定向了！';
          break;
        case 400:
          message = '参数不正确！';
          break;
        case 401:
          message = '您未登录，或者登录已经超时，请先登录！';
          break;
        case 403:
          message = '您没有权限操作！';
          break;
        case 404:
          message = `请求地址出错, ${error.response.config.url}`;
          break;
        case 408:
          message = '请求超时！';
          break;
        case 409:
          message = '系统已存在相同数据！';
          break;
        case 500:
          message = error.response.data.message || '服务器内部错误！';
          break;
        case 501:
          message = '服务未实现！';
          break;
        case 502:
          message = '网关错误！';
          break;
        case 503:
          message = '服务不可用！';
          break;
        case 504:
          message = '服务暂时无法访问，请稍后再试！';
          break;
        case 505:
          message = 'HTTP 版本不受支持！';
          break;
        default:
          message = '异常问题，请联系管理员！';
          break;
      }
    }
    notify().error(`错误: ${message}`);
    return Promise.reject(error);
  }
);


const request = <ResponseType = unknown>(url: string, options?: AxiosRequestConfig<unknown>): Promise<ResponseType> => {
  return new Promise((resolve, reject) => {
    serviceAxios({
      url,
      ...options
    })
      .then((res) => {
        resolve(res?.data);
      })
      .catch((err) => reject(err));
  });
};
export { serviceAxios, request };
