<template>
  <div id="essay">
    <div class="essay-list">
      <div
        v-if="essayList.records && essayList.records.length > 0"
        v-for="item in essayList.records"
        class="essay-card"
        :key="item.id"
      >
        <i class="weather" :class="`qi-${item['weather']}`" />
        <el-card>
          <div class="essay-info_top">
            <img class="relative top-[-1px] w-[20px]" alt="" :src="item['mood']" />
            <span class="create-time h-[20px] lh-[20px]">{{ day.chineseTime(item['createTime']) }}</span>
          </div>
          <p class="content-word" v-html="item.content" />
          <div class="essay-info_bottom">
            <div class="flex flex-row">
              <span class="align-baseline relative top-0 text-[16px]">🌷</span>
              <span class="temperature">
                <el-tooltip effect="dark" content="气温" placement="right">
                  {{ item['temperature'] }}
                </el-tooltip>
              </span>
              <span class="relative top-0 ml-[1px] text-[0.835rem] text-[#db7e8c] flex items-center">℃</span>
            </div>

            <div class="ml-[7px] w-full flex flex-row items-center text-[#db7e8c]">
              <city-address class="relative text-[18px] w-[19px]" />
              <span class="ml-[2px]">{{ item['province'] }}</span>
            </div>

            <div class="heart">
              <i v-if="isLike(item.id)" class="iconfont icon-heart-3-line text-[17px]" @click="pushLike(item.id)" />
              <n-spin v-else-if="loading.indexOf(item.id) !== -1" :size="13" class="like-spin" />
              <i v-else class="iconfont icon-heart-3-fill text-[17px]" @click="openWarning" />
              <span class="like-temperature" v-show="item['likeCount'] !== 0">{{ item['likeCount'] }}</span>
            </div>
          </div>
        </el-card>
      </div>
      <div v-else class="essay-card">
        <el-card>
          <el-empty :image-size="300" />
        </el-card>
      </div>
    </div>
    <div class="format-page">
      <format-paging
        :pager-count="5"
        layout="prev, pager, next"
        :total="total"
        :current="current"
        :page-size="20"
        @currentChange="currentChange"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { useBaseStore } from '@/stores/base';
import { useEssayStore } from '@/stores/essay';
import { useEssay } from '@tanxiang/apis';
import { useDate, usePage } from '@tanxiang/utils';
import { ElNotification, ElMessage as message } from 'element-plus';
import { CityAddress } from '@tanxiang/common';

let day = useDate();
let page = usePage();
let essay = useEssay();

let base = useBaseStore();
let essayStore = useEssayStore();

const total = ref<number>(0);
const current = ref<number>(1);
const loading = ref<any[]>([]);
const essayList = ref<any>({});
const updateDataLoading: any = inject('updateDataLoading');

onMounted(() => {
  getEssayData(1, 20);
});

const pushLike = (id: number) => {
  loading.value.push(id);
  essay
    .essayAddLike(id)
    .then((success: any) => {
      essayStore.setLikes(id);
      essayList.value.records.map((list: any) => {
        if (list.id !== id) return list;
        list.likeCount = success.data;
        return list;
      });
      ElNotification({
        message: '点赞成功！',
        type: 'success',
        duration: 2000,
        position: 'bottom-left',
      });
      nextTick(updateDataLoading());
    })
    .catch((error: any) => {
      ElNotification({
        message: !error || error.name ? '点赞失败！' : error,
        type: 'error',
        duration: 2000,
        position: 'bottom-left',
      });
    })
    .finally(() => {
      loading.value.splice(loading.value.indexOf(id), 1);
      nextTick(() => updateDataLoading());
    });
};

const openWarning = () => {
  ElNotification({
    message: '已经点赞过了！',
    type: 'warning',
    duration: 2000,
    position: 'bottom-left',
  });
};

const getEssayData = (cur: number, size: number) => {
  essay
    .essayList(cur, size)
    .then((success: any) => {
      essayList.value = success.data;
      total.value = success.data.total;
      current.value = success.data.current + 1;
      nextTick(updateDataLoading());
    })
    .catch((error: any) => message.error(!error || error.name ? '随笔获取失败！' : error));
};

const currentChange = ({ current, size }: { current: number; size: number }) => {
  getEssayData(current, size);
};

const isLike = computed(() => (id: any) => {
  // @ts-ignore
  return !essayStore.getLikes.includes(id);
});
</script>

<style scoped>
#essay {
  margin: 15px auto 15px auto;
  max-width: 1000px;
  width: 100%;
  height: 100%;
  min-height: 500px;
  border-radius: 10px;
  background-color: white;
  padding: 50px 35px 20px 35px;
  box-sizing: border-box;
  box-shadow: 0 0 3px rgba(0, 0, 0, 0.12);
}

.essay-card {
  border-radius: 8px;
  margin-bottom: 30px;
  position: relative;
  min-width: 200px;
}

.essay-info_top {
  padding: 0 0 25px 0;
  box-sizing: border-box;
  display: flex;
  position: relative;
  align-items: center;
}

.essay-card .weather {
  position: absolute;
  right: 3px;
  font-size: 3rem;
  color: #ff6a6a99;
  top: -14px;
}

.essay-info_bottom {
  padding: 18px 0 0 0;
  box-sizing: border-box;
  display: flex;
  align-items: center;
}

.essay-info_bottom .heart {
  display: flex;
  justify-content: flex-end;
  align-items: center;
}

.content-word {
  color: #0c1932;
  font-size: 0.925rem;
  word-break: break-all;
  letter-spacing: 1px;
  background-image: url('@/assets/images/wordline.webp');
  background-size: auto 2.5rem !important;
  line-height: 2.5rem;
  min-height: 80px;
}

#essay .create-time {
  margin: 0 5px;
  color: #db7e8c;
  font-size: 0.85rem;
}

#essay .temperature {
  margin-left: 2px;
  color: #db7e8c;
  font-size: 0.875rem;
  display: flex;
  align-items: center;
}

#essay .heart .like-temperature {
  font-size: 0.8rem;
  margin-left: 4px;
  color: #626262;
}

#essay .heart .like-spin {
  margin-right: 2px;
  margin-top: 2px;
}

.icon-heart-3-fill {
  color: red;
}

@media screen and (max-width: 500px) {
  #essay {
    padding: 0;
    background: none;
    box-shadow: none;
  }

  .essay-card {
    background-color: white;
    margin-bottom: 15px;
  }
}
</style>

<style>
.el-notification {
  width: auto;
  padding: 8px 26px 8px 13px;
}

.el-notification .el-notification__closeBtn {
  top: 12px;
}

.content-word img {
  max-width: 570px;
  min-width: 150px;
  width: 100%;
  display: block;
  margin: 0 auto;
}

.essay-card .el-card {
  background: none;
  border-radius: 8px;
  box-shadow: 0 0 5px rgba(0, 0, 0, 0.12);
}

.essay-card .el-card__body {
  padding: 20px 20px 10px 20px;
}

@media screen and (max-width: 500px) {
  .el-timeline-item__tail,
  .el-timeline-item__node {
    display: none;
  }

  .el-timeline-item__wrapper {
    padding-left: 0;
  }
}
</style>