<!--
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2024-12-28 16:55:33
 * @LastEditTime: 2025-01-13 10:28:29
 * @FilePath: /code/blog/client-web/src/layout/layout-header/bar/left/PhoneMenuItemButton.vue
 * @Description: 
 * Copyright (c) 2024 by tanxiang, All Rights Reserved. 
-->

<template>
  <div
    class="h-[3rem] flex items-center flex-row px-[0.75rem] rounded-[0.5rem] cursor-pointer"
    :class="{ active: isActive }"
    @click="toRoute()"
  >
    <auto-icon-park :name="props.item.icon" size="0.95rem" :fill="fill" :theme="props.item.theme" />
    <span
      class="pl-[0.3rem] font-size-[0.8rem] font-default dark:color-[#ccc]"
      :class="{ 'important-color-white': isActive }"
    >
      {{ props.item.name }}
    </span>
  </div>
</template>

<script setup lang="ts">
const route = useRoute();
const router = useRouter();
const global = useGlobalStore();
const changeDrawer = inject('changeDrawer') as Function;
const changeModelValue = inject('changeModelValue') as Function;

const props = defineProps({
  item: { type: Object, required: true }
});

const toRoute = () => {
  // 如果有子元素，则不跳转
  if (props.item.children && props.item.children.length > 0) return;
  if (props.item.redirect) return (window.location.href = window.location.origin + props.item.redirect);
  router.push(props.item.url);
  changeDrawer(false);
};

const isActive = computed(() => props.item.url === route.path);

const fill = computed(() => {
  if (isActive.value) {
    return '#fff';
  }

  if (!global.getTheme) {
    return '#ccc';
  }

  return props.item.fill;
});

onMounted(() => {
  // 表示自己的是子元素，打开自己的父类
  if (isActive.value && route.matched.length > 1) {
    changeModelValue(route.matched[0].name);
  }
});
</script>

<style lang="css" scoped>
.active {
  background-color: orange;
}

.dark .active {
  background-color: #a07321;
}
</style>
