import '@/styles/global.css';
import 'virtual:uno.css';
import initMock from '@/mock';

import { createApp } from 'vue';
import App from './App.vue';
import router from '@/router';
import pinia from '@/store';

const app = createApp(App);
app.use(router);
app.use(pinia);
app.mount('#app');

if (import.meta.env.VITE_APP_REQUEST_MOCK == 'true') {
  initMock();
}

useTitle(import.meta.env.VITE_APP_TITLE);
