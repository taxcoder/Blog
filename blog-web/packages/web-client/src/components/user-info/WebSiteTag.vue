<!--
 * @Author: tanxiang 1571922819@qq.com
 * @Date: 2023-06-24 12:18:39
 * @Description: 标签云
 * @LastEditTime: 2023-12-10 21:49:02
 * @LastEditors: tanxiang 1571922819@qq.com
 * @FilePath: \blog\packages\web-client\src\components\user-info\WebSiteTag.vue
 * @copyright: Copyright (c) 2023 by 1571922819@qq.com, All Rights Reserved.
-->
<template>
  <user-container :isDefer="true" :loading="juiceIsLoading" title="文章标签" iconClass="icon-tags" :size="20">
    <template #content>
      <div id="tag-cloud" class="h-[300px]"></div>
    </template>
    <template #more>
      <span
        class="more pb-[8px] w-full h-[25px] items-end flex text-[0.82rem] text-[#337ecc] cursor-pointer"
        @click="navToTag('all')"
        v-text="'查看更多...'"
      />
    </template>
  </user-container>
</template>

<script setup lang="ts">
import { useGlobalStore } from '@/stores/global';
import { useTag } from '@tanxiang/apis';
import TagCloud from 'TagCloud';

import { useRouter, useRoute } from 'vue-router';

const tag = useTag();
const route = useRoute();
const router = useRouter();
const global = useGlobalStore();
const cloud: any = {
  radius: 325, // 滚动半径, 单位px
  maxSpeed: 'fast', // 滚动最大速度, 取值: slow, normal(默认), fast
  initSpeed: 'fast', // 滚动初速度, 取值: slow, normal(默认), fast
  direction: 135, // 初始滚动方向, 取值角度(顺时针deg): 0 对应 top, 90 对应 left, 135 对应 right-bottom(默认)...
  keep: true, // 鼠标移出组件后是否继续随鼠标滚动, 取值: false, true(默认) 对应 减速至初速度滚动, 随鼠标滚动
  itemClass: 'tag-cloud-item',
};

onMounted(() => {
  tag
    .tags()
    .then((success: any) => {
      // 设置标签云数据
      global.setWebSiteTags(success.data);
      // 只有当前路由的isDraw为true或者是显示网站信息为true时才绘制tag cloud
      if (!route.meta.isDraw || route.meta.showUserInfo) createTagCloud();
    })
    .catch((error: any) => console.error(error));
});
// 判断是否加载
const juiceIsLoading = computed(() => !global.getWebSite.tags);
// 生成随机数（生成颜色）
const random = () => Math.floor((70 + Math.random()) * (Math.random() * 2.4));
/**
 * 获取标签的名称
 * @param tags 标签数组
 * @returns 标签名称数组
 */
const stripping = (tags: any[]) => tags.map((tag: { id: number; name: string }) => tag.name);
// 切换路由
const navToTag = (tag: string) => {
  if (tag === 'all') router.push('/list/tag');
  else router.push(`/list/tag/${global.getWebSite.tags.filter((t) => t.name === tag)[0].id}`);
};
// 监听事件
const listen = (node: any, type: string, opacity: number, fn: Function) => {
  node.addEventListener(type, () => {
    let item: any = document.getElementsByClassName('tag-cloud-item');
    for (let i = 0; i < item.length; i++) {
      item[i].style.opacity = opacity;
    }
    if (opacity === 0) node.style.opacity = 1;
    fn();
  });
};
// 创建标签云
const createTagCloud = () => {
  nextTick(() => {
    // 获取标签数组的长度
    if (!global.getWebSite.tags && !global.getWebSite.tags.length) return;
    let length = global.getWebSite.tags.length;
    // 设置tag cloud的半径
    cloud.radius = 200;
    // 获取标签云节点
    let tc: any = document.getElementById('tag-cloud');
    // 如果没有，则表示出现异常情况
    if (tc == null) return;
    // 获取标签的名称
    let texts: any;
    // 如果标签超过150个，将标签数组按照关联文章的次数进行排序，显示前150个，防止卡顿
    if (global.getWebSite.tags.length > 50) {
      let sort = global.getWebSite.tags.sort((a: any, b: any) => {
        if (a.articleCount > b.articleCount) return -1;
        if (a.articleCount < b.articleCount) return 1;
        return 0;
      });
      texts = stripping(sort.slice(0, 50));
    } else {
      texts = stripping(global.getWebSite.tags);
    }

    // 创建标签云对象
    let tagCloud = TagCloud(tc, texts, cloud);
    // 获取标签云的item
    let item: any = document.getElementsByClassName('tag-cloud-item');
    for (let i = 0; i < item.length; i++) {
      // 往节点添加样式
      item[i].style.color = 'rgb(' + random() + ',' + random() + ',' + random() + ')';
      // 给每个item添加事件
      // 当鼠标移入时，隐藏其他的标签，显示当前选项的标签，离开时再将其他的标签显示
      listen(item[i], 'mouseover', 0, () => tagCloud.pause());
      listen(item[i], 'mouseout', 1, () => tagCloud.resume());
      //监听点击，点击后切换路由
      item[i].addEventListener('click', (e: any) => navToTag(e.target.innerText));
    }
  });
};
</script>

<style scoped></style>

<style>
#tag-cloud > .tagcloud {
  width: 100% !important;
  height: 100% !important;
}

.tag-cloud-item {
  cursor: pointer;
  transition: opacity 0.3s linear;
}
</style>
