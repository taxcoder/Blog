/*
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2025-01-06 13:28:33
 * @LastEditTime: 2025-01-11 08:55:57
 * @FilePath: /code/blog/client-web/src/utils/notifyUtils.ts
 * @Description:
 * Copyright (c) 2025 by tanxiang, All Rights Reserved.
 */
import { Loading } from 'vant';
import { ElMessage } from 'element-plus';

const messageLoading = ref<boolean>(false);

const notifyUtils: any = () => {
  const success = (message: string, duration = 3000) => {
    messageLoading.value = false;
    ElMessage.success({ message: message, duration: duration, plain: true });
  };

  const error = (message: string, duration = 3000) => {
    messageLoading.value = false;
    ElMessage.error({ message: message, duration: duration, plain: true });
  };

  const loading = (message: string, duration = 0) => {
    messageLoading.value = true;
    ElMessage.info({
      message: h('div', { class: 'flex-cc' }, [
        h('span', { class: 'font-size-[0.85rem]', style: 'margin-right: 0.4375rem; color: #868686' }, message),
        h(Loading, { size: '1.05rem' })
      ]),
      duration: duration,
      plain: true
    });
  };

  watch(
    () => messageLoading.value,
    (newVal: boolean) => {
      if (!newVal) ElMessage.closeAll();
    }
  );

  return { success, error, loading, messageLoading };
};

export default notifyUtils;
