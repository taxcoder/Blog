/*
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2025-01-16 06:41:04
 * @LastEditTime: 2025-01-17 03:17:40
 * @FilePath: /code/blog/client-admin/src/mock/user.ts
 * @Description:
 * Copyright (c) 2025 by tanxiang, All Rights Reserved.
 */

const info = {
  id: 1,
  name: 'Shimmer',
  email: '2185067708@qq.com',
  avatar: 'https://img0.baidu.com/it/u=3488237667,417868856&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500',
  level: 0
};

export const user = (mock: any) => {
  mock.onGet('/auth/info').reply(200, {
    data: info
  });

  mock.onPost('/auth/login').reply(200, {
    data: {
      info,
      token: 'si7ILs1rmSL3uS56L6bB447XToQWlc9COBktiY0qdh9dShPRggVUL4hJtlaAAIb4'
    }
  });
};
