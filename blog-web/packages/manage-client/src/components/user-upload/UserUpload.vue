<template>
  <el-upload
    ref="upload"
    action="#"
    class="user-head"
    list-type="picture-card"
    :auto-upload="false"
    :drag="true"
    :file-list="props.list"
    :limit="1"
    @change="onChange"
    @exceed="onExceed"
  >
    <el-icon>
      <Plus />
    </el-icon>

    <template #file="{ file }">
      <div>
        <img class="el-upload-list__item-thumbnail" :src="imageSrc(file)" alt="" />
        <span class="el-upload-list__item-actions">
          <span class="el-upload-list__item-preview" @click="handlePictureCardPreview(file)">
            <el-icon><zoom-in /></el-icon>
          </span>
          <span v-if="!disabled" class="el-upload-list__item-delete" @click="handleDownload(file)">
            <el-icon><Download /></el-icon>
          </span>
          <span v-if="!disabled" class="el-upload-list__item-delete" @click="handleRemove()">
            <el-icon><Delete /></el-icon>
          </span>
        </span>
      </div>
    </template>
  </el-upload>
</template>
<script lang="ts" setup>
import { reactive, ref, watch } from 'vue';
import { Delete, Download, Plus, ZoomIn } from '@element-plus/icons-vue';
import type { UploadFile, UploadFiles, UploadRawFile, UploadUserFile } from 'element-plus';
import { messageInfo } from '@tanxiang/common';

const upload = ref<any>();

const { messageWarning } = messageInfo();

const disabled = ref(false);
const dialogImageUrl = ref('');

const fileList = reactive<any>([]);
const emit = defineEmits(['changeFile']);

const props = defineProps({
  list: { type: Array, required: true },
});

const handleRemove = () => {
  upload.value!.clearFiles();
  fileList.splice(0, fileList.length);
  emit('changeFile', []);
};
//@ts-ignore
const handlePictureCardPreview = (file: UploadFile) => {
  dialogImageUrl.value = file.url;
};

const onChange = (uploadFile: UploadFile, uploadFiles: UploadFiles) => {
  //@ts-ignore
  if (
    uploadFile.raw?.type !== 'image/png' &&
    uploadFile.raw?.type !== 'image/jpg' &&
    uploadFile.raw?.type !== 'image/jpeg' &&
    uploadFile.raw?.type !== 'image/webp' &&
    uploadFile.raw?.type !== 'image/x-icon'
  ) {
    messageWarning('只能传入png、jpg、jpeg、webp、ico格式的图片！', 1.5);
    handleRemove();
    return;
  }
  // @ts-ignore
  fileList.splice(0, fileList.length, ...uploadFiles.map((file: any) => file.raw));
  emit('changeFile', fileList);
};

const onExceed = (files: File[], uploadFiles: UploadUserFile[]) => {
  upload.value!.clearFiles();
  const file: any = files[0] as UploadRawFile;
  //@ts-ignore
  file.uid = uploadFiles[0].uid;
  upload.value!.handleStart(file);
};

const handleDownload = (file: UploadFile) => {};

const imageSrc = computed(() => (file: any) => file.url);

watch(
  () => props.list,
  (val) => {
    // 如果外部已经删除，则清除掉缓存
    if (val.length === 0) handleRemove();
  },
  { deep: true }
);
</script>

<style>
.user-head .el-upload-list,
.user-head .el-upload--picture-card,
.user-head .el-upload-list__item {
  width: 100% !important;
  height: 100% !important;
}

.user-head .el-upload-list__item {
  margin-right: 0 !important;
  display: flex !important;
  align-items: center;
  justify-content: center;
  height: 100% !important;
}

.user-head .el-upload--picture-card {
  border: none !important;
}

.user-head .el-upload-dragger,
.user-head .el-icon {
  height: 100% !important;
}

.user-head .el-upload-list .el-upload-list__item:first-child + .el-upload {
  display: none;
}

.user-head .el-upload-list .el-upload-list__item:first-child.el-list-leave-active + .el-upload {
  display: block !important;
}
</style>