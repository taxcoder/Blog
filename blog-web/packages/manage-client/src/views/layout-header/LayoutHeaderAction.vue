<template>
  <div id="action">
    <a-button type="text" size="large" style="width: var(--header-user-height)">
      <BellOutlined :style="{ fontSize: '18px' }" />
    </a-button>
    <a-dropdown placement="bottom" trigger="hover" class="dropdown-user">
      <a-button type="text">
        <a-avatar :size="28" :src="icon"></a-avatar>
        <span class="user-name">{{ base.getWebStation['userName'] }}</span>
      </a-button>
      <template #overlay>
        <a-menu>
          <a-menu-item @click="head = true">
            <PictureOutlined />
            我的头像
          </a-menu-item>
          <a-divider style="margin: 2px 0 2px 0" />
          <a-menu-item @click="updateInfo">
            <UserOutlined />
            用户信息
          </a-menu-item>
          <a-divider style="margin: 2px 0 2px 0" />
          <a-menu-item @click="logout">
            <LoginOutlined />
            退出登录
          </a-menu-item>
        </a-menu>
      </template>
    </a-dropdown>
    <a-button type="text" size="large" style="width: var(--header-user-height)">
      <SettingOutlined />
    </a-button>

    <custom-upload :fileList="fileList" @change="change" title="头像" :api="upload.uploadHead" field="headIcon" />
  </div>
</template>

<script setup lang="ts">
import CustomUpload from '@/components/custom-upload/CustomUpload.vue';

import { computed, h, onMounted, provide, reactive, ref, watch } from 'vue';
import { BellOutlined, LoginOutlined, PictureOutlined, SettingOutlined, UserOutlined } from '@ant-design/icons-vue';

import { useBaseStore } from '@/stores/base';
import { password, text } from './messageBox';
import { useAuthWebStation, useLogin, useUpload, web } from '@tanxiang/apis';
import { messageInfo } from '@tanxiang/common';

const { messageError, messageSuccess, messageLoading, messageWarning } = messageInfo();

const base = useBaseStore();
const login = useLogin();
const upload = useUpload();
const auth = useAuthWebStation();

const head = ref<boolean>(false);
const isChangePassword = ref<boolean>(false);

const username = ref<string>('');
const loginName = ref<string>('');
const newPassword = ref<string>('');
const oldPassword = ref<string>('');

const fileList = reactive<any[]>([]);

provide('image', head);

onMounted(() => getLoginName());

const getLoginName = (fn?: Function) => {
  auth
    .loginNameInfo()
    .then((success: any) => {
      if (success) {
        loginName.value = success.data.email;
        if (!!fn) fn(success.data.email);
      }
    })
    .catch((error: any) => messageError(!error || error.name ? '网络错误！' : error));
};

const change = (files: any[]) => {
  fileList.splice(0, fileList.length, ...files);
};

const updateInfo = () => {
  let web = base.getWebStation;
  if (!web.loginName) {
    if (!loginName.value || loginName.value.length === 0) {
      getLoginName((email: any) => {
        web.loginName = email;
        base.setWebStation(web);
        info();
      });
      return;
    }
    web.loginName = loginName.value;
    base.setWebStation(web);
  }
  info();
};

const info = () => {
  let temp: any[] = [
    {
      spanName: '用户名：',
      defaultValue: username,
      onInput: usernameInput,
      maxLength: 20,
      placeholder: '',
    },
    {
      spanName: '登录名：',
      defaultValue: loginName,
      onInput: loginNameInput,
      maxLength: 30,
      placeholder: '',
    },
  ];

  //@ts-ignore
  ElMessageBox({
    title: '用户信息',
    beforeClose: beforeClose,
    showCancelButton: true,
    cancelButtonText: '修改密码',
    confirmButtonText: '更新信息',
    distinguishCancelAndClose: true,
    message: h('div', { style: 'padding-bottom: 8px' }, text(temp)),
  }).catch(() => {});
};

// 第一个messagebox的关闭前回调
const beforeClose = (action: any, instance: any, done: () => void) => {
  if (action === 'confirm') {
    isChangePassword.value = false;
  } else if (action === 'cancel') {
    isChangePassword.value = true;
  } else if (action === 'close') {
    isChangePassword.value = false;
    return done();
  }
  let web = base.getWebStation;
  let flag = isChangePassword.value;
  // 如果当前不是修改密码，并且用户名 登录名未修改直接显示警告
  if (!flag && web.userName === username.value && web.loginName === loginName.value) {
    return messageWarning('无法提交未更新的数据！', 1.3);
  }
  let temp: any[] = [
    {
      showCount: false,
      allowClear: false,
      spanName: '新密码：',
      defaultValue: newPassword,
      onInput: passwordInput,
      maxLength: 30,
      placeholder: '不更改密码，无需填写！',
    },
    {
      showCount: false,
      allowClear: false,
      spanName: '确认密码：',
      defaultValue: oldPassword,
      onInput: oldPasswordInput,
      maxLength: 30,
      placeholder: '请输入现有密码进行确认！',
    },
  ];
  // 如果不是修改密码，则把新密码的输入框去掉
  if (!flag) temp.shift();
  submit(flag, temp, done);
};
// 第二个messagebox的关闭前回调
const passwordBeforeClose = (action: any, instance: any, done: () => void) => {
  if (action !== 'confirm') {
    oldPassword.value = '';
    newPassword.value = '';
    return done();
  }
  messageLoading('更新中！');
  new Promise((resolve, reject) => {
    let req = isChangePassword.value
      ? auth.updateAdminPassword({ password: newPassword.value, oldPassword: oldPassword.value })
      : auth.updateAdminStation({ userName: username.value, email: loginName.value, oldPassword: oldPassword.value });
    req.then((success: any) => resolve(success)).catch((error: any) => reject(error));
  })
    .then((success: any) => {
      messageSuccess(success.message);
      let web = base.getWebStation;
      if (!isChangePassword.value) {
        web.userName = username.value;
        web.loginName = loginName.value;
      }
      oldPassword.value = '';
      newPassword.value = '';
      done();
    })
    .catch((error: any) => messageError(!error || error.name ? '网络错误！' : error));
};

const submit = (flag: boolean, temp: any[], done: () => void) => {
  //@ts-ignore
  ElMessageBox({
    title: '用户信息',
    beforeClose: passwordBeforeClose,
    message: h('div', { style: 'padding-bottom: 8px' }, password(temp)),
  });
};

const logout = () => {
  base.setLoading(true);
  if (!loginName.value || loginName.value.length === 0) {
    getLoginName(() => out(loginName.value));
  } else {
    out(loginName.value);
  }
};

const out = (name: string) => {
  login
    .logout(name)
    .then((success: any) => {
      messageSuccess(success.message);
      localStorage.removeItem('token');
      setTimeout(() => location.reload(), 500);
    })
    .catch((error: any) => messageError(!error || error.name ? '网络错误！' : error))
    .finally(() => setTimeout(() => base.setLoading(false), 1750));
};

const usernameInput = (text: any) => (username.value = text.target.value);
const loginNameInput = (text: any) => (loginName.value = text.target.value);
const passwordInput = (text: any) => (newPassword.value = text.target.value);
const oldPasswordInput = (text: any) => (oldPassword.value = text.target.value);

const icon = computed(() => base.getWebStation.headIcon);

watch(
  () => base.getWebStation,
  (newValue: any) => {
    if (!newValue.userName) return;
    username.value = newValue.userName;
    change([{ name: 'image.png', url: newValue.headIcon }]);
  },
  { deep: true, immediate: true }
);
</script>

<style scoped>
#action {
  height: 100%;
  line-height: var(--header-user-height);
  display: flex;
  align-items: center;
  flex-shrink: 0;
}

.dropdown-user {
  height: 100%;
}

.dropdown-user .user-name {
  padding-left: 5px;
}
</style>

<style>
.input-prefix {
  padding: 12px 0 12px 0;
  display: block;
}
</style>