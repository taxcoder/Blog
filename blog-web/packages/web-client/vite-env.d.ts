declare module '@element-plus/*';

declare module '*.vue' {
  // @ts-ignore
  import type { DefineComponent } from 'vue';
  const component: DefineComponent<{}, {}, any>;
  export default component;
}