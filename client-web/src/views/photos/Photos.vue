<!--
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2025-01-03 17:06:23
 * @LastEditTime: 2025-01-14 05:43:05
 * @FilePath: /code/blog/client-web/src/views/photos/Photos.vue
 * @Description: 
 * Copyright (c) 2025 by tanxiang, All Rights Reserved. 
-->
<template>
  <div class="mt-[1.5rem] h-full flex" ref="domRef">
    <div
      class="relative inline-flex overflow-hidden flex-cc important-ml-0 important-font-size-[0.95rem] mx-[0.5rem] h-[2rem] w-[5rem] cursor-pointer rounded-[0.35rem] select-none mb-[0.75rem]"
      :class="[
        { 'photo-category color-white': active === item.id },
        { 'hover:bg-[rgba(242,242,242,0.75)] dark:hover:bg-[rgba(242,242,242,0.25)]': active !== item.id }
      ]"
      v-for="item in buttons"
      @click="changeCategory(item)"
    >
      <span class="z-99">{{ item.name }}</span>
    </div>
  </div>
  <page-loading-light
    v-if="list.length === 0"
    class="important-h-[20rem] dark:bg-[rgb(140 117 75)] rounded-[0.4375rem]"
  ></page-loading-light>
  <Waterfall :list="list" :background-color="global.getTheme ? '#fff' : 'rgb(40, 40, 40)'">
    <template #default="{ item, index }">
      <div class="card [&_span]:hover:transform-translate-y-0 overflow-hidden relative" @click="imagePreview(index)">
        <span
          class="absolute z-9 wh-full flex items-end justify-center pb-[1rem] transition-all-300 box-border transform-translate-y-10 color-white cursor-pointer select-none"
          >{{ item.name }}</span
        >
        <LazyImg :url="item.url" />
      </div>
    </template>
  </Waterfall>
  <div class="wh-full py-[3rem] flex-cc" v-show="list.length > 0 && show">
    <el-button
      type="warning"
      :color="global.getTheme ? '' : '#a2731e'"
      round
      @click="loadMore"
      :disabled="loading"
      class="transition-all"
    >
      <span>加载更多 </span>
      <Loading class="ml-[0.5rem]" size="1.25rem" color="#fff" v-show="loading" />
    </el-button>
  </div>
</template>

<script setup lang="ts">
import { Loading } from 'vant';
import { api as viewerApi } from 'v-viewer';
import { LazyImg, Waterfall } from 'vue-waterfall-plugin-next';

const global = useGlobalStore();

const route = useRoute();
const router = useRouter();
const index = ref(1);
const limit = ref(30);
const show = ref(false);
const change = ref<boolean>(false);
const list = reactive<Array<{ imageId: number; name: string; url: string }>>([]);
const images = reactive<Array<string>>([]);
const active = ref<number>(-1);
const buttons = reactive<Array<{ id: number; name: string }>>([{ id: -1, name: '全部' }]);

useRequest(() => api.getPhotoInit(), {
  onSuccess: (data) => {
    buttons.push(...data.map((m: any) => ({ id: m.id, name: m.name })));
    let find = buttons.find((button: any) => button.name === route.params.name);
    if (find && find.id) {
      active.value = find.id;
    } else {
      active.value = -1;
    }
    loadMore();
  }
});

const { loading, loadMore, reload } = useInfiniteScroll(() => selectRequestFunction(), {
  isNoMore: (result) => !!result && Math.ceil(result.total / limit.value) <= index.value,
  onSuccess: (data) => {
    index.value++;
    list.push(...data.list);
    images.push(...data.list.map((item: any) => item.url));
    useTimeout(() => (show.value = true), 1000);
  },
  onError: () => {
    notifyUtils.error('获取图片失败!');
  },
  onFinally: () => {
    change.value = false;
  }
});

const selectRequestFunction = () => {
  if (active.value === -1) {
    return api.getPhotosResult(index.value, limit.value);
  } else {
    return api.getPhotoById(active.value, index.value, limit.value);
  }
};

const changeCategory = (item: { id: number; name: string }) => {
  reload();
  change.value = true;
  active.value = item.id;
  index.value = 1;
  list.splice(0, list.length);
  images.splice(0, images.length);
};

const imagePreview = (index: number) => {
  viewerApi({
    images: images,
    options: {
      initialViewIndex: index,
      title: false,
      inline: true
    }
  });
};
</script>

<style lang="css" scoped>
.photo-category:before {
  content: '';
  width: 100%;
  height: 100%;
  background-color: orange;
  position: absolute;
  z-index: 9;
  transform: translateX(-5rem);
  animation: active 0.15s ease-in-out forwards;
}

.dark .photo-category:before {
  background-color: #a26e0f;
}

.card:before {
  content: '';
  width: 100%;
  height: 100%;
  position: absolute;
  z-index: 9;
  transition: background-color 0.4s;
  background-color: rgba(0, 0, 0, 0);
}

.card:hover:before {
  background-color: rgba(0, 0, 0, 0.65);
}

@keyframes active {
  0% {
    transform: translateX(-5rem);
  }
  100% {
    transform: translateX(0);
  }
}
</style>
