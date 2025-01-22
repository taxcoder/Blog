<!--
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2025-01-17 03:52:07
 * @LastEditTime: 2025-01-17 03:56:24
 * @FilePath: /code/blog/client-admin/src/views/login/modules/Account.vue
 * @Description: 账号密码登录
 * Copyright (c) 2025 by tanxiang, All Rights Reserved. 
-->

<template>
  <n-form ref="formRef" :model="model" class="grid gap-5">
    <n-form-item-row
      label="用户名"
      :show-label="false"
      path="username"
      :show-feedback="false"
      :validation-status="validationStatus.username"
    >
      <n-input placeholder="用户名" :value="model.username" @input="valueInput('username', $event)" />
    </n-form-item-row>
    <n-form-item-row
      label="密码"
      :show-label="false"
      path="password"
      :show-feedback="false"
      :validation-status="validationStatus.password"
    >
      <n-input
        placeholder="密码"
        type="password"
        :value="model.password"
        show-password-on="mousedown"
        @input="valueInput('password', $event)"
      />
    </n-form-item-row>
    <n-button type="info" block round strong @click="handleValidateClick"> 登录 </n-button>
  </n-form>
</template>

<script setup lang="ts">
import notify from '@/utils/notifyUtils';

interface validationStatusType {
  username: 'success' | 'error' | 'warning' | undefined;
  password: 'success' | 'error' | 'warning' | undefined;
}

const router = useRouter();
const auth = useAuthStore();

const model = reactive({
  username: '',
  password: ''
});

const validationStatus = reactive<validationStatusType>({
  username: 'success',
  password: 'success'
});

const valueInput = (type: 'username' | 'password', value: string) => {
  if (value.trim() !== '') {
    validationStatus[type] = 'success';
  } else {
    validationStatus[type] = 'error';
  }
  model[type] = value;
};

const handleValidateClick = () => {
  if (model.username === '' && model.password === '') {
    notify.error('用户名和密码不能为空');
    validationStatus.username = 'error';
    validationStatus.password = 'error';
  } else if (model.password === '') {
    notify.error('密码不能为空');
    validationStatus.password = 'error';
  } else if (model.username === '') {
    notify.error('用户名不能为空');
    validationStatus.username = 'error';
  } else {
    // 没问题，请求后端接口
    notify.loading('✿ 登陆中 ✿');
    api.login(model).then((success: any) => {
      localStorage.setItem('token', success.token);
      auth.setUserInfo(success.info);
      notify.success('登陆成功');
      router.push('/');
    });
  }
};
</script>

<style lang="css" scoped></style>
