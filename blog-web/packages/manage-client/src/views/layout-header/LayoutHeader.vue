<template>
  <a-layout-header :style="header">
    <div :style="headerTop" v-show="!base.getFullScreen">
      <layout-header-breadcrumb />
      <layout-header-action />
    </div>
    <div class="header-bottom">
      <layout-header-router-list />
      <layout-header-page-operation />
    </div>
  </a-layout-header>
</template>

<script setup lang="ts">
import { reactive, watch } from 'vue';
import { useBaseStore } from '@/stores/base';
import LayoutHeaderBreadcrumb from '@/views/layout-header/LayoutHeaderBreadcrumb.vue';
import LayoutHeaderAction from '@/views/layout-header/LayoutHeaderAction.vue';
import LayoutHeaderRouterList from '@/views/layout-header/LayoutHeaderRouterList.vue';
import LayoutHeaderPageOperation from '@/views/layout-header/LayoutHeaderPageOperation.vue';

const base = useBaseStore();

const header = reactive({
  background: '#fff',
  padding: 0,
  height: !base.getFullScreen ? 'var(--header-height)' : 'var(--header-tab-bar-height)',
  lineHeight: !base.getFullScreen ? 'var(--header-height)' : 'var(--header-tab-bar-height)',
  overflow: 'hidden',
});

const headerTop = reactive({
  display: 'flex',
  'justify-content': 'space-between',
  width: '100%',
  height: !base.getFullScreen ? 'var(--header-user-height)' : 0,
  'align-items': 'center',
});

watch(
  () => base.getFullScreen,
  (newFullScreen: boolean) => {
    header.height = !newFullScreen ? 'var(--header-height)' : 'var(--header-tab-bar-height)';
    header.lineHeight = !newFullScreen ? 'var(--header-height)' : 'var(--header-tab-bar-height)';
    headerTop.height = !newFullScreen ? 'var(--header-user-height)' : '0';
  }
);
</script>

<style scoped>
.header-bottom {
  display: flex;
  height: var(--header-tab-bar-height);
  line-height: var(--header-tab-bar-height);
  justify-content: space-between;
}
.ant-layout-header .ant-tabs-tab {
  height: var(--header-tab-bar-height);
  box-sizing: border-box;
}
</style>
