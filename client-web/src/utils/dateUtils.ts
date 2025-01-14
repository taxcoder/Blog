/*
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2025-01-11 03:03:28
 * @LastEditTime: 2025-01-13 08:32:09
 * @FilePath: /code/blog/client-web/src/utils/dateUtils.ts
 * @Description:
 * Copyright (c) 2025 by tanxiang, All Rights Reserved.
 */
const dateUtils = {
  format: (date: Date | number, format = 'YYYY-MM-DD') => {
    if (typeof date === 'number' && date == 0) {
      return '0000-00-00';
    }

    if (!['Date', 'number'].includes(typeof date)) {
      throw new Error(`dateUtils format: 参数类型传入错误，只支持Date和number类型！你传入的类型为${typeof date}`);
    }

    let dateInfo: Date;
    if (typeof date === 'number' && date.toString().length === 10) {
      dateInfo = new Date(date * 1000);
    } else if (typeof date === 'number' && date.toString().length === 13) {
      dateInfo = new Date(date);
    } else if (typeof date === 'number') {
      throw new Error(`dateUtils format: 参数传入错误！`);
    } else {
      dateInfo = date;
    }

    const o: any = {
      // 将传进来的date对象转换成 YYYY-MM-DD格式
      YYYY: dateInfo.getFullYear(),
      MM: dateInfo.getMonth() + 1,
      DD: dateInfo.getDate(),
      hh: dateInfo.getHours(),
      mm: dateInfo.getMinutes(),
      ss: dateInfo.getSeconds(),
      A:
        dateInfo.getHours() <= 12 && dateInfo.getHours() >= 8
          ? '上午'
          : dateInfo.getHours() <= 18 && dateInfo.getHours() > 12
            ? '下午'
            : '晚上'
    };

    return format.replace(/(YYYY|MM|DD|hh|mm|ss|A)/g, (match: string) => o[match]);
  }
};

export default dateUtils;
