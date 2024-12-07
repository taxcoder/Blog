import axios, { AxiosInstance, AxiosResponse, InternalAxiosRequestConfig } from 'axios';

// @ts-ignore
import base from './env/index';

const service: AxiosInstance = axios.create({
  //@ts-ignore
  baseURL: base.BASE_URL,
  timeout: base.TIME_OUT, // 请求超时时间
  withCredentials: true,
});
// 请求列表
let requestList: any[] = [];

/**
 * 阻止请求列表
 * @param requestList 请求列表
 * @param url 请求地址
 * @param cancel 关闭请求
 * @param errorMessage 错误信息
 */
const stopRepeatRequest = (requestList: any[], url: string, cancel: Function, errorMessage: string) => {
  const errorMsg = errorMessage || '请不要重复提交';
  if (requestList.some((item: any) => item.url === url)) {
    cancel(errorMsg);
  } else {
    requestList.push({ url });
  }
};
/**
 * 允许某个请求可以继续
 * @param requestList 请求列表
 * @param url 请求地址
 */
const allowRequest = (requestList: any[], url: string) => {
  for (let i = 0; i < requestList.length; i++) {
    if (requestList[i].url === url) {
      requestList.splice(i, 1);
      break;
    }
  }
};

// request拦截器
service.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    let cancel: Function | null = null;
    config.cancelToken = new axios.CancelToken((c) => (cancel = c));

    if (!config.url || !cancel) return config;
    // 如果请求的url和取消函数都存在，则阻止重复请求
    stopRepeatRequest(requestList, config.url, cancel, `${config.url}请求被中断`);
    // 如果请求的url是以/api开头，则不做任何处理
    if (config.url.startsWith('/api/')) return config;
    // 如果请求的url不是以/api开头，则给请求头里面加上token
    config.headers.Authorization = localStorage.getItem('token');
    return config;
  },
  (error) => {
    return error;
  }
);

// response 拦截器
service.interceptors.response.use(
  (response: AxiosResponse | any) => {
    // 增加延迟，防止请求过快
    setTimeout(() => allowRequest(requestList, response.config.url), 1000);

    if (response.status === 403 || response.data.code === 403) {
      location.reload();
      localStorage.removeItem('token');
      return;
    }

    if (response.status === 200 && response.data.code === 200) return response.data;
    return Promise.reject(
      response.data && response.data.data ? response.data : response.data ? response.data.message : null
    );
  },
  (error: any) => {
    if (axios.isCancel(error)) {
      console.log(error.message);
    } else {
      // 增加延迟，防止请求过快
      setTimeout(() => allowRequest(requestList, error.config.url), 1000);
    }
  }
);

export { axios as GlobalAxios, service };