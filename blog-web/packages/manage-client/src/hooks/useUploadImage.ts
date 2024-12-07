import { useUpload } from '@tanxiang/apis';
//@ts-ignore
import { messageInfo } from '@tanxiang/common';

const { messageError, messageLoading, messageSuccess } = messageInfo();

const upload: any = useUpload();
//@ts-ignore
const useUploadImage = (edit: ref<null>): any => {
  const uploadImageArticle = async (files: any, callback: Function) => {
    messageLoading('图片正在上传中！');
    await uploadImage('uploadArticleImage', files, callback);
  };

  const uploadImageEssay = async (files: any, callback: Function) => {
    messageLoading('图片正在上传中！');
    await uploadImage('uploadEssayImage', files, callback);
  };

  const uploadImage = async (name: string, files: any, callback: Function) => {
    const image = (resolve: any, reject: any, file: any): void => {
      upload[name](file)
        .then((success: any) => {
          insertInputTxt('\r');
          messageSuccess('上传成功！');
          resolve(success.data);
        })
        .catch((error: any) => {
          messageError(!error || error.name ? '网络错误！' : error);
          reject(error);
        });
    };

    const res = await Promise.all(
      files.map((file: any) => new Promise((resolve, reject) => image(resolve, reject, file)))
    );

    callback(res.map((item: any) => item));
  };

  const insertInputTxt = (selectedText: string) => {
    edit.value?.insert(() => ({
      targetValue: `${selectedText}`,
      select: false,
      deviationStart: 0,
      deviationEnd: 0,
    }));
  };

  return { uploadImageArticle, uploadImageEssay };
};

export default useUploadImage;