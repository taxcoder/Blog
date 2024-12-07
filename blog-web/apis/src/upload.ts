// @ts-ignore
import { service } from './http';

export const useUpload = (base: string = 'auth') => {
  const uploadHead = async (file: any) => {
    const form = new FormData();
    form.append('file', file);
    return service.post(`/${base}/upload/head`, form, {
      headers: { 'Content-Type': 'multipart/form-data' },
    });
  };

  const uploadBackground = async (file: any) => {
    const form = new FormData();
    form.append('file', file);
    return service.post(`/${base}/upload/background`, form, {
      headers: { 'Content-Type': 'multipart/form-data' },
    });
  };

  const uploadArticleImage = async (file: any) => {
    const form = new FormData();
    form.append('file', file);
    return await service.post(`/${base}/upload/image/article`, form, {
      headers: { 'Content-Type': 'multipart/form-data' },
    });
  };

  const uploadEssayImage = async (file: any) => {
    const form = new FormData();
    form.append('file', file);
    return await service.post(`/${base}/upload/image/essay`, form, {
      headers: { 'Content-Type': 'multipart/form-data' },
    });
  };

  const uploadEmoji = async (fileList: any[], name: string) => {
    let form = new FormData();
    fileList.forEach((file: any) => {
      form.append('files', file.originFileObj);
      form.append('names', file.name.substring(0, file.name.lastIndexOf('.')));
    });
    form.append('groupName', name);
    return await service.post(`/${base}/upload/image/emoji`, form, {
      headers: { 'Content-Type': 'multipart/form-data' },
    });
  };

  const uploadImageOnce = async (fileList: any, timestamp: number) => {
    const form = new FormData();

    console.log(fileList);
    form.append('file', fileList.originFileObj);
    form.append('timestamp', timestamp.toString());
    return await service.post(`/${base}/images/add/one`, form, {
      headers: { 'Content-Type': 'multipart/form-data' },
    });
  };

  const uploadImageMore = async (fileList: any[], timestamp: number) => {
    let form = new FormData();
    fileList.forEach((file: any) => {
      form.append('files', file.originFileObj);
    });
    form.append('timestamp', timestamp.toString());
    return await service.post(`/${base}/images/add/more`, form, {
      headers: { 'Content-Type': 'multipart/form-data' },
    });
  };

  return {
    uploadHead,
    uploadBackground,
    uploadArticleImage,
    uploadEssayImage,
    uploadEmoji,
    uploadImageOnce,
    uploadImageMore,
  };
};

export const base64toBlob = (base64: string, type: string = 'application/octet-stream') => {
  const str = atob(base64);
  let n = str.length;
  const u8arr = new Uint8Array(n);
  while (n--) {
    u8arr[n] = str.charCodeAt(n);
  }
  return new Blob([u8arr], { type: type });
};

export const file2Base64 = (file: any) => {
  return new Promise(function (resolve, reject) {
    const reader = new FileReader();
    let imgResult: string | ArrayBuffer | null = '';
    reader.readAsDataURL(file);
    reader.onload = () => (imgResult = reader.result);
    reader.onerror = (error) => reject(error);
    reader.onloadend = () => resolve(imgResult);
  });
};