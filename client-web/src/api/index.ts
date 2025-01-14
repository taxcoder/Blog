/*
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2024-12-30 04:04:18
 * @LastEditTime: 2025-01-10 03:24:32
 * @FilePath: /code/blog/client-web/src/api/index.ts
 * @Description:
 * Copyright (c) 2024 by tanxiang, All Rights Reserved.
 */
import {
  getArticles,
  getArticlesCreateTime,
  getArticlesByCreateTime,
  getArticleById,
  addArticleLikeCount
} from './articles/article';
import { getWebInfo } from './info/info';
import { getToppings } from './topping/topping';
import { addFeelingLikeCount, getFeeling, getFeelings } from './feelings/feeling';
import { getPhotoById, getPhotoInit, getPhotosResult } from './photos/photo';
import { getTags, getArticleByTagId } from './tags/tag';
import { getArticleByCategoryId, getCategorys } from './categorys/category';

const http = {
  getArticles,
  getArticlesCreateTime,
  getArticlesByCreateTime,
  getWebInfo,
  getToppings,
  addFeelingLikeCount,
  getFeeling,
  getFeelings,
  getPhotoById,
  getPhotoInit,
  getPhotosResult,
  getTags,
  getArticleByTagId,
  getCategorys,
  getArticleByCategoryId,
  getArticleById,
  addArticleLikeCount
};

export default http;
