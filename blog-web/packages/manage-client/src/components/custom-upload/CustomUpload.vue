<template>
  <a-modal
    v-model:open="image"
    centered
    :closable="false"
    :title="title"
    @ok="upload"
    :destroyOnClose="true"
    @cancel="cancel"
  >
    <div id="upload">
      <user-upload @ChangeFile="changeFile" :list="fileList" />
    </div>
  </a-modal>
</template>

<script setup lang="ts">
import { inject } from 'vue';
import UserUpload from '@/components/user-upload/UserUpload.vue';

import { useBaseStore } from '@/stores/base';
import { messageInfo } from '@tanxiang/common';
import { useUpload, web } from '@tanxiang/apis';

const base = useBaseStore();
const { messageError, messageLoading, messageSuccess, messageWarning } = messageInfo();
const load = useUpload();

const image: any = inject('image');

const props: any = defineProps({
  fileList: { type: Array, required: true },
  title: { type: String, default: '上传' },
  api: { type: Function, required: true },
  field: { type: String, required: true },
});

const emit = defineEmits(['change']);

const upload = () => {
  let web = base.getWebStation;
  if (props.fileList.length === 0) {
    return messageWarning('请选择一张图片在进行上传！');
  } else if (props.fileList[0].url && props.fileList[0].url === web.headIcon) {
    return messageWarning('头像未改变！');
  } else if (props.fileList[0].url && props.fileList[0].url === web.backgroundImage) {
    return messageWarning('背景未改变！');
  }
  messageLoading('正在上传中！');
  props
    .api(props.fileList[0])
    .then((success: any) => {
      messageSuccess(success.message);
      web[props.field] = success.data;
      base.setWebStation(web);
      image.value = false;
    })
    .catch((error: any) => messageError(!error || error.name ? '网络错误！' : error));
};

const changeFile = (files: any[]) => {
  emit('change', files);
};

const cancel = () => {
  setTimeout(() => emit('change', [{ name: 'image.png', url: base.getWebStation[props.field] }]), 500);
};
</script>

<style>
#upload .el-upload-dragger {
  padding: 100px;
}
</style>