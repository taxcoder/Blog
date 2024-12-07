/*
 * @Author: tanxiang 1571922819@qq.com
 * @Date: 2023-11-24 10:07:16
 * @Description:
 * @LastEditTime: 2023-12-02 20:07:40
 * @LastEditors: tanxiang 1571922819@qq.com
 * @FilePath: \blog\packages\web-client\src\interface\WebSite.ts
 * @copyright: Copyright (c) 2023 by 1571922819@qq.com, All Rights Reserved.
 */
export interface WebSite {
  // 网站ID
  id: string;
  // 网站名称
  name: string;
  // 头像（base64）
  headIcon: string;
  // 作者
  userName: string;
  // 格言，副标题
  motto: string;
  // 网站备案号
  forTheRecord: string;
  // 公告栏
  bulletinBoard: string;
  // 总访问量
  totalVisits: number;
  // 公安备案号
  publicSecurityRegistrationNumber: string;
  // 上次登录时间
  loginUpdateTime: number;
  // 标签
  tags: { id: number; name: string }[];
  // 总文字数
  totalTextQuantity: number;
  // 创建时间
  createTime: number;
  // 数量
  count: { name: string; num: number }[];
  // 文本打印
  text: string[];
  // 背景图片
  backgroundImage: string;
}
