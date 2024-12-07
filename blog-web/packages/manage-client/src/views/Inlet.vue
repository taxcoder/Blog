<template>
  <a-layout style="height: 100%" class="flex-row">
    <layout-sider />
    <a-layout>
      <layout-header />
      <a-layout-content>
        <div class="manage-layout-content">
          <router-view v-slot="{ Component, route }">
            <transition :name="name(route)">
              <keep-alive>
                <component :is="Component" :key="route.path" v-if="route.meta.keepAlive" />
              </keep-alive>
            </transition>
          </router-view>
          <router-view v-slot="{ Component, route }">
            <transition name="slide-fade">
              <component :is="Component" :key="route.path" v-if="!route.meta.keepAlive" />
            </transition>
          </router-view>
        </div>
      </a-layout-content>
    </a-layout>
  </a-layout>
</template>

<script setup lang="ts">
import LayoutSider from '@/views/layout-sider/LayoutSider.vue';
import LayoutHeader from '@/views/layout-header/LayoutHeader.vue';
import { computed, ref } from 'vue';
import { useBaseStore } from '@/stores/base';
import { useAuthWebStation } from '@tanxiang/apis';
import { messageInfo } from '@tanxiang/common';

const { messageError } = messageInfo();

const base = useBaseStore();
const prevPath = ref<string>('');
const auth = useAuthWebStation();
const router = useRouter();

onMounted(() => {
  if (base.getWebStation.id) return;
  base.setLoading(true);
  auth
    .webStationInfo()
    .then((success: any) => {
      base.setWebStation(success.data);
      base.setIsSuccess(true);
    })
    .catch((error: any) => {
      messageError(!error || error.name ? '网络错误！' : error);
      localStorage.removeItem('token');
      base.setIsLogin(false);
      router.replace('/login');
    })
    .finally(() => base.setLoading(false));
});

router.beforeEach((to, from, next) => {
  prevPath.value = from.fullPath;
  next();
});

const name = computed(() => (route: any) => {
  return prevPath.value.indexOf('recovery') !== -1 && route.path.indexOf('recovery') !== -1 ? '' : 'slide-fade';
});
</script>

<style scoped>
.manage-layout-content {
  height: 100%;
  overflow: auto;
}

/* 谷歌，元素隐藏必须设置宽度  不然无效 */
.manage-layout-content::-webkit-scrollbar {
  width: 0;
  height: 30px;
}

/* 使用伪类选择器 ::-webkit-scrollbar ,兼容chrome和safari浏览器 */
/* display: none; 只能直接作用于body html，其他元素设置宽度的方式 */
.manage-layout-content::-webkit-scrollbar {
  display: none;
}

/* /兼容火狐/ */
/* / 兼容IE10+ */
.manage-layout-content {
  scrollbar-width: none;
  -ms-overflow-style: none;
}

.slide-fade-enter-active {
  transition: all 0.5s ease-in-out;
}

.slide-fade-enter-from,
.slide-fade-leave-to {
  transform: translateX(-25px);
  opacity: 0;
}
</style>

<style></style>