/*
 * @Author: tanxiang 1571922819@qq.com
 * @Date: 2023-11-24 13:46:27
 * @Description:
 * @LastEditTime: 2023-12-03 14:15:54
 * @LastEditors: tanxiang 1571922819@qq.com
 * @FilePath: \blog\packages\web-client\src\interface\base.ts
 * @copyright: Copyright (c) 2023 by 1571922819@qq.com, All Rights Reserved.
 */
export interface Status {
  width: number;
  height: number;
  scroll: number;
}

export interface MenuDirectory {
  label: string;
  level: string;
  children?: MenuDirectory[];
}
