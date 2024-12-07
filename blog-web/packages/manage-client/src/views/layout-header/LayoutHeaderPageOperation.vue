<template>
  <div class="page-operation">
    <a-button type="text" @click="refresh" class="operation">
      <ReloadOutlined :spin="spin" />
    </a-button>
    <a-dropdown placement="bottomLeft" trigger="click">
      <a-button type="text" class="operation">
        <ApartmentOutlined />
      </a-button>
      <template #overlay>
        <a-menu>
          <a-menu-item @click="refresh">
            <ReloadOutlined />
            重新加载
          </a-menu-item>
          <a-menu-item @click="close">
            <CloseOutlined />
            关闭标签页
          </a-menu-item>
          <a-divider style="margin: 2px 0 2px 0" />
          <a-menu-item @click="closeLeft">
            <VerticalAlignTopOutlined :rotate="270" />
            关闭左侧标签页
          </a-menu-item>
          <a-menu-item @click="closeRight">
            <VerticalAlignTopOutlined :rotate="90" />
            关闭右侧标签页
          </a-menu-item>
          <a-divider style="margin: 2px 0 2px 0" />
          <a-menu-item @click="closeOther">
            <PicCenterOutlined />
            关闭其他标签页
          </a-menu-item>
          <a-menu-item @click="closeAll">
            <LineOutlined />
            关闭所有标签页
          </a-menu-item>
        </a-menu>
      </template>
    </a-dropdown>
    <a-button @click="change()" type="text" class="operation">
      <CompressOutlined v-if="base.getFullScreen" />
      <ExpandOutlined v-else />
    </a-button>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';

import {
  ReloadOutlined,
  ExpandOutlined,
  CompressOutlined,
  LineOutlined,
  VerticalAlignTopOutlined,
  CloseOutlined,
  PicCenterOutlined,
  ApartmentOutlined,
} from '@ant-design/icons-vue';
import { useBaseStore } from '@/stores/base';
import { useRouter, useRoute } from 'vue-router';
import { routerName } from '@/config/routerMenu';

const route = useRoute();
const base = useBaseStore();
const router = useRouter();

const spin = ref<boolean>(false);

const close = () => {
  // 删除的是自己，将自己删除之后，切换路由为第一个标签页的路由
  base.removeClickRouterList(base.getCurrentListIndex);
  base.setCurrentListIndex(0);
  if (base.getClickRouterList.length > 0) router.push(base.getClickRouterList[0].url);
};

const reload = () => {
  spin.value = true;
  setTimeout(() => (spin.value = false), 500);
};

const closeLeft = () => {
  let index = base.getCurrentListIndex;
  if (index === 0) return;
  base.removeClickRouterList(--index);
  base.setCurrentListIndex(index);
  closeLeft();
};

const closeRight = () => {
  let index = base.getCurrentListIndex;
  let count = base.getClickRouterList.length;
  if (index + 1 === count) return;
  base.removeClickRouterList(index + 1);
  closeRight();
};

const closeOther = () => {
  base.initClickRouterList(base.getClickRouterList[base.getCurrentListIndex]);
  base.setCurrentListIndex(0);
};

const closeAll = () => {
  base.initClickRouterList({ name: routerName.home, url: '/' });
  base.setCurrentListIndex(0);
  router.push(base.getClickRouterList[0].url);
};

const change = () => base.setFullScreen(!base.getFullScreen);
// 开启刷新
const refresh = () => {
  reload();
  router.replace({ path: '/refresh', query: { t: Date.now(), f: router.currentRoute.value.path } });
};
</script>

<style scoped>
.page-operation {
  height: 100%;
  padding-right: var(--header-tab-bar-padding-right);
}
</style>

<style>
.anticon {
  vertical-align: text-top;
}

.operation {
  height: var(--header-tab-bar-height);
}

.header-bottom .anticon-spin {
  animation-duration: 0.5s !important;
  animation-timing-function: ease-out !important;
}
</style>