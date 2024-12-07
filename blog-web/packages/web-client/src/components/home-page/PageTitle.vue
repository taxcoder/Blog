<!--
 * @Author: tanxiang 1571922819@qq.com
 * @Date: 2023-06-11 11:36:16
 * @Description:
 * @LastEditTime: 2023-12-01 00:08:37
 * @LastEditors: tanxiang 1571922819@qq.com
 * @FilePath: \blog\packages\web-client\src\components\home-page\PageTitle.vue
 * @copyright: Copyright (c) 2023 by ${git_name_email}, All Rights Reserved.
-->
<template>
  <div class="flex-center flex-wrap flex-col text-white w-full h-[calc(100%-60px)]">
    <div
      class="flex-center relative w-full bottom-[15px] min-w-[350px] md:text-[3.25rem] text-[2.25rem] box-border px-[10px] text-center"
    >
      {{ props.title }}
    </div>
    <div class="typed flex-center relative w-full text-[1.35rem]" v-if="juiceIsTypedShow">
      <vuetyped :strings="getStrings" :loop="true" :smart-backspace="true" :key="global.getWebSite.text">
        <div class="typing" />
      </vuetyped>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useGlobalStore } from '@/stores/global';

const route = useRoute();

const global = useGlobalStore();

const props = defineProps({
  title: { type: String, require: true },
});
// 从后台获取格言，如果没有获取到，则使用默认的
const getStrings = computed(() => (global.getWebSite.text ? global.getWebSite.text : ['hello world']));
// 如果获取的格言不是空，并且当前是首页，则显示格言
const juiceIsTypedShow = computed(() => {
  return getStrings.value.length > 0 && !!route.meta && route.meta.name === '首页';
});
</script>

<style scoped></style>