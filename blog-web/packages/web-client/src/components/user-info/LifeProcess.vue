<template>
  <user-container title="人生进度条" iconClass="icon-countDown" :size="22">
    <template v-slot:content>
      <div v-for="(item, index) in process" :key="index" class="last-of-type:pb-[8px]">
        <el-text
          class="box-border important-pt-[12px] important-pb-[3px] px-0"
          size="small"
          tag="p"
          type="info"
          v-html="getCurrentDate(index)"
        />
        <el-progress
          :color="item['color']"
          :duration="3"
          :indeterminate="true"
          :percentage="getCurrentProcess(index)"
          :stroke-width="13"
          striped
          striped-flow
        />
      </div>
    </template>
  </user-container>
</template>

<script lang="ts" setup>
import moment from 'moment';

const time = ref<number>(1000 * 60 * 60);
const hour = ref<string>(moment().format('H'));
const week = ref<string>(moment().format('d'));
const day = ref<string>(moment().format('D'));
const month = ref<string>(moment().format('M'));
// 进度条样式
const process = reactive([{ color: '' }, { color: '#e6a23c' }, { color: '#f56c6c' }, { color: '#13ce66' }]);

onMounted(() => setNewDate());
// 设置新日期
const setNewDate = () => {
  setInterval(() => {
    hour.value = moment().format('H');
    week.value = moment().format('d');
    day.value = moment().format('D');
    month.value = moment().format('M');
  }, time.value);
};
// 获取当前日期
const getCurrentDate = computed(
  () => (index: number) =>
    ({
      0: () => `今日已经过去<span style="color: #409eff;">${hour.value}</span>小时`,
      1: () => `这周已经过去<span style="color: #409eff;">${week.value === '0' ? 7 : week.value}</span>天`,
      2: () => `本月已经过去<span style="color: #409eff;">${day.value}</span>天`,
      3: () => `今年已经过去<span style="color: #409eff;">${month.value}</span>个月`,
    }[index]())
);
// 获取当前进度
const getCurrentProcess = computed(
  () => (index: number) =>
    ({
      0: Math.ceil((100 / 24) * parseInt(hour.value)),
      1: week.value === '0' ? 100 : Math.ceil((100 / 7) * parseInt(week.value)),
      2: Math.ceil((100 / moment().daysInMonth()) * parseInt(day.value)),
      3: Math.ceil((100 / 12) * parseInt(month.value)),
    }[index])
);
</script>

<style scoped></style>
