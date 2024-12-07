import { message } from 'ant-design-vue';
const time: number = 1.25;

export default () => {
  const messageSuccess = (
    msg: string,
    duration: number = time,
    fn?: Function,
    timeout: number = 0
  ) => loadMessage(msg, duration, timeout, message.success, fn);

  const messageError = (msg: string, duration: number = time, fn?: Function, timeout: number = 0) =>
    loadMessage(msg, duration, timeout, message.error, fn);

  const messageWarning = (
    msg: string,
    duration: number = time,
    fn?: Function,
    timeout: number = 0
  ) => loadMessage(msg, duration, timeout, message.warning, fn);

  const loadMessage = (
    msg: string,
    duration: number,
    timeout: number,
    info: Function,
    fn?: Function
  ) => {
    if (duration > 10) {
      duration = duration / 1000;
    }
    setTimeout(() => info(msg, duration), timeout);
    if (!!fn) {
      setTimeout(fn, timeout + duration * 1000);
    }
  };

  const messageLoading = (msg: string) => {
    message.loading(msg, 0).then(() => {});
  };

  return { messageSuccess, messageError, messageWarning, messageLoading };
};