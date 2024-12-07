<template>
  <NormalToolbar :title="title" @onClick="markHandler">
    <template #trigger>
      <svg class="iconfont mark" aria-hidden="true">
        <use xlink:href="#icon-mark"></use>
      </svg>
      <div v-if="showToolbarName" class="md-editor-toolbar-item-name item-name">{{ title }}</div>
    </template>
  </NormalToolbar>
</template>

<script setup lang="ts">
import type { PropType } from 'vue';
import { NormalToolbar } from 'md-editor-v3';
import type { InsertContentGenerator } from 'md-editor-v3';

const title = ref<string>('高亮');

const props = defineProps({
  insert: {
    type: Function as PropType<(generator: InsertContentGenerator) => void>,
    default: () => () => null,
  },
  showToolbarName: Boolean,
});

const markHandler = () => {
  const generator: InsertContentGenerator = (selectedText) => {
    return {
      targetValue: `==${selectedText}==`,
      select: true,
      deviationStart: 2,
      deviationEnd: -2,
    };
  };

  props.insert(generator);
};
</script>

<style scoped>
.mark {
  width: 24px;
  fill: currentColor;
  height: 24px;
  display: block;
  padding: 3px 0;
}
</style>
