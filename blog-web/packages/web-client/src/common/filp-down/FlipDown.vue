<!--
 * @Author: tanxiang 1571922819@qq.com
 * @Date: 2023-06-10 19:13:41
 * @Description:
 * @LastEditTime: 2023-12-03 18:12:25
 * @LastEditors: tanxiang 1571922819@qq.com
 * @FilePath: \blog\packages\web-client\src\common\filp-down\FlipDown.vue
 * @copyright: Copyright (c) 2023 by 1571922819@qq.com, All Rights Reserved.
-->
<template>
  <div
    id="flip-down"
    @click="clickFlipDown"
    @mouseover="isHover = true"
    @mouseleave="isHover = false"
    :style="[
      styles,
      setHideFlipDown,
      { '--bottom': `${bottom}px` },
      { 'animation-play-state': isHover ? 'paused' : 'running' },
    ]"
    class="absolute z-10 left-[49%] bottom-[var(--bottom)] drop-shadow-none"
  >
    <i class="iconfont icon-arrow-up text-[3rem] text-white rounded-full origin-center rotate-z-180" id="img-down" />
  </div>
</template>

<script lang="ts">
/**
 * @author: tanxiang
 *
 * @projectName: client-blog
 * @createTime: 2022/10/16 15:22
 * @description: 向下滚动的箭头，点击可以跳转到指定位置
 */

export default defineComponent({
  name: 'FlipDown',
  setup() {
    let isHover = ref<boolean>(false);
    let windowHeight = ref<number>(document.documentElement.clientHeight);
    return { windowHeight, isHover };
  },
  props: {
    theme: { type: Boolean, default: true },
    bottom: { type: Number, default: 30 },
    size: { type: Number, default: 24 },
    scrollTop: { type: Number, default: -1 },
    hideHeight: { type: Number, default: -1 },
    styles: {
      type: Object,
      default: () => {
        return {};
      },
    },
  },
  mounted() {
    window.addEventListener('resize', (event: any) => (this.windowHeight = event.currentTarget.innerHeight));
  },
  methods: {
    clickFlipDown() {
      this.$emit('toNode');
    },
  },
  computed: {
    setHideFlipDown() {
      // 当隐藏高度没有传入时，就根据可视窗口高度的四分之一来进行隐藏
      let isHide = this.hideHeight === -1 ? this.scrollTop >= this.windowHeight / 4 : this.scrollTop > this.hideHeight;
      return { display: isHide ? 'none' : 'block' };
    },
  },
});
</script>

<style scoped lang="css">
:root {
  --bottom: 0;
}

#flip-down {
  animation: swing 1.5s infinite linear;
  animation-direction: alternate;
  animation-timing-function: ease-out;
}

@keyframes swing {
  0% {
    bottom: var(--bottom);
    opacity: 0.3;
  }
  100% {
    bottom: calc(var(--bottom) + 10px);
    opacity: 1;
  }
}
</style>
