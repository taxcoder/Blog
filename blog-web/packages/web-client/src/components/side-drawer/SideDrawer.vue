<template>
  <el-drawer
    :model-value="juiceIsDrawer"
    :append-to-body="true"
    size="75%"
    @close="close"
    direction="ltr"
    :show-close="false"
    :with-header="false"
  >
    <img :src="getWebInfo.headIcon" class="h-[155px] w-full absolute z-0 object-cover top-0" alt="bg" />
    <div class="drawer-user-info h-full w-full relative z-9999 pt-[100px] px-[15px] pb-0">
      <div class="flex flex-col items-center">
        <img :src="getWebInfo.headIcon" class="rounded-full w-[95px]" alt="head" />
        <p class="text-[1.025rem] px-0 pt-[14px] pb-[10px]">
          {{ getWebInfo.userName }}
        </p>
        <p
          class="text-center text-[0.75rem] px-[30px] py-0 leading-[20px] text-[#606266] dark:text-[#c3c3c3] mb-[10px]"
        >
          {{ getWebInfo.motto }}
        </p>
      </div>
      <div class="flex flex-col items-start mt-[5px]">
        <p
          v-for="item in info"
          key="item.id"
          class="text-[#606266] dark:text-[#c3c3c3] py-[7px] text-[0.8rem] flex items-center h-[15px]"
        >
          <color-icon :icon-class="`icon-A_${item.icon}`" padding="0 5px 0 0" />
          <span v-text="item.prefix === '' ? getWebInfo.userName : item.prefix"></span>
          <strong class="px-[5px] text-[#73aaff] font-medium" v-text="getContent(item)" />
          <span v-text="item.suffix"></span>
        </p>
      </div>
      <div class="drawer-user-info__menu pt-[15px]">
        <el-collapse :accordion="true" v-model="activeCollapse">
          <el-collapse-item
            v-for="(item, index) in routerTabs(false)"
            :key="item.key"
            :name="index"
            @click="clickCollapse(item)"
          >
            <template v-slot:title>
              <color-icon :icon-class="item.iconName" padding="0 8px 0 0" :size="15" :top="-1" />
              <span :class="{ 'text-[#73aaff]': active === index }">{{ getTitleName(item, index) }}</span>
            </template>
            <p class="py-[5px] px-[35px]" v-for="i in item.options" :key="i.key" @click="navToUrl(i)">
              {{ i.label }}
            </p>
          </el-collapse-item>
        </el-collapse>
      </div>
    </div>
  </el-drawer>
</template>

<script setup lang="ts">
import { useGlobalStore } from '@/stores/global';

import { routerTabs } from '@/components/home-page/nav';
import { ColorIcon } from '@tanxiang/common';

import type { WebSite } from '@/interface/WebSite';

import { useDate } from '@tanxiang/utils';
import Router from '@/router';

const router: Router = useRouter();
const { headway } = useDate();
const global = useGlobalStore();
const activeCollapse = ref<number>(0);
const active = ref<number>(-1);

const info = reactive([
  {
    id: 1,
    icon: 89,
    prefix: '',
    suffix: '在线',
    content: '',
  },
  {
    id: 2,
    icon: 70,
    prefix: '累计撰写',
    suffix: '篇文章',
    content: '文章',
  },
  {
    id: 3,
    icon: 67,
    prefix: '累计添加',
    suffix: '个标签',
    content: '标签',
  },
  {
    id: 4,
    icon: 65,
    prefix: '累计表达',
    suffix: '次心情',
    content: '说说',
  },
]);

const close = () => global.setIsOpenDrawer(false);
// 点击折叠菜单
const clickCollapse = (item: any) => {
  active.value = activeCollapse.value === 0 ? 0 : activeCollapse.value;
  if (!item.url) return;
  if (router.currentRoute.value.path === item.url) return global.setIsOpenDrawer(false);
  router.push(item.url);
};

const getContent = computed(() => (item: any) => {
  return item.content === ''
    ? getLoginTime.value
    : getWebInfo.value.count.find((i: any) => i.name === item.content).num;
});
// 跳转到指定的url
const navToUrl = (item: any) => {
  if (router.currentRoute.value.path !== item.url) router.push(item.url);
  global.setIsOpenDrawer(false);
};
// 判断是否在线
const juiceIsNotActive = computed(() => new Date().getTime() - getWebInfo.value.loginUpdateTime <= 1000 * 60 * 60);
// 获取登录时间
const getLoginTime = computed(() => {
  return !juiceIsNotActive.value
    ? headway(getWebInfo.value.loginUpdateTime + 1000 * 60 * 60, false, true) + '前'
    : '当前';
});
// 判断是否打开抽屉
const juiceIsDrawer = computed(() => global.getIsOpenDrawer);
// 获取标题的名称
const getTitleName = computed(() => (item: any, index) => {
  nextTick(() => {
    if (item.options.length !== 0) return;
    let collapse = document.getElementsByClassName('el-collapse-item')[index];
    collapse.getElementsByClassName('el-collapse-item__wrap')[0].innerHTML = '';
    collapse.getElementsByClassName('el-collapse-item__arrow')[0].innerHTML = '';
  });
  return item.name;
});

const getWebInfo = computed((): WebSite => global.getWebSite);
</script>

<style scoped></style>

<style>
.drawer-user-info__menu .el-collapse-item__header {
  height: 40px;
}

.drawer-user-info__menu .el-collapse,
.drawer-user-info__menu .el-collapse-item__header,
.drawer-user-info__menu .el-collapse-item__wrap {
  border: none !important;
}

.drawer-user-info__menu .el-collapse-item__content {
  padding-bottom: 0 !important;
}
</style>