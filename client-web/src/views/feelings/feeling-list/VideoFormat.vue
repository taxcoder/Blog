<!--
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2025-01-06 14:33:54
 * @LastEditTime: 2025-01-07 04:24:58
 * @FilePath: /code/blog/client-web/src/views/feeling/VideoFormat.vue
 * @Description: 
 * Copyright (c) 2025 by tanxiang, All Rights Reserved. 
-->
<!--
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2025-01-05 11:59:51
 * @LastEditTime: 2025-01-05 12:54:55
 * @FilePath: /code/blog/client-web/src/views/feeling/ImageFormat.vue
 * @Description: 
 * Copyright (c) 2025 by tanxiang, All Rights Reserved. 
-->

<template>
  <div class="pt-[0.75rem]">
    <vue3VideoPlay
      width="100%"
      height="100%"
      :src="props.videoUrl"
      :type="videoType(props.videoUrl)"
      :controlBtns="controlBtns"
    />
  </div>
</template>

<script setup lang="ts">
const isPhone = inject<any>('isPhone');

const props = defineProps({
  videoUrl: {
    type: String,
    require: true
  }
});

const videoType = (url: any) => {
  if (url.indexOf('.mp4') !== -1) {
    return 'video/mp4';
  } else if (url.indexOf('.flv') !== -1) {
    return 'video/flv';
  } else if (url.indexOf('.m3u8') !== -1) {
    return 'video/m3u8';
  }
};

const controlBtns = computed(() => {
  return isPhone.value
    ? ['audioTrack', 'volume', 'pageFullScreen']
    : ['audioTrack', 'quality', 'speedRate', 'volume', 'setting', 'pip', 'pageFullScreen', 'fullScreen'];
});
</script>

<style lang="css" scoped></style>
