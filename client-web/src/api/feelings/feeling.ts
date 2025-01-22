/*
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2025-01-04 15:30:08
 * @LastEditTime: 2025-01-15 02:31:31
 * @FilePath: /code/blog/client-web/src/api/feelings/feeling.ts
 * @Description:
 * Copyright (c) 2025 by tanxiang, All Rights Reserved.
 */
import { request } from '@/axios';
import { Feeling } from './feeling.d';
// 获取日记列表
export const getFeelings = async (index: number, limit: number) => {
  return request<{ allCount: number; list: Array<Feeling> }>(`/feelings?index=${index}&limit=${limit}`);
};

export const getFeeling = async (feelingId: number) => {
  return request<Feeling>(`/feelings/${feelingId}`);
};
// 给日记点赞
export const addFeelingLikeCount = async (feelingId: number) => {
  return request<Feeling>(`/feelings/${feelingId}/like`, {
    method: 'POST'
  }).then((res) => {
    // 如果点赞成功，给当前的日记ID加入cookie，表示当前网页用户已经点赞过了
    const [state, setState] = useCookieState('feeling_like');
    try {
      if (!state.value) {
        setState(JSON.stringify([feelingId]));
      } else {
        //@ts-ignore
        const feelingLike = JSON.parse(state.value);
        if (!feelingLike.includes(feelingId)) {
          feelingLike.push(feelingId);
          setState(JSON.stringify(feelingLike));
        }
      }
    } catch (error) {
      setState(JSON.stringify([feelingId]));
    }
    return Promise.resolve(res);
  });
};
