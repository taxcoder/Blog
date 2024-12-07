<template>
  <div id="directory" class="article-item-content bg-white sticky top-0 rounded-[7px] w-full">
    <div class="w-full h-full box-border">
      <n-tree
        block-node
        :data="menuDirectory"
        accordion
        :cancelable="false"
        :show-line="true"
        :expand-on-click="true"
        @update-selected-keys="onClickNode"
        @update-expanded-keys="onClickNode"
        class="py-[20px] px-[15px] rounded-[7px] overflow-auto max-h-[850px] text-[16px]"
      ></n-tree>
    </div>
  </div>
</template>

<script setup lang="ts">
import { MenuDirectory } from '@/interface/base';
import { useArticleStore } from '@/stores/article';
import { useBaseStore } from '@/stores/base';

const base = useBaseStore();
const article = useArticleStore();

const status: any = inject('status');
// 预留高度
const buffer = ref<number>(10);
// 文章的目录结构
const menuDirectory = reactive<MenuDirectory[]>([]);

// 格式化目录，目录的层级越大，表示深度越深
const formatDirectory = (options: MenuDirectory[], option: any, index: number) => {
  if (index < 0) index = 0;
  // 如果是第一个节点，则直接添加
  if (options.length === 0) options.push(option);
  // 如果是同级节点，则直接添加
  else if (option.level == options[index].level) options.push(option);
  // 判断当前节点内的层级是否小于最后一个节点的层级，如果小于则直接添加到里面
  else if (option.level < options[index].level) options.push(option);
  else {
    // 递归判断当前节点和目录内的节点的层级关系
    if (!options[index].children) options[index].children = [];
    formatDirectory(options[index].children, option, options[index].children.length - 1);
  }
};
// 点击节点
const onClickNode = (node: any) => {
  // 点击节点，滚动到对应的位置
  //@ts-ignore
  if (!document.getElementById(node[0])) return;
  document.getElementById(node[0]).scrollIntoView();
  // 获取顶部条的高度
  let nav: any = document.getElementById('page-nav')?.clientHeight;
  //如果存在高度的话，则网上滚动顶部条加上缓冲的高度，防止顶部条遮挡信息
  if (nav) window.scrollBy({ top: -nav - buffer.value * 2 });
};

// 监听文章目录的变化，将目录格式化
watch(
  () => article.getDirectory,
  (directory: any) => {
    // 如果目录不存在，则不进行处理
    if (!directory) return;
    // 遍历目录，将目录格式化
    menuDirectory.splice(0, menuDirectory.length);
    directory.forEach((dir: any) => {
      // 将目录节点进行格式化
      let option: MenuDirectory = {
        label: dir.text,
        key: dir.text,
        level: dir.level,
      };
      // 否则，判断目录层级，将节点添加到对应层级的目录中
      formatDirectory(menuDirectory, option, menuDirectory.length - 1);
    });
  },
  { deep: true, immediate: true }
);
</script>

<style scoped></style>