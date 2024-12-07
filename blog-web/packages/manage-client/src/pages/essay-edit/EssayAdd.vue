<template>
  <div style="height: 100%">
    <md-editor
      ref="editor"
      v-if="route.meta.editor"
      :toolbarsExclude="['github']"
      :autoDetectCode="true"
      v-model="text"
      :showToolbarName="showToolbarName"
      :toolbars="toolbars"
      @save="onSave"
      @input="onInput"
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

    <a-modal
      v-model:open="open"
      title="填入自己的心情"
      :closable="false"
      @ok="clickOk"
      @cancel="cancel"
      class="modal-essay"
      :centered="true"
      :width="420"
      :okButtonProps="{ disabled: isDisabled }"
    >
      <el-cascader
        :options="options"
        v-model:value="modelValue"
        placeholder="请选择Emoji"
        class="w-full"
        large="large"
        @change="onChange"
        popper-class="popup"
        :props="cascaderProps"
      >
        <template #default="{ node, data }">
          <span v-if="!node.isLeaf" :data-index="data.value">{{ data.label }}</span>
          <img v-else :src="data.label" :alt="data.name" :title="data.name" class="w-[30px] h-full object-contain" />
          <span v-if="!node.isLeaf">({{ data.children.length }})</span>
        </template>
      </el-cascader>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, onUnmounted, PropType, ref } from 'vue';

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
const { messageError, messageLoading, messageSuccess, messageWarning } = messageInfo();

const route: any = useRoute();
const router: any = useRouter();
const auth: any = useAuthEssay();
const base: any = useBaseStore();
const essay: any = useEssayStore();

const editor = ref<any>();
const ip = ref<string>('');
const value = ref<string>('');
const text = ref<string>('');
const open = ref<boolean>(false);
const initial = ref<string>('');
const showToolbarName = ref<boolean>(true);
const mood = ref<string>('');
const modelValue = ref<string[]>([]);

const cascaderProps = reactive<any>({
  lazy: false,
});

const { uploadImageEssay } = useUploadImage(editor);

// 每次离开就把数据保存再本地
onUnmounted(() => {
  essay.setContent(text.value);
});
// 每次进入就从本地读取
onMounted(() => (text.value = essay.getContent));
// 打开modal
const onSave = () => {
  if (text.value === '' || text.value.length === 0) return messageWarning('内容未变化！');
  open.value = true;
};
//@ts-ignore
const onInput = (event: Event) => essay.setContent(event.currentTarget.innerText);

const clickOk = () => {
  if (text.value === initial.value) return messageError('内容未变化！');
  if (mood.value.length === 0) return messageError('请选择心情！');
  messageLoading('随笔上传中');
  auth
    .addEssay(text.value, mood.value)
    .then((success: any) => {
      open.value = false;
      initial.value = text.value;
      messageSuccess(success.message);
    })
    .catch((error: any) => messageError(!error || error.name ? '添加失败！' : error));
};

const onChange = (value: any) => {
  let emoji = base.getEmojis[value[0]].find((emoji) => emoji.id === value[1]);
  if (emoji) {
    mood.value = emoji.id;
  }
};

const cancel = () => (modelValue.value = []);

const isDisabled = computed(() => (!modelValue.value || modelValue.value.length === 0) && mood.value.length === 0);

const options = computed(() => {
  if (Object.keys(base.getEmojis).length === 0) return [];
  let keys: any[] = Object.keys(base.getEmojis);
  return keys
    .map((key: any) => ({
      label: key,
      value: key,
      children: base.getEmojis[key].map((v: any) => ({
        label: v.icon,
        value: v.id,
        name: v.name,
      })),
    }))
    .reverse();
});
</script>

<style scoped>
#md-editor-v3 {
  height: 100%;
}
</style>

<style>
.popup .el-cascader-menu:nth-child(1) {
  min-width: 130px !important;
}

.popup .el-cascader-menu:nth-child(2) {
  max-width: 240px !important;
  min-width: 240px !important;
}

.popup .el-cascader-menu:first-child .el-cascader-menu__list {
  justify-content: space-between;
  width: 100% !important;
  display: flex !important;
  flex-direction: column;
}

.popup .el-cascader-menu:last-child {
  padding: 5px 0;
}

.popup .el-cascader-menu:last-child .el-cascader-menu__list {
  display: inline-grid;
  width: 100%;
  grid-template-columns: repeat(5, 20%);
  padding: 0;
}

.popup .el-cascader-menu .el-cascader-menu__list {
  min-height: auto !important;
}

.popup .el-cascader-node {
  padding: 0 !important;
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.popup .el-cascader-menu:nth-child(2) .el-cascader-node {
  border-radius: 50%;
  height: 48px;
}
.popup .el-cascader-menu:nth-child(2) .el-cascader-node__label {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0;
}

.popup .el-cascader-node__label {
  width: 100%;
  height: 100%;
}

.popup .is-active {
  background: rgba(0, 125, 225, 0.4);
}

.popup .el-cascader-node__prefix {
  display: none;
}

.modal-essay .ant-modal-body {
  margin: 20px 0;
}

.modal-essay .ant-modal-body .ant-cascader {
  width: 100%;
}

.modal-essay .ant-modal-footer {
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>