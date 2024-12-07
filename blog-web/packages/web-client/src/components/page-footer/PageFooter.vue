<!--
 * @Author: tanxiang 1571922819@qq.com
 * @Date: 2023-06-23 14:20:37
 * @Description:
 * @LastEditTime: 2023-12-02 20:52:43
 * @LastEditors: tanxiang 1571922819@qq.com
 * @FilePath: \blog\packages\web-client\src\components\page-footer\PageFooter.vue
 * @copyright: Copyright (c) 2023 by 1571922819@qq.com, All Rights Reserved.
-->
<template>
  <div
    id="page-footer"
    class="w-full font-light text-[0.975rem] px-[45px] lg:pt-[60px] pt-[40px] pb-[10px] 2xl:mt-[85px] lg:mt-[70px] mt-0 mb-0 mx-auto box-border"
  >
    <p class="foot-p">
      <span class="foot-span lg:text-left">@{{ getYear }}{{ getCurrentDate }} By {{ getWebSiteName }}</span>
      <span class="foot-span lg:text-right">本站已运行{{ time }}</span>
    </p>
    <p class="foot-p">
      <span class="foot-span lg:text-left flex-center lg:important-justify-start">
        <img :src="gongan" class="w-[16px] h-[16px]" alt="公安警徽" />
        {{ getPublicSecurityRegistrationNumber }}
      </span>
      <span class="foot-span lg:text-right">
        <a href="https://beian.miit.gov.cn/" target="_blank" class="text-black dark:text-white">
          {{ getForTheRecord }}
        </a>
      </span>
    </p>
  </div>
</template>

<script setup lang="ts">
import moment from 'moment';

import gongan from '@/assets/base64/gonggan';

import { useGlobalStore } from '@/stores/global';
import { useDate } from '@tanxiang/utils';

const global = useGlobalStore();
const { headway } = useDate();

const time = ref<string>('');
const timer = ref<NodeJS.Timeout>();

onMounted(() => nextTick(() => loopUpdateTime()));

const loopUpdateTime = () => {
  timer.value = setInterval(() => {
    time.value = global.getWebSite ? headway(global.getWebSite.createTime) : '';
    // loopUpdateTime();
  }, 1000);
};
// 获取当前的时间
const getCurrentDate = computed(() => {
  let ment = parseInt(moment().format('y'));
  return ment - getYear.value <= 0 ? '' : '-' + ment;
});
// 获取网站名称
const getWebSiteName = computed(() => global.getWebSite.userName);
// 网站备案号
const getForTheRecord = computed(() => global.getWebSite.forTheRecord);
// 公安备案号
const getPublicSecurityRegistrationNumber = computed(() => global.getWebSite.publicSecurityRegistrationNumber);
// 获取当前年份
const getYear = computed(() => new Date(global.getWebSite.createTime).getFullYear());
</script>

<style scoped></style>