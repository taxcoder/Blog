<template>
  <el-button type="primary" class="side-button" tag="div" @click="changeTheme">
    <template v-slot:icon>
      <n-icon size="22">
        <DarkThemeFilled />
      </n-icon>
    </template>
  </el-button>
</template>

<script setup lang="ts">
import { DarkThemeFilled } from '@tanxiang/common';

import { useBaseStore } from '@/stores/base';

const base = useBaseStore();

const changeTheme = () => base.setTheme(!base.getTheme);
</script>

<style scoped></style>