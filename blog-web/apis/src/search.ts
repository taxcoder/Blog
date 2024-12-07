/*
 * @Author: tanxiang 1571922819@qq.com
 * @Date: 2023-09-30 18:23:37
 * @Description:
 * @LastEditTime: 2023-12-04 21:33:18
 * @LastEditors: tanxiang 1571922819@qq.com
 * @FilePath: \blog\apis\src\search.ts
 * @copyright: Copyright (c) 2023 by 1571922819@qq.com, All Rights Reserved.
 */
// @ts-ignore
import { useAuthArticle } from './article';
// @ts-ignore
import { useAuthClassification } from './classification';
// @ts-ignore
import { useAuthEssay, useEssay } from './essay';
// @ts-ignore
import { service } from './http';
// @ts-ignore
import { useAuthTag } from './tag';
import { useAuthImages } from './images.js';
import { useAuthClassificationNav } from './classification-nav.js';
//@ts-ignore
export const useSearch = (base: string = 'api') => {
  const tableSearch = async (condition: string, searchValue: any, current: number, size: number) => {
    return await service.get(`/${base}/search/${condition}/${searchValue}?current=${current}&size=${size}`);
  };

  const articleSearch = async (value: string, sort: number, time: number, current: number): Promise<any> => {
    return await service.get(`/${base}/search/article/list/${value}?s=${sort}&t=${time}&c=${current}`);
  };

  const tagSearch = async (value: string, current: number) => {
    return await service.get(`/${base}/search/tag/list/${value}?c=${current}`);
  };

  return { tableSearch, articleSearch, tagSearch };
};

export const useAuthSearch = (base: string = 'auth') => {
  const tableSearch = async (tableName: string, condition: string, searchValue: any, current: number, size: number) => {
    if (typeof searchValue === 'object') {
      switch (tableName) {
        case 'images':
          return await useAuthImages().searchImageTime(current, size, searchValue.start, searchValue.end);
        default:
          break;
      }
    } else if (!searchValue || searchValue === '' || searchValue.length === 0) {
      switch (tableName) {
        case 'classification':
          return await useAuthClassification().classificationLimit(1, 15);
        case 'tag':
          return await useAuthTag().tagLimit(1, 15);
        case 'essay':
          return await useEssay().essayList(1, 15);
        case 'article':
          return await useAuthArticle().articleList(1, 15);
        case 'recovery/article':
          return await useAuthArticle().recoveryArticleList(1, 15);
        case 'recovery/essay':
          return await useAuthEssay().essayRecoveryList(1, 15);
        case 'images':
          return await useAuthImages().imagesList(1, 15);
        case 'classificationNavigation':
          return await useAuthClassificationNav().classificationNavList(1, 15);
        default:
          break;
      }
    } else {
      return await service.get(
        `/${base}/${tableName}/search/${condition}/${searchValue}?current=${current}&size=${size}`
      );
    }
  };
  return { tableSearch };
};