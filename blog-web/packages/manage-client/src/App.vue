<!--
 * @Author: tanxiang 1571922819@qq.com
 * @Date: 2023-09-10 21:15:30
 * @Description:
 * @LastEditTime: 2023-12-01 20:34:46
 * @LastEditors: tanxiang 1571922819@qq.com
 * @FilePath: \blog\packages\manage-client\src\App.vue
 * @copyright: Copyright (c) 2023 by ${git_name_email}, All Rights Reserved.
-->
<template>
  <a-config-provider
    :locale="zhCN"
    :theme="{
      token: {
        fontFamily: 'sakura, sans-serif',
        borderRadius: 20,
        fontSize: 14,
      },
      components: {
        Tabs: {
          borderRadius: 5,
          borderRadiusLG: 8,
          fontSize: 13,
          padding: 12,
          colorFillAlter: '#ffffff',
          colorPrimary: '#ffffff',
          marginXS: 0,
        },
        Button: {
          borderRadius: 0,
          borderRadiusLG: 0,
          controlHeightLG: 50,
          paddingContentHorizontal: 10,
        },
        Table: {
          borderRadiusLG: 0,
        },
        Modal: {
          borderRadiusLG: 5,
          fontFamily: 'sakura, sans-serif',
        },
        Checkbox: {
          borderRadiusSM: 4,
        },
        Card: {
          borderRadiusLG: 8,
          paddingLG: 14,
          marginXS: 0,
        },
        Drawer: {
          paddingLG: 0,
        },
      },
    }"
  >
    <global-loading :hidden-loading="!base.getLoading"></global-loading>
    <div v-if="!base.getLoading" class="h-full">
      <inlet v-if="juiceIsLogin" />
      <router-view v-else name="login"></router-view>
    </div>
  </a-config-provider>
</template>
<script setup lang="ts">
import { Data } from '@/config';
import { useBaseStore } from '@/stores/base';
import { usePage } from '@tanxiang/utils';
import zhCN from 'ant-design-vue/es/locale/zh_CN';
import { messageInfo } from '@tanxiang/common';
import { useAuthWebStation } from '@tanxiang/apis';
import { GlobalLoading } from '@tanxiang/common';

const Inlet = defineAsyncComponent(() => import('@/views/Inlet.vue'));

const base: any = useBaseStore();
const auth = useAuthWebStation();

const { messageError } = messageInfo();

const router = useRouter();

const status = reactive<{
  width: number;
  height: number;
}>({
  width: 0,
  height: 0,
});

onBeforeMount(() => {
  // 如果没有token，则跳转到登录页面
  if (!localStorage.getItem('token')) return;
  // 否则去获取用户信息，如果token验证失败了，则表示token异常，跳转到登录页，否则进入系统
  base.setLoading(true);
  auth
    .webStationInfo()
    .then((success: any) => {
      base.setWebStation(success.data);
      base.setIsSuccess(true);
      let name = window.location.pathname;
      if (name === '/login') {
        router.replace('/analysis');
      } else {
        router.replace(name);
      }
    })
    .catch((error: any) => {
      messageError(!error || error.name ? '网络错误！' : error);
      localStorage.removeItem('token');
      base.setIsLogin(false);
      router.replace('/login');
    })
    .finally(() => setTimeout(() => base.setLoading(false), 500));
});

onMounted(() => {
  base.setBreakpoint(usePage().realWidth() <= Data.changeWidth);
  window.addEventListener('resize', (e) => {
    status.width = e.currentTarget['innerWidth'];
    status.height = e.currentTarget['innerWidth'];
  });
});

provide('status', status);

const juiceIsLogin = computed(() => !!base.getIsLogin && base.getIsSuccess);
</script>

<style></style>