<template>
  <div>
    <el-dialog v-model="dialogFormVisible" title="添加目标" width="500" align-center>
      <a-select ref="select" v-model:value="addYear" class="w-full">
        <a-select-option :value="selectYear">{{ selectYear }}</a-select-option>
        <a-select-option :value="selectYear + 1">{{ selectYear + 1 }}</a-select-option>
      </a-select>
      <a-textarea class="mt-[10px]" v-model:value="content" showCount :maxlength="30"></a-textarea>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="close">取消</el-button>
          <el-button type="primary" @click="submitTarget">提交</el-button>
        </div>
      </template>
    </el-dialog>

    <div class="flex justify-between">
      <a-date-picker
        v-model:value="year"
        picker="year"
        class="mb-[10px] rounded-[5px]"
        :allowClear="false"
        @change="dateChange"
      />
      <el-button type="primary" class="button-table" @click="addTarget">
        <el-icon size="14" class="mr-[2px]"><CirclePlusFilled /></el-icon>
        添加
      </el-button>
    </div>
    <a-table
      :columns="columns"
      :data-source="targets"
      size="middle"
      :pagination="{
        hideOnSinglePage: true,
      }"
      :row-key="(record: any) => record.id"
      @expand="expand"
      :expandedRowKeys="expandedRowKeys"
    >
      <template #bodyCell="{ column, text, record }">
        <!-- content格式自定义 -->
        <template v-if="column.dataIndex === 'content'">
          <input-update
            :is-success="record.isSuccess"
            :datas="list"
            :record="setRecord(record, 'content')"
            condition="content"
            :fn="auth.updateTargetContent"
          ></input-update>
        </template>
        <!-- isSuccess格式自定义 -->
        <template v-if="column.dataIndex === 'isSuccess'">
          <div>{{ text ? '已完成' : '未完成' }}</div>
        </template>
        <!-- operation格式自定义 -->
        <template v-if="column.dataIndex === 'operation'">
          <a-button type="primary" danger size="small" class="button-table" @click="onDelete(record)">干掉它</a-button>
          <a-button
            v-if="!record.isSuccess"
            type="primary"
            size="small"
            class="button-table"
            @click="onModify(record, true)"
          >
            已完成
          </a-button>
          <a-button v-else type="primary" size="small" class="button-table modify" @click="onModify(record, false)">
            未完成
          </a-button>
        </template>
      </template>
      <template v-if="!isHidden" #expandedRowRender="{ record, index }">
        <a-table :columns="innerColumns" :data-source="[record]" :pagination="false">
          <template #bodyCell="{ column, index, text, record }">
            <!-- isSuccess格式自定义 -->
            <template v-if="column.dataIndex === 'isSuccess'">
              <div>{{ text ? '已完成' : '未完成' }}</div>
            </template>
            <!-- operation格式自定义 -->
            <template v-if="column.dataIndex === 'operation'">
              <a-button type="primary" danger size="small" class="button-table" @click="onDelete(record)">
                干掉它
              </a-button>
              <a-button
                v-if="!record.isSuccess"
                type="primary"
                size="small"
                class="button-table"
                @click="onModify(record, true)"
              >
                已完成
              </a-button>
              <a-button v-else type="primary" size="small" class="button-table modify" @click="onModify(record, false)">
                未完成
              </a-button>
            </template>
          </template>
        </a-table>
      </template>
    </a-table>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { CirclePlusFilled } from '@element-plus/icons-vue';

import { usePage } from '@tanxiang/utils';
import { messageInfo, InputUpdate } from '@tanxiang/common';
import { useAuthTarget, useTarget } from '@tanxiang/apis';
import { columns, innerColumns } from '@/pages/list-target/tableData';
import dayjs from 'dayjs';

const target = useTarget();
const auth = useAuthTarget();

const { messageError, messageSuccess } = messageInfo();

const list = reactive<any[]>([]);
const year = ref<any>(dayjs());
const addYear = ref<number>(dayjs().year());
const selectYear = ref<number>(dayjs().year());
const width = ref<number>(usePage().realWidth());
const dialogFormVisible = ref<boolean>(false);

const content = ref<string>('');
const targets = reactive<any[]>([]);
const expandedRowKeys = reactive<String[]>([]);

onMounted(() => {
  window.addEventListener('resize', () => (width.value = usePage().realWidth()));
  targetList(year.value.year());
});

const close = () => {
  dialogFormVisible.value = false;
  content.value = '';
};

const submitTarget = () => {
  auth
    .createTarget(content.value, addYear.value)
    .then((success: any) => {
      messageSuccess(success.message);
      dialogFormVisible.value = false;
      content.value = '';
      if (addYear.value === dayjs().year()) targetList(addYear.value);
      addYear.value = dayjs().year();
    })
    .catch((error: any) => messageError(!error || error.name ? '网络错误！' : error));
};

const targetList = (year: number) => {
  target
    .getTargets(year)
    .then((success: any) => {
      let map = success.data.map((item: any) => {
        item['input'] = false;
        item['loading'] = false;
        item['isClick'] = false;
        item['oldContent'] = item.content;
        return item;
      });
      targets.splice(0, targets.length, ...map);
    })
    .catch((error: any) => messageError(!error || error.name ? '网络错误！' : error));
};

const dateChange = (data: any) => targetList(data.$y);

const addTarget = () => {
  dialogFormVisible.value = true;
};

const onDelete = (record: any) => {
  auth
    .deleteTarget(record.id)
    .then((success: any) => {
      messageSuccess(success.message);
      targets.splice(0, targets.length, ...targets.filter((item: any) => item.id !== record.id));
    })
    .catch((error: any) => messageError(!error || error.name ? '网络错误！' : error));
};

const onModify = (record: any, status: boolean) => {
  auth
    .updateTargetStatus(record.id, status)
    .then((success: any) => {
      messageSuccess(success.message);
      let map = targets.map((item: any) => {
        if (item.id === record.id) item.isSuccess = status;
        return item;
      });
      targets.splice(0, targets.length, ...map);
    })
    .catch((error: any) => messageError(!error || error.name ? '网络错误！' : error));
};

const setRecord = (record: any, condition: string) => {
  if (!list.find((l) => l['condition'] === condition && l['id'] === record.id)) {
    list.push({
      id: record.id,
      condition: condition,
      loading: false,
      isClick: false,
      isSuccess: false,
      input: false,
      oldContent: record[condition],
      content: record[condition],
    });
  }
  return record;
};

const expand = (expanded: any, record: any) => {
  // 判断数组的长度是否为0，如果为0直接添加
  if (expandedRowKeys.length === 0) return expandedRowKeys.push(record.id);
  // 当数组长度不为0时，判断当前点击的行是否已经打开
  let number = expandedRowKeys.indexOf(record.id);
  // 如果已经打开，将该行的ID移除数组
  if (number !== -1) return expandedRowKeys.splice(number, 1);
  // 没有打开则移除掉其他打开的行，再打开当前行
  expandedRowKeys.splice(0, expandedRowKeys.length);
  expandedRowKeys.push(record.id);
};

const isHidden = computed(() => width.value >= 768);
</script>

<style scoped>
.button-table {
  margin: 0 5px;
  font-size: 13px;
  border-radius: 4px;
}

.modify {
  background-color: #e6a23c !important;
}

.modify:hover {
  background-color: #eebe77 !important;
}
</style>
<style>
.el-dialog__body {
  padding-top: 7px;
  padding-bottom: 7px;
}
</style>