<!--
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2024-12-20 14:19:30
 * @LastEditTime: 2025-01-13 08:38:55
 * @FilePath: /code/blog/client-web/src/views/archives/Archives.vue
 * @Description: 
 * Copyright (c) 2024 by tanxiang, All Rights Reserved. 
-->

<template>
  <prefix-info :icon="ReadBook" title="时光轴"></prefix-info>
  <div class="wh-full min-h-[10rem] rounded-lg overflow-hidden">
    <page-loading-light v-show="!(list.length > 0)" class="min-h-[20rem]"></page-loading-light>
    <div v-show="list.length > 0" class="min-h-[20rem] pb-4">
      <div v-for="(item, index) in list" class="select-none cursor-pointer">
        <div
          class="m-0 my-3.5 py-[1rem] font-size-[1.325rem] border-b-solid border-b-[0.0625rem] border-b-[rgba(68,68,68,0.1)] color-[#75794A] dark:color-[#acb273] flex-bc"
          @click="changeOpen(index)"
        >
          <span>{{ item }}{{ fetchs[item].data ? `(${fetchs[item].data.length})` : '' }}</span>
          <span class="loader" v-if="fetchs[item].loading"></span>
        </div>
        <el-collapse-transition>
          <div class="wh-full" v-show="fetchs[item].data && open[index]">
            <div
              v-for="fetch in fetchs[item].data"
              class="timeline box-border py-3 flex items-center pl-[3rem] cursor-pointer hover:bg-[rgba(240,240,240,0.5)] dark:hover:bg-[rgba(240,240,240,0.1)] rounded-[0.2rem] wh-full"
              @click="toJump(fetch.id)"
            >
              <span class="time color-[#888888] font-size-[0.85rem] mr-[1rem] w-[2.5rem]"
                >{{ time(fetch.createTime) }}
              </span>
              <span class="color-[#666666] dark:color-[#b9b9b9] font-size-[0.95rem] truncate">{{ fetch.title }}</span>
            </div>
          </div>
        </el-collapse-transition>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ReadBook } from '@icon-park/vue-next';

const router = useRouter();

const open = reactive<Boolean[]>([]);
const list = reactive<any[]>([]);

const { fetchRun, fetchs } = useFetchs(
  api.getArticlesByCreateTime,
  { manual: true },
  {
    fetchKey: (year: any) => year
  }
);

const archives = useRequest(() => api.getArticlesCreateTime(), {
  ready: true,
  cacheKey: 'archives',
  staleTime: 50000,
  onSuccess: () => {
    if (!Array.isArray(archives.data.value)) return;
    list.push(...archives.data.value);
  }
});
const toJump = (id: number) => {
  router.push('/article/' + id);
};

const time = (dateTime: number) => dateUtils.format(dateTime, 'MM-DD');
const changeOpen = (index: number) => (open[index] = !open[index]);

watchEffect(() => list.forEach((year: number) => fetchRun(year)));

watch(
  () => list,
  () => open.push(...list.map(() => true)),
  { deep: true }
);
</script>

<style lang="css" scoped>
.timeline {
  position: relative;
}

.timeline::before {
  content: '';
  width: 0.5rem;
  height: 0.1rem;
  background-color: #ffa500;
  transform: rotateZ(90deg);
  position: absolute;
  left: 1.25rem;
  top: 0.2rem;
}

.timeline:after {
  content: '';
  width: 0.5rem;
  height: 0.1rem;
  background-color: #ffa500;
  transform: rotateZ(90deg);
  position: absolute;
  left: 1.25rem;
  bottom: 0.2rem;
}

.time::before {
  content: '';
  width: 0.5rem;
  height: 0.5rem;
  border-radius: 50%;
  background-color: #ffa500;
  display: block;
  position: absolute;
  left: 1.25rem;
  top: 0.95rem;
  transition: transform 250ms;
}

.timeline:hover .time::before {
  transform: scale(1.75);
  background-color: #b07200;
}

.loader {
  width: 1rem;
  height: 1rem;
  border-radius: 50%;
  border-top: 0.1875rem solid orange;
  border-right: 0.1875rem solid transparent;
  -webkit-animation: rotation 1s linear infinite;
  animation: rotation 1s linear infinite;
}

@keyframes rotation {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}
</style>
