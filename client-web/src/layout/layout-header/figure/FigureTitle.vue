<!--
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2024-12-20 18:17:52
 * @LastEditTime: 2025-01-03 14:49:38
 * @FilePath: /code/blog/client-web/src/layout/layout-header/figure/FigureTitle.vue
 * @Description:  
 * Copyright (c) 2024 by tanxiang, All Rights Reserved. 
-->
<template>
  <div class="pb-[7.5rem] route-active" v-if="!isPhone">
    <h1 class="center-text glitch" :data-text="global.getInfo?.title">{{ global.getInfo?.title }}</h1>
    <div class="z-99 relative flex-cc font-size-[2rem] mt-[2.5rem]">
      <vuetyped class="color-white" :strings="global.getInfo?.strings" :showCursor="true" :typeSpeed="150" :loop="true">
        <div class="typing"></div>
      </vuetyped>
    </div>
  </div>
</template>

<script setup lang="ts">
const global = useGlobalStore();

const isPhone = inject('isPhone');
</script>

<style lang="css" scoped>
@media screen and (max-width: 48rem) {
  .glitch {
    display: none;
  }
}

.center-text {
  margin: 0;
  font-size: 5rem;
  transition: background 1s;
}

.glitch {
  position: relative;
  color: #fff;
  mix-blend-mode: lighten;
  white-space: nowrap;
}

.glitch:before,
.glitch:after {
  content: attr(data-text);
  position: absolute;
  top: 0;
  width: 100%;
  background: rgba(0, 0, 0, 0);
  clip: rect(0, 0, 0, 0);
}

.glitch:before {
  left: -1px;
  text-shadow: 1px 0 #ff3b00;
}

.glitch:after {
  left: 57px;
  text-shadow: -1px 0 #00a1d6;
}

.glitch:hover:before {
  text-shadow: 4px 0 #ff3b00;
  animation: glitch-loop-1 0.8s infinite ease-in-out alternate-reverse;
}

.glitch:hover:after {
  text-shadow: -5px 0 #00a1d6;
  animation: glitch-loop-2 0.8s infinite ease-in-out alternate-reverse;
}

.glitch + .layout-header-info:before {
  top: -1.875rem;
  content: '';
}

@keyframes glitch-loop-1 {
  0% {
    clip: rect(36px, 9999px, 9px, 0);
  }

  25% {
    clip: rect(25px, 9999px, 99px, 0);
  }

  50% {
    clip: rect(50px, 9999px, 102px, 0);
  }

  75% {
    clip: rect(30px, 9999px, 92px, 0);
  }

  to {
    clip: rect(91px, 9999px, 98px, 0);
  }
}

@keyframes glitch-loop-2 {
  0% {
    top: -1px;
    left: 1px;
    clip: rect(65px, 9999px, 119px, 0);
  }

  25% {
    top: -6px;
    left: 4px;
    clip: rect(79px, 9999px, 19px, 0);
  }

  50% {
    top: -3px;
    left: 2px;
    clip: rect(68px, 9999px, 11px, 0);
  }

  75% {
    top: 0;
    left: -4px;
    clip: rect(95px, 9999px, 53px, 0);
  }

  to {
    top: -1px;
    left: -1px;
    clip: rect(31px, 9999px, 149px, 0);
  }
}
</style>
