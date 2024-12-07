<template>
  <a-layout-sider
    breakpoint="md"
    :collapsed="!base.getOpen"
    :collapsedWidth="base.getBreakpoint ? 0 : 60"
    :trigger="null"
    :width="Data.siderWidth"
    @breakpoint="onBreakpoint"
    :style="{ overflow: 'hidden' }"
  >
    <div class="web-title" @click="router.push('/')">
      <svg class="icon important-w-[28px]" aria-hidden="true">
        <use xlink:href="#icon-tiantianquan"></use>
      </svg>
      <span>Light Admin</span>
    </div>
    <a-menu
      v-model:selectedKeys="state.selectedKeys"
      v-model:openKeys="state.openKeys"
      theme="dark"
      mode="inline"
      :items="items"
      :inlineCollapsed="false"
      @click="itemClick"
      @openChange="openChange"
      triggerSubMenuAction="click"
    ></a-menu>
  </a-layout-sider>
</template>

<script setup lang="ts">
//@ts-ignore
import { ItemType } from 'ant-design-vue';
import { reactive, computed, watchEffect } from 'vue';
import { Data } from '@/config';
import { routerMenu } from '@/config/routerMenu';
import { useBaseStore } from '@/stores/base';
import { useRouter, useRoute } from 'vue-router';

const router = useRouter();
const route = useRoute();

const base: any = useBaseStore();

const state = reactive({
  openKeys: [] as any[],
  selectedKeys: [] as any[],
});

const onBreakpoint = (broken: boolean) => base.setBreakpoint(broken);

const itemClick = ({ item }: { item: any }) => {
  if (router.currentRoute.value.path === item.originItemValue.url) return;
  router.push(item.originItemValue.url);
};

const openChange = (ret: string[]) => {
  // 非移动端
  if (!base.getBreakpoint && !base.getOpen && ret.length > 0) base.setOpen(true);
};

const item = (r: any, prefix = '') => {
  return r.display !== undefined
    ? false
    : {
        label: r.label,
        key: r.key,
        icon: r.icon(),
        url: prefix + r.url,
        children:
          r.children.length === 0 || r.notShowChildren
            ? undefined
            : r.children.map((c: any) => item(c, prefix + r.url)).filter((t: any) => t !== false),
      };
};

const deduplication = (list: any[], data: any) => {
  if (list.filter((l) => l === data).length > 0) return;
  list.push(data);
};

// 递归遍历树型结构
const findKey = (node: any, route: any, path) => {
  if (!node.children || node.children.length === 0) {
    if (route.name === node.label) {
      deduplication(path, node.key);
      return {
        path: path,
        node: node,
      };
    }
  } else {
    for (let i = 0; i < node.children.length; i++) {
      deduplication(path, node.key);
      let res = findKey(node.children[i], route, path);
      if (res) return res;
      path.pop();
    }
  }
};

const items = computed(() =>
  routerMenu
    .map((r: any) => {
      if (r.key === 'refresh' || r.key === 'notFound') return false;
      return item(r);
    })
    .filter((r: any) => r !== false)
);

watchEffect(() => {
  // 防止重定向进入
  if (route.matched.length === 0) return;
  // 防止进行刷新操作时进入
  if (route.path === '/refresh' || route.path === '*') return;
  // 获取到路由数组内对应路由的信息
  let newArray: any = items.value.filter((i: any) => i.label === route.name)[0];

  // 如果只有一个，表示他没有子节点，并且当获取到的路由信息存在时进入
  if (route.matched.length === 1 && newArray) {
    state.openKeys = [];
    state.selectedKeys = [newArray.key];
    return;
  }
  // 选中一级菜单
  let node: any = items.value.filter((i: any) => i.label === route.matched[0].name)[0];
  if (route.fullPath.startsWith('/base/recovery/')) {
    state.openKeys = [node.key];
    state.selectedKeys = [node.children[0].key];
    return;
  } else if (!route.meta.display) {
    state.selectedKeys = state.openKeys = [];
    return;
  }
  let res = findKey(node, route, [node.key]);
  state.openKeys = res.path;
  state.selectedKeys = [res.node.key];
});
</script>

<style scoped>
.web-title {
  color: white;
  font-size: 30px;
  display: inline-flex;
  padding: 13px 0 10px 17px;
  align-items: center;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  cursor: pointer;
}

.web-title span {
  margin-left: 15px;
  font-size: 19px;
  font-weight: 700;
}
</style>

<style>
.ant-menu-submenu-placement-rightTop {
  display: none !important;
}
</style>