<template>
  <div id="emoji">
    <a-modal v-model:open="open" :centered="true" title="添加表情" @cancel="cancel" @ok="ok" :width="534">
      <div class="clearfix flex">
        <span class="flex-1 text-[0.93rem]">图片：</span>
        <a-upload
          :max-count="40"
          class="flex-[8_8_0%]"
          :multiple="true"
          :capture="null"
          v-model:file-list="fileList"
          list-type="picture-card"
          @preview="handlePreview"
          :before-upload="() => false"
        >
          <div v-if="fileList.length < 40">
            <plus-outlined />
            <div style="margin-top: 8px">Upload</div>
          </div>
        </a-upload>
        <a-modal :open="previewVisible" :title="previewTitle" :footer="null" @cancel="handleCancel">
          <img alt="example" style="width: 100%" :src="previewImage" />
        </a-modal>
      </div>
      <div class="mt-[20px] flex-center">
        <span class="flex-1 text-[0.93rem]">组名：</span>
        <el-select v-model="text" placeholder="Select" class="w-full flex-[8_8_0%]">
          <el-option v-for="item in cities" :key="item.value" :label="item.label" :value="item.value" />
          <template #footer>
            <el-button
              v-if="isAdding"
              type="primary"
              @click="onAddOption"
              v-text="'增加分组'"
              class="w-full rounded-[8px] flex-center"
            />
            <el-button
              v-if="isAdding"
              type="danger"
              @click="clearGroupName"
              v-text="'清除自定义分组'"
              class="w-full rounded-[8px] flex-center important-ml-0 mt-[5px]"
            />
            <template v-else>
              <el-input v-model="optionName" class="rounded-lg" placeholder="输入分组名" />
              <div class="mt-[7px] text-center">
                <el-button type="primary" @click="onConfirm" v-text="'提交'" />
                <el-button @click="clear" v-text="'取消'" />
              </div>
            </template>
          </template>
        </el-select>
      </div>
    </a-modal>
    <table-search
      :formState="formState"
      :name="name"
      @dataSource="dataSource"
      @current="current"
      @add="add"
      @local="local"
      :local="true"
    />
    <list-emoji-table :source="showDataSource" @dataSource="dataSource" @current="current" />
  </div>
</template>

<script setup lang="ts">
import ListEmojiTable from '@/pages/list-emoji/ListEmojiTable.vue';
import TableSearch from '@/components/table-search/TableSearch.vue';
import { useEmojiStore } from '@/stores/emoji';
import { useAuthEmoji, useUpload } from '@tanxiang/apis';
import { messageInfo } from '@tanxiang/common';
import { UploadProps, notification } from 'ant-design-vue';
import { PlusOutlined } from '@ant-design/icons-vue';
import { useBaseStore } from '@/stores/base';

const route = useRoute();
const router = useRouter();
const emoji = useEmojiStore();
const base = useBaseStore();
const auth = useAuthEmoji();
const tabCurrent = ref<number>(0);
const open = ref<boolean>(false);
const isAdding = ref<boolean>(true);
// 新增的分组的名称
const optionName = ref<string>('');
// 选中的分组
const text = ref<string>('');

const previewVisible = ref(false);
const previewImage = ref('');
const previewTitle = ref('');
const fileList = ref<UploadProps['fileList']>([]);

const { messageError, messageWarning, messageLoading, messageSuccess } = messageInfo();

const tableSource = ref<{ records: any[]; total: number }>({
  records: [],
  total: 0,
});

const showDataSource = ref<{ records: any[]; total: number }>({
  records: [],
  total: 0,
});

const name = ref<string>('emoji');

const formState = reactive<any>({
  key: [
    { value: 'id', label: 'ID', placeholder: '请输入一个正整数，不能为0' },
    { value: 'name', label: '表情名称', placeholder: '根据表情名称模糊查询' },
    { value: 'groupName', label: '组名', placeholder: '根据组名模糊查询' },
  ],
  select: 'id',
  value: '',
});

provide('tabCurrent', tabCurrent);
provide('deleteEmoji', (id: string) => deleteEmoji(id));
provide('updateEmoji', (id: string, groupName: string) => updateEmoji(id, groupName));

const openNotificationWithIcon = (type: string, title: string, description: string) => {
  notification[type]({
    message: title,
    description: description,
  });
};

const cancel = () => {
  open.value = false;
  text.value = cities.value[0].label;
  fileList.value.splice(0, fileList.value.length);
};

const ok = () => {
  messageLoading('正在上传中！');
  if (fileList.value.length === 0) messageWarning('没有需要上传的图片！');
  let temp = fileList.value
    .map((item, index) => (item.name.replace(/(.jpg|.png|.jpeg|.webp)/g, '').length > 20 ? index : -1))
    .filter((item) => item != -1);
  if (temp.length !== 0) {
    return messageWarning(`文件名过长，名称长度请勿大于20个字符，存在问题的图片索引：${temp}`);
  }

  useUpload()
    .uploadEmoji(fileList.value, text.value)
    .then((res: any) => {
      messageSuccess(res.message);
      if (res.data && res.data.length > 0) {
        let desc = '';
        let errorList = [];
        res.data.forEach((item: any) => {
          desc += item + '\r';
          errorList.push(...fileList.value.filter((list: any) => list.name === item));
        });
        fileList.value.splice(0, fileList.value.length, ...errorList);
        openNotificationWithIcon('warning', '警告信息', `文件\r${desc}上传失败，重新上传！`);
        return;
      } else {
        clear();
        setTimeout(() => {
          router.replace({ path: '/refresh', query: { t: Date.now(), f: route.path } });
        }, 1000);
      }
    })
    .catch((err: any) => {
      messageError(!err || err.name ? '网络错误！' : err);
      if (err.data && err.data.length > 0) {
        let desc = '';
        err.data.forEach((item: any) => (desc += item + '\r'));
        openNotificationWithIcon('warning', '警告信息', `文件\r${desc}上传成功，请及时删除！`);
        clear();
      }
    });
};

const add = () => {
  open.value = true;
};

const clear = () => {
  optionName.value = '';
  fileList.value.splice(0, fileList.value.length);
  isAdding.value = true;
};

const clearGroupName = () => {
  emoji.clearGroup();
  emoji.setGroup(emoji.getInitGroup);
};

const onConfirm = () => {
  if (optionName.value) {
    emoji.addGroup(optionName.value);
    clear();
  }
};

const onAddOption = () => (isAdding.value = false);

const local = (obj: { value: string; select: string }) => {
  if (obj.value === '') {
    showDataSource.value.records.splice(0, showDataSource.value.records.length, ...tableSource.value.records);
    showDataSource.value.total = tableSource.value.records.length;
  } else {
    let temp = tableSource.value.records.filter((record) => record[obj.select].indexOf(obj.value) !== -1);
    showDataSource.value.records.splice(0, showDataSource.value.records.length, ...temp);
    showDataSource.value.total = temp.length;
  }
};

const deleteEmoji = (id: string) => {
  let temp = tableSource.value.records.filter((record) => record.id !== id);
  tableSource.value.records.splice(0, tableSource.value.records.length, ...temp);
  tableSource.value.total = temp.length;
  local({ value: formState.value, select: formState.select });
};
/**
 * 更新emoji里面的一条数据
 * @param id emojiId
 * @param groupName 组名
 */
const updateEmoji = (id: string, groupName: string) => {
  auth
    .updateGroupName(id, groupName)
    .then(() => {
      let temp = tableSource.value.records.map((record: any) => {
        if (record.id === id) record.groupName = groupName;
        return record;
      });
      tableSource.value.records.splice(0, tableSource.value.records.length, ...temp);
      local({ value: formState.value, select: formState.select });
    })
    .catch((err) => messageError(!err || err.name ? '网络错误！' : err));
};

const dataSource = (source: any) => {
  let temp = source.records[0];
  base.setEmojis(temp);
  let record = [];
  let group = Object.keys(temp);
  group.forEach((key) => {
    for (let value of temp[key]) {
      value['groupName'] = key;
      record.push(value);
    }
  });

  source.records.splice(0, source.records.length);
  source.records.push(...record);
  source.total = record.length;
  tableSource.value = source;
  local({ value: formState.value, select: formState.select });
  emoji.setGroup(group);
  emoji.setInitGroup(group);
};

const current = (c: any) => (tabCurrent.value = c);

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

const cities = computed(() => {
  let group = emoji.getGroup.map((item: string) => ({ label: item, value: item }));
  text.value = group[0].label;
  return group;
});

watch(
  () => formState,
  () => {
    emoji.setSearch({ name: name.value, value: formState.value, select: formState.select });
  },
  { deep: true }
);
</script>

<style>
.ant-upload-select-picture-card i {
  font-size: 32px;
  color: #999;
}

.ant-upload-select-picture-card .ant-upload-text {
  margin-top: 8px;
  color: #666;
}

.ant-upload-select-picture-card,
.ant-upload-list-item-container:nth-child(4n) {
  margin-inline-end: 0 !important;
}
</style>