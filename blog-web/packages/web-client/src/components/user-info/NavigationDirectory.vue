<template>
  <div
    class="navigation-directory sticky top-0 bg-[#fbfbfb] dark:important-bg-[#2d2d2d] rounded-[5px] py-[20px] text-[16px]"
  >
    <div v-for="item in navigationStore.getDatas">
      <div class="text-[18px] px-[20px] py-[15px] font-bold cursor-pointer" @click="jump(item)">
        {{ item }}
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useNavigationStore } from '@/stores/navigation';

const navigationStore = useNavigationStore();

const jump = (item: any) => {
  document.getElementById('navigation-' + item).scrollIntoView({ behavior: 'smooth', block: 'start' });
};
</script>

<style scoped></style>