<template>
  <layout-sider-phone v-if="base.getBreakpoint && !base.getFullScreen" />
  <layout-sider-pc v-show="!base.getBreakpoint && !base.getFullScreen" />
</template>

<script setup lang="ts">
import { useBaseStore } from '@/stores/base';
import LayoutSiderPhone from '@/views/layout-sider/LayoutSiderPhone.vue';
import LayoutSiderPc from '@/views/layout-sider/LayoutSiderPc.vue';

const base: any = useBaseStore();
</script>

<style scoped></style>
