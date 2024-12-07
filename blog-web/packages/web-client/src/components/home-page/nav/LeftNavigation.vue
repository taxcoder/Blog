<template>
  <n-tabs
    v-model:value="tabName"
    :animated="true"
    :bar-width="0"
    class="common-nav-tabs"
    size="large"
    trigger="hover"
    type="line"
  >
    <template #prefix>
      <span
        class="title text-[25px] cursor-pointer text-white mr-[10px] ml-[20px] transition-font-size"
        :class="[{ 'important-text-[#000000]': props.isActive }, { 'text-[0.4rem]': !juiceIsComputer }]"
        v-text="global.getWebSite.name"
        @click="router.push('/list/article')"
      />
    </template>
    <template #default>
      <n-tab
        v-if="juiceIsComputer"
        v-for="(item, index) in routerTabs()"
        :name="item.name"
        class="group"
        @mouseenter="onMouse('enter', index)"
        @mouseleave="onMouse('leave', index)"
      >
        <n-dropdown
          :keyboard="false"
          :options="item.options"
          :style="juiceIsEmptyOptions(item.options) ? dropdown : {}"
          class="custom-dropdown"
          placement="top"
          @mouseenter="onMouse('enter', index)"
          @mouseleave="onMouse('leave', index)"
          @select="onSelect(index, $event)"
        >
          <div class="w-[80px] flex-center">
            <div v-if="index === 0" class="router-tab-bar"></div>
            <i :class="item.iconName" class="iconfont" />
            <span class="nav-options" @click="item['url'] ? navToRouter(item['url']) : void 0" v-text="item.name" />
            <i
              v-if="juiceIsEmptyOptions(item.options)"
              class="iconfont rotate-90 group-hover:rotate-180 icon-arrow-up"
            />
          </div>
        </n-dropdown>
      </n-tab>
    </template>
  </n-tabs>
</template>

<script setup lang="ts">
import { useBaseStore } from '@/stores/base';
import { useGlobalStore } from '@/stores/global';
import { useRouter, useRoute } from 'vue-router';

import { minWidth } from '@/config';
import type { ItemOptions } from '@/types/navigation-options';
import { routerTabs, tabNames } from './index';

import { usePage } from '@tanxiang/utils';

const router: any = useRouter();
const route: any = useRoute();
const base = useBaseStore();
const global = useGlobalStore();

// tab标签的名称
const tabName = ref(tabNames[base.getCurrent]);
// dropdown的样式
let dropdown = { maxWidth: '500px', width: '80px' };
const popoverRef = ref<any>(null);

const status: any = inject('status');

const props = defineProps({
  isActive: { type: Boolean, required: true },
});

const onMouse = (status: string, index: number) => {
  if (status === 'leave') {
    document.querySelector('.router-tab-bar').classList.add('slow');
    updateRouterBarPosition(getCurrent.value);
  } else {
    document.querySelector('.router-tab-bar').classList.remove('slow');
    updateRouterBarPosition(index);
  }
};
// 获取当前元素在routerTabs里面的索引位置
const getCurrent = computed(() => {
  return routerTabs()
    .map((item: any, index: number) => {
      if (item.options.length === 0 && item.url === route.path) return index;
      // 判斷当前路由是否再有子元素的tab里面
      let option = item.options.find((optionKey: ItemOptions) => optionKey.url === route.path);
      if (item.options.length > 0 && option) return index;
    })
    .filter((f) => f !== undefined)[0];
});

// 选中的tab标签，进行路由切换路由
const onSelect = (index: number, key: string) => {
  let container = document.querySelectorAll('.v-binder-follower-container');
  for (let i = 0; i < container.length; i++) {
    container[i].style.display = 'none';
  }
  //@ts-ignore
  let path: any = routerTabs()[index].options.find((optionKey: ItemOptions) => optionKey.key === key).url;
  navToRouter(path);
  base.setCurrent(index);
};
// 判断是否是空属性
const juiceIsEmptyOptions = (p: ItemOptions[]): Boolean => p.length !== 0;
// 切换到其他的路由
const navToRouter = (url: string | undefined) => {
  // 只有存在url才会进行跳转
  if (!!url && url.startsWith('/')) router.replace(url);
};

const updateRouterBarPosition = (index: number) => {
  let wrapper: any = document.querySelector('.router-tab-bar');
  if (!wrapper) return;
  let position = index * 100 - 5;
  let properties = `translateX(${position < 0 ? 0 : position}px)`;
  // //@ts-ignore
  wrapper.style.setProperty('--bar-position', properties);
};

// 判断是否是电脑
const juiceIsComputer = computed(() => {
  return status.width > minWidth && !usePage().device();
});

watchEffect(() => {
  if (route.path === '/list/article') base.setCurrent(0);
});

watch(
  () => route.path,
  () =>
    nextTick(() => {
      updateRouterBarPosition(getCurrent.value);
      setTimeout(() => {
        let container = document.querySelectorAll('.v-binder-follower-container');
        for (let i = 0; i < container.length; i++) {
          container[i].style.display = 'block';
        }
      }, 400);
    })
);

watch(
  () => juiceIsComputer.value,
  (newValue: boolean) => {
    // 如果导航栏隐藏就进行处理
    if (!newValue) return;
    nextTick(() => updateRouterBarPosition(getCurrent.value));
  },
  { immediate: true }
);
</script>

<style scoped>
@import '@/components/home-page/index.css';
</style>

<style>
.n-tabs-tab-wrapper {
  min-width: 80px;
}

.router-tab-bar {
  --bar-width: 80px !important;
  --bar-position: translateX(0px);
}

.group {
  position: relative;
}

.router-tab-bar.slow {
  transition-delay: 0.25s;
}

.router-tab-bar {
  position: absolute;
  background-image: var(--active-button-bg-img-light) !important;
  border-radius: 50px !important;
  bottom: 8px !important;
  height: 3px !important;
  max-width: var(--bar-width) !important;
  width: var(--bar-width) !important;
  transform: var(--bar-position);
  transition: transform 0.15s linear;
}
</style>