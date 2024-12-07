<!--
 * @Author: tanxiang 1571922819@qq.com
 * @Date: 2023-06-11 19:46:30
 * @Description:
 * @LastEditTime: 2023-12-04 21:45:06
 * @LastEditors: tanxiang 1571922819@qq.com
 * @FilePath: \blog\packages\web-client\src\common\search\BlogSearch.vue
 * @copyright: Copyright (c) 2023 by 1571922819@qq.com, All Rights Reserved.
-->
<template>
  <div
    v-show="juiceIsSearchHidden"
    id="blog-search"
    class="py-[20px] px-[20px]"
    :class="{ 'tag-search': tabs[index].name === 'tag' }"
  >
    <el-tabs v-model="active" @tab-click="conTabClick" :stretch="true">
      <el-tab-pane v-for="tab in tabs" :key="tab.key" :label="tab.label" :name="tab.name">
        <div v-show="tabs[index].name === 'article'" class="flex justify-between z-9999 relative">
          <div class="flex-1 text-center">
            <span @click="clickStatusSort" class="cursor-pointer">{{ statusSortName }}</span>
            <i class="iconfont rotate-180 icon-arrow-up"></i>
          </div>
          <div class="flex-1 text-center">
            <span @click="clickTimeSort" class="cursor-pointer">{{ timeSortName }}</span>
            <i class="iconfont rotate-180 icon-arrow-up"></i>
          </div>
        </div>
        <div
          class="min-h-[100px] dis-scroll relative overflow-auto mt-[15px]"
          :class="[setSearchHeight, { 'overflow-hidden': sort }]"
          id="search-data-info"
        >
          <div v-if="active === 'article'" :class="['h-7/10', { 'flex-center': articleSearchData.length === 0 }]">
            <article-data-info
              :searchData="articleSearchData"
              :search-height="setSearchHeight"
              :current="articleCurrent"
              @init-data="articleInitData"
              :loading="articleLoading"
              @clearSearchData="clearArticleSearchData"
            />
          </div>
          <div v-else :class="['h-7/10', { 'flex-center': tagSearchData.length === 0 }]">
            <tag-data-info
              :searchData="tagSearchData"
              :search-height="setSearchHeight"
              :current="tagCurrent"
              @init-data="tagInitData"
              :loading="tagLoading"
              @clearSearchData="clearTagSearchData"
            />
          </div>
          <change-search-info
            :time-sort="timeSort"
            :status-sort="statusSort"
            :is-status-sort="isStatusSort"
            @change-status-sort="changeStatusSort"
            @change-time-sort="changeTimeSort"
          />
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup lang="ts">
import { useGlobalStore } from '@/stores/global';
import { useSearch } from '@tanxiang/apis';

import { messageInfo } from '@tanxiang/common';

import TagDataInfo from '@/common/search/TagDataInfo.vue';
import ArticleDataInfo from '@/common/search/ArticleDataInfo.vue';
import ChangeSearchInfo from '@/common/search/ChangeSearchInfo.vue';
import { Ref } from 'vue';

const { messageError, messageLoading, messageSuccess } = messageInfo();
const global = useGlobalStore();
const active = ref<string>('article');
const search = useSearch();

const timer = ref<any>(null);
const initTimer = ref<any>(null);
const tagInitTimer = ref<any>(null);
const isStatusSort = ref<boolean>(true);
const sort = ref<boolean>(false);
const index = ref<number>(0);

const articleCurrent = ref<number>(1);
const tagCurrent = ref<number>(1);
const articleLoading = ref<boolean>(true);
const tagLoading = ref<boolean>(true);

const articleSearchData = reactive<any[]>([]);
const tagSearchData = reactive<any[]>([]);

const status = reactive<{ width: number; height: number }>({
  width: 0,
  height: 0,
});

const tabs = reactive<{ key: number; label: string; name: string }[]>([
  {
    key: 1,
    label: '文章',
    name: 'article',
  },
  {
    key: 2,
    label: '标签',
    name: 'tag',
  },
]);

const statusSort = reactive<{ name: string; active: boolean; key: number }[]>([
  {
    name: '综合排序',
    active: true,
    key: -1,
  },
  {
    name: '最新优先',
    active: false,
    key: 0,
  },
  {
    name: '最热优先',
    active: false,
    key: -1,
  },
]);

const timeSort = reactive<{ name: string; active: boolean; key: number }[]>([
  {
    name: '时间不限',
    active: true,
    key: -1,
  },
  {
    name: '最近一天',
    active: false,
    key: 1,
  },
  {
    name: '最近一周',
    active: false,
    key: 7,
  },
  {
    name: '最近三月',
    active: false,
    key: 90,
  },
]);

const statusSortName = ref<string>(statusSort[0].name);
const timeSortName = ref<string>(timeSort[0].name);
const sortNumber = ref<number>(statusSort[0].key);
const timeNumber = ref<number>(timeSort[0].key);

provide('sort', sort);

onMounted(() => {
  window.addEventListener('resize', onResize);
  onResize();
});

const onResize = () => {
  status.width = window.innerWidth;
  status.height = window.innerHeight;
};

const conTabClick = (tabs: any) => (index.value = tabs.index);

const setStatusSortIsActive = (sort: boolean) => {
  if (timer.value !== null) clearTimeout(timer.value);
  timer.value = setTimeout(() => (isStatusSort.value = sort), 100);
};

const clickStatusSort = () => {
  sort.value = !sort.value;
  setStatusSortIsActive(true);
};

const clickTimeSort = () => {
  sort.value = !sort.value;
  setStatusSortIsActive(false);
};

const changeStatusSort = ({ index, key }: { index: number; key: number }) => {
  sortNumber.value = key;
  updateArticleList(index, statusSort, statusSortName);
};

const changeTimeSort = ({ index, key }: { index: number; key: number }) => {
  timeNumber.value = key;
  updateArticleList(index, timeSort, timeSortName);
};

const updateArticleList = (index: number, sortStatus: any[], name: Ref<string>) => {
  messageLoading('请求数据中');
  search
    .articleSearch(global.getSearchInput, sortNumber.value, timeNumber.value, 1)
    .then((success: any) => {
      sortStatus.forEach((item: { name: string; active: boolean }) => (item.active = false));
      sortStatus[index].active = true;
      name.value = sortStatus[index].name;
      sort.value = false;
      articleSearchData.splice(0, articleSearchData.length, ...success.data.records);
      messageSuccess('请求成功！');
    })
    .catch((error: any) => messageError(!error || error.name ? '查询失败' : error));
};

const init = (api: Promise<Function>, data: any[], current: Ref<number>, fn: Function) => {
  api
    .then((success: any) => {
      if (!success) return;
      data.push(...success.data.records);
      if (fn) fn(success.data);
    })
    .catch((error: any) => messageError(!error || error.name ? '查询失败' : error))
    .finally(() => {
      articleLoading.value = false;
      tagLoading.value = false;
    });
};

const clearArticleSearchData = () => {
  articleLoading.value = true;
  articleSearchData.splice(0, articleSearchData.length);
};

const clearTagSearchData = () => {
  tagLoading.value = true;
  tagSearchData.splice(0, tagSearchData.length);
};

const articleInitData = ({ page, fn }: { page: number; fn: Function }) => {
  articleCurrent.value = page;
  if (page === 1) {
    clearArticleSearchData();
  }
  if (initTimer.value) clearTimeout(initTimer.value);
  initTimer.value = setTimeout(() => {
    if (global.getSearchInput.length > 0) {
      init(
        search.articleSearch(global.getSearchInput, sortNumber.value, timeNumber.value, page),
        articleSearchData,
        articleCurrent,
        fn
      );
    }
  }, 500);
};

const tagInitData = ({ page, fn }: { page: number; fn: Function }) => {
  tagCurrent.value = page;
  if (page === 1) {
    clearTagSearchData();
  }
  if (tagInitTimer.value) clearTimeout(tagInitTimer.value);
  tagInitTimer.value = setTimeout(() => {
    if (global.getSearchInput.length > 0) {
      init(search.tagSearch(global.getSearchInput, page), tagSearchData, tagCurrent, fn);
    }
  }, 500);
};

const juiceIsSearchHidden = computed(() => {
  return global.getSearchInput && global.getSearchInput.length > 0;
});

const setSearchHeight = computed(() => {
  return status.width === 0 || status.width > 800 ? 'search-height' : 'search-height-all';
});
</script>

<style scoped>
#blog-search {
  overflow: hidden;
  scrollbar-width: none;
  -ms-overflow-style: none;
}

#blog-search::-webkit-scrollbar {
  display: none;
}
</style>

<style>
.warp ~ .is-vertical > div {
  background-color: #828282;
  opacity: 0.5;
  width: 5px;
}

.search-message-box {
  min-height: 100px;
}

.search-message-box .el-message-box__content {
  height: calc(100% - 53px);
  padding-left: 0 !important;
  padding-right: 0 !important;
}

.search-message-box .el-message-box__content .el-message-box__container,
.search-message-box .el-message-box__content .el-message-box__container .el-message-box__message {
  height: 100%;
}

@media screen and (min-width: 800px) {
  .search-message-box {
    min-width: 800px !important;
  }
}

@media screen and (max-width: 800px) {
  .search-message-box {
    max-height: 100% !important;
    height: 100% !important;
    width: 100% !important;
    min-width: 100% !important;
  }

  .el-overlay-message-box {
    padding: 0 !important;
  }
}
</style>