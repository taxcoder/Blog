<template>
  <a-modal
    v-model:open="open"
    :title="title"
    :bodyStyle="bodyStyle"
    :closable="false"
    @ok="clickOk"
    @cancel="cancel"
    class="modal-delete"
    :centered="true"
    :width="props.width"
    :cancelButtonProps="cancelButtonProps"
    :okButtonProps="okButtonProps"
    :wrapClassName="wrapClassName"
  >
    <slot name="content"></slot>
    <delete-link
      ref="link"
      :isDisplay="isDisplay"
      :columns="props.columns"
      :options="props.options"
      :is-transfer="isTransfer"
    />
    <slot name="other" />
  </a-modal>
</template>

<script setup lang="ts">
import { messageInfo } from '@tanxiang/common';

import { ref, computed, nextTick } from 'vue';
import DeleteLink from '@/components/modal-operation/DeleteLink.vue';

const { messageLoading, messageError } = messageInfo();

const link = ref<any>();
const data = ref<any>({});
const open = ref<boolean>(false);
const isDisplay = ref<boolean>(true);

const props = defineProps({
  okClick: { type: Function, required: true },
  width: { type: Number, default: 600 },
  title: { type: String, default: '删除' },
  wrapClassName: { type: String, default: '' },
  notDisabled: { type: Boolean, default: false },
  bodyStyle: { type: Object, default: {} },
  cancelButtonProps: { type: Object, default: {} },
  okButtonProps: { type: Object, default: {} },
  isTransfer: { type: Boolean, default: false },
  options: { type: Array, default: () => [] },
  columns: { type: Array, default: () => [] },
});

const showModal = (key: any, fn: Function, flag = true) => {
  data.value = key;
  open.value = true;
  isDisplay.value = flag;
  nextTick(() => {
    if (fn) fn(key, link.value, true);
  });
};

const clickOk = () => {
  let list = link.value.list;
  if (
    !isDisplay.value &&
    list.datas.length > 0 &&
    (linkData.value.length === 0 || linkData.value.length !== list.datas.length)
  ) {
    return messageError('仍有关联的数据！');
  }

  messageLoading('删除中');
  props.okClick(data, list, cancel);
};

const cancel = () => {
  open.value = false;
  data.value = {};
  link.value.list.datas = [];
  link.value.list.checkList = [];
};

const linkData = computed(() => {
  let temp = [];
  if (!link.value || link.value.list.newLinkDatas.length === 0) return temp;
  link.value.list.newLinkDatas.forEach((item: any) => temp.push(...item.datas));
  return temp;
});

defineExpose({ showModal, cancel });
</script>

<style scoped></style>

<style>
.modal-delete .ant-checkbox-wrapper {
  padding: 8px 12px;
}
</style>