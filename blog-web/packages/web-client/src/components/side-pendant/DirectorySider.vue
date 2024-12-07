<template>
  <el-drawer v-model="sider" :with-header="false" :append-to-body="true" :size="getWindowSize">
    <user-index class="user-right" :show="true" :showUserInfo="false"></user-index>
  </el-drawer>

  <transition name="slide-fade">
    <el-button :dark="true" type="primary" class="side-button" tag="div" @click="openSider">
      <template v-slot:icon>
        <n-icon size="23">
          <directory />
        </n-icon>
      </template>
    </el-button>
  </transition>
</template>

<script setup lang="ts">
import { Directory } from '@tanxiang/common';
import UserIndex from '@/components/user-info/UserIndex.vue';

import { useRoute } from 'vue-router';

const sider = ref<boolean>(false);
const status = inject('status');

const route = useRoute();

const openSider = () => (sider.value = !sider.value);

const getWindowSize = computed(() => {
  return status.width > 600 ? '400px' : '75%';
});
</script>

<style scoped></style>