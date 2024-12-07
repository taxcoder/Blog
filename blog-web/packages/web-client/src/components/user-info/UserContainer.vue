<!--
 * @Author: tanxiang 1571922819@qq.com
 * @Date: 2023-06-20 10:14:30
 * @Description:
 * @LastEditTime: 2023-12-03 18:57:15
 * @LastEditors: tanxiang 1571922819@qq.com
 * @FilePath: \blog\packages\web-client\src\components\user-info\UserContainer.vue
 * @copyright: Copyright (c) 2023 by ${git_name_email}, All Rights Reserved.
-->
<template>
  <el-card
    class="important-rounded-[5px] mt-[5px] shadow-[var(--box-shadow)]"
    shadow="never"
    body-style="padding: 0;position: relative"
  >
    <div class="w-full h-full px-[20px] pt-[5px] pb-[7px] box-border">
      <div class="h-[40px] grid items-center grid-cols-10">
        <color-icon :iconClass="props.iconClass" :size="props.size" :left="props.left" class="col-span-1" />
        <span class="text-[0.75rem] lh-[30px] text-left ml-[6px] col-span-9">{{ title }}</span>
      </div>
      <el-divider />
      <div class="w-full h-full overflow-hidden">
        <el-skeleton :loading="loading && isDefer" animated>
          <template #template>
            <el-skeleton-item
              v-for="item in 6"
              variant="text"
              :key="item"
              class="important-w-full important-h-[15px] mx-0 first-of-type:mt-[10px] first-of-type:mb-[4px] my-[4px] last-of-type:mt-[4px] last-of-type:mb-[3px]"
            />
          </template>
          <template #default><slot name="content" /></template>
        </el-skeleton>
      </div>
      <div><slot name="more"></slot></div>
    </div>
  </el-card>
</template>
icon

<script setup lang="ts">
import { ColorIcon } from '@tanxiang/common';

import { ElCard, ElDivider, ElSkeleton, ElSkeletonItem } from 'element-plus';

const props = defineProps({
  loading: { type: Boolean, default: true },
  isDefer: { type: Boolean, default: false },
  title: String,
  size: { type: Number, default: 20 },
  iconClass: { type: String, default: '' },
  left: { type: Number, default: 0 },
});
</script>

<style scoped></style>
