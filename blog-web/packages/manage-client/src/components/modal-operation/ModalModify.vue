<template>
  <a-modal
    v-model:open="open"
    title="更新"
    :closable="false"
    @ok="clickOk"
    @cancel="cancel"
    class="modal-modify"
    :key="Math.random()"
    :centered="true"
    :okText="props.okText"
    :ok-button-props="{
      danger: props.danger,
    }"
    :width="props.width"
  >
    <delete-link
      ref="link"
      :isDisplay="isDisplay"
      :columns="columns"
      :is-transfer="isTransfer"
      :options="props.options"
    >
      <template #input>
        <a-input v-model:value="inputValue" show-count :maxlength="props.maxLength" />
      </template>
    </delete-link>
    <slot name="other" />
  </a-modal>
</template>

<script setup lang="ts">
import { messageInfo } from '@tanxiang/common';

import { ref, watchEffect, nextTick } from 'vue';
import DeleteLink from '@/components/modal-operation/DeleteLink.vue';

const { messageLoading, messageError } = messageInfo();

const link = ref<any>();
const modify = ref<any>({});
const open = ref<boolean>(false);
const inputValue = ref<string>('');
const isDisplay = ref<boolean>(true);

const props = defineProps({
  okClick: { type: Function, default: () => {} },
  maxLength: { type: Number, default: 20 },
  okText: { type: String, default: '确定' },
  danger: { type: Boolean, default: false },
  width: { type: [Number, String], default: 600 },
  columns: { type: Array, default: () => [] },
  isTransfer: { type: Boolean, default: false },
  options: { type: Array, default: () => [] },
});

const showModal = (key: any, fn: Function, flag = true, isOpen = true) => {
  modify.value = key;
  open.value = isOpen;
  isDisplay.value = flag;
  nextTick(() => {
    if (fn) fn(key, link.value, false);
  });
};

const clickOk = () => {
  let list = link.value.list;
  if (props.isTransfer) {
    let temp = [];
    list.newLinkDatas.forEach((item) => temp.push(...item.datas));
    if (modify.value.name === inputValue.value && temp.length === 0) {
      return messageError('数据未变化！');
    }
  } else {
    if (modify.value.name === inputValue.value && !list.checkList.length) return messageError('数据未变化，无法提交！');
  }
  messageLoading('更新中');
  props.okClick(modify, inputValue.value, list, cancel);
};

const cancel = () => {
  open.value = false;
  modify.value = {};
  link.value.list.datas = [];
  link.value.list.checkList = [];
};

watchEffect(() => {
  if (link.value && modify.value.name) inputValue.value = modify.value.name;
});

defineExpose({ showModal, cancel });
</script>

<style scoped></style>

<style>
.modal-modify .ant-checkbox-wrapper {
  padding: 8px 8px;
}
</style>