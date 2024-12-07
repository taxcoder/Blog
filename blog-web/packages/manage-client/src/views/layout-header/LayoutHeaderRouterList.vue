<template>
  <a-tabs
    :activeKey="base.currentListIndex"
    type="editable-card"
    size="small"
    hide-add
    class="tabs-transform"
    :tabBarStyle="tabBarStyle"
    @tabClick="click"
    tabPosition="top"
    @edit="tabEdit"
  >
    <a-tab-pane :key="index" :tab="item.name" v-for="(item, index) in base.getClickRouterList" />
  </a-tabs>
</template>

<script setup lang="ts">
import { reactive, ref, watch, watchEffect } from 'vue';
//@ts-ignore
import { useBaseStore } from '@/stores/base';
import { useRoute, useRouter } from 'vue-router';
import { routerName } from '@/config/routerMenu';

const route = useRoute();
const router = useRouter();
const index = ref<number>(0);

const tabBarStyle = reactive({ color: '#121212', padding: '0 15px' });

const base = useBaseStore();

const click = (index: any) => router.push(base.getClickRouterList[index].url);

const tabEdit = (action: any, event: any) => {
  let index = base.getCurrentListIndex;
  // 如果删除操作，并且删除的不是自己，直接删除即可
  if (event === 'remove' && action !== index) {
    base.removeClickRouterList(action);
    base.setCurrentListIndex(index > action ? index - 1 : index);
    return;
  }

  if (event === 'remove' && action === index && base.getClickRouterList.length === 1) {
    router.push('/');
    base.removeClickRouterList(action);
    base.setCurrentListIndex(0);
    return;
  }

  // 如果是删除操作，并且删除的会是自己，将自己删除之后，切换路由为第一个标签页的路由
  if (event === 'remove' && action === index) {
    base.removeClickRouterList(action);
    base.setCurrentListIndex(0);
    router.push(base.getClickRouterList.slice(0, 1)[0].url);
  }
};

watchEffect(() => {
  // 每次路由发生变换的时候，将激活的标签页切换
  base.getClickRouterList.forEach((list: any, index: number) => {
    if (list.name === route.name && base.getCurrentListIndex !== index) return base.setCurrentListIndex(index);
    if (route.matched.length >= 3 && list.name === route.matched[1].name && base.getCurrentListIndex !== index)
      return base.setCurrentListIndex(index);
  });
});

watch(
  () => base.getClickRouterList,
  (list: any) => {
    if (list.length === 0) base.pushClickRouterList({ name: routerName.home, url: '/' });
  },
  { deep: true }
);
</script>

<style>
.ant-layout-header .header-bottom .ant-tabs-nav {
  height: 100%;
  box-sizing: border-box;
  padding: 0 0 0 var(--header-tab-bar-padding-left) !important;
}

.ant-tabs-nav:before {
  border-bottom: none !important;
}

.ant-tabs-tab {
  border-bottom: none !important;
}

.ant-layout-header .ant-tabs {
  width: calc(100% - calc(var(--header-tab-bar-height) * 3) - var(--header-tab-bar-padding-right));
}

.ant-layout-header .ant-tabs .ant-tabs-tab-remove {
  color: #121212;
  transition: color 0.6s ease-in-out;
}

.ant-layout-header .ant-tabs-tab-active {
  background-color: #1677ff !important;
}

.ant-tabs-tab-remove {
  margin-left: 3px !important;
}

.ant-layout-header .ant-tabs-tab-active .ant-tabs-tab-remove {
  transition: color 0.2s ease-in;

  color: white;
}

.ant-layout-header .ant-tabs-tab-active:hover .ant-tabs-tab-remove {
  color: white !important;
}

.ant-layout-header .ant-tabs-tab-with-remove:hover .ant-tabs-tab-remove {
  color: #4096ff;
}
</style>