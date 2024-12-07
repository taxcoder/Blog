<!--
 * @Author: tanxiang 1571922819@qq.com
 * @Date: 2023-06-10 14:51:02
 * @Description:
 * @LastEditTime: 2023-11-30 23:21:41
 * @LastEditors: tanxiang 1571922819@qq.com
 * @FilePath: \blog\packages\web-client\src\components\home-page\HomePage.vue
 * @copyright: Copyright (c) 2023 by ${git_name_email}, All Rights Reserved.
-->
<template>
  <div id="home-page" class="w-full relative home-bg">
    <page-navigation />
    <page-title :title="props.title" class="z-10 relative" />
    <div
      :style="setBackImage"
      class="bg-center bg-no-repeat bg-cover bg-fixed w-full h-full absolute top-0 bg-center z-0 brightness-75"
    ></div>
  </div>
</template>

<script setup lang="ts">
import { usePage } from '@tanxiang/utils';

import { useGlobalStore } from '@/stores/global';

const { getOffsetTop } = usePage();
const global: any = useGlobalStore();

const status: any = inject('status');

const props = defineProps({
  title: { type: String, required: true },
});

// 跳转到指定节点
const toNode = () => {
  let top: any = getOffsetTop(document.getElementById('start'));
  let nav: any = document.getElementById('page-nav')?.clientHeight;
  let start: any = Math.floor((top - nav) / 10) * 10;
  if (status.scroll < start) {
    window.scrollBy({ left: 0, top: start - status.scroll < 100 ? 10 : 100 });
    window.requestAnimationFrame(toNode);
  }
};
// 设置背景图片
const setBackImage = computed(() => ({
  backgroundImage: `url(${global.getWebSite.backgroundImage})`,
}));
</script>

<style scoped>
.home-bg {
  height: calc(var(--home-page) / var(--zoom));
}

#home-page:after {
  content: '';
  width: 100%;
  height: 10%;
  position: absolute;
  bottom: -1px;
  left: 0;
  background: linear-gradient(to top, #f5f5f5, transparent) !important;
}

.dark #home-page:after {
  background: linear-gradient(to top, #101010, transparent) !important;
}
</style>