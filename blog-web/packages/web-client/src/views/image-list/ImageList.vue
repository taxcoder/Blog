<template>
  <div class="mx-auto max-w-[1200px]">
    <div v-for="image in imageGroup(images)">
      <div class="text-[20px] mt-[20px] mb-[10px]">{{ getDateTime(image['uploadTime']) }}</div>
      <n-space :size="5">
        <el-image
          v-for="img in image['images']"
          :src="img.image"
          :key="img.id"
          lazy
          :style="getSize"
          :preview-src-list="[img.image]"
          :hide-on-click-modal="true"
          :preview-teleported="true"
          :fit="'cover'"
        >
          <template #placeholder>
            <div :style="getSize" class="flex-center bg-[#0001] dark:bg-[#333333] rounded-[5px]">
              <van-loading />
            </div>
          </template>
          <template #error>
            <div :style="getSize" class="flex-center bg-[#0001] dark:bg-[#333333] rounded-[5px]">
              <el-icon>
                <icon-picture />
              </el-icon>
            </div>
          </template>
        </el-image>
      </n-space>
    </div>
  </div>
</template>

<script setup lang="ts">
import dayjs from 'dayjs';
import CustomParseFormat from 'dayjs/plugin/CustomParseFormat.js';
import { Picture as IconPicture } from '@element-plus/icons-vue';
import { useBaseStore } from '@/stores/base';
import { onMounted, computed } from 'vue';
import { useImages } from '@tanxiang/apis';
import { messageInfo } from '@tanxiang/common';

dayjs.extend(CustomParseFormat);

const useImage = useImages();
const { messageError } = messageInfo();

const base = useBaseStore();
const status = inject('status');
const imageWidth = ref<number>(0);
const imageHeight = ref<number>(0);

const images = reactive([]);

onMounted(() => {
  useImage
    .imagesList()
    .then((res: any) => images.push(...res.data))
    .catch((err: any) => messageError(err.message || '获取图片失败'));
});

const getDateTime = (time: any) => {
  let day = dayjs(time, 'YYYY年MM月DD日');
  if (dayjs().isSame(day, 'day')) {
    return '今天';
  } else if (dayjs().subtract(1, 'day').isSame(day, 'day')) {
    return '昨天';
  } else if (dayjs().year() === day.year()) {
    return day.format('MM月DD日');
  }
  return time;
};

const imageGroup = computed(() => (data: any[]) => {
  data.sort((a: any, b: any) => new Date(b.uploadTime).getTime() / 1000 - new Date(a.uploadTime).getTime() / 1000);
  let ret = [];
  data.forEach((item: any) => {
    let time = dayjs.unix(new Date(item.uploadTime).getTime() / 1000).format('YYYY年MM月DD日');
    // 如果数组内没有数据，直接添加
    if (ret.length === 0) {
      ret.push({ uploadTime: time, images: [{ id: item.id, image: item.url }] });
      return;
    }
    //索引位置，如果索引没有变化，则表示当前数据在数组内不存在
    let indexList = -1;
    // 遍历数组
    ret.forEach((i: any, index: number) => {
      if (i.uploadTime === time) indexList = index;
      return;
    });
    // 如果没有则直接添加，否则在索引位置添加
    if (indexList !== -1) {
      ret[indexList].images.push({ id: item.id, image: item.url });
    } else {
      ret.push({ uploadTime: time, images: [{ id: item.id, image: item.url }] });
    }
  });
  return ret;
});

const getSize = computed(() => {
  return {
    width: imageWidth.value + 'px',
    height: imageHeight.value + 'px',
  };
});
watch(
  () => status.width,
  () => {
    let width = status.width / document.body.style.getPropertyValue('--zoom');
    if (width - 30 <= 450) {
      imageWidth.value = imageHeight.value = (width - 30 - 10) / 3;
    } else if (width - 30 <= 600) {
      imageWidth.value = imageHeight.value = (width - 30 - 15) / 4;
    } else if (width - 30 <= 900) {
      imageWidth.value = imageHeight.value = (width - 30 - 30) / 6;
    } else if (width - 30 <= 1200) {
      imageWidth.value = imageHeight.value = (width - 30 - 30) / 6;
    } else if (base.getOpenActive) {
      imageWidth.value = imageHeight.value = (900 - 20) / 5;
    } else {
      imageWidth.value = imageHeight.value = 195.84;
    }
  },
  { deep: true, immediate: true }
);

watch(
  () => base.getOpenActive,
  () => {
    imageWidth.value = imageHeight.value = (900 - 20) / 5;
  }
);
</script>

<style scoped></style>