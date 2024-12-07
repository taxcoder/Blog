import md5 from 'js-md5';
// @ts-ignore
import { service } from './http';
// @ts-ignore
import { salt } from '../index';
import * as stream from 'stream';

const useLogin = () => {
  const login = async (user: { email: string; password: string; remember: boolean }) => {
    //@ts-ignore
    user.password = md5(salt + user.password);
    return await service.post('/api/login', user);
  };

  const webLogin = async (user: { username: string; password: string }) => {
    const userInfo = {
      email: user.username,
      password: user.password,
    };

    //@ts-ignore
    userInfo.password = md5(salt + userInfo.password);
    return await service.post('/api/web/login', userInfo);
  };

  // 判断token是否有效，如果存在token则请求，不存在则删除本地存储里面的虚假token
  const isLogin = async () => {
    return await service.post('/admin/isLogin');
  };

  const logout = async (email: string) => {
    return await service.post(`/auth/logout/${email}`);
  };

  const webLogout = async (username: string) => {
    return await service.post(`/admin/logout/${username}`);
  };

  return { login, logout, webLogin, isLogin, webLogout };
};

export { useLogin };