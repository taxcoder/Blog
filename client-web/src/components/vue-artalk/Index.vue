<!--
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2025-01-05 04:26:01
 * @LastEditTime: 2025-01-07 09:56:25
 * @FilePath: /code/blog/client-web/src/components/vue-artalk/Index.vue
 * Copyright (c) 2025 by tanxiang, All Rights Reserved. 
-->
<template>
  <div ref="el"></div>
</template>

<script lang="ts" setup>
import Artalk, { init, loadCountWidget } from './index';
const route = useRoute();
const global = useGlobalStore();

const el = ref<HTMLElement>();
let artalk: Artalk;

onMounted(() => {
  artalk = init(el, !global.getTheme);
  artalk.on('comment-inserted', () => loadCountWidget());
});

watch(
  () => [route.path, global.getTheme],
  ([path, theme]) => {
    artalk.update({ darkMode: !theme });
    artalk.reload();
  }
);

onBeforeUnmount(() => artalk.destroy());
</script>
