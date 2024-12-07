<template>
  <div
    class="bg-white dark:important-bg-[#2d2d2d] rounded-[7px] important-px-[20px] important-pt-[5px] important-pb-[20px] mt-[15px]"
  >
    <div v-for="d in data">
      <div class="text-[24px] font-bold py-[20px]" :id="'navigation-' + d.name">{{ d.name }}</div>
      <n-grid item-responsive :cols="getGridCols" :y-gap="10" :x-gap="10">
        <n-grid-item v-for="item in d.datas" class="cursor-pointer">
          <n-card embedded hoverable header-class="navigation-header-card" tag="a" :href="item['url']" target="_blank">
            <el-text
              size="large"
              class="h-[45px] important-text-[#1c1c1c] dark:important-text-white block"
              line-clamp="2"
            >
              {{ item['description'] }}
            </el-text>
            <template #header>
              <el-text truncated class="important-text-[#1c1c1c] dark:important-text-white important-text-[18px]">
                {{ item['title'] }}
              </el-text>
            </template>
          </n-card>
        </n-grid-item>
      </n-grid>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useNavigation } from '@tanxiang/apis';
import { messageInfo } from '@tanxiang/common';
import { useNavigationStore } from '@/stores/navigation';

const navigation = useNavigation();
const navigationStore = useNavigationStore();
const { messageError } = messageInfo();

const status = inject('status');

const data = reactive<
  {
    name: string;
    datas: any[];
  }[]
>([]);

onMounted(() => {
  navigation
    .selectAll()
    .then((res: any) => {
      res.data.forEach((item: any) => {
        if (data.length === 0 || !data.find((d) => d.name === item.classificationNavigationName)) {
          data.push({ name: item.classificationNavigationName, datas: [item] });
        } else {
          data.map((d) => {
            if (d.name === item.classificationNavigationName) d.datas.push(item);
            return d;
          });
        }
      });
      navigationStore.setDatas(data.map((d) => d.name));
    })
    .catch((err: any) => messageError(!err || err.name ? '网络错误' : err));
});

const getGridCols = computed(() => {
  return Math.floor(status.width / 400);
});
</script>

<style scoped></style>