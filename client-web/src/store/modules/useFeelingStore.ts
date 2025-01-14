/*
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2024-12-31 09:48:22
 * @LastEditTime: 2025-01-07 03:41:57
 * @FilePath: /code/blog/client-web/src/store/modules/useFeelingStore.ts
 * @Description:
 * Copyright (c) 2024 by tanxiang, All Rights Reserved.
 */

import { Feeling } from '@/api/feelings/feeling.d';
import { defineStore } from 'pinia';

const deleteRepeat = (arr: any) => {
  let map = new Map();
  for (let item of arr) {
    if (!map.has(item.id)) map.set(item.id, item);
  }
  return [...map.values()];
};

export const useFeelingStore = defineStore('feeling', {
  state: () => ({
    feelings: {
      allCount: 0,
      list: [] as Array<Feeling>
    }
  }),
  getters: {
    getFeelings: (state) => {
      return state.feelings;
    }
  },
  actions: {
    setFeelings(feelings: { allCount: number; list: Feeling[] }) {
      this.feelings.allCount = feelings.allCount;
      this.feelings.list.push(...feelings.list);
    }
  }
});
