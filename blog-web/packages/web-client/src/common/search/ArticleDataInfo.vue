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
          class="item bg-white dark:bg-transparent important-mb-[10px] box-border py-[5px] cursor-pointer"
          :style="{ height: itemSize + 'px' }"
          @click="jump(item.id)"
        >
          <div
            class="font-bold text-[1.025rem] pb-[10px] overflow-hidden text-ellipsis whitespace-nowrap"
            v-html="highlight(item.title)"
          ></div>
          <div class="flex flex-row h-[78px]">
            <div class="flex-[3_3_0%]">
              <div class="flex items-center">
                <n-avatar round size="small" :src="item.headIcon" />
                <span class="text-[0.95rem] ml-[5px]">
                  {{ item.author }} | {{ dateUtil.shortTime(item.createTime, '-', true) }}
                </span>
              </div>
              <div class="pt-[7px] box-border max-h-[50px] search-prefix-content" :data-index="index">
                <span class="content-word" v-html="highlight(item.prefixContent)"></span>
              </div>
            </div>
            <div class="flex-1">
              <img :src="item.image" alt="" class="w-full h-[78px] object-cover" />
            </div>
          </div>
          <div class="flex justify-between pt-[10px]">
            <div>
              <van-icon v-if="true" name="good-job-o" />
              <span>1</span>
            </div>
            <div>
              <span
                class="p-[5px] text-[0.65rem] text-[#474747] bg-[#e9e9e9] rounded-[3px]"
                @click.stop="listArticleClassification(item.classification.id)"
              >
                {{ item.classification.name }}
              </span>
            </div>
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
import { useDate } from '@tanxiang/utils';
const dateUtil = useDate();

const global = useGlobalStore();

const pageCount = ref<any>(5);
const current = ref<number>(1);
const size = ref<number>(20);
const itemSize = ref<number>(140);
const loadTimer = ref<any>(null);
const listLoading = ref<boolean>(false);
const refreshLoading = ref<boolean>(false);
const scrollTop = ref<number>(10);

const { messageSuccess } = messageInfo();

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

const jump = (id: number) => {
  document.location.href = location.origin + `/articles/${id}`;
};

const listArticleClassification = (id: number) => {
  document.location.href = location.origin + `/list/classification/${id}`;
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

const highlight = (value: string) => {
  let temp = value;
  let keyword = new Set();
  let index = temp.toLowerCase().indexOf(global.getSearchInput.toLowerCase());
  let sub = 0;
  let i = 0;
  while (index !== -1) {
    if (i > 10) break;
    keyword.add(value.substring(index + sub, index + sub + global.getSearchInput.length));
    sub = index + 1;
    temp = temp.substring(index + 1, temp.length);
    index = temp.toLowerCase().indexOf(global.getSearchInput.toLowerCase());
    i++;
  }

  keyword.forEach(
    (data: string) => (value = value.replace(new RegExp(data, 'g'), `<span style='color: red'>${data}</span>`))
  );
  return value;
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

<style scoped>
.search-prefix-content {
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 3;
}
</style>
