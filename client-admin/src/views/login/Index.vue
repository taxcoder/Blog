<!--
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2025-01-15 08:09:24
 * @LastEditTime: 2025-01-17 07:04:17
 * @FilePath: /code/blog/client-admin/src/views/login/Index.vue
 * @Description: 
 * Copyright (c) 2025 by tanxiang, All Rights Reserved. 
-->

<template>
  <div class="flex flex-row wh-full dark:bg-[#161616] select-none">
    <div class="flex-[4] transition-flex-750 flex-cc relative max-md:hidden">
      <div class="h-full w-80% panel filter-blur-100 flex-cc absolute"></div>
      <div class="flex-cc flex-col z-9">
        <n-image
          class="mb-[2rem] h-[22.5rem] animate-float"
          width="400"
          src="http://39.107.143.109/assets/bg-BMHLGnO3.png"
          preview-disabled
        />
        <div
          class="chroma-text font-bold text-3xl mb-[0.375rem] text-center color-transparent important-bg-clip-text whitespace-nowrap"
        >
          欢迎登陆博客管理系统
        </div>
        <div
          class="chroma-text font-bold color-transparent important-bg-clip-text whitespace-nowrap text-lg text-center"
        >
          或许我们只是差点运气
        </div>
        <div></div>
      </div>
    </div>
    <div class="flex-auto dark:bg-[#161616] bg-gray-100 flex-cc flex-col">
      <div class="flex-cc">
        <n-avatar color="none" src="public/icon.svg"></n-avatar>
        <span class="ml-[0.625rem] font-bold text-xl dark:color-[#fffFffE6]">博客管理平台</span>
      </div>
      <n-tabs v-model:value="tagValue" justify-content="space-evenly" size="large" animated class="px-8 w-sm mt-4">
        <n-tab-pane name="signin" tab="账号密码登陆" class="color-white">
          <account></account>
        </n-tab-pane>
        <n-tab-pane name="phone" tab="手机号登录">
          <phone></phone>
        </n-tab-pane>
      </n-tabs>
    </div>
  </div>
  <div
    class="fixed top-0 right-0 w-[2rem] h-[2rem] m-3 z-99 rounded-full p-1 box-border cursor-pointer"
    :class="[setBackground]"
    @click="changeTheme(global.getTheme)"
  >
    <img src="@/assets/svg/sun.svg" title="亮色模式" class="wh-full" v-show="global.getTheme === 'light'" />
    <img src="@/assets/svg/night.svg" title="深色模式" class="wh-full" v-show="global.getTheme === 'dark'" />
    <img src="@/assets/svg/sun-cloud.svg" title="跟随模式" class="wh-full" v-show="global.getTheme === 'auto'" />
  </div>
</template>

<script setup lang="ts">
import Account from './modules/Account.vue';
import Phone from './modules/Phone.vue';

const [darkMode, setDarkMode] = useDarkMode();
const global = useGlobalStore();
const tagValue = ref<string>('phone');

const changeTheme = (theme: 'auto' | 'dark' | 'light') => {
  if (theme === 'auto') {
    setDarkMode(true);
    global.setTheme(true);
  } else if (theme === 'dark') {
    setDarkMode(false);
    global.setTheme(false);
  } else {
    setDarkMode(void 0);
    global.setTheme(void 0);
  }
};

const setBackground = computed(() => {
  if (global.getTheme === 'auto') {
    return 'bg-[#fff]';
  } else if (global.getTheme === 'dark') {
    return 'bg-[#404040]';
  } else {
    return 'bg-[#ccc]';
  }
});
</script>

<style lang="css" scoped>
.panel {
  background: linear-gradient(155deg, rgba(7, 7, 9, 0.0823529412) 12%, #409eff 36%, rgba(7, 7, 9, 0.0823529412) 76%);
}

.chroma-text {
  background: linear-gradient(270deg, rgba(198, 141, 255, 0.9019607843) 8.92%, #5685ff 46.17%, #409eff 92.17%);
}

.animate-float {
  animation: animate-float 5s ease-in-out infinite;
}

@keyframes animate-float {
  0% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-20px);
  }
  100% {
    transform: translateY(0);
  }
}
</style>
