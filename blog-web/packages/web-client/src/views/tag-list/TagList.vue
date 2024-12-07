<template>
  <div id="tag">
    <div class="tag-count-all">{{ `标签总览：${tagList.length}` }}</div>
    <div class="tag-list-data">
      <div
        v-if="tagList && tagList.length > 0"
        v-for="tag in tagList"
        :key="tag.id"
        class="tag-info"
        @click="jump(tag.id)"
      >
        <n-icon :size="15">
          <TagSharp />
        </n-icon>
        <span class="tag-name">{{ tag.name }}</span>
        <span class="tag-count">{{ tag['articleCount'] }}</span>
      </div>
      <div v-else>
        <el-empty></el-empty>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useTag } from '@tanxiang/apis';

import { useGlobalStore } from '@/stores/global';
import { messageInfo, TagSharp } from '@tanxiang/common';

const router = useRouter();

const { tags } = useTag();
const { messageError } = messageInfo();
const global = useGlobalStore();
const updateDataLoading: any = inject('updateDataLoading');

const tagList = ref<any[]>([]);

onMounted(() => {
  if (global.getWebSite.tags) {
    tagList.value = global.getWebSite.tags;
    nextTick(() => updateDataLoading());
  } else {
    tags()
      .then((success: any) => global.setWebSiteTags(success.data))
      .catch((error: any) => messageError(error ? error.message : '获取标签失败'))
      .finally(() => {
        nextTick(() => updateDataLoading());
      });
  }
});

const jump = (tagId: string | number) => router.push(`/list/tag/${tagId}`);

watch(
  () => global.getWebSite.tags,
  (newTags: any) => (tagList.value = newTags),
  { deep: true }
);
</script>

<style scoped>
#tag {
  background-color: white;
  border-radius: 10px;
  width: 100%;
  min-height: 200px;
  font-family: 'sakura', sans-serif;
  box-sizing: border-box;
  padding: 30px 30px 20px 30px;
  max-width: 900px;
  margin: 0 auto;
  border: 1px solid #ececec;
}

.tag-count-all {
  font-size: 1.25rem;
}

.tag-list-data {
  margin-top: 30px;
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
}

.tag-info {
  cursor: pointer;
  border: 1px solid #e5e5e5;
  margin: 0 6px 15px 5px;
  padding: 10px 10px;
  border-radius: 7px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  transition: transform 0.25s ease;
}

.tag-info .tag-count {
  background-color: #e9e9e9;
  border-radius: 3px;
  padding: 1px 3px;
}

.tag-info .tag-name {
  font-size: 0.85rem;
  font-family: 'sakura', sans-serif;
  padding: 0 5px;
}

.tag-info:hover {
  color: white;
  transform: scale(1.1);
  background-color: #409eff;
}

.tag-info:hover .tag-count {
  background-color: white;
  color: #409eff;
}
</style>