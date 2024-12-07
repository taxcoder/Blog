<template>
  <user-container title="今年目标" iconClass="icon-a-bianzu1x" :size="25">
    <template v-slot:content>
      <div class="mb-[10px]">
        <div
          v-for="target in global.getTarget"
          :key="target.id"
          class="flex items-start flex-row mt-[20px] cursor-default"
        >
          <i v-if="!target['isSuccess']" class="iconfont icon-dagou-weigouxuan color-[#1296db]"></i>
          <i v-else class="iconfont icon-dagou color-[#1296db]"></i>
          <el-text class="important-pl-[5px]" :tag="target['isSuccess'] ? 'del' : 'p'">
            {{ target.content }}
          </el-text>
        </div>
      </div>
    </template>
  </user-container>
</template>

<script setup lang="ts">
import dayjs from 'dayjs';
import { useTarget } from '@tanxiang/apis';
import { useGlobalStore } from '@/stores/global';

const target = useTarget();

const global: any = useGlobalStore();
// 获取当前年份的目标
onMounted(() => {
  target
    .getTargets(dayjs().year())
    .then((res: any) => global.setTarget(res.data))
    .catch((err: any) => {
      console.log(err);
    });
});
</script>

<style scoped></style>