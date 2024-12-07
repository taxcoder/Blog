<template>
  <div id="article-page-content">
    <div class="item-info mt-[15px] bg-white">
      <el-image class="w-full h-[275px]" style="filter: brightness(0.9)" :src="articleData?.image" fit="cover" />
      <span class="classification-name" @click="jump">
        {{ classificationName }}
      </span>
      <div class="info-title"><span v-text="title" /></div>
      <div class="content-base">
        <div v-for="(item, index) in content" class="content-base-item" :key="item.name">
          <color-icon class="content-item-icon" :iconClass="item.icon" :size="14" :top="-1" />
          <span v-text="item.prompt" />
          <span>{{ info(index) }}</span>
        </div>
      </div>
    </div>
    <div class="item-content">
      <MdPreview
        :modelValue="markdownContent"
        @onGetCatalog="onGetCatalog"
        :theme="base.getTheme ? 'dark' : 'light'"
        showCodeRowNumber
        @onHtmlChanged="onHtmlChanged"
      />
    </div>
    <div class="px-[20px] py-[10px] text-[14px] color-[#50505c]">
      <div
        class="bg-[#f7f7f7] dark:bg-transparent important-text-[#262626] dark:important-text-[#b9b9b9] rounded-[1px] py-[10px] px-[6px] border-l-5 border-solid border-[#ff9b00]"
      >
        <div class="ml-[3px]">
          <p class="mb-[9px]">本文标题：{{ title }}</p>
          <p class="my-[9px]">本文作者：{{ articleAuthor }}</p>
          <p class="my-[9px]">创建时间：{{ time('createTime') }}</p>
          <p class="my-[9px]">本文链接：{{ currentUrl }}</p>
          <p class="mt-[9px]">
            版权声明：本博客所有文章除特别声明外，均采用
            <el-link
              class="important-font-bold important-text-black dark:important-text-white relative top-[-1px] text-[15px]"
              :underline="false"
              herf="https://creativecommons.org/licenses/by-nc-sa/4.0/deed.zh"
            >
              BY-NC -SA
            </el-link>
            许可协议。转载请注明出处！
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useDate } from '@tanxiang/utils';
//@ts-ignore
import { MdPreview } from 'md-editor-v3';

import { useArticleStore } from '@/stores/article';
import { useBaseStore } from '@/stores/base';
import { ColorIcon } from '@tanxiang/common';

const util = useDate();

const router: any = useRouter();
const article: any = useArticleStore();
const base: any = useBaseStore();

const fontCount = ref<number>(0);

const content = reactive([
  {
    name: 'author',
    icon: 'icon-A2',
    prompt: '作者：',
  },
  {
    name: 'create',
    icon: 'icon-A3',
    prompt: '发表于：',
  },
  {
    name: 'update',
    icon: 'icon-A1',
    prompt: '更新于：',
  },
  {
    name: 'font-count',
    icon: 'icon-A5',
    prompt: '字数总计：',
  },
  {
    name: 'read-time',
    icon: 'icon-A4',
    prompt: '阅读时间：',
  },
]);

const props = defineProps({
  markdownContent: String,
});

const articleData: any = inject('articleData');

const init = () => {};

const onHtmlChanged = (html: string) => (fontCount.value = html.replace(/<[^>]*>/g, '').length);

const onGetCatalog = (list: any[]) => {
  if (list[0] && list[0].level === 1) list.shift();
  article.setDirectory(list);
};

const jump = () => {
  if (articleData.value) router.push(`/list/classification/${articleData.value.classification.id}`);
};

const title = computed(() => {
  base.setTitle(articleData.value ? articleData.value.title : '');
  return articleData.value ? articleData.value.title : '';
});

const info = computed(() => (index: number) => {
  switch (index) {
    case 0:
      return author.value;
    case 1:
      return time.value('createTime');
    case 2:
      return time.value('updateTime');
    case 3:
      return fontCount.value;
    case 4:
      return Math.ceil(readTime(fontCount.value, 6)) + '分钟';
  }
});

const author = computed(() => (articleData.value ? articleData.value.author : ''));

const classificationName = computed(() => {
  return !!articleData.value && !!articleData.value.classification ? articleData.value.classification.name : '';
});

const articleAuthor = computed(() => {
  return !!articleData.value && !!articleData.value.author ? articleData.value.author : '';
});

const currentUrl = computed(() => {
  return window.location.href;
});

const time = computed(() => (t: any) => articleData.value ? util.shortTime(articleData.value[t], '/') : '');

const readTime = (fontCont: number, imageCount: number) => fontCont / (500 + imageCount * 5);
</script>

<style scoped>
#article-page-content {
  width: 100%;
  height: 100%;
}

.classification-name {
  position: absolute;
  top: 10px;
  cursor: pointer;
  left: 0;
  background-color: #308eef;
  padding: 12px 12px;
  color: white;
  border-top-right-radius: 3px;
  border-bottom-right-radius: 3px;
}

.item-info {
  position: relative;
}

.info-title {
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 30px 0;
  font-size: max(1.5vw, 2rem);
  font-family: 'sakura', sans-serif;
}

.item-info-options {
  position: relative;
  display: flex;
  align-items: center;
  font-family: 'sakura', sans-serif;
  font-size: max(0.8vw, 0.6rem);
}

.breadcrumb-custom {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-flow: wrap;
}

.content-base {
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-flow: wrap;
  padding: 0 15px;
  box-sizing: border-box;
}

.content-base-item {
  position: relative;
  display: inline-flex;
  color: #292929;
  align-items: center;
  margin-bottom: 6px;
}

.content-base-item:not(:last-child) {
  margin-right: 15px;
}

.content-base-item:not(:last-child):after {
  content: '';
  width: 2px;
  height: 130%;
  position: absolute;
  right: -7px;
  top: -3px;
  background-color: #bfbfbf;
}

.content-item-icon {
  margin-right: 3px;
}

.icon-zhuanlanyonghu {
  font-size: 14px;
}

.icon-rili {
  font-size: 13px;
}

.icon-gengxinshijian {
  font-size: 15px;
}

.icon-icon-14-zishutongji {
  font-size: 12px;
}

.icon-time {
  font-size: 14px;
}

@media all and (max-width: 448px) {
  .content-base-item:first-child {
    margin-left: 15px;
  }

  .content-base-item:nth-child(even):after {
    display: none;
  }

  .info-title {
    margin-bottom: 15px;
  }
}

p {
  white-space: pre-wrap;
  overflow-wrap: break-word;
  line-height: 1.5;
}
</style>