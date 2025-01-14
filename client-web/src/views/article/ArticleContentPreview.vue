<!--
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2025-01-11 10:49:33
 * @LastEditTime: 2025-01-13 08:47:52
 * @FilePath: /code/blog/client-web/src/views/article/ArticleContentPreview.vue
 * @Description: 
 * Copyright (c) 2025 by tanxiang, All Rights Reserved. 
-->
<template>
  <MdPreview :id="id" :modelValue="props.content" previewTheme="recall" @onHtmlChanged="onHtmlChanged" />
  <!-- <MdCatalog :editorId="id" :scrollElement="scrollElement" /> -->
</template>

<script setup lang="ts">
import { MdPreview } from 'md-editor-v3';

const props = defineProps({
  content: {
    type: String,
    default: ''
  }
});

const textCount = ref<number>(0);

const id = ref<string>('preview-only');

const onHtmlChanged = (html: any) => {
  let div = document.createElement('div');
  div.innerHTML = html;
  textCount.value = div.innerText.length;
};

defineExpose({
  textCount
});
</script>
