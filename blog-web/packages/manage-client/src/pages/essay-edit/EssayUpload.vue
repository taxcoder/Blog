<template>
  <div style="height: 100%">
    <md-editor
      v-if="route.meta.editor"
      ref="editor"
      :toolbarsExclude="['github']"
      :autoDetectCode="true"
      v-model="text"
      :showToolbarName="showToolbarName"
      :toolbars="toolbars"
      @save="onSave"
      @onUploadImg="uploadImageEssay"
      :footers="['markdownTotal', '=', 0, 'scrollSwitch']"
    >
      <template #defToolbars>
        <MarkExtension :showToolbarName="showToolbarName" />
        <EmojiExtension :showToolbarName="showToolbarName" />
      </template>
      <template #defFooters>
        <TimeNow />
      </template>
    </md-editor>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref, computed } from 'vue';

import { MdEditor } from 'md-editor-v3';
import { toolbars } from '@/config/md-config';

import TimeNow from '@/components/markdown/time-now/TimeNow.vue';
import MarkExtension from '@/components/markdown/mark-extension/MarkExtension.vue';
import EmojiExtension from '@/components/markdown/emoji-extension/EmojiExtension.vue';

import { useRoute, useRouter } from 'vue-router';
import { useEssayStore } from '@/stores/essay';
import { useBaseStore } from '@/stores/base';
import { useAuthEssay } from '@tanxiang/apis';
//@ts-ignore
import useUploadImage from '@/hooks/useUploadImage';
import { messageInfo } from '@tanxiang/common';

const { messageError, messageLoading, messageSuccess } = messageInfo();

const route: any = useRoute();
const router: any = useRouter();
const auth: any = useAuthEssay();
const base: any = useBaseStore();
const essay: any = useEssayStore();

const open = ref<boolean>(false);
const editor = ref<any>();
const text = ref<string>('');
const initial = ref<string>('');
const showToolbarName = ref<boolean>(true);

const { uploadImageEssay } = useUploadImage(editor);

onMounted(() => {
  notEmpty();
});

const notEmpty = () => {
  // 有数据的情况下
  if (essay.getEssayById && route.params.id === essay.getEssayById.id) {
    text.value = initial.value = essay.getEssayById ? essay.getEssayById.content : '';
  } else {
    // 有ID，但没有数据的情况下
    auth
      .essayById(route.params.id)
      .then((success: any) => {
        // 如果获取不到数据，表示传入的ID异常，删除当前的标签页，在跳转到404页
        if (success.data === null) {
          base.removeClickRouterList(base.getCurrentListIndex);
          return router.push('/404');
        }
        text.value = success.data.content;
        initial.value = success.data.content;
        essay.setEssayById(success.data);
      })
      .catch((error: any) => messageError(!error || error.name ? '网络错误！' : error));
  }
};

const onSave = (v: string) => {
  if (v === initial.value) return messageError('内容未变化！');
  messageLoading('文章保存中');
  auth
    .essayUpdateContentById(route.params.id, text.value)
    .then((success: any) => {
      messageSuccess(success.message);
      initial.value = text.value;
      essay.getEssayById.cotent = text.value;
      essay.setEssayById(essay.getEssayById);
      essay.setIsRefer(true);
    })
    .catch((error: any) => messageError(!error || error.name ? '网络错误！' : error));
};
</script>

<style>
#md-editor-v3 {
  height: 100%;
}
</style>