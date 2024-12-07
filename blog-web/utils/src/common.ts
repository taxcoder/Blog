const useCommon = () => {
  const isTrueOrFlag = (flag: boolean, success: Function, failure: Function) => {
    return flag ? success() : failure();
  };

  const parseJwtToken = (token: string) => {
    //data.data.normal_login_token为发送Ajax获取到的token信息
    let strings = token.split('.'); //通过split()方法将token转为字符串数组
    //取strings[1]数组中的第二个字符进行解析
    let userInfo = JSON.parse(
      decodeURIComponent(escape(window.atob(strings[1].replace(/-/g, '+').replace(/_/g, '/'))))
    );
    //然后可以拿到解析后的数据，可以console.log()打印下，
    return userInfo.sub;
  };
  return { isTrueOrFlag, parseJwtToken };
};

export default useCommon;