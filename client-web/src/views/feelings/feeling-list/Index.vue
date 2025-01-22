<!--
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2025-01-05 11:06:08
 * @LastEditTime: 2025-01-11 09:26:40
 * @FilePath: /code/blog/client-web/src/views/feelings/feeling-list/Index.vue
 * @Description: 
 * Copyright (c) 2025 by tanxiang, All Rights Reserved. 
-->

<template>
  <div class="flex max-md:py-[1rem] md:py-[2rem] max-md:flex-col md:flex-row">
    <div class="w-[5rem] max-md:px-[0.5rem] max-md:pb-[0.5rem]">
      <el-avatar :size="50" :src="props.feeling.headImg" shape="square" />
    </div>
    <div class="flex-auto max-md:px-[0.5rem] md:px-[1rem]">
      <div class="flex flex-col h-[2.5rem]">
        <div class="flex-1 flex items-center justify-start font-size-[1.10rem] color-[#44507B] dark:color-[#7389d8]">
          {{ props.feeling.author }}
        </div>
        <div class="flex-1 flex items-center justify-start font-size-[0.85rem] color-[#AAB0BF]">
          {{ time(props.feeling.createTime) }}
        </div>
      </div>
      <div class="pt-[1.25rem] pb-[1.5rem] dark:color-[#dadada]">
        <div v-html="props.feeling.content" class="text-base"></div>
        <div
          v-if="
            Array.isArray(props.feeling.imagesUrl) &&
            props.feeling.imagesUrl.length > 0 &&
            isEmptyStringRegex(props.feeling.videosUrl)
          "
        >
          <image-format :image-url="props.feeling.imagesUrl"></image-format>
        </div>
        <div v-if="!isEmptyStringRegex(props.feeling.videosUrl) && props.feeling.imagesUrl.length === 0">
          <video-format :video-url="props.feeling.videosUrl"></video-format>
        </div>
      </div>
      <div
        class="flex-bc py-[0.65rem] px-[1.1rem] bg-[#f6f7ff] dark:bg-[#404040] rounded-md select-none"
        v-if="route.path === '/feelings'"
      >
        <div class="flex flex-row items-center cursor-pointer" @click="addLike(props.feeling.id)">
          <icon-like
            theme="outline"
            size="0.85rem"
            :fill="global.getTheme ? '#44507b' : '#ccc'"
            v-show="!likes.includes(props.feeling.id)"
          />
          <icon-like
            class="relative top-[-0.0625rem]"
            theme="filled"
            size="0.85rem"
            fill="#ff0000"
            v-show="likes.includes(props.feeling.id)"
          />
          <span class="font-size-[0.8rem] dark:color-[#ccc] color-[#44507b] ml-[0.125rem]">{{
            props.feeling.likeCount + count
          }}</span>
        </div>
        <el-button color="#000" size="small" class="min-h-[1.5rem] rounded-[0.3125rem]" @click="buttonClick()">
          <icon-message-one theme="outline" size="0.8rem" fill="#fff" class="relative top-[0.0625rem]" />
          <span class="font-size-[0.95rem] color-[#fff] ml-[0.25rem]">
            <span class="artalk-comment-count" :data-page-key="`/feeling/${props.feeling.id}`">0</span>
          </span>
        </el-button>
      </div>
      <slot></slot>
    </div>
  </div>
</template>

<script setup lang="ts">
import ImageFormat from './ImageFormat.vue';
import VideoFormat from './VideoFormat.vue';

import { loadCountWidget } from '@/components/vue-artalk';

const global = useGlobalStore();
const route = useRoute();
const router = useRouter();

const isPhone = inject('isPhone');

const notify = notifyUtils(isPhone);
const [state, setState] = useCookieState('feeling_like');

const count = ref<number>(0);
const likes = reactive<number[]>([]);

const props = defineProps({
  feeling: { type: Object as any, default: () => {} }
});

onMounted(() => {
  nextTick(() => {
    loadCountWidget();
  });
});

const buttonClick = () => {
  if (route.path === '/feelings') {
    router.push(`/feeling/${props.feeling.id}`);
  }
};

const time = (time: number) => {
  return dateUtils.format(time, 'YYYY年MM月DD日');
};

const isEmptyStringRegex = (str: any) => {
  return !str || /^\s*$/.test(str);
};

const addLike = (id: number) => {
  if (notify.messageLoading.value === true) return;
  if (likes.includes(id)) return;
  useRequest(() => api.addFeelingLikeCount(id), {
    onBefore: () => {
      notify.loading('请稍后');
      likes.push(id);
      count.value++;
    },
    onSuccess: () => {
      notify.success('点赞成功!');
    },
    onError: (error) => {
      console.log(error);
      likes.splice(likes.indexOf(id), 1);
      count.value--;
    }
  });
};

watch(
  () => state.value,
  (newVal: any) => {
    try {
      likes.splice(0, likes.length);
      likes.push(...JSON.parse(newVal));
    } catch (error) {
      setState(JSON.stringify(likes));
    }
  },
  { deep: true, immediate: true }
);
</script>

<style lang="css" scoped>
.out-enter-active .inner,
.out-leave-active .inner {
  transition: all 0.3s ease-in-out;
}

.out-enter-from .inner,
.out-leave-to .inner {
  transform: translateX(30px);
  opacity: 0;
}
</style>
