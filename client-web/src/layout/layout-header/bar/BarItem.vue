<!--
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2024-12-20 13:59:38
 * @LastEditTime: 2025-01-14 06:52:52
 * @FilePath: /code/blog/client-web/src/layout/layout-header/bar/BarItem.vue
 * @Description: 
 * Copyright (c) 2024 by tanxiang, All Rights Reserved. 
-->
<template>
  <div class="flex-cc wh-full">
    <!-- 导航栏 -->
    <ul
      id="layout-header-bar-items"
      class="wh-full flex-cc flex-auto p-0 md:max-lg:pl-[0.625rem] md:max-lg:justify-start color-[#282828] font-[fangsong] font-700 transition-justify-content"
    >
      <li
        v-for="(item, index) in global.getRoutes"
        @click="toRoute(item)"
        class="h-full w-[var(--item-width)] flex-cc cursor-pointer relative [&_div.line]:hover:color-[var(--theme-global-color)] [&_div.line]:hover:transform-translate-x-0 overflow-hidden m-[5px] mb-0 mt-0 relative"
      >
        <div
          :class="[isActive(item), { open: isOpen(visible, index) }]"
          @mouseenter="mouseEnterParent(visible, index)"
          @mouseleave="mouseLeaveParent(visible, index)"
          class="font-size-[1.1rem] [&.active]:color-[var(--theme-global-color)] [&.active]:dark:important-color-[#d38900] dark:color-[#868888] h-full flex-cc grow-1"
        >
          <auto-icon-park
            class="pack transform-translate-y-[0]"
            :name="item.icon"
            :size="item.size"
            :fill="fill(item, visible, index)"
            :theme="theme(item, visible, index)"
          ></auto-icon-park>
          <span class="ml-[0.275rem]">{{ item.name }}</span>
        </div>
        <div
          v-if="item.children.length > 0"
          class="fixed top-13.5 z-999"
          @mouseenter="mouseEnterParent(visible, index)"
          @mouseleave="mouseLeaveParent(visible, index)"
        >
          <transition name="secondary" mode="default">
            <ul
              v-show="visible[index]['status']"
              class="secondary-level font-size-[1.125rem] bg-white dark:bg-[#252525] px-[0.3125rem] py-[0.25rem] min-w-[4.5rem] rounded-[0.4375rem]"
            >
              <li
                class="py-[0.3125rem] h-[2.5rem] flex-cc hover:color-[var(--theme-global-color)] border-transparent [&]:not-last:border-1 [&]:not-last:border-solid [&]:not-last:border-b-slate-200 dark:[&]:not-last:border-b-slate-500 dark:color-[#868888]"
                v-for="(i, x) in item.children"
                @click="toRoute(i)"
                @mouseenter="mouseEnter(visible[index].children, x)"
                @mouseleave="mouseLeave(visible[index].children, x)"
              >
                <div>
                  <auto-icon-park
                    :name="i.icon"
                    :size="i.size"
                    :fill="fill(i, visible[index].children, x)"
                    :theme="theme(i, visible[index].children, x)"
                  >
                  </auto-icon-park>
                  <span class="ml-[0.275rem] font-size-[1rem]" :class="routerActive(i, visible[index].children, x)">{{
                    i.name
                  }}</span>
                </div>
              </li>
            </ul>
          </transition>
        </div>
        <div
          :class="isActive(item)"
          class="w-full h-[0.25rem] bottom-0 transition-transform-300 [&.active]:transform-translate-x-0 transform-translate-x-[calc(-1.1*var(--item-width))] absolute bg-[var(--theme-global-color)] rounded-lg"
        ></div>
      </li>
    </ul>
  </div>
</template>

<script setup lang="ts">
const route = useRoute();
const router = useRouter();
const global = useGlobalStore();

const visible = reactive<Array<{ status: boolean; children: Array<any> }>>([]);

const toRoute = (item: any) => {
  if (item.children.length > 0) return;
  if (item.redirect && item.redirect.endsWith('/')) {
    window.open(location.origin + item.redirect, '_blank');
  } else if (item.redirect) {
    router.push(item.redirect);
  } else {
    router.push(item.url);
  }
};
const mouseEnter = (visibleData: any, index: number) => (visibleData[index]['status'] = true);
const mouseLeave = (visibleData: any, index: number) => (visibleData[index]['status'] = false);
const mouseEnterParent = (visibleData: any, index: number) => {
  visibleData[index]['status'] = true;
  if (visibleData[index]['timeout']) clearTimeout(visibleData[index]['timeout']);
};
const mouseLeaveParent = (visibleData: any, index: number) => {
  if (visibleData[index]['children'].length > 0) {
    visibleData[index]['timeout'] = setTimeout(() => (visibleData[index]['status'] = false), 100);
  } else {
    visibleData[index]['status'] = false;
  }
};

const condition = (flag: boolean, success: any, error: any) => {
  return flag ? success : error;
};

const isActive = computed(() => (item: any) => {
  let status = route.name === item.name || isList.value(item);
  return { line: !status, active: status };
});

const isOpen = computed(() => (visibleData: any, index: number) => {
  return visibleData[index]['status'];
});

const isList = computed(() => (item: any) => {
  return item.name === '清单' && /\/(category|tag)(s){0,1}(\/\\d+){0,1}/.test(route.path);
});

const theme = computed(() => (item: any, visibleData: any, index: number) => {
  return route.name === item.name || isList.value(item) || visibleData[index]['status'] ? 'filled' : 'filled';
});

const routerActive = computed(() => (item: any, visibleData: any, index: number) => {
  return route.name === item.name || isList.value(item) || visibleData[index]['status'] ? 'color-orange' : '';
});

const fill = computed(() => (item: any, visibleData: any, index: number) => {
  return route.name === item.name || isList.value(item) || visibleData[index]['status']
    ? condition(global.getTheme, item.activeFill, item.activeDarkFill)
    : condition(global.getTheme, item.fill, item.darkFill);
});

const initVisible = (routes: any) => {
  return routes.map((item: any) => {
    return condition(
      item.children && item.children.length > 0,
      { status: false, timeout: null, children: initVisible(item.children) },
      { status: false, timeout: null, children: [] }
    );
  });
};

// 初始化数据
watch(
  () => global.getRoutes,
  (newVal) => visible.push(...initVisible(newVal)),
  { deep: true }
);

watch(
  () => visible,
  (newVal) => {
    if (newVal.length === 0 && global.getRoutes.length > 0) visible.push(...initVisible(global.getRoutes));
  },
  { deep: true, immediate: true }
);
</script>

<style lang="css" scoped>
#layout-header-bar-items {
  --item-width: 5rem;
}

.secondary-level {
  box-shadow: 0 0 19px -5px #b4b4b4;
}

.dark .secondary-level {
  box-shadow: 0 0 19px -5px #000000 !important;
}

.open .pack {
  animation: pack 75ms 5;
}

.bar-active #layout-header-bar-items {
  animation: bar-active 1.25s forwards;
}

@-webkit-keyframes pack {
  0%,
  100% {
    transform: translateY(0);
  }
  25% {
    transform: translateY(-0.15rem);
  }
  75% {
    transform: translateY(0.15rem);
  }
}

@keyframes pack {
  0%,
  100% {
    transform: translateY(0);
  }
  25% {
    transform: translateY(-0.15rem);
  }
  75% {
    transform: translateY(0.15rem);
  }
}

@-webkit-keyframes bar-active {
  from {
    transform: translateX(2rem);
  }
  to {
    transform: translateX(0);
  }
}

@keyframes bar-active {
  from {
    transform: translateX(2rem);
  }
  to {
    transform: translateX(0);
  }
}

.secondary-enter-active,
.secondary-leave-active {
  transition: transform 0.55s ease;
}
.secondary-enter-from,
.secondary-leave-to {
  opacity: 0;
  transform: translateY(0.9375rem);
}
</style>
