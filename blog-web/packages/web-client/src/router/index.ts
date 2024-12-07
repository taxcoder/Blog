import { createRouter, createWebHistory } from 'vue-router';
//@ts-ignore
import { routeName } from '@/enum';

import ArticleList from '@/views/article-list/ArticleList.vue';
import { useLogin } from '@tanxiang/apis';
//@ts-ignore
import { useBaseStore } from '@/stores/base';

const ClassificationArticle = () => import('@/views/classification-article/ClassificationArticle.vue');
const TagList = () => import('@/views/tag-list/TagList.vue');
const TagArticle = () => import('@/views/tag-article/TagArticle.vue');
const ArchivedList = () => import('@/views/archived-list/ArchivedList.vue');
const EssayList = () => import('@/views/essay-list/EssayList.vue');
const ArticlePage = () => import('@/views/article-page/ArticlePage.vue');
const ImageList = () => import('@/views/image-list/ImageList.vue');
const ExcerptList = () => import('@/views/excerpt-list/ExcerptList.vue');
const NavigationList = () => import('@/views/navigation-list/NavigationList.vue');

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      name: 'home',
      path: '/',
      redirect: '/list/article',
    },
    {
      name: 'ArticleList',
      path: '/list/article',
      component: ArticleList,
      meta: {
        name: routeName.home,
        position: true,
        showUserInfo: true,
        isDraw: false,
      },
    },
    {
      name: 'ArchivedList',
      path: '/list/archived',
      component: ArchivedList,
      meta: {
        name: routeName.archived,
        position: true,
        showUserInfo: true,
        isDraw: false,
      },
    },
    {
      name: 'TagList',
      path: '/list/tag',
      component: TagList,
      meta: {
        name: routeName.tag,
        position: true,
        showUserInfo: false,
        isDraw: true,
      },
    },
    {
      name: 'TagArticle',
      path: '/list/tag/:id',
      component: TagArticle,
      meta: {
        name: '',
        position: true,
        showUserInfo: true,
        isDraw: false,
      },
    },
    {
      name: 'classificationArticle',
      path: '/list/classification/:id',
      component: ClassificationArticle,
      meta: {
        name: '',
        position: true,
        showUserInfo: true,
        isDraw: false,
      },
    },
    {
      name: 'EssayList',
      path: '/list/essay',
      component: EssayList,
      meta: {
        name: routeName.essay,
        position: true,
        showUserInfo: false,
        isDraw: true,
      },
    },
    {
      name: 'ArticlePage',
      path: '/articles/:id',
      component: ArticlePage,
      meta: {
        position: true,
        showUserInfo: false,
        isDraw: false,
      },
    },
    {
      name: 'ImageList',
      path: '/list/image',
      component: ImageList,
      meta: {
        name: '生活图库',
        position: true,
        showUserInfo: false,
        isDraw: true,
      },
    },
    {
      name: 'ExcerptList',
      path: '/list/excerpt',
      component: ExcerptList,
      meta: {
        name: '摘抄',
        position: true,
        showUserInfo: false,
        isDraw: true,
      },
    },
    {
      name: 'NavigationList',
      path: '/list/navigation',
      component: NavigationList,
      meta: {
        name: '导航',
        position: false,
        isDraw: false,
        showUserInfo: false,
      },
    },
  ],
});

router.beforeEach((to, from, next) => {
  const base = useBaseStore();
  if (localStorage.getItem('token')) {
    useLogin()
      .isLogin()
      .then(() => {})
      .catch(() => {});
  }
  next();
});

export default router;