<template>
  <transition name="slide-fade">
    <el-button
      :dark="true"
      type="primary"
      class="side-button"
      tag="div"
      @click="changeOpenActive"
      v-show="juiceIsActive"
    >
      <template v-slot:icon>
        <n-icon size="23">
          <ArrowsDiagonalMinimize v-if="openActive" class="transform-rotate-z-45" />
          <ArrowFitFilled v-else />
        </n-icon>
      </template>
    </el-button>
  </transition>
</template>

<script setup lang="ts">
import { useBaseStore } from '@/stores/base';
import { ArrowFitFilled } from '@tanxiang/common';
import { ArrowsDiagonalMinimize } from '@tanxiang/common';

const base = useBaseStore();

const openActive = ref(base.getOpenActive);

const status: any = inject('status');
// 设置当前是否处于全开状态（个人信息是否隐藏）
const changeOpenActive = () => {
  openActive.value = !openActive.value;
  base.setOpenActive(openActive.value);
  base.setOpenUserActive(!base.getOpenUserActive);
};
// 判断是否打开状态
const juiceIsActive = computed(() => {
  let ret = status.width > 1200;
  openActive.value = ret && base.getOpenUserActive;
  base.setOpenActive(openActive.value);
  return ret;
});
</script>

<style scoped>
.slide-fade-enter-active,
.slide-fade-leave-active {
  transition: transform 0.5s ease-out;
}

.slide-fade-enter-from,
.slide-fade-leave-to {
  transform: translateX(200px);
}
</style>