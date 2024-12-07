<template>
  <div id="analysis">
    <div class="info-card">
      <a-card class="info-card__item" bordered v-for="item in card" :key="item.id">
        <a-skeleton active :paragraph="{ rows: 5, width: '100%' }" :title="false" :loading="loading">
          <div class="analysis-info">
            <div class="all-visit" v-text="item.name" />
            <a-divider />
            <div class="analysis-info-container">
              <div class="data-statistic">
                <a-statistic :value="item.num">
                  <template #formatter="{ value }">{{ total(value) }}</template>
                </a-statistic>
              </div>
              <div class="data-icon">
                <svg class="icon" aria-hidden="true">
                  <use :xlink:href="`#${item.icon}`"></use>
                </svg>
              </div>
            </div>
          </div>
        </a-skeleton>
      </a-card>
    </div>
    <div class="info-submit">
      <future-weather :day="day" :max-temperature="max" :min-temperature="min" />
    </div>
    <div class="other">
      <RoseChart :data="classification" name="分类" @change="change" @restore="restore" />
      <RoseChart :data="essayProvince" name="随笔" @change="change" @restore="restore" />
      <RoseChart :data="tag" name="标签" @change="change" @restore="restore" />
    </div>
  </div>
</template>

<script setup lang="ts">
import FutureWeather from '@/common/future-weather/FutureWeather.vue';
import RoseChart from '@/common/rose-chart/RoseChart.vue';
import { messageInfo } from '@tanxiang/common';
import { useBaseStore } from '@/stores/base';
import { useAuthWebStation } from '@tanxiang/apis';
import { computed, onMounted, reactive, ref } from 'vue';
//@ts-ignore
import { useRouter } from 'vue-router';
import { classificationAll, essayAddress, tagAll, webInfo } from './info';

const { messageError } = messageInfo();

const prevWeather = ref<number>(-1);
const loading = ref<boolean>(false);
const tagSize = ref<number>(5);
const essaySize = ref<number>(5);
const classificationSize = ref<number>(5);

const router = useRouter();
const base = useBaseStore();
const web = useAuthWebStation();

const card = reactive<any[4]>([
  {
    id: 1,
    name: '总访问数',
    num: base.getWebStation.totalVisits,
    icon: 'icon-fangwenkongzhi',
  },
  {
    id: 2,
    name: '总字数',
    num: base.getWebStation.totalTextQuantity,
    icon: 'icon-zishu',
  },
  {
    id: 3,
    name: '总文章数',
    num: 0,
    icon: 'icon-wenzhang',
  },
  {
    id: 4,
    name: '总日记数',
    num: 0,
    icon: 'icon-yunyingriji',
  },
]);
const day = reactive<any[30]>([]);
const min = reactive<any[30]>([]);
const max = reactive<any[30]>([]);

const classifications = reactive<any[15]>([]);
const tags = reactive<any[15]>([]);
const essay = reactive<any[15]>([]);

onMounted(() => {
  changeWeather();
  classificationAll(classifications);
  essayAddress(essay);
  tagAll(tags);
  webInfo(card);
});

const pushDay = (key: any, newArray: number[]) => {
  key.splice(0, newArray.length);
  key.push(...newArray);
};

const changeWeather = () => {
  // TODO: 实现请求后端接口实现获取天气信息
  let weather: any = base.getWeather;
  /**
   * 1. 如果添加时间不存在，表示还没有存储任何的数据
   * 2. 如果存储的时间和当前获取的时间进行比较大于1个小时，则让其重新进行请求，更新数据
   */
  if (weather.pushTime && new Date().getTime() - weather.pushTime > 1000 * 60 * 60) base.initWeather();
  /**
   * 1. 如果没有存储，则发出请求，获取后进行存储
   * 2. 如果已经存储直接从pinia取出即可，减少请求的次数
   * 3. key是读取的次数，比如key=3，从30天的数组内去取出前3条数据，表示前3天
   */
  if (!weather['future'] || weather['future'].length === 0) {
    web
      .weather()
      .then((success: any) => {
        base.setWeather('pushTime', new Date(success.data.updateTime).getTime());
        base.setWeather('future', success.data.daily);
        weatherInfo(weather);
      })
      .catch((error: any) => messageError(!error || error.name ? '网络错误！' : error));
  } else {
    weatherInfo(weather);
  }
};

const weatherInfo = (weather: any) => {
  let future = weather['future'].slice(0, 30);
  pushDay(
    day,
    future.map((f: any) => parseInt(f.fxDate.split('-')[2]))
  );
  pushDay(
    min,
    future.map((f: any) => parseInt(f.tempMin))
  );
  pushDay(
    max,
    future.map((f: any) => parseInt(f.tempMax))
  );
};

const classification = computed(() => {
  classifications.sort((a: any, b: any) => b.count - a.count);
  if (classifications.length <= classificationSize.value || classifications.length === classificationSize.value + 1) {
    return classifications.map((f: any) => ({ value: f.count, name: f.name }));
  }
  let temp = classifications.slice(0, classificationSize.value).map((f: any) => ({ value: f.count, name: f.name }));
  let count = 0;
  classifications.slice(classificationSize.value, classifications.length).forEach((c: any) => (count += c.count));
  temp.push({ value: count, name: '其他' });
  return temp;
});

const tag = computed(() => {
  tags.sort((a: any, b: any) => b.articleCount - a.articleCount);
  if (tags.length <= tagSize.value || tags.length === tagSize.value + 1) {
    return tags.map((f: any) => ({ value: f.articleCount, name: f.name }));
  }
  let temp = tags.slice(0, tagSize.value).map((f: any) => ({ value: f.articleCount, name: f.name }));
  let count = 0;
  tags.slice(tagSize.value + 1, tags.length).forEach((c: any) => (count += c.articleCount));
  temp.push({ value: count, name: '其他' });
  return temp;
});

const essayProvince = computed(() => {
  essay.sort((a: any, b: any) => b.count - a.count);
  if (essay.length <= essaySize.value || essay.length === essaySize.value + 1) {
    return essay.map((f: any) => ({ value: f.count, name: f.province }));
  }
  let temp = essay.slice(0, essaySize.value).map((f: any) => ({ value: f.count, name: f.province }));
  let count = 0;
  essay.slice(essaySize.value + 1, essay.length).forEach((c: any) => (count += c.count));
  temp.push({ value: count, name: '其他' });
  return temp;
});

const restore = (name: string) => {
  switch (name) {
    case 'tags':
      pushDay(tags, base.getRoses.tag);
      break;
    case 'classifications':
      pushDay(classifications, base.getRoses.classification);
      break;
    default:
      pushDay(essay, base.getRoses.essay);
      break;
  }
};

const total = computed(() => (value: number) => {
  if (value < 10000) {
    return value.toLocaleString();
  } else if (value >= 100000000) {
    return parseFloat((value / 100000000).toFixed(2)).toLocaleString() + '亿';
  }
  return parseFloat((value / 10000).toFixed(2)).toLocaleString() + '万';
});

const change = ({ name, value, operation }: { name: string; value: string; operation: boolean }): void => {
  if (operation) {
    router.push(`list/${name}?key=${name === 'essays' ? 'province' : 'name'}&value=${value}`);
  } else {
    if (name === 'tags' && tagSize.value < tags.length && tags[0].articleCount > 0) {
      let tagTemp = JSON.parse(JSON.stringify(tags));
      pushDay(tags, tagTemp.splice(tagSize.value, tagTemp.length));
    } else if (
      name === 'classifications' &&
      classificationSize.value < classifications.length &&
      classifications[0].count > 0
    ) {
      let classificationTemp = JSON.parse(JSON.stringify(classifications));
      pushDay(classifications, classificationTemp.splice(classificationSize.value, classificationTemp.length));
    }
  }
};
</script>

<style scoped>
#analysis {
  --pic-height: 325px;
  --card-height: 132px;
}

#analysis::-webkit-scrollbar {
  display: none;
}

@media all and (max-width: 1000px) {
  #analysis {
    grid-template-rows:
      calc(calc(calc(var(--card-height) + var(--header-tab-bar-padding-left)) * 4) - var(--header-tab-bar-padding-left))
      auto calc(
        calc(calc(var(--pic-height) + var(--header-tab-bar-padding-left)) * 3) - var(--header-tab-bar-padding-left)
      ) !important;
  }

  #analysis .info-card {
    flex-direction: column !important;
  }

  #analysis .info-card .info-card__item {
    margin: 0 !important;
    margin-bottom: 7px !important;
  }
  #analysis .other {
    flex-direction: column;
  }

  #analysis .other .rose:not(:last-child) {
    margin-bottom: var(--header-tab-bar-padding-left);
    margin-right: 0 !important;
  }
}

#analysis {
  height: 100%;
  width: 100%;
  display: inline-grid;
  box-sizing: border-box;
  grid-template-rows: var(--card-height) auto var(--pic-height);
  grid-row-gap: var(--header-tab-bar-padding-left);
  grid-template-columns: 100%;
  overflow: auto;
  scrollbar-width: none;
  -ms-overflow-style: none;
}

#analysis .info-card {
  display: flex;
  flex-direction: row;
  width: 100%;
  user-select: none;
}

#analysis .info-card .info-card__item {
  flex: 1;
}

#analysis .info-card .info-card__item {
  margin-left: 8px;
}

#analysis .info-card .info-card__item:first-child {
  margin: 0;
}

.analysis-info {
  min-width: 150px;
}

.analysis-info .all-visit {
  margin-bottom: 5px;
  font-size: 13px;
  font-weight: bold;
}

.analysis-info .analysis-info-container {
  margin-top: 5px;
  display: flex;
  flex-direction: row;
  padding: 10px 0 0 0;
}

.analysis-info .analysis-info-container .data-statistic {
  flex: 2;
  display: flex;
  align-items: center;
}

.analysis-info .analysis-info-container .data-icon {
  flex: 0.25;
  font-size: 40px;
}

.info-submit {
  background-color: white;
  border-radius: 10px;
  padding: 25px 0 0;
}

#analysis .other {
  display: flex;
  width: 100%;
}

#analysis .other .rose {
  border-radius: 10px;
  overflow: hidden;
}

#analysis .other .rose:not(:last-child) {
  margin-right: var(--header-tab-bar-padding-left);
}

::v-deep(.analysis-info .all-visit + .ant-divider) {
  margin: 0 !important;
}
</style>