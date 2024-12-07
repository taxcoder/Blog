<!--
 * @Author: tanxiang 1571922819@qq.com
 * @Date: 2023-07-30 13:36:41
 * @Description:
 * @LastEditTime: 2023-12-02 19:11:23
 * @LastEditors: tanxiang 1571922819@qq.com
 * @FilePath: \blog\packages\web-client\src\components\side-pendant\BackHome.vue
 * @copyright: Copyright (c) 2023 by 1571922819@qq.com, All Rights Reserved.
-->
<template>
  <el-button type="primary" class="side-button" tag="div" @click="returnHome()">
    <template v-slot:icon>
      <el-icon size="20"><HomeFilled /></el-icon>
    </template>
  </el-button>
</template>

<script setup lang="ts">
//@ts-ignore
import { HomeFilled } from '@element-plus/icons-vue';

const router = useRouter();

const returnHome = () => router.replace('/');
</script>

<style scoped></style>
