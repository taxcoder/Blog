<template>
  <el-dialog
    class="login-dialog"
    v-model="dialogVisible"
    title="登录"
    width="500"
    :before-close="handleClose"
    :append-to-body="true"
    :align-center="true"
  >
    <el-form v-model="userInfo" label-width="80px">
      <el-form-item label="账号">
        <el-input v-model="userInfo.username" autocomplete="on"></el-input>
      </el-form-item>
      <el-form-item label="密码">
        <el-input v-model="userInfo.password" autocomplete="on" show-password></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="handleClose">取消</el-button>
        <el-button type="primary" @click="login">登录</el-button>
      </div>
    </template>
  </el-dialog>

  <div
    id="tx-comment-info"
    v-if="comments.open"
    class="rounded-[7px] bg-white dark:important-bg-[#1e1e1e] min-h-[200px] p-[5px]"
    :class="{
      'flex-center': comments.result.length,
    }"
  >
    <van-loading v-if="comments.loading" color="#0094ff" class="flex-center flex-col">加载中.. .</van-loading>
    <div class="w-full">
      <el-empty v-if="!comments.result.length" :image-size="35" class="h-[250px]" description="期待你的评论！">
        <template #image>
          <el-icon size="40">
            <ChatLineRound />
          </el-icon>
        </template>
      </el-empty>
      <div class="p-[10px]">
        <div v-for="item in comments.result" :key="item.id" class="flex mb-[7px] p-[10px]">
          <div class="mr-[5px]">
            <el-avatar :src="item.image"></el-avatar>
          </div>
          <div class="pl-[5px] text-black dark:text-white">
            <div class="flex items-center flex-row mb-[10px] h-[22px] line-h-[22px]">
              <div class="mr-[10px] text-[#19d863] h-full">{{ item.author }}</div>
              <div
                class="rounded-[7px] text-white px-[7px] h-full flex-center text-[13px]"
                :class="{
                  'bg-[#debc13] dark:bg-[#ac9314]': item.status,
                  'bg-[#7b7b7b] dark:bg-[#3d3d3d]': !item.status,
                }"
              >
                {{ item.status ? '博主' : '访客' }}
              </div>
            </div>
            <div class="mb-[7px] text-[#818181]">
              {{ shortTime(item['time'], '-', true, false) }}
            </div>
            <div class="text-[17px] break-all" v-html="item.content"></div>
          </div>
        </div>
      </div>
      <div class="w-full p-[10px] box-border">
        <div class="bg-[#f9f9f9] px-[10px] py-[5px] rounded-[10px]">
          <div class="flex flex-row border-b-1 border-[#dbdbdb] border-dashed">
            <van-field
              v-model="comments.text.author"
              required
              rows="1"
              :maxlength="20"
              :border="false"
              placeholder="昵称"
            />
            <van-field
              v-model="comments.text.email"
              required
              rows="1"
              :maxlength="50"
              :border="false"
              placeholder="邮箱"
            />
            <van-field
              v-model="comments.text.webUrl"
              rows="1"
              :maxlength="200"
              :border="false"
              placeholder="网址(非必填)"
            />
          </div>
          <van-field
            class="important-pt-[5px]"
            v-model="comments.text.content"
            rows="1"
            :autosize="{
              minHeight: 200,
              maxHeight: 200,
            }"
            show-word-limit
            :maxlength="600"
            :border="false"
            type="textarea"
            placeholder="良言一句三冬暖 恶语伤人六月寒"
          />
        </div>
        <div class="flex items-center justify-between mt-[10px]">
          <n-button strong size="small" type="warning" @click="dialog">
            <template #icon>
              <el-icon v-if="!global.getToken"><Promotion /></el-icon>
              <el-icon v-else><Close /></el-icon>
            </template>
          </n-button>
          <div>
            <n-button strong type="tertiary" size="small" @click="cancel()">取消</n-button>
            <n-button strong type="info" class="ml-[5px]" size="small" @click="submit()">提交</n-button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { Promotion, ChatLineRound, Close } from '@element-plus/icons-vue';

import { ElMessageBox } from 'element-plus';

import { messageInfo } from '@tanxiang/common';
import { useBaseStore } from '@/stores/base.js';
import { useGlobalStore } from '@/stores/global.js';
import { useDate } from '@tanxiang/utils';

const { messageWarning } = messageInfo();

const base = useBaseStore();
const global = useGlobalStore();
const { shortTime } = useDate();

const dialogVisible = ref<boolean>(false);

const userInfo = reactive<{ username: string; password: string }>({
  username: '',
  password: '',
});

const comments = reactive<{
  open: boolean;
  loading: boolean;
  text: {
    author: string;
    email: string;
    webUrl: string;
    content: string;
  };
  init: boolean;
  result: {
    id: number;
    content: string;
    author: string;
    webUrl: string;
    createTime: string;
    image: string;
    status: boolean;
  }[];
}>({
  open: false,
  loading: false,
  text: {
    author: '',
    email: '',
    webUrl: '',
    content: '',
  },
  init: true,
  result: [],
});

const emit = defineEmits(['submit', 'login', 'logout']);

const cancel = () => {
  comments.open = false;
  comments.text.author = '';
  comments.text.email = '';
  comments.text.webUrl = '';
  comments.text.content = '';
};

const clear = () => {
  comments.text.content = '';
};

const dialog = () => {
  if (global.getToken) {
    ElMessageBox.alert('是否退出登录？', '退出登录', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      customClass: 'logout',
      closeOnClickModal: true,
      showCancelButton: true,
      callback: (action: any) => {
        if (action === 'confirm') emit('logout');
      },
    });
  } else {
    dialogVisible.value = true;
  }
};

const submit = () => {
  if (comments.text.author === '' || comments.text.author.length === 0) {
    return messageWarning('昵称不能为空！');
  }
  if (comments.text.email === '' || comments.text.email.length === 0) {
    return messageWarning('邮箱不能为空！');
  }

  if (comments.text.content === '' || comments.text.content.length === 0) {
    return messageWarning('内容不能为空！');
  }

  emit('submit', comments.text);
};

const login = () => {
  if (userInfo.username === '' || userInfo.username.length === 0) {
    return messageWarning('用户名不能为空！');
  }

  if (userInfo.password === '' || userInfo.password.length === 0) {
    return messageWarning('密码不能为空！');
  }

  emit('login', userInfo, (author: string, email: string, webUrl: string) => {
    console.log(author, email, webUrl);
    comments.text.author = author;
    comments.text.email = email;
    comments.text.webUrl = webUrl;
    handleClose();
  });
};

const onSubmit = () => {
  emit('onSubmit');
};

const handleClose = () => {
  userInfo.password = '';
  userInfo.username = '';
  dialogVisible.value = false;
};

defineExpose({ comments, clear });
</script>

<style>
#tx-comment-info input,
#tx-comment-info textarea,
#tx-comment-info input::placeholder,
#tx-comment-info textarea::placeholder {
  font-size: 15px;
}

#tx-comment-info input::placeholder,
#tx-comment-info textarea::placeholder {
  color: #282828 !important;
}

#tx-comment-info .van-cell {
  padding: 0;
  background: none;
}

#tx-comment-info .van-field:not(:last-child) {
  padding-right: 10px;
  position: relative;
  border-right: 1px dashed #dbdbdb;
}

#tx-comment-info .van-field__control {
  height: 35px;
  padding-left: 10px;
}

#tx-comment-info .van-hairline--top-bottom:after,
#tx-comment-info .van-hairline-unset--top-bottom:after {
  border: none;
}

.login-dialog .el-input__wrapper {
  border-radius: 5px !important;
}

.logout {
  height: auto !important;
  min-height: auto;
  max-width: 400px !important;
}
</style>