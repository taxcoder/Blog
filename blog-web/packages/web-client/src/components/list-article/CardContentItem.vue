<!--
 * @Author: tanxiang 1571922819@qq.com
 * @Date: 2023-06-21 10:56:35
 * @Description:
 * @LastEditTime: 2023-12-11 12:49:33
 * @LastEditors: tanxiang 1571922819@qq.com
 * @FilePath: \blog\packages\web-client\src\components\list-article\CardContentItem.vue
 * @copyright: Copyright (c) 2023 by 1571922819@qq.com, All Rights Reserved.
-->
<template>
  <div
    class="transition-width duration-300 md:px-[20px] md:py-[34px] px-[10px] py-[10px] box-border overflow-hidden relative flex flex-col md:w-full lg:important-w-auto"
  >
    <div class="flex items-center h-[35px] flex-1">
      <span
        class="article-top color-white px-0 py-[7px] rounded-[3px] mr-[5px] min-w-[44px] flex-center"
        v-if="item['top']"
        v-text="'置顶'"
      />
      <el-tooltip effect="dark" placement="top">
        <template #content><p class="text-[1rem]" v-text="item.title" /></template>
        <span class="text-[1.075rem] lh-[25px]" v-text="item.title" />
      </el-tooltip>
    </div>
    <div class="h-[30px] flex text-[#818181] md:mt-[2px] mt-0 items-center flex-1">
      <div class="border-r-[1px] border-solid border-[#818181] pr-[8px] h-[50%] inline-flex items-center">
        <color-icon iconClass="icon-A_89" :size="14" :top="-1" />
        <span class="text-[0.75rem] ml-[4px]" v-text="`发表于：${dateUtil.shortTime(item.createTime)}`" />
      </div>
      <div
        :class="setUpdateTimeDisplay"
        class="px-[8px] border-r-[1px] border-[#818181] border-solid h-[50%] inline-flex items-center"
      >
        <color-icon iconClass="icon-A1" :size="14" :top="-1" />
        <span class="text-[0.75rem] ml-[4px]" v-text="`更新于：${dateUtil.shortTime(item['updateTime'])}`" />
      </div>
      <div class="pl-[8px] h-[50%] inline-flex items-center">
        <color-icon iconClass="icon-A_70" :size="14" :top="-1" />
        <span class="text-[0.75rem] ml-[4px]" v-text="item['author']" />
      </div>
    </div>
    <div
      class="md:mt-[15px] mt-[5px] md:text-[0.95rem] text-[0.85rem] md:lh-[25px] lh-[20px] text-[#595959] overflow-hidden flex-[5_5_0%] text-ellipsis md:line-clamp-6 line-clamp-3"
    >
      <span v-text="item['prefixContent']" />
    </div>
    <div class="hidden md:grid grid-cols-1 xl:grid-cols-10 flex-1 h-[30px] mt-[10px] text-[#818181]">
      <div class="h-full hidden xl:flex items-center col-span-7">
        <span v-for="i in juiceIsAdditional(item['tag'])" class="inline-flex mr-[1px] items-center">
          <n-icon :size="15"><TagSharp /></n-icon>
          <el-link
            :underline="false"
            class="text-only-one first-of-type:important-mr-0 my-0 mx-[2px] text-[0.75rem] relative left-[1px] top-[1px]"
            v-text="i.name"
            :href="`/list/tag/${i.id}`"
          />
        </span>
      </div>
      <div class="text-[0.75rem] h-full flex items-center justify-end xl:col-span-3">
        <color-icon iconClass="icon-A7" :size="16" :top="-1" :left="-1" />
        <el-link
          :underline="false"
          v-text="item['classification'].name"
          :href="`/list/classification/${item['classification'].id}`"
        />
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ColorIcon, TagSharp } from '@tanxiang/common';
import { useDate } from '@tanxiang/utils';

const dateUtil = useDate();

const status: any = inject('status');

defineProps({
  item: { type: Object, required: true },
});
// 判断标签是否超过3个
const juiceIsAdditional = computed(() => (tag: any) => tag.slice(0, 3));
// 设置更新时间是否显示
const setUpdateTimeDisplay = computed(() => {
  return (status.width <= 860 && status.width > 600) || status.width <= 450 ? 'important-hidden' : 'inline-flex';
});
</script>

<style scoped>
.article-top {
  background: -webkit-linear-gradient(0deg, #3ca5f6 0%, #a86af9 100%);
}
</style>