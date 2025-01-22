/*
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2024-12-30 02:58:53
 * @LastEditTime: 2025-01-08 06:11:08
 * @FilePath: /code/blog/client-web/src/mock/index.ts
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

import { article } from './article';
import { category } from './category';
import { tag } from './tag';
import { feeling } from './feeling';
import { topping } from './topping';
import { photo } from './photo';

// 创建一个 MockAdapter 实例
let mock = new MockAdapter(serviceAxios, { delayResponse: 2000 });

const initMock = () => {
  article(mock);
  tag(mock);
  category(mock);
  feeling(mock);
  topping(mock);
  photo(mock);

  mock.onGet('/info').reply(200, {
    data: {
      avatar: 'https://avatars.githubusercontent.com/u/1571922819?v=4',
      strings: ['Hello World!', 'stranger', 'I am a programmer!'],
      title: 'Hi, Stranger !',
      name: 'Shimmer',
      icpRecord: '湘ICP备2023033221号-1',
      policeRecord: '湘公网安备43040002000120号',
      createTime: '2024/12/27',
      articleCount: 150,
      tagCount: 10,
      feelingCount: 100
    }
  });
};

// 导出
export default initMock;
