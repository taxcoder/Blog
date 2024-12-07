/*
 * @Author: tanxiang 1571922819@qq.com
 * @Date: 2023-06-18 08:54:28
 * @Description:
 * @LastEditTime: 2023-12-02 20:45:57
 * @LastEditors: tanxiang 1571922819@qq.com
 * @FilePath: \blog\utils\src\day.ts
 * @copyright: Copyright (c) 2023 by 1571922819@qq.com, All Rights Reserved.
 */
const useDate = () => {
  const current = () => {
    return new Date().getTime();
  };

  const days = (time: number) => {
    let residue: number = current() - time;
    return Math.floor(residue / (1000 * 60 * 60 * 24));
  };

  const headway = (time: number, second: boolean = true, isDisabled: boolean = false) => {
    let residue: number = current() - time;
    // 年
    let yearDate: number = residue / (1000 * 60 * 60 * 24 * 365);
    // 天
    let dayDate: number = (yearDate - Math.floor(yearDate)) * 365;
    // 时
    let hourData: number = (dayDate - Math.floor(dayDate)) * 24;
    // 分
    let minuteData: number = (hourData - Math.floor(hourData)) * 60;

    let times: { key: number; prefix: string }[] = [
      { key: Math.floor(yearDate), prefix: '年' },
      { key: Math.floor(dayDate), prefix: '天' },
      { key: Math.floor(hourData), prefix: '时' },
      { key: Math.floor(minuteData), prefix: '分钟' },
    ];

    if (second) {
      //秒
      let seconds: number = (minuteData - Math.floor(minuteData)) * 60;
      times.push({ key: Math.floor(seconds), prefix: '秒' });
    }

    let size: number = 0;
    let date: string = '';
    while (size < times.length) {
      let key: number = times[size].key;
      let flag: boolean = key < 10 && size !== 0 && !isDisabled;
      date =
        date +
        (key !== 0
          ? (flag ? '0' + key : key) + times[size].prefix
          : size === times.length - 1
          ? '00' + times[size].prefix
          : '');

      if (isDisabled && date !== '') break;
      size++;
    }
    return date;
  };

  const headwayBoard = (time: number) => {
    let residue: number = current() - time;
    // 年
    let yearDate: number = residue / (1000 * 60 * 60 * 24 * 365);
    // 天
    let dayDate: number = (yearDate - Math.floor(yearDate)) * 365;

    return Math.floor(yearDate) === 0
      ? Math.floor(dayDate) + '天'
      : Math.floor(yearDate) + '年' + Math.floor(dayDate) + '天';
  };

  const year = (time: number) => {
    return new Date(time).getFullYear();
  };

  const addPrefix = (time: string | number) => {
    return time.toString().length < 2 ? '0' + time : time;
  };

  const shortTime = (time: string, separate: string = '-', all: boolean = false, seconds = true) => {
    let date = new Date(time);
    return all
      ? date.getFullYear() +
          separate +
          (date.getMonth() + 1) +
          separate +
          date.getDate() +
          ' ' +
          addPrefix(date.getHours()) +
          ':' +
          addPrefix(date.getMinutes()) +
          (seconds ? ':' + addPrefix(date.getSeconds()) : '')
      : date.getFullYear() + separate + (date.getMonth() + 1) + separate + date.getDate();
  };

  const chineseTime = (time: string) => {
    let date = new Date(time);
    return date.getFullYear() + '年' + date.getMonth() + '月' + date.getDate() + '日';
  };
  return { current, headway, days, shortTime, year, chineseTime, headwayBoard };
};

export default useDate;