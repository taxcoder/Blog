<!--
 * @Author: tanxiang 1571922819@qq.com
 * @Date: 2023-06-21 10:43:37
 * @Description:
 * @LastEditTime: 2023-12-03 19:22:32
 * @LastEditors: tanxiang 1571922819@qq.com
 * @FilePath: \blog\packages\web-client\src\components\list-article\CardContent.vue
 * @copyright: Copyright (c) 2023 by 1571922819@qq.com, All Rights Reserved.
-->
<template>
  <div class="article-card-content h-[330px] overflow-hidden grid relative transition-all duration-300">
    <!-- 具体的内容 -->
    <card-content-item v-if="juiceIsShow" :item="item" />
    <div class="article-img h-full overflow-hidden relative">
      <el-image :lazy="true" class="w-full h-full" fit="cover" :src="item['image']">
        <template #error>
          <div
            class="w-full h-full flex-center text-[2rem] text-[var(--el-text-color-secondary)] bg-[var(--el-fill-color-light)]"
          >
            <el-icon>
              <Picture />
            </el-icon>
          </div>
        </template>
      </el-image>
    </div>
    <!-- 具体的内容 -->
    <card-content-item v-if="!juiceIsShow" :item="item" />
  </div>
</template>

<script lang="ts" setup>
// @ts-ignore
import { Picture } from '@element-plus/icons-vue';

const props = defineProps({
  change: Boolean,
  index: { type: Number, required: true },
  item: { type: Object, required: true },
});

const juiceIsShow = computed(() => (props.change ? !props.change : props.index % 2 !== 0));
</script>

<style scoped>
.article-list-card-item:nth-child(odd) .article-card-content {
  grid-template-columns: 400px auto;
}

.article-list-card-item:nth-child(even) .article-card-content {
  grid-template-columns: auto 400px;
}

@media all and (min-width: 600px) {
  .article-list-card-item .article-img:nth-child(odd) {
    clip-path: polygon(0 500%, 100% 0%, 0 0, 0 0);
  }

  .article-list-card-item .article-img:nth-child(even) {
    clip-path: polygon(100% -500%, 100% 100%, 0 100%, 0 100%);
  }
}

@media all and (max-width: 600px) {
  .article-img::after {
    content: '';
    position: absolute;
    z-index: 100;
    left: 0;
    width: 100%;
    height: 75px;
    bottom: -5px;
    background-image: linear-gradient(to bottom, rgba(255, 255, 255, 0) 0, rgba(255, 255, 255, 1) 80%) !important;
  }
}

@media screen and (min-width: 600px) and (max-width: 750px) {
  .article-card-content {
    grid-template-columns: 50% 50% !important;
  }
}

@media screen and (max-width: 600px) {
  .article-card-content {
    height: 398px;
    grid-template-columns: 100% !important;
    grid-template-rows: 59% 41%;
  }
}
</style>

<style>
.article-card-content:hover .article-img img {
  transition: transform 0.5s;
  transform: rotate(0.01turn) scale(1.1);
}
</style>