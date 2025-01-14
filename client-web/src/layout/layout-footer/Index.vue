<!--
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2024-12-10 10:26:07
 * @LastEditTime: 2024-12-30 10:37:50
 * @FilePath: /code/blog/client-web/src/layout/layout-footer/Index.vue
 * @Description:  网站主题的页脚
 * Copyright (c) 2024 by tanxiang, All Rights Reserved. 
-->

<template>
  <div id="layout-footer" class="md:pt[8rem] pt-[3rem] pb-[0.5rem] h-[20vh] flex-cc box-border">
    <div class="w-full">
      <div class="flex-cc h-[2.25rem] py-[0.25rem]">
        <img class="h-full sakura" src="@/assets/svg/sakura.svg" />
      </div>
      <div class="flex-cc h-[1.5rem] font-size-[0.9rem] color-[#666666]">
        <span>Crafted with</span>
        <icon-like
          class="mx-[0.15rem]"
          theme="filled"
          size="1rem"
          fill="#ff0000"
          strokeLinejoin="bevel"
          strokeLinecap="square"
        />
        <span>by</span>
        <span class="color-[#000] dark:color-[#adadad] px-[0.15rem] relative top-[0.0625rem]">
          {{ name }}
        </span>
      </div>
      <div class="flex-cc h-[1.5rem] font-size-[0.875rem] color-[#B9B9B9]">
        <span class="font-size-[1.05rem] px-[0.15rem]"> © </span>
        <span>{{ beforePresent }}</span>
        <span class="px-[0.15rem]">{{ name }}</span>
        <a href="#" class="decoration-none color-inherit outline-none">{{ global.getInfo?.icpRecord }}</a>
      </div>
      <div class="flex-cc h-[1.5rem] font-size-[0.875rem] color-[#B9B9B9]">
        {{ global.getInfo?.policeRecord }}
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
const global = useGlobalStore();

const beforePresent = computed(() => {
  let create = new Date(global.getInfo?.createTime).getFullYear();
  let current = new Date().getFullYear();
  return create == current ? create : `${create}-${current}`;
});

const name = computed(() => {
  return global.getInfo?.name;
});
</script>

<style lang="css" scoped>
.dark #layout-footer {
  background: rgb(40, 40, 40);
}

@media screen and (max-width: 48rem) and (min-height: 62.5rem) {
  #layout-footer {
    padding-top: 8rem;
  }
}

.sakura {
  user-select: none;
  animation: sakura 10s linear infinite;
}

@keyframes sakura {
  0% {
    transform: rotate(0);
  }
  100% {
    transform: rotate(360deg);
  }
}

@-webkit-keyframes sakura {
  0% {
    transform: rotate(0);
  }
  100% {
    transform: rotate(360deg);
  }
}
</style>
