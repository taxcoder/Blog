<!--
 * @Author: tanxiang 1571922819@qq.com
 * @Date: 2023-06-15 22:41:16
 * @Description:
 * @LastEditTime: 2023-12-04 20:07:41
 * @LastEditors: tanxiang 1571922819@qq.com
 * @FilePath: \blog\packages\web-client\src\components\ContentMain.vue
 * @copyright: Copyright (c) 2023 by 1571922819@qq.com, All Rights Reserved.
-->
<template>
  <div
    id="list"
    :class="{ 'max-w-full': route.name === 'NavigationList' }"
    class="h-full max-w-[1260px] mx-auto my-auto min-h-[500px] relative px-[15px] pb-0 pt-[60px] box-border"
  >
    <el-container>
      <el-aside :width="setAsideWidth" v-if="!props.position && !props.isDraw">
        <transition name="left-slide-fade">
          <slot name="user-left">
            <user-index class="user-left" :show="juiceIsShow" :showUserInfo="showUserInfo" />
          </slot>
        </transition>
      </el-aside>
      <el-main>
        <slot />
      </el-main>
      <el-aside :width="setAsideWidth" v-if="props.position && !props.isDraw">
        <transition name="slide-fade">
          <slot name="user-right">
            <user-index class="user-right" :show="juiceIsShow" :showUserInfo="showUserInfo" />
          </slot>
        </transition>
      </el-aside>
    </el-container>
  </div>
</template>

<script lang="ts" setup>
import { useBaseStore } from '@/stores/base';
import { useRoute } from 'vue-router';

import { usePage } from '@tanxiang/utils';

const route = useRoute();
const base = useBaseStore();

const status: any = inject('status');

const props = defineProps({
  position: { type: Boolean, required: true },
  showUserInfo: { type: Boolean, default: true },
  isDraw: { type: Boolean, default: false },
});
// 侧边栏是否显示
const juiceIsShow = computed(() => {
  return status.width > 1200 && !usePage().device() && base.getOpenUserActive;
});
// 侧边栏宽度
const setAsideWidth = computed(() => (!props.isDraw && juiceIsShow.value ? '330px' : '0'));
</script>

<style scoped>
.slide-fade-enter-active,
.slide-fade-leave-active {
  transition: transform 0.3s ease-out;
}

.slide-fade-enter-from,
.slide-fade-leave-to {
  transform: translateX(3000px);
}

.left-slide-fade-enter-active,
.left-slide-fade-leave-active {
  transition: transform 0.3s ease-out;
}

.left-slide-fade-enter-from,
.left-slide-fade-leave-to {
  transform: translateX(-3000px);
}
</style>