<template>
  <div id="excerpt" class="h-screen">
    <a-modal v-model:open="open" :centered="true" title="添加摘抄" @cancel="cancel" @ok="ok" :width="534">
      <a-collapse v-model:activeKey="activeKey" accordion class="excerpt-collapse">
        <a-collapse-panel key="content" header="文本">
          <a-textarea
            v-model:value="excerpts.content"
            placeholder="请输入摘抄内容"
            :maxLength="300"
            :autoSize="{ minRows: 4, maxRows: 6 }"
            @change="change('content')"
            class="w-full important-rounded-0 important-min-h-[200px]"
          />
        </a-collapse-panel>
        <a-collapse-panel key="file" header="图片">
          <user-upload
            :list="excerpts.file"
            @change-file="changeFile"
            :class="{ 'important-h-[200px]': excerpts.file.length === 0 }"
          />
        </a-collapse-panel>
      </a-collapse>
    </a-modal>
    <table-search
      :formState="formState"
      :name="name"
      :clearable="false"
      :show="false"
      @dataSource="dataSource"
      @current="current"
      @add="add"
    ></table-search>

    <list-excerpt-table :source="tableSource" @dataSource="dataSource" @current="current" />
  </div>
</template>

<script setup lang="ts">
import TableSearch from '@/components/table-search/TableSearch.vue';
import { provide, reactive, ref, watch, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { UploadProps } from 'ant-design-vue';
import { messageInfo } from '@tanxiang/common';
import ListExcerptTable from '@/pages/list-excerpt/ListExcerptTable.vue';
import { useExcerptStore } from '@/stores/excerpt';
import { useAuthExcerpt } from '@tanxiang/apis';

const { messageLoading, messageWarning, messageError, messageSuccess } = messageInfo();

const auth = useAuthExcerpt();
const route: any = useRoute();
const router: any = useRouter();
const excerpt = useExcerptStore();
const activeKey = ref<string>('content');

const open = ref<boolean>(false);
const tabCurrent = ref<number>(0);
const name = ref<string>('excerpt');

const tableSource = reactive<any>({
  total: 0,
  records: [],
  size: 15,
  current: 1,
});

const excerpts = reactive<any>({
  file: [] as File[],
  content: '' as string,
});

const formState = reactive<any>({
  key: [{ value: 'id', label: 'ID', placeholder: '请输入一个正整数，不能为0' }],
  select: 'id',
  value: '',
});

onMounted(() => {
  if (route.query.key && formState.key.find((c: any) => c.value === route.query.key))
    formState.select = route.query.key;
  if (route.query.value !== void 0) formState.value = route.query.value;
});

provide('tabCurrent', tabCurrent);

const dataSource = (source: any) => {
  tableSource.current = source.current;
  tableSource.size = source.size;
  tableSource.total = source.total;
  tableSource.records.splice(0, tableSource.records.length, ...source.records);
};

const current = (c: any) => (tabCurrent.value = c);

const add = () => (open.value = true);

const cancel = () => (open.value = false);

const ok = () => {
  messageLoading('正在上传中！');
  if (activeKey.value === 'content') {
    if (excerpts.content.length === 0 || excerpts.content === '') {
      messageError('摘抄内容为空！');
      return;
    }
    if (excerpts.content.length > 300) {
      messageError('摘抄内容不能超过300个字！');
      return;
    }
    auth
      .excerptInsertContent(excerpts.content)
      .then((success: any) => {
        messageSuccess(success.data);
        router.replace({ path: '/refresh', query: { t: Date.now(), f: route.path } });
        cancel();
      })
      .catch((error: any) => {
        messageError(!error || error.name ? '添加失败！' : error);
      });
  } else {
    if (excerpts.file.length === 0) {
      messageError('请上传图片！');
      return;
    }
    auth
      .excerptInsertFile(excerpts.file[0])
      .then((success: any) => {
        messageSuccess(success.data);
        router.replace({ path: '/refresh', query: { t: Date.now(), f: route.path } });
        cancel();
      })
      .catch((error: any) => messageError(!error || error.name ? '更新失败！' : error));
  }
};

const change = (data: string) => {
  if (data === 'content') {
    excerpts.file.splice(0, excerpts.file.length);
  } else {
    excerpts.content = '';
  }
};

const changeFile = (file: UploadProps['fileList']) => {
  excerpts.file.splice(0, excerpts.file.length, ...file);
  change('file');
};

watch(
  () => formState,
  () => {
    excerpt.setSearch({ name: name.value, value: formState.value, select: formState.select });
  },
  { deep: true }
);

watch(
  () => formState.select,
  () => {
    formState.value = '';
  }
);
</script>

<style></style>