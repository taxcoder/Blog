<!--
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2025-01-17 03:52:14
 * @LastEditTime: 2025-01-17 04:10:28
 * @FilePath: /code/blog/client-admin/src/views/login/modules/Phone.vue
 * @Description: 手机号登录
 * Copyright (c) 2025 by tanxiang, All Rights Reserved. 
-->

<template>
  <n-form ref="formRef" :model="model" class="grid gap-5">
    <n-form-item-row
      label="手机号"
      :show-label="false"
      path="phone"
      :show-feedback="false"
      :validation-status="validationStatus.phone"
    >
      <n-input placeholder="手机号" :value="model.phone" @input="valueInput('phone', $event)" />
    </n-form-item-row>
    <n-form-item-row
      label="验证码"
      :show-label="false"
      path="code"
      :show-feedback="false"
      :validation-status="validationStatus.code"
    >
      <n-input placeholder="验证码" :value="model.code" @input="valueInput('code', $event)" />
      <n-button class="ml-2" type="info" ghost @click="getCode()">获取验证码</n-button>
    </n-form-item-row>
    <n-button type="info" block round strong @click="handleValidateClick"> 登录 </n-button>
  </n-form>
</template>

<script setup lang="ts">
import notify from '@/utils/notifyUtils';

interface validationStatusType {
  phone: 'success' | 'error' | 'warning' | undefined;
  code: 'success' | 'error' | 'warning' | undefined;
}

const model = reactive({
  phone: '',
  code: ''
});

const validationStatus = reactive<validationStatusType>({
  phone: 'success',
  code: 'success'
});

const valueInput = (type: 'phone' | 'code', value: string) => {
  if (value.trim() !== '') {
    validationStatus[type] = 'success';
  } else {
    validationStatus[type] = 'error';
  }
  model[type] = value;
};

const isPureNumber = (str: string) => {
  return /^\d+$/.test(str);
};

const getCode = () => {
  if (model.phone === '') {
    notify.error('手机号不能为空');
    validationStatus.phone = 'error';
  } else if (!/^1[3-9]\d{9}$/.test(model.phone)) {
    notify.error('请输入正确的手机号');
    validationStatus.phone = 'error';
  } else {
    notify.loading('请稍等');
    // 调用接口，先判断手机号是不是管理员的手机号，如果不是，返回错误信息，告诉用户不是管理员手机号，不能用于登陆
  }
};

const handleValidateClick = () => {
  if (model.phone === '' && model.code === '') {
    notify.error('手机号和验证码不能为空');
    validationStatus.phone = 'error';
    validationStatus.code = 'error';
  } else if (model.code === '') {
    notify.error('验证码不能为空');
    validationStatus.code = 'error';
  } else if (model.code.trim().length !== 6 && !isPureNumber(model.code)) {
    notify.error('验证码格式错误');
    validationStatus.code = 'error';
  } else if (model.phone === '') {
    notify.error('手机号不能为空');
    validationStatus.phone = 'error';
  } else if (!/^1[3-9]\d{9}$/.test(model.phone)) {
    notify.error('手机号格式错误');
    validationStatus.phone = 'error';
  } else {
    // 请求后端接口，传入手机号和验证码，判断是否正确，正确则登录，错误则提示错误信息
  }
};
</script>

<style lang="css" scoped></style>
