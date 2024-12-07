import { createRouter, createWebHistory, Router } from 'vue-router';
import { useBaseStore } from '@/stores/base';
import { routerName } from '@/config/routerMenu';

import AdminLogin from '@/pages/admin-login/AdminLogin.vue';

const NotFound = () => import('@/pages/not-found/NotFound.vue');

const router: Router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/404',
      name: routerName.notFound,
      component: NotFound,
    },
    {
      path: '/login',
      name: routerName.login,
      components: {
        login: AdminLogin,
      },
    },
    {
      path: '/',
      redirect: '/analysis',
    },
    {
      path: '/:pathMatch(.*)',
      redirect: '/404',
    },
  ],
});

router.beforeEach((to, from, next) => {
  // @ts-ignore
  document.title = to.name;
  if (to.path === '/login' && !localStorage.getItem('token')) {
    return next();
  } else if (!localStorage.getItem('token')) {
    return next('login');
  } else if (to.path === '/login' && localStorage.getItem('token')) {
    return next('/');
  }
  let size = 0;
  const base: any = useBaseStore();
  if (from.fullPath === '/404') {
    base.getClickRouterList.forEach((r: any, index: number) => {
      if (r.name === '404') return (size = index);
    });
    base.removeClickRouterList(size);
  }
  next();
});

router.afterEach((to, from) => {
  const base: any = useBaseStore();
  base.setPreviousRoutePath(from.path);

  if (from.path === '/login' || to.path === '/login') return;
  if (to.path === '/refresh' || to.path === '/') return;
  if (to.path === '/404') return base.pushClickRouterList({ name: '404', url: '/404' });

  let listIndex = -1;
  if (
    to.matched.length >= 3 &&
    base.getClickRouterList.filter(
      (r: any, index: number) => r.name === to.matched[to.matched.length - 1].name && (listIndex = index) !== -1
    ).length === 0
  ) {
    return base.pushClickRouterList({ name: to.matched[to.matched.length - 1].name, url: to.path });
  } else if (to.matched.length >= 3 && listIndex !== -1) {
    base.removeClickRouterList(listIndex);
    return base.pushClickRouterList({ name: to.matched[to.matched.length - 1].name, url: to.path });
  }
  base.pushClickRouterList({ name: to.name, url: to.path });
});

export default router;