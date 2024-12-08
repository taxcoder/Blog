/*
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2024-12-07 16:22:33
 * @LastEditTime: 2024-12-08 11:01:41
 * @FilePath: /blog/client-app/src/main.ts
 * @Description:
 * Copyright (c) 2024 by tanxiang, All Rights Reserved.
 */
import { createApp } from "vue";
import App from "./App.vue";
import pinia from "@/store";
import "element-plus/dist/index.css";

const app = createApp(App);
app.use(pinia);
app.mount("#app");
export default app;
