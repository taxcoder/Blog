/*
 * @Author: tanxiang 1571922819@qq.com
 * @Date: 2023-08-04 16:53:59
 * @Description:
 * @LastEditTime: 2023-12-01 20:11:06
 * @LastEditors: tanxiang 1571922819@qq.com
 * @FilePath: \blog\utils\src\page.mts
 * @copyright: Copyright (c) 2023 by ${git_name_email}, All Rights Reserved.
 */
const usePage = () => {
  const getOffsetTop = (el: any): number => {
    if (el.offsetParent) return getOffsetTop(el.offsetParent) + el.offsetTop;
    else return el.offsetTop;
  };

  const device = (): boolean => {
    const userAgent = navigator.userAgent.toLowerCase();
    return /ipad|iphone|midp|rv:1.2.3.4|ucweb|android|windows ce|windows mobile/.test(userAgent);
  };

  const realWidth = (): number => {
    return device() ? window.outerWidth : window.innerWidth;
  };

  return { getOffsetTop, device, realWidth };
};

export default usePage;