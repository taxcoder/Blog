// @ts-ignore
import { service } from './http';

export const useTarget = (base: string = 'api') => {
  return {
    // 获取所有的目标
    getTargets: async (year: number) => {
      return await service.get(`${base}/list/target/${year}`);
    },
  };
};

export const useAuthTarget = (base: string = 'auth') => {
  return {
    // 创建目标
    createTarget: async (data: string, year: number) => {
      let form = new FormData();
      form.append('content', data);
      form.append('year', year.toString());
      return await service.post(`${base}/target/add`, form);
    },
    // 更新目标内容
    updateTargetContent: async (id: number, data: string) => {
      let form = new FormData();
      form.append('content', data);
      return await service.put(`${base}/target/update/content/${id}`, form);
    },
    // 更新目标状态
    updateTargetStatus: async (id: number, data: boolean) => {
      let form = new FormData();
      form.append('status', data.toString());
      return await service.put(`${base}/target/update/status/${id}`, form);
    },
    // 删除一个目标
    deleteTarget: async (id: number) => {
      return await service.delete(`${base}/target/delete/${id}`);
    },
  };
};