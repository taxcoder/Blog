/*
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2025-01-04 15:37:24
 * @LastEditTime: 2025-01-15 02:38:28
 * @FilePath: /code/blog/client-web/src/mock/tag.ts
 * @Description:
 * Copyright (c) 2025 by tanxiang, All Rights Reserved.
 */
import { Tag } from '@/api/tags/tag.d';
import { articles } from './article';

const tags: Array<Tag> = [];

const frontendBackendTerms = [
  'React',
  'Vue',
  'Angular',
  'JavaScript',
  'TypeScript',
  'Node.js',
  'Express',
  'Django',
  'Flask',
  'Spring',
  'Hibernate',
  'MySQL',
  'PostgreSQL',
  'MongoDB',
  'Redis',
  'Docker',
  'Kubernetes',
  'AWS',
  'Azure',
  'GCP',
  'RESTful',
  'GraphQL',
  'WebSocket',
  'JWT',
  'OAuth',
  'CI/CD',
  'Jenkins',
  'Git',
  'GitHub',
  'GitLab',
  'Webpack',
  'Babel',
  'ESLint',
  'Prettier',
  'Sass',
  'Less',
  'Tailwind CSS',
  'Bootstrap',
  'jQuery',
  'Axios',
  'Vuex',
  'Redux',
  'MobX',
  'Nginx',
  'Apache',
  'Vite',
  'Parcel',
  'Next.js',
  'Nuxt.js',
  'Svelte'
];

for (let i = 1; i <= frontendBackendTerms.length; i++) {
  const randomIndex = Math.floor(Math.random() * frontendBackendTerms.length);
  tags.push({
    id: i,
    name: frontendBackendTerms[randomIndex],
    count: Math.floor(Math.random() * 10) + 1 // 随机生成1到10之间的文章数量
  });
}

export const tag = (mock: any) => {
  mock.onGet('/tags').reply(200, {
    data: tags
  });

  for (let i = 1; i <= frontendBackendTerms.length; i++) {
    mock.onGet(`/tags/${i}?index=1&limit=10`).reply(200, {
      data: {
        id: i,
        name: frontendBackendTerms[i - 1],
        allCount: articles.list.filter((element) => element.tags.find((tag) => tag.id === i)).length,
        list: articles.list.filter((element) => element.tags.find((tag) => tag.id === i)).slice(0, 10)
      }
    });
  }
};
