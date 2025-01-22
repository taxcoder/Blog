/*
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2025-01-15 08:53:33
 * @LastEditTime: 2025-01-16 09:02:38
 * @FilePath: /code/blog/client-admin/src/utils/notifyUtils.ts
 * @Description:
 * Copyright (c) 2025 by tanxiang, All Rights Reserved.
 */
import { createDiscreteApi } from 'naive-ui';

enum MessageType {
  success = 'success',
  error = 'error',
  warning = 'warning',
  info = 'info',
  loading = 'loading'
}

const { message } = createDiscreteApi(['message']);

const notify = {
  success: (msg: string, duration = 2500) => notifyCommon(MessageType.success, msg, duration),
  error: (msg: string, duration = 2500) => notifyCommon(MessageType.error, msg, duration),
  info: (msg: string, duration = 2500) => notifyCommon(MessageType.info, msg, duration),
  warning: (msg: string, duration = 2500) => notifyCommon(MessageType.warning, msg, duration),
  loading: (msg: string, duration = 3000000) => notifyCommon(MessageType.loading, msg, duration)
};
/**
 *
 * @param type 消息类型
 * @param msg 消息内容
 * @param duration 持续时间
 */
const notifyCommon = (type: MessageType, msg: string, duration: number = 3000) => {
  // 先销毁已经存在的message
  message.destroyAll();
  // 创建新的message
  message[type](() => h('span', { style: { color: `var(--n-icon-color-${type})` } }, msg), {
    duration: duration,
    keepAliveOnHover: true
  });
};

export default notify;
