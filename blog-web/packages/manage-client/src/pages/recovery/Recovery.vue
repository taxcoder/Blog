<template>
  <div id="recovery">
    <table-search
      :formState="articleFormState"
      name="recovery/article"
      @dataSource="dataSource"
      @current="current"
      v-if="show"
    >
      <template #other>
        <el-button :type="recovery.type" :icon="recovery.icon" @click="change">{{ recovery.text }}</el-button>
      </template>
    </table-search>
    <table-search :formState="essayFormState" name="recovery/essay" @dataSource="dataSource" @current="current" v-else>
      <template #other>
        <el-button :type="recovery.type" :icon="recovery.icon" @click="change">{{ recovery.text }}</el-button>
      </template>
    </table-search>
    <router-view v-slot="{ Component, route }">
      <keep-alive>
        <component :is="Component" :key="route.path" @dataSource="dataSource" @current="current" />
      </keep-alive>
    </router-view>
  </div>
</template>

<script setup lang="ts">
import TableSearch from '@/components/table-search/TableSearch.vue';
import { useRoute, useRouter } from 'vue-router';
import { computed, h, provide, reactive, ref, watch } from 'vue';
import { BulbOutlined, CoffeeOutlined } from '@ant-design/icons-vue';
//@ts-ignore
import { useRecoveryStore } from '@/stores/recovery';

const route = useRoute();
const router = useRouter();
const recoveryStore = useRecoveryStore();

const flag = ref<boolean>(true);
const tableEssaySource = ref<any>({});
const tableArticleSource = ref<any>({});
const tabEssayCurrent = ref<number>(-1);
const tabArticleCurrent = ref<number>(-1);

provide('tabArticleCurrent', tabArticleCurrent);
provide('tabEssayCurrent', tabEssayCurrent);
provide('tableArticleSource', tableArticleSource);
provide('tableEssaySource', tableEssaySource);

const recovery = reactive<any>({
  type: '',
  icon: null,
  text: '',
});

const articleFormState = reactive<any>({
  key: [
    { value: 'id', label: 'ID', placeholder: '请输入一个正整数，不能为0' },
    { value: 'title', label: '标题', placeholder: '根据标题模糊查询' },
    { value: 'classification', label: '分类', placeholder: '根据分类ID和分类名查询' },
    { value: 'position', label: '位置', placeholder: '根据上传地查询' },
    { value: 'like/gte', label: '点赞数(大于等于)', placeholder: '请输入一个非负整数' },
    { value: 'like/lte', label: '点赞数(小于等于)', placeholder: '请输入一个非负整数' },
    { value: 'like/equ', label: '点赞数(等于)', placeholder: '请输入一个非负整数' },
  ],
  select: 'id',
  value: '',
});

const essayFormState = reactive<any>({
  key: [
    { value: 'id', label: 'ID', placeholder: '请输入一个正整数，不能为0' },
    { value: 'content', label: '内容', placeholder: '根据内容模糊查询' },
    { value: 'like/gte', label: '点赞数(大于等于)', placeholder: '请输入一个非负整数' },
    { value: 'like/lte', label: '点赞数(小于等于)', placeholder: '请输入一个非负整数' },
    { value: 'like/equ', label: '点赞数(等于)', placeholder: '请输入一个非负整数' },
  ],
  select: 'id',
  value: '',
});

const change = () => router.push(`/base/recovery/${show.value ? 'essay' : 'article'}`);

const dataSource = (source: any) => {
  if (show.value) {
    tableArticleSource.value = source;
  } else {
    tableEssaySource.value = source;
  }
};

const current = (c: number) => {
  if (show.value) {
    tabArticleCurrent.value = c;
  } else {
    tabEssayCurrent.value = c;
  }
};

const show = computed(() => {
  if (route.path.indexOf('/base/recovery/article') !== -1) {
    flag.value = true;
  } else if (route.path.indexOf('/base/recovery/essay') !== -1) {
    flag.value = false;
  }
  return flag.value;
});

watch(
  () => articleFormState,
  () =>
    recoveryStore.setArticleSearch({
      name: 'recovery/article',
      value: articleFormState.value,
      select: articleFormState.select,
    }),
  { deep: true }
);

watch(
  () => essayFormState,
  () =>
    recoveryStore.setEssaySearch({
      name: 'recovery/essay',
      value: essayFormState.value,
      select: essayFormState.select,
    }),
  { deep: true }
);

watch(
  () => show.value,
  (newValue: string) => {
    recovery.type = newValue ? 'success' : 'danger';
    recovery.icon = newValue
      ? () => h(BulbOutlined, { style: { fontSize: '16px' } })
      : () => h(CoffeeOutlined, { style: { fontSize: '16px' } });
    recovery.text = newValue ? '切换随笔' : '切换文章';
  },
  { immediate: true }
);
</script>

<style scoped>
#recovery {
  height: 100%;
  overflow: auto;
  /* /兼容火狐/ */
  /* / 兼容IE10+ */
  scrollbar-width: none;
  -ms-overflow-style: none;
}

.slide-fade-enter-active {
  transition: all 0.5s ease-in-out;
}

.slide-fade-enter-from,
.slide-fade-leave-to {
  transform: translateX(-25px);
  opacity: 0;
}

/* 谷歌，元素隐藏必须设置宽度  不然无效 */
#recovery::-webkit-scrollbar {
  width: 0;
  height: 30px;
}

/* 使用伪类选择器 ::-webkit-scrollbar ,兼容chrome和safari浏览器 */
/* display: none; 只能直接作用于body html，其他元素设置宽度的方式 */
.manage-layout-content::-webkit-scrollbar {
  display: none;
}
</style>
