<template>
  <DropdownToolbar :title="title" :visible="state.visible" @onChange="onChange">
    <template #overlay>
      <div class="emoji-container">
        <a-select ref="select" v-model:value="value" class="w-full">
          <a-select-option :value="d" v-for="d in data">{{ d }}</a-select-option>
        </a-select>
        <ol class="emojis" v-if="data.length > 0">
          <li v-for="emoji of emojis" :key="`emoji-${emoji.id}`" @click="emojiHandler(emoji.address, emoji.name)">
            <img :src="emoji.address" :alt="emoji.name" :title="emoji.name" class="w-full h-full object-cover" />
          </li>
        </ol>
      </div>
    </template>
    <template #trigger>
      <svg class="md-editor-icon emoji" aria-hidden="true">
        <use xlink:href="#icon-emoji"></use>
      </svg>
      <div v-if="showToolbarName" class="md-editor-toolbar-item-name item-name">{{ title }}</div>
    </template>
  </DropdownToolbar>
</template>

<script lang="ts" setup>
import type { PropType } from 'vue';
import { reactive, onMounted, ref } from 'vue';

import { DropdownToolbar } from 'md-editor-v3';
import type { InsertContentGenerator } from 'md-editor-v3';

import { useAuthEmoji } from '@tanxiang/apis';
import { useBaseStore } from '@/stores/base';
import { messageInfo } from '@tanxiang/common';

const { messageError } = messageInfo();

const auth = useAuthEmoji();
const base = useBaseStore();

const value = ref<string>('');
const current = ref<number>(0);
const title = ref<string>('Emoji');

const data = reactive<string[]>([]);
const dataSource = reactive<any[]>([]);

onMounted(() => {
  if (Object.keys(base.getEmojis).length === 0) {
    auth
      .emojiList()
      .then((success: any) => {
        // 存储着emoji的分组名
        data.push(...Object.keys(success.data.records[0]));
        value.value = data[current.value];
        base.setEmojis(success.data.records[0]);
      })
      .catch(() => messageError('网络错误！'));
  } else {
    data.push(...Object.keys(base.getEmojis));
    value.value = data[current.value];
  }
});

const props = defineProps({
  insert: {
    type: Function as PropType<(generator: InsertContentGenerator) => void>,
    default: () => null,
  },
  showToolbarName: Boolean,
});

const emojiHandler = (emoji: string, name: string) => {
  const generator: InsertContentGenerator = () => {
    return {
      targetValue: `<img src='${emoji}' alt='${name}' title='${name}' class='emoji-img' />`,
      select: true,
      deviationStart: 0,
      deviationEnd: 0,
    };
  };

  props.insert(generator);
  state.visible = false;
};

const state = reactive({ visible: false });

const onChange = (visible: boolean) => (state.visible = visible);

const emojis = computed(() => {
  return base.getEmojis[value.value];
});
</script>

<style scoped>
.emoji {
  width: 24px;
  fill: currentColor;
  height: 24px;
  display: block;
  padding: 3px 0;
}

.emoji-container {
  border-radius: 3px;
  border: 1px solid #e6e6e6;
}

.item-name {
  font-weight: 700;
}

.emojis {
  position: relative;
  width: var(--emoji-container-width);
  margin: 10px 0 10px 10px;
  padding-top: 1px;
  padding-left: 1px;
  max-height: var(--emoji-container-height);
  background-color: #fff;
  overflow: auto;
}

.emojis::after {
  content: '';
  clear: left;
  display: block;
}

.emojis * {
  user-select: none;
}

.emojis li {
  cursor: pointer;
  float: left;
  height: 40px;
  width: 40px;
  overflow: hidden;
  margin: -1px 0 0 -1px;
  padding: 4px 2px;
  text-align: center;
  list-style: none;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 23px;
}

.emojis li:hover {
  position: relative;
  border: 1px solid #63a35c;
  z-index: 12;
}

.theme-dark .emoji-container {
  border-color: #2d2d2d;
}

.theme-dark .emojis {
  background-color: inherit;
}

.theme-dark .emojis li {
  border-color: #2d2d2d;
}

.theme-dark .emojis li:hover {
  border: 1px solid #497744;
}
</style>

<style>
.emojis::-webkit-scrollbar {
  display: none;
}
</style>