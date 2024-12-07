// @ts-ignore
import { init, count, web, text, useAuthWebStation } from './src/global';
// @ts-ignore
import { useArticle, useAuthArticle } from './src/article';
// @ts-ignore
import { useTag, useAuthTag } from './src/tag';
// @ts-ignore
import { useUpload, base64toBlob, file2Base64 } from './src/upload';
// @ts-ignore
import { useClassification, useAuthClassification } from './src/classification';
// @ts-ignore
import { useEssay, useAuthEssay } from './src/essay';
// @ts-ignore
import { useAuthEmoji } from './src/emoji';
// @ts-ignore
import { useSearch, useAuthSearch } from './src/search';
// @ts-ignore
import { useLogin } from './src/login';
// @ts-ignore
import { useAuthTarget, useTarget } from './src/target';
// @ts-ignores
import { useImages, useAuthImages } from './src/images';
//@ts-ignore
import { useAuthClassificationNav } from './src/classification-nav';
//@ts-ignore
import { useNavigation, useAuthNavigation } from './src/navigation';
//@ts-ignore
import { useAuthExcerpt, useExcerpt } from './src/excerpt';
//@ts-ignore
import { useAuthChats, useChats } from './src/chats';

const salt: string = 'd07974dd747f104cc1743477e6f7f0ec';

export {
  init,
  useTag,
  count,
  text,
  web,
  useAuthTag,
  useArticle,
  useAuthArticle,
  useUpload,
  useClassification,
  useEssay,
  useAuthClassification,
  useAuthEssay,
  useAuthEmoji,
  useAuthSearch,
  useSearch,
  useAuthWebStation,
  useLogin,
  salt,
  base64toBlob,
  file2Base64,
  useAuthTarget,
  useTarget,
  useImages,
  useAuthImages,
  useAuthClassificationNav,
  useAuthNavigation,
  useExcerpt,
  useAuthExcerpt,
  useAuthChats,
  useChats,
  useNavigation,
};