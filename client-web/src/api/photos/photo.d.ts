/*
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2025-01-08 03:31:43
 * @LastEditTime: 2025-01-08 05:45:51
 * @FilePath: /code/blog/client-web/src/api/photos/photo.d.ts
 * @Description:
 * Copyright (c) 2025 by tanxiang, All Rights Reserved.
 */
export type Photo = {
  id: number;
  name: string;
  images: Array<{
    imageId: number;
    name: string;
    url: string;
  }>;
};
