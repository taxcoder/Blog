<!--
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2024-12-20 10:50:05
 * @LastEditTime: 2025-01-15 02:46:42
 * @FilePath: /code/blog/client-web/src/views/categorys/Categorys.vue
 * @Description: 
 * Copyright (c) 2024 by tanxiang, All Rights Reserved. 
-->
<template>
  <el-card body-class="min-h-[15rem] flex-cc important-p-0" class="important-rounded-[0.4rem]">
    <page-loading-light
      v-if="loading"
      class="important-h-[20rem] dark:bg-[rgb(140 117 75)] rounded-[0.4375rem]"
    ></page-loading-light>
    <div class="wh-full p-4 py-8 place-self-start inline-block text-center select-none" v-else>
      <div
        v-for="(item, index) in data"
        :key="item.id"
        class="inline-flex p-2.5 mr-[0.5rem] mb-[0.5rem] last:mr-0 rounded-[5px] font-size-[0.9rem] font-bold color-white cursor-pointer shadow-xl hover:opacity-60 transition-all duration-150 whitespace-pre dark:filter-brightness-90"
        :style="`background-color: ${index >= color.length ? color[Math.floor(index % color.length)] : color[index]}`"
        @click="toJump(item)"
      >
        {{ item.name }} {{ item.count }}
      </div>
    </div>
  </el-card>
  <el-card
    v-if="!loading"
    body-class="flex-cc  flex-col important-px-0 important-pb-0 important-pt-8"
    class="mt-4 important-rounded-[0.4rem]"
  >
    <div class="font-size-[2rem] dark:filter-brightness-90">文章分类 饼图</div>
    <vue-echart :series="series" :update="update"></vue-echart>
  </el-card>
</template>

<script setup lang="ts">
import { getCategorys } from '@/api/categorys/category';
import { color } from '@/constant/color';

const update = ref<boolean>(false);
const router = useRouter();
const category = useCategoryStore();

const series = reactive<{
  type: string;
  radius: string;
  label: any;
  data: any[];
}>({
  type: 'pie',
  radius: '50%',
  label: {
    backgroundColor: 'transparent',
    color: 'inherit'
  },
  data: []
});

onActivated(() => {
  update.value = !update.value;
});

const { data, loading } = useRequest(() => getCategorys(), {
  cacheKey: 'category-list',
  staleTime: 50000,
  onSuccess: () => {
    if (!data.value) return;
    category.setCategorys(data.value);
    series.data.push(...data.value.map((m: any) => ({ value: m.count, name: m.name })));
  }
});

const toJump = (item: any) => {
  router.push(`/categorys/${item.id}`);
};
</script>

<style lang="css" scoped></style>
