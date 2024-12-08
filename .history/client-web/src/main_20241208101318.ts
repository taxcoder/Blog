import "@/plugins/style";

import { createApp } from "vue";
import App from "./App.vue";
import pinia from "@/store";
import ElementPlus from "element-plus";
import zhCn from "element-plus/lib/locale/lang/zh-cn";

const app = createApp(App);
app.use(ElementPlus, { locale: zhCn });
app.use(pinia);
app.mount("#app");

export default app;
