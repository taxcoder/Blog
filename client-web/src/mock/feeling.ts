/*
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2025-01-04 15:41:10
 * @LastEditTime: 2025-01-15 02:37:02
 * @FilePath: /code/blog/client-web/src/mock/feeling.ts
 * @Description:
 * Copyright (c) 2025 by tanxiang, All Rights Reserved.
 */

import { Feeling } from '@/api/feelings/feeling.d';

const feelings: { allCount: number; list: Array<Feeling> } = {
  allCount: 12,
  list: []
};

for (let i = 1; i <= feelings.allCount; i++) {
  let images: any = [];
  // images = new Array(Math.ceil(Math.random() * 9)).fill(
  //   'https://img2.baidu.com/it/u=3214599870,4242692442&fm=253&fmt=auto&app=120&f=JPEG?w=501&h=500'
  // );

  let videos: any = '';
  // videos = 'https://lixingyong.com/upload/snow-in-chengdu-2024-01.mp4';

  feelings.list.push({
    id: i,
    author: 'Shimmer',
    headImg: 'https://img2.baidu.com/it/u=3214599870,4242692442&fm=253&fmt=auto&app=120&f=JPEG?w=501&h=500',
    content:
      '承诺的 1.3.3 版本终于发版了，尽管有些晚了，但好在也算完成了。这里说一下主题后续开发方向吧。<br>由于工作原因，之后将没有太多的时间去维护主题，但我心里一直有一个想要完善的主题，现在的主题还没有达成我的要求，所以我还会慢慢进行更新。',
    province: '广东',
    city: '深圳',
    imagesUrl: images,
    videosUrl: videos,
    likeCount: Math.floor(Math.random() * 100),
    createTime: 1704041350 + Math.floor(Math.random() * (1735920000 - 1704041350))
  });
}

export const feeling = (mock: any) => {
  for (let i = 1; i <= Math.ceil(feelings.allCount / 10); i++) {
    mock.onGet(`/feelings?index=${i}&limit=10`).reply(200, {
      data: {
        allCount: feelings.allCount,
        list: feelings.list.slice((i - 1) * 10, i * 10)
      }
    });
  }

  for (let i = 1; i <= feelings.list.length; i++) {
    mock.onGet(`/feelings/${i}`).reply(200, {
      data: feelings.list.find((item: any) => item.id == i)
    });
  }

  mock.onPost('/feelings/1/like').reply(200, {
    data: '点赞成功！'
  });
  mock.onPost('/feelings/2/like').reply(200, {
    data: '点赞成功！'
  });
  mock.onPost('/feelings/3/like').reply(200, {
    data: '点赞成功！'
  });
  mock.onPost('/feelings/4/like').reply(200, {
    data: '点赞成功！'
  });
  mock.onPost('/feelings/5/like').reply(200, {
    data: '点赞成功！'
  });
  mock.onPost('/feelings/6/like').reply(200, {
    data: '点赞成功！'
  });
  mock.onPost('/feelings/7/like').reply(200, {
    data: '点赞成功！'
  });
};
