// @ts-ignore
import { messageInfo } from '@tanxiang/common';

const { messageSuccess } = messageInfo();

const useReload = () => {
  const commonOperation = (text: string, list: any, cancel: Function, request: Function, current: number = 1) => {
    messageSuccess(text);
    if (list !== null && list.length !== 0) {
      list.checkBoxAll = false;
      list.indeterminate = false;
    }
    setTimeout(() => request(current), 750);
    cancel();
  };

  return { commonOperation };
};

export default useReload;