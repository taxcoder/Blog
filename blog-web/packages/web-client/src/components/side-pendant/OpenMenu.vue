<!--
 * @Author: tanxiang 1571922819@qq.com
 * @Date: 2023-07-30 13:19:53
 * @Description:
 * @LastEditTime: 2023-12-10 17:46:04
 * @LastEditors: tanxiang 1571922819@qq.com
 * @FilePath: \blog\packages\web-client\src\components\side-pendant\OpenMenu.vue
 * @copyright: Copyright (c) 2023 by 1571922819@qq.com, All Rights Reserved.
-->
<!--
 * @Author: tanxiang 1571922819@qq.com
 * @Date: 2023-07-30 13:19:53
 * @Description:
 * @LastEditTime: 2023-12-02 21:23:45
 * @LastEditors: tanxiang 1571922819@qq.com
 * @FilePath: \blog\packages\web-client\src\components\side-pendant\OpenMenu.vue
 * @copyright: Copyright (c) 2023 by 1571922819@qq.com, All Rights Reserved.
-->
<template>
  <el-button type="primary" class="side-button" tag="div" @click="isMenuItemActive()">
    <template v-slot:icon>
      <n-icon size="18" class="setting-button">
        <SettingsSharp />
      </n-icon>
    </template>
  </el-button>
</template>

<script setup lang="ts">
import { SettingsSharp } from '@tanxiang/common';

const setMenuItem: Function = inject<Function>('isMenuItemActive');

const isMenuItemActive = () => setMenuItem();
</script>

<style scoped>
.setting-button {
  animation: turn 1.5s linear infinite forwards;
}

@keyframes turn {
  0% {
    transform: rotateZ(0);
  }
  100% {
    transform: rotateZ(360deg);
  }
}
</style>