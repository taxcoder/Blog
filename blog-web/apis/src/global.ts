import md5 from 'js-md5';
// @ts-ignore
import { GlobalAxios, service } from './http';
// @ts-ignore
import { salt } from '../index';

const web = async () => {
  return await service.get(`/api/init`);
};

const text = async () => {
  return await service.get(`/api/text`);
};

const count = async () => {
  return await service.get(`/api/count`);
};

const init = () => {
  return GlobalAxios.all([web(), text(), count()]).then((success: any) => {
    let web: any = success[0];
    web['text'] = success[1];
    web['count'] = success[2];
    return new Promise((resolve) => {
      resolve(web);
    });
  });
};

const useAuthWebStation = (base: string = 'auth') => {
  const updateWebStation = async (obj: any) => {
    return await service.put(`/${base}/web/update/info`, obj);
  };

  const updateAdminStation = async (obj: any) => {
    //@ts-ignore
    obj.oldPassword = md5(salt + obj.oldPassword);
    return await service.put(`/${base}/admin/update/info`, obj);
  };

  const updateAdminPassword = async (obj: any) => {
    let data = new FormData();
    // @ts-ignore
    data.append('password', md5(salt + obj.password));
    // @ts-ignore
    data.append('oldPassword', md5(salt + obj.oldPassword));
    return await service.put(`/${base}/admin/update/password`, data);
  };

  const loginNameInfo = async () => {
    return await service.get(`/${base}/admin/get/info`);
  };

  const webStationInfo = async () => {
    return web();
  };

  const weather = async () => {
    return await service.get(`/${base}/future/weather/info`);
  };

  const updatePrintText = async (obj: { id: number; content: string }[]) => {
    return await service.put(`/${base}/print/update/info`, obj);
  };
  return {
    updateWebStation,
    webStationInfo,
    loginNameInfo,
    updateAdminStation,
    weather,
    updatePrintText,
    updateAdminPassword,
  };
};

export { count, init, text, useAuthWebStation, web };