<template>
  <div id="excerpt-list">
    <n-timeline :icon-size="35" size="large" class="w-full">
      <n-timeline-item v-for="(excerpt, index) in excerpts.records" :key="excerpt.id">
        <template #icon>
          <BookStar24Fill />
        </template>
        <template #default>
          <n-image
            lazy
            :show-toolbar="false"
            v-if="isImage(excerpt.content)"
            :src="getUrl(excerpt.content)"
            object-fit="cover"
            width="100%"
            :height="status.width > 600 ? 300 : 200"
            fallback-src="data:image/png;base64,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"
          ></n-image>
          <div v-else class="text-[17px]" v-html="excerpt.content"></div>
        </template>
        <template #footer>
          <div class="text-[16px]">
            <div>{{ shortTime(excerpt['createTime'], '-', true, false) }}</div>
            <div class="inline-flex cursor-pointer self-start mt-[10px]" @click="openChat(excerpt['id'], index)">
              <el-icon size="18">
                <ChatLineRound />
              </el-icon>
              <span class="text-[18px] ml-[1px] transform translate-y-[-2px]">
                {{ excerpt['chatNumber'] == '0' ? '' : excerpt['chatNumber'] }}
              </span>
            </div>
            <comment-info
              ref="comment"
              @submit="(text) => submit(text, index, excerpt)"
              @login="login"
              @logout="logout"
            />
          </div>
        </template>
      </n-timeline-item>
    </n-timeline>
  </div>
</template>

<script setup lang="ts">
import { useCommon, useDate } from '@tanxiang/utils';
import { useChats, useExcerpt, useLogin } from '@tanxiang/apis';
import { messageInfo } from '@tanxiang/common';
import { ChatLineRound } from '@element-plus/icons-vue';
import { BookStar24Fill } from '@tanxiang/common';
import CommentInfo from '@/components/comment-info/CommentInfo.vue';
import { computed } from 'vue';
import { useBaseStore } from '@/stores/base.js';
import useWebLogout from '@/hooks/useWebLogout';
import { useGlobalStore } from '@/stores/global.js';

const base = useBaseStore();
const global = useGlobalStore();

const { shortTime } = useDate();
const { messageError, messageSuccess, messageWarning } = messageInfo();

const status = inject('status');

const comment = ref<any>(null);

const excerpts = reactive<{
  current: number;
  size: number;
  total: number;
  records: any[];
}>({
  current: 1,
  size: 15,
  total: 0,
  records: [],
});

onMounted(() => {
  useExcerpt()
    .excerptList(1, 15)
    .then((res) => {
      excerpts.current = res.data.current;
      excerpts.size = res.data.size;
      excerpts.total = res.data.total;
      excerpts.records.push(...res.data.records);
    })
    .catch((err) => messageError(!err || err.name ? '网络错误！' : err.message));
});

const openChat = (id: any, dataIndex: number) => {
  let find = comment.value[dataIndex].comments;
  find.open = !find.open;
  if (!find.result.length && find.open && find.init) {
    useChats()
      .chatList(id)
      .then((res) => {
        find.init = false;
        if (global.getToken) {
          find.text.author = global.getWebSite.userName;
          find.text.email = useCommon().parseJwtToken(global.getToken);
          find.text.webUrl = window.location.origin;
        }
        find.result.push(
          ...res.data.map((r) => ({
            id: r.id,
            content: r.content,
            time: r.createTime,
            author: r.author,
            webUrl: r.webUrl,
            status: r.status,
            image: r.status ? global.getWebSite.headIcon : r.image,
          }))
        );
      })
      .catch((err) => {
        console.log(err);
        // messageError(!err || err.name ? '网络错误！' : err.message)
      })
      .finally(() => (find.loading = false));
  }
};

const submit = (text: any, dataIndex: number, excerpt: any) => {
  if (global.getToken) {
    useChats()
      .webChatAdd(excerpt.id, text)
      .then((success) => {
        messageSuccess(success.message);
        comment.value[dataIndex].comments.result.push({
          id: success.data.id,
          content: success.data.content,
          time: success.data.createTime,
          author: success.data.author,
          webUrl: success.data.webUrl,
          status: success.data.status,
          image: success.data.status ? global.getWebSite.headIcon : success.data.image,
        });
        comment.value[dataIndex].clear();
      })
      .catch((err) => messageError(!err || err.name ? '网络错误！' : err.message));
  } else {
    useChats()
      .chatAdd(excerpt.id, text)
      .then((success) => {
        messageSuccess(success.message);
        comment.value[dataIndex].comments.result.push({
          id: success.data.id,
          content: success.data.content,
          time: success.data.createTime,
          author: success.data.author,
          webUrl: success.data.webUrl,
          status: success.data.status,
          image: '',
        });
        comment.value[dataIndex].clear();
      })
      .catch((err) => messageError(!err || err.name ? '网络错误！' : err.message));
  }
};

const login = (userInfo: { username: string; password: string }, operation: Function) => {
  if (userInfo.username.length > 40) {
    return messageWarning('用户名长度过长！');
  }

  if (!/^([a-zA-Z]|[0-9])(\w|-)+@[a-zA-Z0-9]+\.([a-zA-Z]{2,4})$/.test(userInfo.username)) {
    return messageWarning('用户名格式错误！');
  }

  useLogin()
    .webLogin(userInfo)
    .then((success: any) => {
      global.setToken(success.data);
      messageSuccess(success.message);
      operation(global.getWebSite.userName, useCommon().parseJwtToken(success.data), window.location.origin);
    })
    .catch((error) => {
      console.log(error);
      messageError(!error || error.name ? '网络错误！' : error);
    });
};

const logout = () => {
  useWebLogout().logout(useGlobalStore().getToken, () => {
    global.setToken('');
    localStorage.removeItem('token');
  });
};

const isImage = computed(() => (content: string) => {
  return content.startsWith("<img src='") && content.endsWith('/>');
});

const getUrl = computed(() => (content: string) => {
  return content.match("src='.*webp")[0].replace("src='", '');
});

const format = computed(() => (data: any) => {
  return data.map((item: any) => ({
    id: item.id,
    time: item.createTime,
    author: item.author,
    content: item.content,
    image: '',
  }));
});
</script>

<style>
#excerpt-list .el-empty__image {
  width: auto;
  height: 25px;
}

.n-image-preview-overlay {
  background-color: rgba(0, 0, 0, 0.8);
}
</style>