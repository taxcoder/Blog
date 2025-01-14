<!--
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2024-12-19 09:42:15
 * @LastEditTime: 2024-12-20 13:42:26
 * @FilePath: /code/blog/client-web/src/common/wave/Wave.vue
 * @Description: 
 * Copyright (c) 2024 by tanxiang, All Rights Reserved. 
-->
<template>
  <div class="relative">
    <div id="wave">
      <svg
        class="waves"
        xmlns="http://www.w3.org/2000/svg"
        xmlns:xlink="http://www.w3.org/1999/xlink"
        viewBox="0 24 150 28"
        preserveAspectRatio="none"
        shape-rendering="auto"
      >
        <defs>
          <path id="gentle-wave" d="M-160 44c30 0 58-18 88-18s 58 18 88 18 58-18 88-18 58 18 88 18 v44h-352z" />
        </defs>
        <g class="parallax">
          <use xlink:href="#gentle-wave" x="48" y="0" :fill="fill(0.7)" />
          <use xlink:href="#gentle-wave" x="48" y="3" :fill="fill(0.5)" />
          <use xlink:href="#gentle-wave" x="48" y="5" :fill="fill(0.3)" />
          <use xlink:href="#gentle-wave" x="48" y="7" :fill="fill(1)" />
        </g>
      </svg>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue';

const props = defineProps({
  theme: {
    type: Boolean,
    default: true
  },
  hidden: {
    type: Boolean,
    default: false
  }
});

const fill = computed(() => (a: any) => {
  return props.theme ? `rgba(255,255,255,${a})` : `rgba(40,40,40,${a})`;
});
</script>

<style lang="css" scoped>
#wave {
  position: absolute;
  bottom: -2px;
  width: 100%;
}

.header {
  position: relative;
  text-align: center;
  background: linear-gradient(60deg, rgba(84, 58, 183, 1) 0%, rgba(0, 172, 193, 1) 100%);
}

.inner-header {
  height: 65vh;
  width: 100%;
  margin: 0;
  padding: 0;
}

.waves {
  position: relative;
  width: 100%;
  height: 7.5vh;
  margin-bottom: -4px;
}

.content {
  position: relative;
  height: 20vh;
  text-align: center;
  background-color: white;
}

.content a {
  margin: 0 5px;
  font-size: 12px;
  color: #333;
}

.content a:hover {
  color: #000;
}

/* Animation */
.parallax > use {
  animation: move-forever 40s cubic-bezier(0.55, 0.5, 0.45, 0.5) infinite;
}
.parallax > use:nth-child(1) {
  animation-delay: -2s;
  animation-duration: 14s;
}
.parallax > use:nth-child(2) {
  animation-delay: -4s;
  animation-duration: 20s;
}
.parallax > use:nth-child(3) {
  animation-delay: -6s;
  animation-duration: 26s;
}
.parallax > use:nth-child(4) {
  animation-delay: -8s;
  animation-duration: 40s;
}
@keyframes move-forever {
  0% {
    transform: translate3d(-90px, 0, 0);
  }
  100% {
    transform: translate3d(85px, 0, 0);
  }
}
</style>
