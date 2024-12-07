<template>
  <div id="archived">
    <div id="archived-info">
      <div class="archived-all">文章总览：{{ size }}</div>
      <div v-if="archived && archived.length > 0" v-for="(item, index) in archived" :key="index" class="archived-info">
        <!-- 年份 -->
        <div v-if="typeof item !== 'object'" class="archived-year">
          {{ item }}
        </div>
        <!-- 真实数据 -->
        <div v-else class="archived-real-data">
          <el-container>
            <el-aside width="125px">
              <div class="article-image">
                <el-image :src="item.image" fit="cover" loading="lazy" @load="load">
                  <template #placeholder>
                    <div class="image-slot">
                      <n-spin :size="18" stroke="#898989" :stroke-width="15" />
                    </div>
                  </template>
                  <template #error>
                    <div class="image-slot">
                      <el-icon class="error-img">
                        <icon-picture />
                      </el-icon>
                    </div>
                  </template>
                </el-image>
              </div>
            </el-aside>
            <el-main>
              <div class="article-status">
                <p class="title" @click="articleInfo(item.id)">{{ item.title }}</p>
                <p class="time">{{ shortTime(item['createTime'], '/', true) }}</p>
                <span class="index">{{ getCurrent(item) }}</span>
              </div>
            </el-main>
          </el-container>
        </div>
      </div>
      <div v-else class="archived-info">
        <el-empty />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useDate } from '@tanxiang/utils';

import { Picture as IconPicture } from '@element-plus/icons-vue';
import { useArticle } from '@tanxiang/apis';
import { useBaseStore } from '@/stores/base';

const router = useRouter();
const base = useBaseStore();
const article: any = useArticle();
const { year, shortTime } = useDate();

const size = ref<number>(0);
const archived = ref<any>({});
const oldData = ref<any[]>([]);

const updateDataLoading: any = inject('updateDataLoading');

onMounted(() => {
  article
    .archivedList()
    .then((success: any) => {
      size.value = success.data.length;
      oldData.value = success.data;
      archived.value = formatRecord(success.data);
    })
    .catch((error: any) => {
      console.error(error);
    })
    .finally(() => {
      nextTick(() => updateDataLoading());
    });
});

const formatRecord = (records: any) => {
  let newArray: any[] = [];
  let currentYear: number = 0;
  records.forEach((record: any) => {
    let yearItem = year(record.createTime);
    if (currentYear !== yearItem) newArray.push((currentYear = yearItem));
    newArray.push(record);
  });
  return newArray;
};

const getCurrent = (item: any) => {
  let count = 0;
  oldData.value.find((data: any, index: number) => {
    let flag = data === item;
    if (flag) count = index;
    return flag;
  });
  return count + 1;
};

const load = (e: Event) => {
  //@ts-ignore
  let tmp = e.target.parentNode.parentNode;
  tmp.style.border = '1px solid #efefef';
};

const articleInfo = (id: number) => {
  router.push('/articles/' + id);
};
</script>

<style scoped>
#archived {
  width: 100%;
  height: 100%;
  padding-top: 15px;
}

#archived #archived-info {
  background-color: white;
  width: 100%;
  height: 100%;
  border-radius: 10px;
  padding: 50px 35px 20px 35px;
  box-sizing: border-box;
  transition: padding 0.15s linear;
}

@media screen and (max-width: 500px) {
  #archived .archived-info {
    padding: 50px 15px 15px 15px;
  }

  #archived .article-status .title {
    font-weight: 500 !important;
  }
}

@media screen and (max-width: 700px) {
  #archived .article-status .index {
    display: none;
  }
}

#archived .archived-all {
  font-family: 'sakura', sans-serif;
  font-size: 1.6rem;
  padding-bottom: 5px;
}

.dark #archived .archived-year {
  color: #fff;
}

#archived .archived-year {
  font-size: 1.15rem;
  color: #000000;
  font-family: 'sakura', sans-serif;
}

#archived .archived-info {
  padding: 8px 0;
}

#archived .archived-info .archived-real-data {
  display: flex;
  align-items: center;
}

#archived .article-image {
  border-radius: 10px;
  box-sizing: border-box;
  width: 100%;
  overflow: hidden;
  height: 70px;
  cursor: pointer;
}

#archived .article-status {
  padding: 12px 0 0 15px;
  height: 70px;
  position: relative;
  box-sizing: border-box;
}

#archived .article-status .title {
  white-space: nowrap;
  overflow: hidden;
  font-size: 1rem;
  text-overflow: ellipsis;
  padding: 0 0 15px 0;
  font-family: 'sakura', sans-serif;
  font-weight: 700;
  cursor: pointer;
}

#archived .article-status .title:hover {
  color: #409eff;
}

#archived .article-status .time {
  font-family: 'sakura', sans-serif;
  color: #676767;
}

#archived .article-status .index {
  position: absolute;
  right: 0;
  top: 25%;
  color: #2f2f2f;
  font-size: 2rem;
  font-family: 'pacifico', sans-serif;
}

.image-slot {
  width: 100%;
  height: 100%;
  background-color: #f5f7fa;
  display: flex;
  align-items: center;
  justify-content: center;
}

.error-img {
  font-size: 20px;
  color: #898989;
}
</style>

<style>
#archived .article-image .el-image {
  overflow: initial;
  width: 100%;
  height: 100%;
}

#archived .article-image:hover .el-image {
  transform: scale(1.2);
  transition: transform 0.25s linear;
}
</style>