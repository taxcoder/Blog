/*
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2025-01-04 15:29:59
 * @LastEditTime: 2025-01-05 10:20:33
 * @FilePath: /code/blog/client-web/src/api/feelings/feeling.d.ts
 * @Description:
 * Copyright (c) 2025 by tanxiang, All Rights Reserved.
 */
export type Feeling = {
  id: number;
  author: string;
  headImg: string;
  content: string;
  province: string;
  city: string;
  imagesUrl: Array<string>;
  videosUrl: string;
  likeCount: number;
  createTime: number;
};
