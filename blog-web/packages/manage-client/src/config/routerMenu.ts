import { h, reactive } from 'vue';
import {
  BulbOutlined,
  CoffeeOutlined,
  DatabaseOutlined,
  DeleteOutlined,
  EditOutlined,
  FolderOutlined,
  GoldOutlined,
  ProfileOutlined,
  ReadOutlined,
  SettingOutlined,
  TagsOutlined,
  RadarChartOutlined,
  SmileOutlined,
  TrophyOutlined,
  FileImageOutlined,
  ShareAltOutlined,
  ScissorOutlined,
} from '@ant-design/icons-vue';

const Analysis = () => import('@/pages/analysis/Analysis.vue');
const ArticleAdd = () => import('@/pages/article-edit/ArticleAdd.vue');
const ArticleUpload = () => import('@/pages/article-edit/ArticleUpload.vue');
const EssayAdd = () => import('@/pages/essay-edit/EssayAdd.vue');
const EssayUpload = () => import('@/pages/essay-edit/EssayUpload.vue');
const ListTag = () => import('@/pages/list-tag/ListTag.vue');
const ListClassification = () => import('@/pages/list-classification/ListClassification.vue');
const ListEssay = () => import('@/pages/list-essay/ListEssay.vue');
const ListArticle = () => import('@/pages/list-article/ListArticle.vue');
const RecoveryArticle = () => import('@/pages/recovery/RecoveryArticle.vue');
const RecoveryEssay = () => import('@/pages/recovery/RecoveryEssay.vue');
const BaseSetting = () => import('@/pages/base-setting/BaseSetting.vue');
const Recovery = () => import('@/pages/recovery/Recovery.vue');
const Refresh = () => import('@/pages/refresh/Refresh.vue');
const ListEmoji = () => import('@/pages/list-emoji/ListEmoji.vue');
const ListTarget = () => import('@/pages/list-target/ListTarget.vue');
const ListImages = () => import('@/pages/list-images/ListImages.vue');
const ListClassificationNavigation = () =>
  import('@/pages/list-classification-navigation/ListClassificationNavigation.vue');
const ListNavigation = () => import('@/pages/list-navigation/ListNavigation.vue');
const ListExcerpt = () => import('@/pages/list-excerpt/ListExcerpt.vue');

export enum routerName {
  home = '洞若观火',
  editor = '奇思妙想',
  editorWriteEssay = '直写胸臆',
  editorUpdateEssay = '字斟句酌',
  editorWriteArticle = '文思泉涌',
  editorUpdateArticle = '精益求精',
  list = '分门别类',
  listTags = '标签列表',
  listClassifications = '分类列表',
  listEssays = '随笔列表',
  listArticles = '文章列表',
  articlesRelevantlyList = '文章关联列表',
  essayRelevantlyList = '随笔关联列表',
  recovery = '回收列表',
  recoveryArticle = '回收-文章',
  recoveryEssay = '回收-随笔',
  base = '网站管理',
  baseSetting = '网站设置',
  refresh = '刷新',
  notFound = '404',
  login = '管理员登录',
  emoji = '表情列表',
  target = '目标列表',
  images = '图集列表',
  navigation = '导航列表',
  classificationNavigation = '导航分类',
  navigationRelevantlyList = '导航关联列表',
  excerpt = '摘抄列表',
}

export const routerMenu: any[] = reactive([
  {
    label: routerName.home,
    key: 'analysis',
    icon: () => h(RadarChartOutlined, { style: { fontSize: '16px' } }),
    url: '/analysis',
    component: Analysis,
    children: [],
    keepAlive: true,
  },
  {
    label: routerName.editor,
    key: 'editor',
    icon: () => h(EditOutlined, { style: { fontSize: '16px' } }),
    url: '/editor',
    show: true,
    keepAlive: false,
    redirect: '/editor/article',
    children: [
      {
        label: routerName.editorUpdateEssay,
        key: 'updateEssay',
        icon: () => h(CoffeeOutlined, { style: { fontSize: '16px' } }),
        url: '/essay/:id(\\d+)',
        component: EssayUpload,
        children: [],
        display: false,
        keepAlive: false,
      },
      {
        label: routerName.editorWriteEssay,
        key: 'writeEssay',
        icon: () => h(CoffeeOutlined, { style: { fontSize: '16px' } }),
        url: '/essay',
        component: EssayAdd,
        children: [],
        keepAlive: false,
      },
      {
        label: routerName.editorWriteArticle,
        key: 'writeArticle',
        icon: () => h(BulbOutlined, { style: { fontSize: '16px' } }),
        url: '/article',
        component: ArticleAdd,
        children: [],
        keepAlive: false,
      },
      {
        label: routerName.editorUpdateArticle,
        key: 'updateArticle',
        icon: () => h(BulbOutlined, { style: { fontSize: '16px' } }),
        url: '/article/:id(\\d+)',
        component: ArticleUpload,
        children: [],
        display: false,
        keepAlive: false,
      },
    ],
  },
  {
    label: routerName.list,
    key: 'list',
    icon: () => h(DatabaseOutlined, { style: { fontSize: '16px' } }),
    url: '/list',
    keepAlive: true,
    redirect: '/list/article/articles',
    children: [
      {
        label: routerName.articlesRelevantlyList,
        key: 'articlesRelevantlyList',
        url: '/article',
        redirect: '/list/article/articles',
        icon: () => h(ShareAltOutlined, { style: { fontSize: '16px' } }),
        children: [
          {
            label: routerName.listClassifications,
            key: 'classifications',
            icon: () => h(FolderOutlined, { style: { fontSize: '16px' } }),
            url: '/classifications',
            component: ListClassification,
            children: [],
            keepAlive: true,
          },
          {
            label: routerName.listTags,
            key: 'tags',
            icon: () => h(TagsOutlined, { style: { fontSize: '16px' } }),
            url: '/tags',
            component: ListTag,
            children: [],
            keepAlive: true,
          },
          {
            label: routerName.listArticles,
            key: 'articles',
            icon: () => h(ReadOutlined, { style: { fontSize: '16px' } }),
            url: '/articles',
            component: ListArticle,
            children: [],
            keepAlive: true,
          },
        ],
        keepAlive: true,
      },
      {
        label: routerName.essayRelevantlyList,
        key: 'essayRelevantlyList',
        url: '/essay',
        redirect: '/list/essay/essays',
        icon: () => h(ShareAltOutlined, { style: { fontSize: '16px' } }),
        children: [
          {
            label: routerName.listEssays,
            key: 'essays',
            icon: () => h(ProfileOutlined, { style: { fontSize: '16px' } }),
            url: '/essays',
            component: ListEssay,
            children: [],
            keepAlive: true,
          },
          {
            label: routerName.emoji,
            key: 'emojis',
            icon: () => h(SmileOutlined, { style: { fontSize: '16px' } }),
            url: '/emojis',
            component: ListEmoji,
            children: [],
            keepAlive: true,
          },
        ],
        keepAlive: true,
      },
      {
        label: routerName.navigationRelevantlyList,
        key: 'navigationRelevantlyList',
        icon: () => h(ShareAltOutlined, { style: { fontSize: '16px' } }),
        url: '/navigation',
        children: [
          {
            label: routerName.navigation,
            key: 'navigations',
            icon: () => h(TrophyOutlined, { style: { fontSize: '16px' } }),
            url: '/navigations',
            component: ListNavigation,
            children: [],
            keepAlive: true,
          },
          {
            label: routerName.classificationNavigation,
            key: 'classificationNavigations',
            icon: () => h(TrophyOutlined, { style: { fontSize: '16px' } }),
            url: '/classificationNavigations',
            component: ListClassificationNavigation,
            children: [],
            keepAlive: true,
          },
        ],
        keepAlive: true,
      },
      {
        label: routerName.target,
        key: 'target',
        icon: () => h(TrophyOutlined, { style: { fontSize: '16px' } }),
        url: '/target',
        component: ListTarget,
        children: [],
        keepAlive: true,
      },
      {
        label: routerName.images,
        key: 'images',
        icon: () => h(FileImageOutlined, { style: { fontSize: '16px' } }),
        url: '/images',
        component: ListImages,
        children: [],
        keepAlive: true,
      },
      {
        label: routerName.excerpt,
        key: 'excerpt',
        icon: () => h(ScissorOutlined, { style: { fontSize: '16px' } }),
        url: '/excerpt',
        component: ListExcerpt,
        children: [],
        keepAlive: true,
      },
    ],
  },
  {
    label: routerName.base,
    key: 'base',
    icon: () => () => h(SettingOutlined, { style: { fontSize: '16px' } }),
    url: '/base',
    redirect: '/base/setting',
    children: [
      {
        label: routerName.recovery,
        key: 'recovery',
        icon: () => h(DeleteOutlined, { style: { fontSize: '16px' } }),
        url: '/recovery',
        component: Recovery,
        notShowChildren: true,
        keepAlive: true,
        redirect: '/base/recovery/article',
        children: [
          {
            label: routerName.recoveryEssay,
            key: 'recoveryEssay',
            icon: () => h(TagsOutlined, { style: { fontSize: '16px' } }),
            url: '/essay',
            component: RecoveryEssay,
            children: [],
            display: false,
            keepAlive: true,
          },
          {
            label: routerName.recoveryArticle,
            key: 'recoveryArticle',
            icon: () => h(TagsOutlined, { style: { fontSize: '16px' } }),
            url: '/article',
            component: RecoveryArticle,
            children: [],
            display: false,
            keepAlive: true,
          },
        ],
      },
      {
        label: routerName.baseSetting,
        key: 'setting',
        icon: () => h(GoldOutlined, { style: { fontSize: '16px' } }),
        url: '/setting',
        component: BaseSetting,
        keepAlive: true,
        children: [],
      },
    ],
  },
  {
    label: routerName.refresh,
    key: 'refresh',
    icon: () => () => h(SettingOutlined, { style: { fontSize: '16px' } }),
    url: '/refresh',
    children: [],
    component: Refresh,
    keepAlive: false,
  },
]);

export const initRouter = (router: any) => {
  routerMenu.forEach((menu: any) => {
    let route: any = routeParam(null, null, menu);
    router.addRoute(route);
  });
};

const routeParam = (prefix: string | null, parent: any, menu: any) => {
  return {
    name: menu.label,
    path: prefix ? prefix + menu.url : menu.url,
    component: menu.component,
    redirect: menu.redirect,
    meta: {
      keepAlive: menu.keepAlive,
      key: menu.key,
      editor: parent && parent.show ? parent.show : !!menu.show,
      display: menu.display !== false,
    },
    children: isChildrenNull(prefix ? prefix + menu.url : menu.url, menu),
  };
};

const isChildrenNull = (url: string, menu: any) => {
  if (menu.children && menu.children.length !== 0) {
    return menu.children.map((child: any) => routeParam(url, menu, child));
  }
  return [];
};