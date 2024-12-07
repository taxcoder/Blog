<template>
  <div class="inline-flex flex-row w-full input-update">
    <a-input
      v-if="!textarea"
      v-model:value="getData.content"
      :readOnly="!getData.input"
      :bordered="getData.input"
      @pressEnter="submit(getData)"
      @input="input($event)"
      class="h-[30px]"
      :class="[
        { 'line-through': props.isSuccess && !getData.input },
        { 'color-[#1677FF]': props.isSuccess && !getData.input },
      ]"
    />
    <a-textarea
      v-else
      :autoSize="true"
      v-model:value="getData.content"
      :readOnly="!getData.input"
      :bordered="getData.input"
      @pressEnter="submit(getData)"
      @input="input($event)"
      class="h-[30px]"
      :class="[
        { 'line-through': props.isSuccess && !getData.input },
        { 'color-[#1677FF]': props.isSuccess && !getData.input },
      ]"
    />
    <LoadingOutlined v-show="getData.loading" class="text-[18px] ml-[5px]" />
    <CheckOutlined
      v-show="!getData.loading && getData.isClick"
      class="cursor-pointer text-[18px] ml-[5px]"
      @click="submit(getData)"
    />
    <EditOutlined
      v-show="!getData.loading && !getData.isClick"
      class="cursor-pointer text-[18px] ml-[5px] opacity-0 edit"
      :class="{
        'color-[#ccc]': props.isSuccess,
        'important-cursor-no-drop': props.isSuccess,
      }"
      @click="change(getData)"
    />
  </div>
</template>

<script setup lang="ts">
import { CheckOutlined, EditOutlined, LoadingOutlined } from '@ant-design/icons-vue';
import { computed, reactive, watch, watchEffect } from 'vue';
import messageInfo from '../../config/message';

const { messageError, messageSuccess } = messageInfo();

const list = reactive<any[]>([]);

//@ts-ignore
const props = defineProps({
  datas: {
    type: Array as any[],
    required: true,
  },
  record: {
    type: Object as any,
    required: true,
  },
  textarea: {
    type: Boolean,
    default: false,
  },
  fn: {
    type: Function as Promise<any>,
    required: true,
  },
  isSuccess: {
    type: Boolean,
    default: false,
  },
  condition: {
    type: String,
    required: true,
  },
});

const change = (record: any) => {
  if (!record.input) {
    let map = list.map((item: any) => {
      let status = item.id === record.id && item.condition === props.condition;
      item.input = status ? !item.input : false;
      if (status) {
        item.isClick = true;
      } else if (!item.loading && item.content === item.oldContent) {
        item.isClick = false;
      } else if (!item.loading) {
        submit(item);
      }
      return item;
    });
    list.splice(0, list.length, ...map);
  }
};

const submit = (record: any) => {
  if (props.isSuccess) {
    record.input = false;
    record.isClick = false;
    return messageError('已完成的目标不能修改！');
  } else if (getData.value.content === getData.value.oldContent) {
    record.input = false;
    record.isClick = false;
    return messageError('内容未改变！');
  }
  record.loading = true;
  props
    .fn(record.id, getData.value.content)
    .then((success: any) => {
      messageSuccess(success.data);
      getData.value.oldContent = getData.value.content;
    })
    .catch((error: any) => messageError(!error || error.name ? '网络错误！' : error))
    .finally(() => {
      record.loading = false;
      record.isClick = false;
      record.input = false;
    });
};

const input = (e: any) => {
  if (e.target.value !== getData.value.content) {
    getData.value.content = e.target.value;
  }
};

const getData = computed((): any => {
  if (list.length === 0) return {};
  return list.find((d: any) => d.id === props.record['id'] && d.condition === props.condition);
});

watch(
  () => props.datas,
  (newValue) => {
    list.splice(0, list.length, ...newValue);
  },
  { deep: true }
);

watchEffect(() => {
  if (list.length === 0 && props.datas.length !== 0) {
    list.splice(0, list.length, ...props.datas);
  }
});
</script>

<style scoped>
.input-update:hover .edit {
  opacity: 1 !important;
}
</style>