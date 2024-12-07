<template>
  <div id="navigation" class="h-screen">
    <a-modal v-model:open="open" :centered="true" title="上传图片" @cancel="cancel" @ok="ok" :width="534">
      <div>
        <div class="clearfix flex mb-[10px]">
          <span class="flex-1 text-[0.93rem] min-w-[50px]">图片：</span>
          <user-upload
            @change-file="changeFile"
            :list="navigation.file"
            class="h-full w-full flex-8"
            :class="{
              'important-h-[200px]': navigation.file.length === 0,
            }"
          />
          <a-modal :open="previewVisible" :title="previewTitle" :footer="null" @cancel="handleCancel">
            <img alt="example" style="width: 100%" :src="previewImage" />
          </a-modal>
        </div>
        <div class="clearfix flex mb-[10px]">
          <span class="flex-1 text-[0.93rem] min-w-[50px]">网址：</span>
          <a-input
            v-model:value="navigation.url"
            placeholder="导航网站网址"
            :maxLength="200"
            showCount
            size="small"
            class="w-full"
          ></a-input>
        </div>
        <div class="clearfix flex mb-[10px]">
          <span class="flex-1 text-[0.93rem] min-w-[50px]">标题：</span>
          <a-input
            v-model:value="navigation.title"
            placeholder="导航网站标题"
            :maxLength="20"
            showCount
            size="small"
            class="w-full"
          ></a-input>
        </div>
        <div class="clearfix flex">
          <span class="flex-1 text-[0.93rem] min-w-[50px]">描述：</span>
          <a-textarea
            v-model:value="navigation.description"
            placeholder="导航网站描述"
            autoSize
            :maxLength="50"
            showCount
            size="small"
            class="w-full"
          ></a-textarea>
        </div>
        <div class="clearfix flex">
          <span class="flex-1 text-[0.93rem] min-w-[50px]">分类：</span>
          <a-select class="w-full" v-model:value="navigation.cnId" :options="getOptions"></a-select>
        </div>
      </div>
    </a-modal>
    <table-search
      :formState="formState"
      :name="name"
      :clearable="false"
      :show="false"
      @dataSource="dataSource"
      @current="current"
      @add="add"
    ></table-search>
    <list-navigation-table :source="tableSource" @dataSource="dataSource" @current="current" />
  </div>
</template>

<script setup lang="ts">
import TableSearch from '@/components/table-search/TableSearch.vue';
import { provide, reactive, ref, watch, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useAuthNavigation } from '@tanxiang/apis';
import { UploadProps } from 'ant-design-vue';
import { messageInfo } from '@tanxiang/common';
import ListNavigationTable from '@/pages/list-navigation/ListNavigationTable.vue';
import { useNavigationStore } from '@/stores/navigation';
import UserUpload from '@/components/user-upload/UserUpload.vue';

const route: any = useRoute();
const navigations = useNavigationStore();
const router: any = useRouter();
const auth = useAuthNavigation();

const { messageLoading, messageWarning, messageError, messageSuccess } = messageInfo();

const show = ref<boolean>(false);
const open = ref<boolean>(false);
const tableSource = reactive<any>({
  total: 0,
  records: [],
  size: 15,
  current: 1,
});
const tabCurrent = ref<number>(0);
const previewImage = ref('');
const previewTitle = ref('');
const previewVisible = ref(false);

const options = reactive<{ value: any; label: string }[]>([]);

const name = ref<string>('navigation');

const formState = reactive<any>({
  key: [
    { value: 'id', label: 'ID', placeholder: '请输入一个正整数，不能为0' },
    { value: 'cnName', label: 'ID', placeholder: '请输入字符串' },
    { value: 'title', label: 'ID', placeholder: '请输入字符串' },
    { value: 'description', label: 'ID', placeholder: '请输入字符串' },
    { value: 'ok', label: '状态', placeholder: '请输入0或1' },
  ],
  select: 'id',
  value: '',
});

const navigation = reactive<any>({
  title: '',
  url: '',
  description: '',
  cnId: -1,
  file: [] as File[],
});

onMounted(() => {
  if (route.query.key && formState.key.find((c: any) => c.value === route.query.key))
    formState.select = route.query.key;
  if (route.query.value !== void 0) formState.value = route.query.value;
});

provide('tabCurrent', tabCurrent);

const dataSource = (source: any) => {
  tableSource.current = source.current;
  tableSource.size = source.size;
  tableSource.total = source.total;
  tableSource.records.splice(0, tableSource.records.length, ...source.records);
};

const current = (c: any) => (tabCurrent.value = c);

const add = () => (open.value = true);

const cancel = () => (open.value = false);

const ok = () => {
  messageLoading('正在上传中！');
  if (navigation.title === '' || navigation.title.length === 0) return messageWarning('标题不能为空！');
  if (navigation.description === '' || navigation.description.length === 0) return messageWarning('描述不能为空！');
  if (navigation.cnId === -1) return messageWarning('未选择分类，无法添加！');
  let regex =
    /^(http:\/\/www\.|https:\/\/www\.|http:\/\/|https:\/\/)?[a-z0-9]+([\-.]{1}[a-z0-9]+)*\.[a-z]{2,5}(:[0-9]{1,5})?(\/.*)?$/;
  if (!regex.test(navigation.url)) return messageWarning('网址格式错误！');
  auth
    .addNavigation(
      navigation.cnId,
      navigation.url,
      navigation.title,
      navigation.description,
      navigation.file.length === 0 ? null : navigation.file
    )
    .then((res) => {
      messageSuccess(res.data);
      cancel();
      navigation.cnId = -1;
      navigation.url = '';
      navigation.title = '';
      navigation.description = '';
      navigation.file.splice(0, navigation.file.length);
      router.replace({ path: '/refresh', query: { t: Date.now(), f: route.path } });
    })
    .catch((err) => messageError(!err || err.name ? '网络错误' : err));
};

const handleCancel = () => {
  previewVisible.value = false;
  previewTitle.value = '';
};

const getBase64 = (file: File) => {
  return new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = () => resolve(reader.result);
    reader.onerror = (error) => reject(error);
  });
};

const handlePreview = async (file: UploadProps['fileList'][number]) => {
  if (!file.url && !file.preview) {
    file.preview = (await getBase64(file.originFileObj)) as string;
  }
  previewImage.value = file.url || file.preview;
  previewVisible.value = true;
  previewTitle.value = file.name || file.url.substring(file.url.lastIndexOf('/') + 1);
};

const changeFile = (file: UploadProps['fileList']) => {
  navigation.file.splice(0, navigation.file.length, ...file);
};

const getOptions = computed(() => {
  if (options.length !== 0) {
    if (navigation.cnId === -1) navigation.cnId = options[0].value;
    return options;
  }

  if (navigations.getCnName.length !== 0) {
    options.push(...navigations.getCnName);
    if (navigation.cnId === -1) navigation.cnId = options[0].value;
    return options;
  }

  auth
    .selectCnInfo()
    .then((info) => {
      navigations.setCnName(info.data);
      options.push(...navigations.getCnName);
      if (navigation.cnId === -1) navigation.cnId = options[0].value;
    })
    .catch((err) => messageError(!err || err.name ? '网络错误！' : err));
});

watch(
  () => formState,
  () => {
    navigations.setSearch({ name: name.value, value: formState.value, select: formState.select });
  },
  { deep: true }
);

watch(
  () => formState.select,
  () => {
    formState.value = '';
  }
);
</script>

<style>
.input-clear .ant-picker {
  padding: 6px 12px 4px 10px;
  width: 100%;
}

.input-clear .ant-picker input[disabled] {
  cursor: pointer;
}
</style>