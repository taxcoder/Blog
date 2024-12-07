<!--
 * @Author: tanxiang 1571922819@qq.com
 * @Date: 2023-06-15 21:03:44
 * @Description: 网站的基本信息
 * @LastEditTime: 2023-12-10 13:47:14
 * @LastEditors: tanxiang 1571922819@qq.com
 * @FilePath: \blog\packages\web-client\src\components\user-info\UserInfo.vue
 * @copyright: Copyright (c) 2023 by 1571922819@qq.com, All Rights Reserved.
-->
<template>
  <el-card
    class="important-rounded-[5px] shadow-[var(--box-shadow)]"
    shadow="never"
    body-style="padding: 0; position: relative"
  >
    <!-- 图层 -->
    <div
      class="w-full h-full absolute top-0 z-8 bg-gradient-to-t from-white important-from-50% important-to-80% dark:from-[var(--el-bg-color-overlay)]"
    ></div>
    <div class="relative w-full h-[140px] flex-center pt-[60px]">
      <!-- 图片 -->
      <div
        class="dark:bg-right bg-[url(https://static.recall.top/%20sets/duitang.webp)] w-full h-full absolute top-0"
      ></div>
      <el-avatar
        :size="120"
        shape="circle"
        fit="cover"
        :src="global.getWebSite.headIcon"
        alt="头像"
        class="z-9 border-6 border-color-white border-solid"
      />
      <span
        class="w-[16px] h-[16px] rounded-9999 absolute bottom-[15px] right-[90px] z-10 border-solid border-5 border-color-white"
        :class="{ 'bg-[#6bdf8f]': !juiceIsOnline, 'bg-[#ccc]': juiceIsOnline }"
      />
    </div>
    <div class="text-center text-[1.15rem] px-[20px] pt-[20px] font-bold relative z-9">{{ getAuthor }}</div>
    <div class="text-[0.85rem] text-center important-py-0 important-px-[45px] lh-[20px] mt-[5px] relative z-9">
      {{ getMotto }}
    </div>
    <div class="h-[80px] px-[30px] pt-[5px] pb-[10px] relative z-9">
      <el-row :gutter="20" class="important-ml-0 h-full w-full">
        <el-col v-for="item in global.getWebSite.count" :span="24 / global.getWebSite.count.length">
          <div class="h-full pt-[12px] text-center text-[1.15rem] box-border">
            <p v-text="item.num" class="text-[1.25rem] text-center mb-[8px] cursor-pointer" @click="navToUrl(item)" />
            <p v-text="item.name" class="text-[0.9rem] text-center" />
          </div>
        </el-col>
      </el-row>
    </div>
  </el-card>
</template>

<script setup lang="ts">
import { useGlobalStore } from '@/stores/global';

const router = useRouter();
const global = useGlobalStore();

let onlineTime = ref<number>(1000 * 60 * 60);
const navToUrl = (item: any) => {
  let name = {
    文章: () => router.push('/list/article'),
    说说: () => router.push('/list/essay'),
    标签: () => router.push('/list/tag'),
  };
  name[item.name] && name[item.name]();
};
//获取作者
const getAuthor = computed(() => global.getWebSite.userName);
// 获取签名
const getMotto = computed(() => global.getWebSite.motto);
// 是否在线
const juiceIsOnline = computed(() => {
  return new Date().getTime() - global.getWebSite.loginUpdateTime > onlineTime.value;
});
</script>

<style scoped></style>
