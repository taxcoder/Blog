/*
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2025-01-08 03:34:39
 * @LastEditTime: 2025-01-15 02:37:50
 * @FilePath: /code/blog/client-web/src/mock/photo.ts
 * @Description:
 * Copyright (c) 2025 by tanxiang, All Rights Reserved.
 */
import { Photo } from '@/api/photos/photo.d';

let names = ['4K', '动漫', '插画', '雷姆'];

let urls = [
  {
    name: '可爱的小女孩',
    url: 'https://tk.illlt.com:759/api.php?type=%E6%89%8B%E6%9C%BA%E4%BA%8C%E6%AC%A1%E5%85%83'
  },
  {
    name: '水手服女孩',
    url: 'https://tk.illlt.com:759/api.php?type=%E6%89%8B%E6%9C%BA%E4%BA%8C%E6%AC%A1%E5%85%83'
  },
  {
    name: '黄昏下的女孩',
    url: 'https://tk.illlt.com:759/api.php?type=%E7%94%B5%E8%84%91%E7%AB%AF-%E4%BA%8C%E6%AC%A1%E5%85%83/MC%E9%85%B1%E5%8A%A8%E6%BC%AB'
  },
  {
    name: '睡衣雷姆',
    url: 'https://tk.illlt.com:759/api.php?type=%E7%94%B5%E8%84%91%E7%AB%AF-%E4%BA%8C%E6%AC%A1%E5%85%83/%E5%8A%A8%E6%BC%AB%E5%90%88%E9%9B%86%E5%88%86%E5%8C%BA%E5%9B%9B'
  }
];
let photos: Array<Photo> = Array.from({ length: names.length }, (_, i) => {
  return {
    id: i + 1,
    name: names[i],
    images: Array.from({ length: 120 }, (_, i) => ({
      imageId: i + 1,
      name: urls[i % 4].name,
      url: urls[Math.ceil(Math.random() * 4) % 4].url
    }))
  };
});

export const photo = (mock: any) => {
  mock.onGet('/photos').reply(200, {
    data: photos.map((item: Photo) => ({ id: item.id, name: item.name }))
  });

  let arr: Array<{ imageId: number; name: string; url: string }> = [];
  photos.map((item: Photo) => item.images).forEach((item: any) => arr.push(...item));
  for (let i = 1; i <= 4; i++) {
    mock.onGet(`/photos?index=${i}&limit=30`).reply(200, {
      data: {
        list: arr.slice((i - 1) * 30, i * 30),
        total: arr.length
      }
    });
  }

  for (let i = 0; i <= 3; i++) {
    mock.onGet(`/photos/${photos[i].id}?index=1&limit=30`).reply(200, {
      data: {
        list: photos[i].images.slice(0, 30),
        total: photos[i].images.length
      }
    });
    mock.onGet(`/photos/${photos[i].id}?index=2&limit=30`).reply(200, {
      data: {
        list: photos[i].images.slice(30, 60),
        total: photos[i].images.length
      }
    });
    mock.onGet(`/photos/${photos[i].id}?index=3&limit=30`).reply(200, {
      data: {
        list: photos[i].images.slice(60, 90),
        total: photos[i].images.length
      }
    });
    mock.onGet(`/photos/${photos[i].id}?index=4&limit=30`).reply(200, {
      data: {
        list: photos[i].images.slice(90, 120),
        total: photos[i].images.length
      }
    });
  }
};
