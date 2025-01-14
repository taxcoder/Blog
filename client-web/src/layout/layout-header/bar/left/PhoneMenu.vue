<!--
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2024-12-25 10:38:31
 * @LastEditTime: 2025-01-14 07:04:53
 * @FilePath: /code/blog/client-web/src/layout/layout-header/bar/left/PhoneMenu.vue
 * @Description: 
 * Copyright (c) 2024 by tanxiang, All Rights Reserved. 
-->

<template>
  <div class="wh-full">
    <el-drawer
      v-model="show"
      size="80%"
      :with-header="false"
      :append-to-body="true"
      direction="ltr"
      :style="[setStyle]"
    >
      <div class="wh-full flex flex-col">
        <!-- 头像 -->
        <div class="w-full flex-cc h-[8.5rem] pt-[1.05rem]">
          <el-avatar :size="100" :src="global.getInfo.avatar"></el-avatar>
        </div>
        <!-- 名字 -->
        <div class="w-full flex-cc h-[1.75rem] font-size-[1.1rem] dark:color-[#999999] color-[#606266]">
          {{ global.getInfo.name }}
        </div>
        <!-- 打字机 -->
        <div class="w-full flex-cc h-[1.75rem] dark:color-[#999999] color-[#606266]">
          <vuetyped
            class="font-size-[0.85rem]"
            :strings="global.getInfo.strings"
            :showCursor="true"
            :typeSpeed="150"
            :loop="true"
          >
            <div class="typing"></div>
          </vuetyped>
        </div>
        <!-- 信息 -->
        <div class="w-full px-[1.25rem] py-[0.5rem] box-border h-[7rem]">
          <div class="site-info wh-full rounded-[0.45rem] dark:bg-[#232323] bg-[#fff] grid grid-rows-3 py-[0.15rem]">
            <div v-for="item in info" :key="item.id" class="flex justify-start items-center pl-[0.625rem]">
              <auto-icon-park
                class="relative top-[0.0625rem]"
                :name="item.icon"
                :fill="global.getTheme ? item.fill : item.darkFill"
                :theme="item.theme"
                :size="item.size"
              >
              </auto-icon-park>
              <div class="pl-[0.625rem] font-size-[0.8rem] color-[#606266] dark:color-[#999999]">
                <span>{{ item.prefix }}</span>
                <span class="color-orange">{{ item.count }}</span>
                <span>{{ item.suffix }}</span>
              </div>
            </div>
          </div>
        </div>
        <!-- 菜单 -->
        <div class="w-full flex-auto py-[0.5rem] px-[1.25rem] box-border">
          <layout-header-bar-left-phone-menu-item></layout-header-bar-left-phone-menu-item>
        </div>
        <!-- 版权 -->
        <div class="copyright w-full h-[2.5rem] flex-cc font-size-[0.8rem] color-[#9f9f9f]">
          <span class="font-size-[1rem] mr-[0.125rem]">©</span> {{ beforePresent }} {{ global.getInfo.name }}
        </div>
      </div>
    </el-drawer>
    <div class="flex-cc wh-full" @click="show = true">
      <icon-hamburger-button theme="filled" size="1.75rem" fill="#f5a623" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { Editor, Tag, Sunny } from '@icon-park/vue-next';
const global = useGlobalStore();
const show = ref<boolean>(false);
const isPhone = inject('isPhone') as Ref<boolean>;

provide('changeDrawer', (status: boolean) => {
  show.value = status;
});

const info = reactive([
  {
    id: 'article',
    icon: markRaw(Editor),
    size: '0.95rem',
    fill: '#606266',
    darkFill: '#999999',
    prefix: `累计撰写 `,
    count: global.info.articleCount ? global.info.articleCount : 0,
    suffix: ' 篇文章',
    theme: 'outline'
  },
  {
    id: 'tag',
    icon: markRaw(Tag),
    size: '0.95rem',
    fill: '#606266',
    darkFill: '#999999',
    prefix: `累计创建 `,
    count: global.info.tagCount ? global.info.tagCount : 0,
    suffix: ' 个标签',
    theme: 'outline'
  },
  {
    id: 'feeling',
    icon: markRaw(Sunny),
    size: '0.95rem',
    fill: '#606266',
    darkFill: '#999999',
    prefix: `累计发表 `,
    count: global.info.feelingCount ? global.info.feelingCount : 0,
    suffix: ' 篇心情',
    theme: 'outline'
  }
]);

const referInfoData = (item: any, array: { article: number; tag: number; feeling: number }) => {
  switch (item.id) {
    case 'article':
      return (item.count = array.article);
    case 'tag':
      return (item.count = array.tag);
    case 'comment':
      return (item.count = array.feeling);
  }
};

const beforePresent = computed(() => {
  let create = new Date(global.getInfo.createTime).getFullYear();
  let current = new Date().getFullYear();
  return create == current ? create : `${create}-${current}`;
});

const setStyle = computed(() => {
  return {
    '--el-drawer-padding-primary': '0',
    '--el-bg-color': global.getTheme ? '#f2f6fc' : '#141414'
  };
});

watch(
  () => [global.info.feelingCount, global.info.articleCount, global.info.tagCount],
  ([feeling, article, tag]) => info.forEach((item) => referInfoData(item, { article, tag, feeling })),
  { deep: true }
);
</script>

<style lang="css" scoped>
.copyright {
  box-shadow: 0px 0px 2px #ccc;
}

.el-drawer__body {
  padding: 0;
}

.site-info {
  box-shadow: 0 0px 10px -5px #949494;
}

.dark .site-info {
  box-shadow: 1px 1px 3px 1px #1b1b1b;
}
</style>
