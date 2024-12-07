<!--
 * @Author: tanxiang 1571922819@qq.com
 * @Date: 2023-09-09 12:36:08
 * @Description:
 * @LastEditTime: 2023-12-03 16:42:24
 * @LastEditors: tanxiang 1571922819@qq.com
 * @FilePath: \blog\common\src\components\global-loading\GlobalLoading.vue
 * @copyright: Copyright (c) 2023 by 1571922819@qq.com, All Rights Reserved.
-->
<template>
  <div
    class="page-loading top-0 left-0 fixed flex-center z-9999 w-full h-full"
    :class="{ 'loading-hidden': props.hiddenLoading }"
  >
    <div class="loader"></div>
  </div>
</template>

<script setup lang="ts">
const props = defineProps({
  hiddenLoading: { type: Boolean, default: true },
});
</script>

<style scoped lang="css">
.page-loading {
  background-image: linear-gradient(
    120deg,
    rgba(252, 203, 144, 0.4) 0%,
    rgba(213, 126, 235, 0.4) 100%
  );
  opacity: 1;
  z-index: 9999;
  transition: opacity 0.15s linear, z-index 0.15s linear;
}

.loader {
  width: 35px;
  height: 80px;
  position: relative;
}

.loader::after {
  content: '';
  position: absolute;
  inset: 0;
  padding: 3px 5px;
  border-top: 1px solid #bbb6aa;
  border-bottom: 4px solid #bbb6aa;
  background: linear-gradient(#612329 0 0) bottom no-repeat content-box, #e4e0d7;
  mix-blend-mode: darken;
  animation: loading 1.5s infinite linear;
}
.loader::before {
  content: '';
  position: absolute;
  inset: -18px calc(50% - 2px) 8px;
  background: #eb6b3e;
  transform-origin: bottom;
  transform: rotate(8deg);
}

.loading-hidden {
  opacity: 0;
  z-index: -9999;
  /*animation: pageHidden 0.4s linear forwards;*/
}

@keyframes loading {
  0% {
    background-size: 100% 100%;
  }
  100% {
    background-size: 100% 5%;
  }
}
</style>