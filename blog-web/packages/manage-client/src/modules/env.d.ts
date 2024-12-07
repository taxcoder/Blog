declare module '*.vue' {
  import { ComponentOptions } from 'vue';
  const component: ComponentOptions;
  export default component;
}

declare module '@tanxiang/utils';
declare module '@tanxiang/apis';