<template>
  <div class="h-full w-full">
    <div id="settings">
      <el-form
        :model="settings"
        label-width="95px"
        label-position="left"
        :rules="rules"
        ref="ruleFormRef"
        :scroll-to-error="true"
        :status-icon="true"
      >
        <el-form-item label="网站名" prop="name">
          <el-input
            v-model="settings.name"
            maxlength="40"
            show-word-limit
            type="textarea"
            :clearable="true"
            :autosize="{ minRows: 1, maxRows: 4 }"
          />
        </el-form-item>
        <el-form-item label="座右铭" prop="motto">
          <el-input
            v-model="settings.motto"
            maxlength="60"
            show-word-limit
            type="textarea"
            :clearable="true"
            :autosize="{ minRows: 1, maxRows: 4 }"
          />
        </el-form-item>
        <el-form-item label="公告栏" prop="bulletinBoard">
          <el-input
            v-model="settings.bulletinBoard"
            maxlength="200"
            show-word-limit
            type="textarea"
            :clearable="true"
            :autosize="{ minRows: 1, maxRows: 6 }"
          />
        </el-form-item>
        <el-form-item label="网站备案号" prop="forTheRecord">
          <el-input
            v-model="settings.forTheRecord"
            maxlength="50"
            show-word-limit
            type="textarea"
            :clearable="true"
            :autosize="{ minRows: 1, maxRows: 4 }"
          />
        </el-form-item>
        <el-form-item label="公安备案号" prop="publicSecurityRegistrationNumber">
          <el-input
            v-model="settings.publicSecurityRegistrationNumber"
            maxlength="50"
            show-word-limit
            type="textarea"
            :clearable="true"
            :autosize="{ minRows: 1, maxRows: 4 }"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSubmit(ruleFormRef)">提交</el-button>
        </el-form-item>
      </el-form>
    </div>

    <a-modal v-model:open="modalOpen" centered :closable="false" title="格言" @ok="updateOk" :destroyOnClose="true">
      <div v-for="(item, index) in length" class="mb-4">
        <a-input
          :default-value="saying.defaultSay[index]"
          @change="(text: any) => changeInput(text, index)"
          class="input-modal"
          showCount
          :maxlength="40"
          :key="item"
        />
      </div>
      <div class="operation mb-4 flex items-center justify-end w-full">
        <a-button shape="circle" :icon="h(PlusOutlined)" @click="add" v-show="length < 5" />
        <a-button
          class="ml-2"
          shape="circle"
          :icon="h(MinusOutlined)"
          @click="sub"
          v-show="length > 1 && length > saying.defaultSay.length"
        />
      </div>
    </a-modal>

    <custom-upload
      :fileList="fileList"
      @change="change"
      title="网络背景图"
      :api="upload.uploadBackground"
      field="backgroundImage"
    />

    <a-float-button-group trigger="hover" type="primary" :style="{ right: '25px', bottom: '25px' }">
      <template #icon>
        <FlagOutlined />
      </template>
      <a-float-button @click="open">
        <template #tooltip>
          <div>格言</div>
        </template>
        <template #icon>
          <HighlightOutlined />
        </template>
      </a-float-button>
      <a-float-button @click="openBackground">
        <template #tooltip>
          <div>背景图</div>
        </template>
        <template #icon>
          <BgColorsOutlined />
        </template>
      </a-float-button>
    </a-float-button-group>
  </div>
</template>

<script setup lang="ts">
import { h, onActivated, reactive, ref, provide, watch } from 'vue';
import { messageInfo } from '@tanxiang/common';
import { text, useAuthWebStation, useUpload } from '@tanxiang/apis';
import { useBaseStore } from '@/stores/base';

import CustomUpload from '@/components/custom-upload/CustomUpload.vue';

import { BgColorsOutlined, FlagOutlined, HighlightOutlined, MinusOutlined, PlusOutlined } from '@ant-design/icons-vue';

const { messageError, messageSuccess, messageWarning } = messageInfo();

const ruleFormRef = ref<any>();
const upload = useUpload();
const auth = useAuthWebStation();
const base = useBaseStore();

const background = ref<boolean>(false);
const modalOpen = ref<boolean>(false);
const length = ref<number>(-1);

const fileList = reactive<any[]>([]);

const settings = reactive<any>({
  name: '',
  motto: '',
  forTheRecord: '',
  bulletinBoard: '',
  publicSecurityRegistrationNumber: '',
});

const saying = reactive<any>({
  say: [] as string[],
  defaultSay: [] as string[],
});

provide('image', background);

onActivated(() => {
  // 每次进来如果有请求过数据，则不在重新请求
  if (base.getWebStation && base.getWebStation.id) {
    updateWeb(base.getWebStation);
  } else {
    auth
      .webStationInfo()
      .then((success: any) => {
        base.setWebStation(success.data);
        updateWeb(base.getWebStation);
      })
      .catch((error: any) => messageError(!error || error.name ? '网络错误！' : error));
  }
});

const change = (files: any[]) => {
  fileList.splice(0, fileList.length, ...files);
};

const updateWeb = (ret: any) => {
  settings.name = ret.name;
  settings.motto = ret.motto;
  settings.forTheRecord = ret.forTheRecord;
  settings.bulletinBoard = ret.bulletinBoard;
  settings.publicSecurityRegistrationNumber = ret.publicSecurityRegistrationNumber;
};

const checkName = (rule: any, value: any, callback: any) => {
  if (!value) callback('网站名不能为空！');
  else callback();
};
const checkMotto = (rule: any, value: any, callback: any) => {
  if (!value) callback('格言不能为空！');
  else callback();
};
const checkForTheRecord = (rule: any, value: any, callback: any) => {
  if (!value) callback('网站备案号不能为空！');
  else callback();
};
const checkPublicSecurityRegistrationNumber = (rule: any, value: any, callback: any) => {
  if (!value) callback('公安备案号不能为空！');
  else callback();
};
const checkBulletinBoard = (rule: any, value: any, callback: any) => {
  if (!value) callback('公告栏不能为空！');
  else callback();
};

const rules = reactive<any>({
  name: [{ validator: checkName, trigger: 'blur' }],
  motto: [{ validator: checkMotto, trigger: 'blur' }],
  forTheRecord: [{ validator: checkForTheRecord, trigger: 'blur' }],
  publicSecurityRegistrationNumber: [{ validator: checkPublicSecurityRegistrationNumber, trigger: 'blur' }],
  bulletinBoard: [{ validator: checkBulletinBoard, trigger: 'blur' }],
});

const isSame = () => {
  return (
    settings.name === base.getWebStation.name &&
    settings.motto === base.getWebStation.motto &&
    settings.bulletinBoard === base.getWebStation.bulletinBoard &&
    settings.forTheRecord === base.getWebStation.forTheRecord &&
    settings.publicSecurityRegistrationNumber === base.getWebStation.publicSecurityRegistrationNumber
  );
};

const onSubmit = async (ruleFormRef: any) => {
  if (!ruleFormRef) return;
  await ruleFormRef.validate((valid: any, fields: any) => {
    if (!valid) return;
    if (isSame()) return messageWarning('无法提交相同的数据！');
    // 发出真正的请求
    auth
      .updateWebStation(settings)
      .then((success: any) => {
        messageSuccess(success.message);
        let web = base.getWebStation;
        //@ts-ignore
        web.name = settings.name;
        web.motto = settings.motto;
        web.forTheRecord = settings.forTheRecord;
        web.bulletinBoard = settings.bulletinBoard;
        web.publicSecurityRegistrationNumber = settings.publicSecurityRegistrationNumber;
        base.setWebStation(web);
      })
      .catch((error: any) => messageError(error.message));
  });
};

const open = () => {
  text()
    .then((success: any) => {
      modalOpen.value = true;
      saying.defaultSay.splice(0, saying.defaultSay.length);
      saying.defaultSay.push(...success.data);
      saying.say.splice(0, saying.say.length);
      saying.say.push(...success.data);
      length.value = saying.defaultSay.length;
    })
    .catch((error: any) => messageError(!error || error.name ? '网络错误！' : error));
};

/**
 * 更新格言
 */
const updateOk = () => {
  if (saying.defaultSay.every((value, index) => value === saying.say[index])) {
    return messageWarning('数据未变更！');
  }
  useAuthWebStation()
    .updatePrintText(saying.say.map((c: any, index: number) => ({ id: index + 1, content: c })))
    .then((success: any) => {
      messageSuccess(success.message);
      modalOpen.value = false;
    })
    .catch((error: any) => messageError(!error || error.name ? '网络错误！' : error));
};

/**
 * 当input内的值发生改变时，将存储的数据进行修改
 * @param text 更新的值
 * @param index 在数组内的索引
 */
const changeInput = (text: any, index: number) => (saying.say[index] = text.target.value);

const add = () => (length.value = length.value >= 5 ? 5 : length.value + 1);

const sub = () => {
  if (length.value <= 1 || length.value <= saying.defaultSay.length) return;
  length.value = length.value - 1;
};

const openBackground = () => {
  background.value = true;
  change([{ name: 'image.png', url: base.getWebStation.backgroundImage }]);
};
</script>

<style scoped>
#settings {
  width: 100%;
  background-color: white;
  max-width: 600px;
  margin: 100px auto 0 auto;
  display: flex;
  padding: 45px 20px 10px 15px;
  flex-direction: column;
  border-radius: 10px;
}

.input-modal:not(:last-child) {
  margin-bottom: 20px;
}
</style>

<style>
.ant-btn-primary,
.el-input-group__append {
  background-color: #409eff !important;
}

.el-input-group__append .el-icon {
  color: white;
}

#settings .el-form-item__content {
  justify-content: center;
}

.operation .ant-btn {
  border-radius: 50% !important;
}
</style>