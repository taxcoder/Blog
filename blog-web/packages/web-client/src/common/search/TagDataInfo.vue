<template>
  <van-pull-refresh v-model="refreshLoading" @refresh="onRefresh">
    <n-virtual-list
      v-if="props.searchData.length > 0"
      :class="[searchHeight]"
      :item-size="itemSize"
      :items="props.searchData"
      @scroll="scrollbars"
      :item-resizable="true"
    >
      <template #default="{ item, index }">
        <div
          :key="item.key"
          class="item important-mb-0 box-border py-[15px] px-[10px] cursor-pointer flex items-center flex-row border-b-1 border-solid border-slate-100 dark:border-zinc-800"
          :style="{ height: itemSize + 'px' }"
          @click="jump(item.id)"
        >
          <n-avatar :size="65" class="dark:bg-[#424242] flex-none">
            <van-icon name="points" size="2rem" />
          </n-avatar>
          <div class="flex flex-col w-full h-full flex-auto pl-[10px]">
            <div class="text-sm text-gray-600 dark:text-stone-400 flex-1 flex items-center" v-html="item.name"></div>
            <div
              class="text-xs text-gray-400 dark:text-stone-500 flex-1 flex items-center"
              v-html="`${item.articleCount}篇内容`"
            ></div>
          </div>
        </div>
      </template>
    </n-virtual-list>
    <n-spin v-else-if="loading" size="large" stroke="#409eff">
      <span class="h-[100px] flex flex-col-reverse">数据加载中</span>
    </n-spin>
    <n-empty v-else description="你什么也找不到"></n-empty>
  </van-pull-refresh>
</template>

<script setup lang="ts">
import { useGlobalStore } from '@/stores/global';
import { messageInfo } from '@tanxiang/common';

const { messageSuccess } = messageInfo();

const pageCount = ref<any>(5);
const scrollTop = ref<number>(10);
const timer = ref<any>();
const current = ref<number>(1);
const itemSize = ref<number>(85);
const global = useGlobalStore();
const listLoading = ref<boolean>(false);
const refreshLoading = ref<boolean>(false);
const loadTimer = ref<any>(null);

const props = defineProps({
  searchData: {
    type: Array,
    required: true,
  },
  searchHeight: {
    type: String,
    required: true,
  },
  current: {
    type: Number,
    required: true,
  },
  loading: {
    type: Boolean,
    default: false,
  },
});

const emit = defineEmits(['initData', 'clearSearchData']);

onMounted(() => nextTick(() => updatePaddingTop()));

const updatePaddingTop = () => {
  let searchHeightAll: any = document.querySelector('.search-height-all');
  if (searchHeightAll) {
    searchHeightAll.style.paddingTop = `${scrollTop.value}px`;
  } else {
    let searchHeight: any = document.querySelector('.search-height');
    searchHeight.style.paddingTop = `${scrollTop.value}px`;
  }
};

const onRefresh = () => {
  emit('initData', {
    page: 1,
    fn: () => {
      messageSuccess('刷新成功');
      refreshLoading.value = false;
      current.value = 1;
    },
  });
};

const jump = (id: number) => {
  document.location.href = location.origin + `/list/tag/${id}`;
};

const scrollbars = (event: any) => {
  let ret = 10 - event.target.scrollTop;
  scrollTop.value = ret < 0 ? 0 : ret;
  updatePaddingTop();
  // 总高度
  let allHeight = event.target.scrollHeight;
  // 距离底部的高度
  let bottom = allHeight - Math.ceil(event.target.scrollTop + event.target.clientHeight);
  // 请求新的数据
  if (bottom < itemSize.value * 5 && !listLoading.value) onLoad();
};

const onLoad = () => {
  listLoading.value = true;
  if (loadTimer.value) clearTimeout(loadTimer.value);
  loadTimer.value = setTimeout(() => {
    current.value = current.value + 1;
    emit('initData', {
      page: current.value,
      fn: (data: any) => {
        if (Math.ceil(data.total / data.size) >= current.value) listLoading.value = false;
      },
    });
  }, 300);
};

watch(
  () => global.getSearchInput,
  (newValue: string) => {
    if (props.searchData.length > 0) emit('clearSearchData');
    current.value = 1;
    if (newValue.length !== 0) emit('initData', { page: 1 });
  },
  { immediate: true, deep: true }
);
</script>

<style scoped></style>

<style>
.tag-search .el-tabs__header {
  margin-bottom: 5px !important;
}
</style>
