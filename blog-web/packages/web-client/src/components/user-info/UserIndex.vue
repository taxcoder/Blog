<!--
 * @Author: tanxiang 1571922819@qq.com
 * @Date: 2023-06-17 20:23:45
 * @Description:
 * @LastEditTime: 2023-12-03 18:55:18
 * @LastEditors: tanxiang 1571922819@qq.com
 * @FilePath: \blog\packages\web-client\src\components\user-info\UserIndex.vue
 * @copyright: Copyright (c) 2023 by 1571922819@qq.com, All Rights Reserved.
-->
<template>
  <div id="user-info" class="w-full h-full min-w-[var(--user-info)]" v-show="props.show">
    <div class="user-info-container h-full box-border pt-[15px]">
      <div v-if="props.showUserInfo && juiceIsDraw" class="w-full h-full p-0">
        <user-info />
        <web-site-board />
        <life-process />
        <div class="sticky top-0">
          <web-site-info />
          <year-target />
          <web-site-tag />
        </div>
      </div>
      <div v-else-if="getRoute === 'ArticlePage' && isDirectory" class="w-full h-full p-0">
        <article-directory />
      </div>
      <div v-else-if="getRoute === 'NavigationList' && idDatas" class="w-full h-full p-0">
        <navigation-directory />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import NavigationDirectory from '@/components/user-info/NavigationDirectory.vue';
import { useNavigationStore } from '@/stores/navigation';
import { useArticleStore } from '@/stores/article';

const route = useRoute();

const articleStore = useArticleStore();
const navigationStore = useNavigationStore();

const props = defineProps({
  show: { type: Boolean, required: true },
  showUserInfo: { type: Boolean, required: true },
});

const juiceIsDraw = computed(() => {
  return !route.meta || !route.meta.isDraw;
});

const isDirectory = computed(() => {
  return articleStore.getDirectory?.length > 0;
});

const idDatas = computed(() => {
  return navigationStore.getDatas.length > 0;
});

const getRoute = computed(() => route.name);
</script>

<style scoped>
#list .user-left .user-info-container {
  padding-right: 10px;
  padding-left: 30px;
}

#list .user-right .user-info-container {
  padding-right: 30px;
  padding-left: 10px;
}
</style>