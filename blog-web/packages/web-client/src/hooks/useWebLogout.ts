import { useLogin } from '@tanxiang/apis';
import { messageInfo } from '@tanxiang/common';
import { useCommon } from '@tanxiang/utils';

const { messageSuccess, messageError } = messageInfo();

const useWebLogout = () => {
  const logout = (token: string, fn: Function) => {
    useLogin()
      .webLogout(useCommon().parseJwtToken(token))
      .then((success: any) => {
        messageSuccess(success.message);
        fn();
      })
      .catch((error: any) => messageError(!error || error.name ? '网络错误！' : error));
  };

  return { logout };
};

export default useWebLogout;