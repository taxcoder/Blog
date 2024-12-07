<template>
  <login-page :login="login" />
</template>

<script setup lang="ts">
import { LoginPage, messageInfo } from '@tanxiang/common';
import { useBaseStore } from '@/stores/base';
import { useLogin } from '@tanxiang/apis';

const router = useRouter();
const base = useBaseStore();

const { messageSuccess, messageError } = messageInfo();

const login = (result: { checked: boolean; email: string; password: string }) => {
  useLogin()
    .login({ email: result.email, password: result.password, remember: result.checked })
    .then((success: any) => {
      messageSuccess(success.message);
      localStorage.setItem('token', success.data);
      base.setIsLogin(success.data);
      base.setIsSuccess(true);
      setTimeout(() => router.replace('/'), 1000);
    })
    .catch((error: any) => messageError(!error || error.name ? '网络错误！' : error));
};
</script>

<style></style>