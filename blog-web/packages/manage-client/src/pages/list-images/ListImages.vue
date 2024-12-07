<template>
  <div id="images" class="h-screen">
    <a-modal v-model:open="open" :centered="true" title="上传图片" @cancel="cancel" @ok="ok" :width="534">
      <div class="clearfix flex">
        <span class="flex-1 text-[0.93rem] min-w-[50px]">图片：</span>
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
      <a-date-picker v-model:value="uploadTime" class="rounded-[5px] cursor-pointer w-full" />
    </a-modal>
    <table-search
      :formState="formState"
      :name="name"
      :clearable="false"
      :show="false"
      @dataSource="dataSource"
      @current="current"
      @add="add"
    >
      <template #select v-if="formState.select !== 'id'">
        <a-range-picker
          v-model:value="picker"
          :format="dateFormat"
          class="rounded-0 important-cursor-pointer w-full"
          :picker="formState.select"
          @calendarChange="panelChange"
          :disabled="isDisabled"
        />
        <div
          @click="isOpenVanCalender('start')"
          class="z-9999 absolute w-40% h-full cursor-pointer left-0"
          v-show="isDisabled"
        ></div>
        <div
          @click="isOpenVanCalender('end')"
          class="z-9999 absolute w-40% h-full cursor-pointer right-[35px]"
          v-show="isDisabled"
        ></div>
        <van-popup
          v-if="isDisabled"
          v-model:show="show"
          :style="{ padding: '64px' }"
          teleport="body"
          position="bottom"
          :transition-appear="true"
          :close-on-popstate="true"
          :safe-area-inset-bottom="true"
          :safe-area-inset-top="true"
        >
          <van-date-picker
            v-model="currentDate"
            title="选择日期"
            :formatter="formatter"
            @confirm="onConfirm"
            :min-date="minDate"
            :max-date="maxDate"
            :columns-type="columnsType"
          ></van-date-picker>
        </van-popup>
      </template>
    </table-search>
    <list-images-table :source="tableSource" @dataSource="dataSource" @current="current" />
  </div>
</template>

<script setup lang="ts">
import TableSearch from '@/components/table-search/TableSearch.vue';
import { provide, reactive, ref, watch, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import ListImagesTable from '@/pages/list-images/ListImagesTable.vue';
import dayjs, { Dayjs } from 'dayjs';
import { useImagesStore } from '@/stores/images';
import { useAuthImages, useUpload } from '@tanxiang/apis';
import { PlusOutlined } from '@ant-design/icons-vue';
import { UploadProps } from 'ant-design-vue';
import { messageInfo } from '@tanxiang/common';

const route: any = useRoute();
const images = useImagesStore();
const router: any = useRouter();
const auth = useAuthImages();

const { messageLoading, messageWarning, messageError, messageSuccess } = messageInfo();

const minDate = ref<Date>(new Date('2020-01-01'));
const maxDate = ref<any>(new Date());
const currentDate = ref<string[]>([]);

const show = ref<boolean>(false);
const open = ref<boolean>(false);
const tableSource = ref<any>({});
const tabCurrent = ref<number>(0);

const currentStatus = ref<string>('start');

const previewVisible = ref(false);
const previewImage = ref('');
const previewTitle = ref('');
const fileList = ref<UploadProps['fileList']>([]);

const picker = reactive<[Dayjs, Dayjs]>([]);
const uploadTime = ref<Dayjs>(dayjs());

const name = ref<string>('images');

const formState = reactive<any>({
  key: [
    { value: 'id', label: 'ID', placeholder: '请输入一个正整数，不能为0' },
    { value: 'year', label: '日期(年)', placeholder: '' },
    { value: 'month', label: '日期(月)', placeholder: '' },
    { value: 'date', label: '日期(日)', placeholder: '' },
  ],
  select: 'id',
  value: '',
});

const status = inject('status');

const emit = defineEmits(['searchData']);

onMounted(() => {
  if (route.query.key && formState.key.find((c: any) => c.value === route.query.key))
    formState.select = route.query.key;
  if (route.query.value !== void 0) formState.value = route.query.value;
});

provide('tabCurrent', tabCurrent);

const dataSource = (source: any) => (tableSource.value = source);

const current = (c: any) => (tabCurrent.value = c);

const add = () => (open.value = true);

const isOpenVanCalender = (status: string) => {
  currentStatus.value = status;
  if (!isDisabled.value) return;
  show.value = true;
  if (status === 'start') {
    switch (formState.select) {
      case 'year':
        currentDate.value = [picker[0].year().toString()];
        break;
      case 'month':
        currentDate.value = [picker[0].year().toString(), (picker[0].month() + 1).toString()];
        break;
      case 'date':
        currentDate.value = [
          picker[0].year().toString(),
          (picker[0].month() + 1).toString(),
          picker[0].date().toString(),
        ];
        break;
    }
  } else {
    switch (formState.select) {
      case 'year':
        currentDate.value = [picker[1].year().toString()];
        break;
      case 'month':
        currentDate.value = [picker[1].year().toString(), (picker[1].month() + 1).toString()];
        break;
      case 'date':
        currentDate.value = [
          picker[1].year().toString(),
          (picker[1].month() + 1).toString(),
          picker[1].date().toString(),
        ];
        break;
    }
  }
};

const cancel = () => {
  open.value = false;
  fileList.value.splice(0, fileList.value.length);
  uploadTime.value = dayjs();
};

const ok = () => {
  messageLoading('正在上传中！');
  if (fileList.value.length === 0) messageWarning('没有需要上传的图片！');
  if (fileList.value.length === 1) {
    useUpload()
      .uploadImageOnce(fileList.value[0], uploadTime.value.unix() * 1000)
      .then((res: any) => {
        messageSuccess(res.message);
        formState.value = '';
        formState.select = formState.select !== 'id' ? 'id' : 'year';
        cancel();
      })
      .catch((err: any) => messageError(!err || err.name ? '网络错误！' : err));
  } else {
    useUpload()
      .uploadImageMore(fileList.value, uploadTime.value.unix() * 1000)
      .then((res: any) => {
        messageSuccess(res.message);
        formState.value = '';
        formState.select = formState.select !== 'id' ? 'id' : 'year';
        cancel();
      })
      .catch((err: any) => messageError(!err || err.name ? '网络错误！' : err));
  }
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

const panelChange = (data: Dayjs[], dateStrings, info) => {
  if (info.range === 'start') return;
  if (formState.select === 'year') {
    formState.value = {
      start: dayjs(new Date(dateStrings[0] + '-01-01 00:00:00')).unix() * 1000,
      end: dayjs(new Date(dateStrings[1] + '-12-31 23:59:59')).unix() * 1000,
    };
  } else if (formState.select === 'month') {
    let s = dayjs(new Date(dateStrings[0] + '/01 00:00:00'));
    let e =
      data[1].month() + 1 < 12
        ? dayjs(new Date(data[1].year() + '-' + (data[1].month() + 2) + '-01 00:00:00')).subtract(1, 'day')
        : dayjs(new Date(data[1].year() + '-12-31 23:59:59'));
    formState.value = { start: s.unix() * 1000, end: e.unix() * 1000 };
  } else if (formState.select === 'date') {
    formState.value = {
      start: dayjs(new Date(dateStrings[0] + ' 00:00:00')).unix() * 1000,
      end: dayjs(new Date(dateStrings[1] + ' 23:59:59')).unix() * 1000,
    };
  }
};

const formatter = (type, option) => {
  if (type === 'year') {
    option.text += '年';
  }
  if (type === 'month') {
    option.text += '月';
  }
  if (type === 'day') {
    option.text += '日';
  }
  return option;
};

const onConfirm = (selected) => {
  let selectedValues = selected.selectedValues;
  if (currentStatus.value === 'start') {
    let temp = picker[0];
    switch (formState.select) {
      case 'year':
        picker[0] = dayjs(selectedValues[0] + '-' + (temp.month() + 1) + '-' + temp.date());
        break;
      case 'month':
        picker[0] = dayjs(selectedValues[0] + '-' + selectedValues[1] + '-' + temp.date());
        break;
      case 'date':
        picker[0] = dayjs(selectedValues[0] + '-' + selectedValues[1] + '-' + selectedValues[2]);
        break;
    }
  } else {
    let temp = picker[1];
    switch (formState.select) {
      case 'year':
        picker[1] = dayjs(selectedValues[0] + '-' + (temp.month() + 1) + '-' + temp.date());
        break;
      case 'month':
        picker[1] = dayjs(selectedValues[1] + '-' + selectedValues[1] + '-' + temp.date());
        break;
      case 'date':
        picker[1] = dayjs(selectedValues[1] + '-' + selectedValues[1] + '-' + selectedValues[2]);
        break;
    }
  }
  show.value = false;
  panelChange(
    picker,
    formState.select === 'year'
      ? [picker[0].year(), picker[1].year()]
      : formState.select === 'month'
      ? [picker[0].year() + '/' + (picker[0].month() + 1), picker[1].year() + '/' + (picker[1].month() + 1)]
      : formState.select === 'date'
      ? [
          picker[0].year() + '/' + (picker[0].month() + 1) + '/' + picker[0].date(),
          picker[1].year() + '/' + (picker[1].month() + 1) + '/' + picker[1].date(),
        ]
      : '',
    { range: 'end' }
  );
};

const columnsType = computed(() => {
  return formState.select === 'year'
    ? ['year']
    : formState.select === 'month'
    ? ['year', 'month']
    : ['year', 'month', 'day'];
});

const dateFormat = computed(() => {
  if (formState.select === 'year') return 'YYYY';
  if (formState.select === 'month') return 'YYYY/MM';
  if (formState.select === 'date') return 'YYYY/MM/DD';
  return 'YYYY/MM/DD';
});

const isDisabled = computed(() => {
  return (status.width === 0 ? window.innerWidth : status.width) < 768;
});

watch(
  () => formState,
  () => {
    images.setSearch({ name: name.value, value: formState.value, select: formState.select });
  },
  { deep: true }
);

watch(
  () => [formState.select, isDisabled.value],
  ([newSelect, isDisabled]) => {
    formState.value = '';
    if (newSelect === 'id') return;
    if (isDisabled && picker.length === 0) {
      picker.push(dayjs(minDate.value));
      picker.push(dayjs(maxDate.value));
    }
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