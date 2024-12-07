/*
 * @Author: tanxiang 1571922819@qq.com
 * @Date: 2023-06-10 14:03:03
 * @Description:
 * @LastEditTime: 2023-12-01 23:43:25
 * @LastEditors: tanxiang 1571922819@qq.com
 * @FilePath: \blog\common\env.d.ts
 * @copyright: Copyright (c) 2023 by 1571922819@qq.com, All Rights Reserved.
 */
declare module '*.vue' {
  import { ComponentOptions } from 'vue';
  const component: ComponentOptions;
  export default component;
}