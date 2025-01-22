/*
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2024-12-30 02:58:53
 * @LastEditTime: 2025-01-16 06:44:16
 * @FilePath: /code/blog/client-admin/src/mock/index.ts
 * @Description:
 * Copyright (c) 2025 by tanxiang, All Rights Reserved.
 */
/*
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2024-12-30 02:58:53
 * @LastEditTime: 2025-01-04 08:10:40
 * @FilePath: /code/blog/client-web/src/mock/index.ts
 * @Description:
 * Copyright (c) 2024 by tanxiang, All Rights Reserved.
 */
import MockAdapter from 'axios-mock-adapter';
import { serviceAxios } from '@/axios';

import { user } from './user';

// 创建一个 MockAdapter 实例
let mock = new MockAdapter(serviceAxios, { delayResponse: 2000 });

const initMock = () => {
  user(mock);
};

// 导出
export default initMock;
