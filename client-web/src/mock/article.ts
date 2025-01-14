/*
 * @Author: taxcoder 1571922819@qq.com
 * @LastEditors: taxcoder 1571922819@qq.com
 * @Date: 2025-01-04 15:35:14
 * @LastEditTime: 2025-01-11 09:57:18
 * @FilePath: /code/blog/client-web/src/mock/article.ts
 * @Description:
 * Copyright (c) 2025 by tanxiang, All Rights Reserved.
 */
import { content } from './content';
import dateUtils from '@/utils/dateUtils';
import { ArticleInfo } from '@/api/articles/article.d';

export let articles: ArticleInfo = {
  allCount: 50,
  list: []
};

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

for (let i = 1; i <= articles.allCount; i++) {
  articles.list.push({
    id: i,
    title: `文章${i}`,
    description:
      '这是文章1的内容这是文章1的内容这是文章1的内容这是文章1的内容这是文章1的内容这是文章1的内容这是文章1的内容这是文章1的内容这是文章1的内容这是文章1的内容这是文章1的内容这是文章1的内容这是文章1的内容这是文章1的内容这是文章1的内容这是文章1的内容这是文章1的内容这是文章1的内容这是文章1的内容这是文章1的内容这是文章1的内容这是文章1的内容这是文章1的内容这是文章1的内容这是文章1的内容这是文章1的内容这是文章1的内容这是文章1的内容这是文章1的内容这是文章1的内容这是文章1的内容这是文章1的内容这是文章1的内容这是文章1的内容这是文章1的内容这是文章1的内容这是文章1的内容这是文章1的内容这是文章1的内容这是文章1的内容这是文章1的内容这是文章1的内容这是文章1的内容这是文章1的内容这是文章1的内容这是文章1的内容这是文章1的内容这是文章1的内容这是文章1的内容这是文章1的内容这是文章1的内容这是文章1的内容',
    category: { id: i % 7, name: '分类1' },
    tags: [
      {
        id: Math.ceil(Math.random() * frontendBackendTerms.length),
        name: frontendBackendTerms[Math.ceil(Math.random() * frontendBackendTerms.length)]
      },
      {
        id: Math.ceil(Math.random() * frontendBackendTerms.length),
        name: frontendBackendTerms[Math.ceil(Math.random() * frontendBackendTerms.length)]
      },
      {
        id: Math.ceil(Math.random() * frontendBackendTerms.length),
        name: frontendBackendTerms[Math.ceil(Math.random() * frontendBackendTerms.length)]
      },
      {
        id: Math.ceil(Math.random() * frontendBackendTerms.length),
        name: frontendBackendTerms[Math.ceil(Math.random() * frontendBackendTerms.length)]
      }
    ],
    content: content,
    likeCount: Math.floor(Math.random() * 1000),
    cover: 'https://picsum.photos/200/300',
    author: 'Shimmer',
    createTime: 1420041600 + Math.floor(Math.random() * (1735920000 - 1420041600)),
    updateTime: 1735539760
  });

  let arr: number[] = [];
  articles.list[i - 1].tags = articles.list[i - 1].tags.filter((item) => {
    if (arr.length === 0 || !arr.includes(item.id)) {
      arr.push(item.id);
      return true;
    } else {
      return false;
    }
  });
}

let list = articles.list
  .map((item) => dateUtils.format(item.createTime, 'YYYY'))
  .filter((value, index, array) => array.indexOf(value) === index)
  .sort((a: string, b: string) => parseInt(b) - parseInt(a));

export const article = (mock: any) => {
  // mock模拟一个get方法的响应数据
  for (let i = 1; i <= articles.allCount / 10; i++) {
    mock.onGet(`/api/articles?index=${i}&limit=10`).reply(200, {
      data: {
        allCount: articles.allCount,
        list: articles.list.slice((i - 1) * 10, i * 10)
      }
    });
  }

  for (let i = 0; i < list.length; i++) {
    mock.onGet(`/api/articles/createTime/${list[i]}`).reply(200, {
      data: articles.list
        .filter((item: any) => dateUtils.format(item.createTime, 'YYYY') == list[i])
        .sort((a, b) => b.createTime - a.createTime)
    });
  }

  mock.onGet('/api/articles/years').reply(200, {
    data: list
  });

  for (let i = 1; i < articles.list.length; i++) {
    // mock.onGet(`/api/article/${i}`).reply(500, {
    //   message: '文章不存在！'
    // });
    mock.onGet(`/api/article/${i}`).reply(200, {
      data: articles.list.find((item: any) => item.id == i)
    });
  }

  mock.onPost('/api/article/1/like').reply(200, {
    data: '点赞成功！'
  });
  mock.onPost('/api/article/2/like').reply(200, {
    data: '点赞成功！'
  });
  mock.onPost('/api/article/3/like').reply(200, {
    data: '点赞成功！'
  });
  mock.onPost('/api/article/4/like').reply(200, {
    data: '点赞成功！'
  });
  mock.onPost('/api/article/5/like').reply(200, {
    data: '点赞成功！'
  });
  mock.onPost('/api/article/6/like').reply(200, {
    data: '点赞成功！'
  });
  mock.onPost('/api/article/7/like').reply(200, {
    data: '点赞成功！'
  });
};
