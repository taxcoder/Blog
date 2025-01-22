<!--
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2024-12-31 10:58:06
 * @LastEditTime: 2025-01-15 02:49:45
 * @FilePath: /code/blog/client-web/src/views/home/topping/Index.vue
 * @Description: 
 * Copyright (c) 2024 by tanxiang, All Rights Reserved. 
-->

<template>
  <div class="mb-[2rem]">
    <prefix-info :icon="AnchorTwo" title="置顶"></prefix-info>
    <el-skeleton :loading="loading" animated :throttle="{ leading: 500, trailing: 500, initVal: true }">
      <template #template>
        <div class="grid gap-[0.3125rem]" :class="{ 'grid-cols-3': !isPhone, 'grid-cols-1': isPhone }">
          <el-skeleton-item variant="image" style="width: 100%; height: 15.3125rem" />
          <el-skeleton-item v-if="!isPhone" variant="image" style="width: 100%; height: 15.3125rem" />
          <el-skeleton-item v-if="!isPhone" variant="image" style="width: 100%; height: 15.3125rem" />
        </div>
      </template>
      <template #default>
        <div v-if="!isPhone" class="grid gap-[0.3125rem]" :class="[setGridCols]">
          <el-card
            shadow="always"
            body-style="padding: 0; height: 15.3125rem"
            v-for="item in props.toppings"
            @click="toJump(item.id)"
          >
            <home-topping-item :item="item" :phone="isPhone"></home-topping-item>
          </el-card>
        </div>
        <div v-else>
          <van-swipe class="my-swipe" :autoplay="3000" indicator-color="white" :height="245">
            <van-swipe-item v-for="item in props.toppings" :key="item.id">
              <topping-item :item="item" :phone="isPhone" @click="toJump(item.id)"></topping-item>
            </van-swipe-item>
          </van-swipe>
        </div>
      </template>
    </el-skeleton>
  </div>
</template>

<script setup lang="ts">
import { AnchorTwo } from '@icon-park/vue-next';
import { Topping } from '@/api/topping/topping.d';
import ToppingItem from './ToppingItem.vue';

const router = useRouter();

const loading = ref<boolean>(true);

const isPhone = inject('isPhone') as Ref<boolean>;

const props = defineProps({
  toppings: {
    type: Array<Topping>,
    default: () => []
  }
});

const toJump = (id: number) => {
  router.push('/articles/' + id);
};

const setGridCols = computed(() => {
  switch (props.toppings.length) {
    case 1:
      return { 'grid-cols-1': true };
    case 2:
      return { 'grid-cols-2': true };
    case 3:
    default:
      return { 'grid-cols-3': true };
  }
});

watch(
  () => props.toppings,
  (newVal: any) => {
    if (newVal.length > 0) loading.value = false;
  },
  { deep: true }
);
</script>

<style lang="css" scoped></style>
