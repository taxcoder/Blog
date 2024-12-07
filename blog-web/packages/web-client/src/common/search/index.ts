/*
 * @Author: tanxiang 1571922819@qq.com
 * @Date: 2023-06-12 22:07:14
 * @Description:
 * @LastEditTime: 2023-12-03 21:52:34
 * @LastEditors: tanxiang 1571922819@qq.com
 * @FilePath: \blog\packages\web-client\src\common\search\index.ts
 * @copyright: Copyright (c) 2023 by 1571922819@qq.com, All Rights Reserved.
 */
// @ts-ignore
import { ElMessageBox } from 'element-plus';

import BlogSearch from '@/common/search/BlogSearch.vue';
import SearchInput from '@/common/search/SearchInput.vue';

const open = () => {
  ElMessageBox({
    title: '搜索',
    showConfirmButton: false,
    closeOnClickModal: true,
    closeOnPressEscape: false,
    lockScroll: true,
    customClass: 'search-message-box',
    callback: () => {},
    message: () => h('div', { style: { height: '100%' } }, [h(SearchInput), h(BlogSearch)]),
  });
};

export { open };
