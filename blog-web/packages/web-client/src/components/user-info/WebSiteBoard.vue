<template>
  <user-container title="公告栏目" iconClass="icon-gonggao1" :size="25">
    <template v-slot:content>
      <div class="board-content w-full pt-[6px] box-border min-h-[75px]">
        <el-text size="small">{{ getBulletinBoard }}</el-text>
      </div>
    </template>
  </user-container>
</template>

<script setup lang="ts">
import { useGlobalStore } from '@/stores/global';

const global: any = useGlobalStore();
// 获取公告栏目
const getBulletinBoard = computed(() => {
  let board = global.getWebSite.bulletinBoard;
  return board ? board : '暂无公告';
});
</script>

<style scoped></style>
