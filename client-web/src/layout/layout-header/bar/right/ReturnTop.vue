<!--
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2024-12-25 07:12:55
 * @LastEditTime: 2024-12-26 07:49:22
 * @FilePath: /code/blog/client-web/src/common/framework/header/bar/right/ReturnTop.vue
 * @Description: 
 * Copyright (c) 2024 by tanxiang, All Rights Reserved. 
-->
<template>
  <transition>
    <div class="flex-ec w-full h-[2.5rem] cursor-pointer">
      <div
        id="return_top"
        @mouseenter="setHover(true)"
        @mouseleave="setHover(false)"
        @touchstart="setHover(true)"
        @touchend="setHover(false)"
        class="w-[2.5rem] h-full rounded-full box-border p-1.75 transition-all-250"
        :class="[addReturnTopClass]"
      >
        <div
          v-show="percentage > 0"
          class="flex-cc w-full h-full bg-[#363636] color-white font-size-[0.75rem] rounded-full"
          @click="returnTop()"
        >
          <icon-arrow-up
            v-show="isHover"
            :class="{ open: isHover }"
            :stroke-width="8"
            theme="filled"
            size="20"
            :fill="global.getTheme ? '#fff' : '#333'"
          />
          <span v-show="!isHover && !percentageValue(100)" :class="{ open: !isHover }">{{ percentage }}</span>
          <span v-show="!isHover && percentageValue(100)" :class="{ to_top: percentageValue(100) }"> 返回顶部 </span>
        </div>
      </div>
    </div>
  </transition>
</template>

<script setup lang="ts">
const isPhone = inject<any>('isPhone');
const percentage = inject<any>('percentage');
const global = useGlobalStore();

const isHover = ref(false);

const setHover = (status: boolean) => (isHover.value = status);
const returnTop = () => document.querySelector('#layout-header')?.scrollIntoView({ behavior: 'smooth' });

const percentageValue = computed(
  () => (num: number) => (num === 100 ? percentage.value >= num : percentage.value === num)
);

const addReturnTopClass = computed(() => {
  return {
    'w-full important-p-1.75': percentageValue.value(100),
    '[&_div]:hover:bg-[var(--theme-global-color)] hover:p-0.5': !isPhone.value,
    '[&_div]:active:bg-[var(--theme-global-color)] active:p-0.5': isPhone.value
  };
});
</script>

<style lang="css" scoped>
.open {
  animation: open 0.5s linear;
}

.to_top {
  animation: to_top 0.45s linear;
}

@-webkit-keyframes to_top {
  0% {
    opacity: 0;
  }
  10% {
    opacity: 0;
  }
  100% {
    opacity: 1;
  }
}

@keyframes to_top {
  0% {
    opacity: 0;
  }
  10% {
    opacity: 0;
  }
  100% {
    opacity: 1;
  }
}

@-webkit-keyframes open {
  0% {
    opacity: 0;
  }
  50% {
    opacity: 0.8;
  }
  100% {
    opacity: 1;
  }
}

@keyframes open {
  0% {
    opacity: 0;
  }
  50% {
    opacity: 0.8;
  }
  100% {
    opacity: 1;
  }
}
</style>
