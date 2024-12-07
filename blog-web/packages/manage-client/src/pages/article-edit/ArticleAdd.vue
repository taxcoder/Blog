<template>
  <div style="height: 100%" id="article-edit">
    <md-editor
      ref="editor"
      v-if="route.meta.editor"
      @onHtmlChanged="onHtmlChanged"
      :toolbarsExclude="['github']"
      :autoDetectCode="true"
      v-model="text"
      :showToolbarName="showToolbarName"
      :toolbars="toolbars"
      @save="onSave"
      @onUploadImg="uploadImageArticle"
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
      :destroyOnClose="true"
      v-model:open="addModal"
      centered
      title="文章上传"
      :bodyStyle="bodyStyle"
      @ok="submit"
      :key="Math.random()"
    >
      <a-form
        :model="formState"
        name="article-form"
        :scrollToFirstError="true"
        style="width: 100%"
        :key="Math.random()"
        class="form-article"
      >
        <a-form-item label="分类" name="classificationId">
          <el-autocomplete
            style="width: 100%"
            v-model="formState.classificationValue"
            :fetch-suggestions="classificationQuerySearch"
            :highlight-first-item="true"
            :fit-input-width="true"
            :select-when-unmatched="true"
            @select="classificationSelect"
            placeholder="请选择一个分类"
            :debounce="0"
            size="default"
          >
            <template #default="{ item }">
              <span class="link">{{ item.label }}</span>
            </template>
          </el-autocomplete>
        </a-form-item>
        <a-form-item label="标签" name="tagIds">
          <el-select-v2
            v-model:modelValue="formState.tagValue"
            filterable
            clearable
            allow-create
            size="default"
            autocomplete='inline"'
            :reserve-keyword="false"
            :collapse-tags-tooltip="true"
            :multiple-limit="3"
            :max-collapse-tags="2"
            :options="formState.tag"
            style="width: 100%; min-width: 250px"
            multiple
            mode="tags"
            collapse-tags
            @change="change"
            @input="selectInput"
            placeholder="请勿超过三个标签"
          />
        </a-form-item>
        <a-form-item label="标题" name="title">
          <a-input :allowClear="true" v-model:value="formState.title" :maxlength="60" :showCount="true" />
        </a-form-item>
        <a-form-item label="是否置顶" name="isTop">
          <a-switch v-model:checked="formState.isTop" />
        </a-form-item>
        <a-form-item label="封面" name="headImage">
          <a-upload
            v-if="addModal"
            v-model:file-list="formState.image"
            list-type="picture-card"
            @preview="handlePreview"
            :maxCount="1"
            :before-upload="beforeUpload"
          >
            <div v-if="formState.image < 1">
              <plus-outlined />
              <div style="margin-top: 8px">Upload</div>
            </div>
          </a-upload>
          <a-modal
            v-if="addModal"
            :open="previewVisible"
            :title="previewTitle"
            :footer="null"
            @cancel="handleCancel"
            class="upload-modal"
          >
            <img alt="example" style="width: 100%" :src="previewImage" />
          </a-modal>
        </a-form-item>
        <a-form-item label="前言" name="prefix_content" class="prefix_content">
          <a-textarea
            auto-size
            :allowClear="true"
            v-model:value="formState.prefixContent"
            :maxlength="200"
            :rows="15"
            :showCount="true"
          />
          <el-button type="danger" @click="contentFill">从文章内填充</el-button>
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { onMounted, onUnmounted, reactive, ref } from 'vue';
import { toolbars } from '@/config/md-config';
import { MdEditor } from 'md-editor-v3';
import MarkExtension from '@/components/markdown/mark-extension/MarkExtension.vue';
import TimeNow from '@/components/markdown/time-now/TimeNow.vue';
import EmojiExtension from '@/components/markdown/emoji-extension/EmojiExtension.vue';

import { useArticleStore } from '@/stores/article';
import { PlusOutlined } from '@ant-design/icons-vue';

import { useRoute, useRouter } from 'vue-router';
import { useArticle, useAuthArticle, useAuthClassification, useAuthTag, useTag } from '@tanxiang/apis';

import { messageInfo } from '@tanxiang/common';
const { messageError, messageLoading, messageSuccess, messageWarning } = messageInfo();
import useUploadImage from '@/hooks/useUploadImage';

const route = useRoute();
const router = useRouter();
const article = useArticle();
const auth = useAuthArticle();
const articleStore = useArticleStore();

const editor = ref<any>();
const initText = ref<string>('');
const html = ref<string>('');
const text = ref<string>('');
const addModal = ref<boolean>(false);
const showToolbarName = ref<boolean>(false);

const previewVisible = ref(false);
const previewImage = ref('');
const previewTitle = ref('');

const bodyStyle = reactive<any>({
  padding: '5px 0',
  minHeight: '300px',
  maxHeight: '650px',
  overflowX: 'hidden',
});

const formState = reactive<any>({
  classificationValue: '',
  classification: [],
  tagValue: [],
  tag: [],
  title: '',
  isTop: false,
  image: [],
  prefixContent: '',
});

const { uploadImageArticle } = useUploadImage(editor);

onMounted(() => {
  read();

  useAuthClassification()
    .classificationAll()
    .then((success: any) => {
      formState.classification.splice(0, formState.classification.length);
      formState.classification.push(...success.data.map((s: any) => ({ value: s.name, label: s.name, id: s.id })));
    })
    .catch(() => messageError('网络错误！'));

  useTag()
    .tags()
    .then((success: any) => {
      formState.tag.splice(0, formState.tag.length);
      formState.tag.push(...success.data.map((s: any) => ({ value: s.name, label: s.name, id: s.id })));
    })
    .catch(() => messageError('网络错误！'));
});

onUnmounted(() =>
  articleStore.setFormState({
    classificationValue: formState.classificationValue,
    classification: formState.classification,
    tagValue: formState.tagValue,
    tag: formState.tag,
    content: text.value,
    title: formState.title,
    isTop: formState.isTop,
    image: formState.image,
    prefixContent: formState.prefixContent,
  })
);

const read = () => {
  // TODO：读取本地存储的随笔内容
  formState.image = articleStore.getFormState.image;
  formState.classificationValue = articleStore.getFormState.classificationValue;
  formState.classification = articleStore.getFormState.classification;
  formState.tagValue = articleStore.getFormState.tagValue;
  formState.tag = articleStore.getFormState.tag;
  formState.title = articleStore.getFormState.title;
  formState.isTop = articleStore.getFormState.isTop;
  formState.prefixContent = articleStore.getFormState.prefixContent;
  text.value = articleStore.getFormState.content;
};

const onHtmlChanged = (str: string) => {
  str = str.replace(/<\/?[^>]*>/g, ''); //去除HTML tag
  str = str.replace(/[ | ]*\n/g, '\n'); //去除行尾空白
  str = str.replace(/\n[\s| | ]*\r/g, '\n'); //去除多余空行
  str = str.replace(/ /gi, ''); //去掉
  str = str.replace(/ |　/gi, '');
  str = str.replace(/\r\n/gi, '\n');
  str = str.replace(/\n\n/gi, '\n');
  str = str.replace(/\n\n/gi, '\n');
  str = str.replace(/\n\n/gi, '\n');
  str = str.replace(/\n\n/gi, '\n');
  str = str.replace(/\n/gi, ' \n');
  str = str.replace('\n\n', '\n');
  str = str.replace(' ', '');
  html.value = str.substring(0, 200);
};

const change = (data: string[]) => {
  if (data.length === 0) return;
  let tags = data.filter((d: any) => formState.tag.filter((t: any) => d === t.value).length === 0);
  if (tags.length === 0) return;
  // 如果是自己创建的标签，则上传到服务器

  useAuthTag()
    .tagAdd(tags)
    .then((success: any) => {
      messageSuccess(success.message);

      let temp = JSON.parse(JSON.stringify(formState.tagValue));
      let ret = JSON.parse(JSON.stringify(formState.tag));

      formState.tagValue.splice(0, formState.tagValue.length);
      formState.tag.splice(0, formState.tag.length);

      ret = ret.filter((t: any) => data.filter((d: any) => d !== t.value));
      ret.push(...success.data.map((t: any) => ({ value: t.name, label: t.name, id: t.id })));

      formState.tag.push(...ret);

      setTimeout(() => formState.tagValue.push(...temp), 0);
    })
    .catch((error: any) => messageError(!error || error.name ? '网络错误！' : error, 3));
};

const onSave = () => {
  if (text.value.trim() === '') return messageWarning('文章内容不为空！');
  addModal.value = true;
};

const submit = () => {
  let classificationId = formState.classification.find((c: any) => c.value === formState.classificationValue);
  if (!classificationId) {
    return messageWarning('请选择一个分类！');
  } else if (formState.tagValue.length === 0) {
    return messageWarning('必须选择一个标签！');
  } else if (formState.title === '' || formState.title.trim().length === 0) {
    return messageWarning('文章必须存在标题！');
  } else if (formState.prefixContent === '' || formState.prefixContent.trim().length === 0) {
    return messageWarning('前言必须存在！');
  } else {
    let image = !formState.image || formState.image.length === 0 ? new Blob() : dataURItoBlob(formState.image[0].url);
    messageLoading('文章添加中!');
    // 新增文章
    auth
      .addArticle({
        text: text.value,
        classificationId: classificationId.id,
        tagsId: formState.tagValue.map((t: any) => formState.tag.find((g: any) => t === g.value).id),
        title: formState.title,
        top: formState.isTop,
        // 如果没有图片，后面自己添加，如果是本地上传的图片，则没有url
        image: image,
        prefixContent: formState.prefixContent.trim(),
      })
      .then((success: any) => {
        addModal.value = false;
        articleStore.initFormState();
        messageSuccess(success.message);
        articleStore.setIsRefer(true);
        setTimeout(() => router.push(`/editor/article/${success.data}`), 800);
      })
      .catch((error: any) => messageError(!error || error.name ? '网络错误！' : error));
  }
};

const selectInput = (item: any) => {
  if (item.target.value.length >= 20) {
    messageWarning('标签长度不能超过20个字符');
    item.target.value = item.target.value.replace().substring(0, 19);
  }
};

const beforeUpload: any = (file: any) => {
  blob2Base64(file, (data: any) => {
    formState.image = [
      {
        uid: '-1',
        name: 'image.png',
        status: 'done',
        url: data,
      },
    ];
  });
  return false;
};

const blob2Base64 = (file: Blob, callback: Function) => {
  let reader = new FileReader();
  reader.readAsDataURL(file);
  reader.onload = function (e) {
    callback(e.target.result);
  };
};

const contentFill = () => (formState.prefixContent = html.value.trim());

const classificationSelect = (item: any) => {
  // 表示存在匹配的情况
  if (item.id) return;
  if (item.value.trim().length > 4) return messageWarning('标签不能超过4个字符！');
  useAuthClassification()
    .classificationAdd(item.value)
    .then((success: any) => {
      messageSuccess(success.message);
      formState.classification.push({
        value: success.data.name,
        label: success.data.name,
        id: success.data.id,
      });
    })
    .catch((error: any) => messageError(!error || error.name ? '网络错误！' : error));
};

const classificationQuerySearch = (query: string, cb: Function) => {
  let result =
    query === '' || query.length === 0
      ? formState.classification
      : formState.classification.filter((c: any) => c.value.toLowerCase().indexOf(query.toLowerCase()) !== -1);
  cb(result);
};

function getBase64(file: File) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = () => resolve(reader.result);
    reader.onerror = (error) => reject(error);
  });
}

function dataURItoBlob(base64: string) {
  let byteString;
  if (base64.split(',')[0].indexOf('base64') >= 0) byteString = atob(base64.split(',')[1]); //base64 解码
  else {
    byteString = unescape(base64.split(',')[1]);
  }
  let mimeString = base64.split(',')[0].split(':')[1].split(';')[0]; //mime类型 -- image/png
  let ia = new Uint8Array(byteString.length); //创建视图
  for (let i = 0; i < byteString.length; i++) ia[i] = byteString.charCodeAt(i);
  return new Blob([ia], {
    type: mimeString,
  });
}

const handleCancel = () => {
  previewVisible.value = false;
  previewTitle.value = '';
};

const handlePreview = async (file: any) => {
  if (!file.url && !file.preview) {
    file.preview = (await getBase64(file.originFileObj)) as string;
  }
  previewImage.value = file.url || file.preview;
  previewVisible.value = true;
  previewTitle.value = file.name || file.url.substring(file.url.lastIndexOf('/') + 1);
};
</script>

<style scoped>
#md-editor-v3 {
  height: 100%;
}
</style>

<style>
.form-article .el-input__wrapper,
.form-article .el-select-v2__wrapper {
  border-radius: 20px !important;
}

.form-article .ant-form-item {
  margin-bottom: 12px !important;
}

.form-article .prefix_content .ant-form-item-control-input-content {
  display: flex;
  flex-direction: column;
}

.form-article .ant-upload-list-item-container {
  width: 100% !important;
  height: 100% !important;
}

.form-article .ant-upload-wrapper.ant-upload-picture-card-wrapper .ant-upload.ant-upload-select {
  width: 100% !important;
  height: 200px !important;
}

.form-article
  .ant-upload-wrapper.ant-upload-picture-card-wrapper
  .ant-upload-list.ant-upload-list-picture-card
  .ant-upload-list-item {
  height: 100% !important;
}

.form-article .ant-upload-list-item .ant-upload-list-item-uploading,
.form-article .ant-upload-wrapper .ant-upload-list {
  width: 100% !important;
}

.form-article
  .ant-upload-wrapper.ant-upload-picture-card-wrapper
  .ant-upload-list.ant-upload-list-picture-card
  .ant-upload-list-item-progress {
  bottom: 70px !important;
}

.form-article
  .ant-upload-wrapper.ant-upload-picture-card-wrapper
  .ant-upload-list.ant-upload-list-picture-card
  .ant-upload-list-item-thumbnail {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 200px !important;
}

/* 谷歌，元素隐藏必须设置宽度  不然无效 */
.ant-modal-body::-webkit-scrollbar {
  width: 0;
  height: 30px;
}

/* /兼容火狐/ */
.ant-modal-body {
  scrollbar-width: none;
}

/* / 兼容IE10+ */
.ant-modal-body {
  -ms-overflow-style: none;
}
</style>