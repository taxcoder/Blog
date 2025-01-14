<!--
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2024-12-28 11:30:57
 * @LastEditTime: 2025-01-13 10:28:37
 * @FilePath: /code/blog/client-web/src/layout/layout-header/bar/left/PhoneMenuItem.vue
 * @Description: 
 * Copyright (c) 2024 by tanxiang, All Rights Reserved. 
-->
<template>
  <div class="menu w-full bg-[#fff] dark:bg-[#232323] rounded-[0.45rem] px-[1rem] py-[0.5rem] box-border">
    <div v-for="item in global.getRoutes" class="pb-[0.125rem]">
      <div v-if="item.children && item.children.length">
        <el-collapse v-model="modelValue" accordion style="--el-border-color-lighter: none">
          <el-collapse-item :title="item.name" :name="item.name">
            <template #title>
              <layout-header-bar-left-phone-menu-item-button :item="item" />
            </template>
            <layout-header-bar-left-phone-menu-item-button
              v-for="i in item.children"
              :item="i"
              class="important-pl-[1.75rem]"
            />
          </el-collapse-item>
        </el-collapse>
      </div>
      <layout-header-bar-left-phone-menu-item-button :item="item" v-else />
    </div>
  </div>
</template>

<script setup lang="ts">
const global = useGlobalStore();

const modelValue = ref<string>('');

provide('changeModelValue', (value: string) => {
  modelValue.value = value;
});
1;
</script>

<style lang="css" scoped>
.menu {
  box-shadow: 0 0px 10px -5px #949494;
}

.dark .menu {
  box-shadow: 1px 1px 3px 1px #1b1b1b;
}
</style>
