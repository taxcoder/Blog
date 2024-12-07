export const tabNames: string[] = ['首页', '生活', '文章', '工具'];

export const routerTabs = (flag: boolean = true): any[] => {
  return [
    {
      name: '首页',
      key: 'home',
      iconName: flag ? 'icon-home' : 'icon-home1',
      options: [],
      url: '/list/article',
    },
    {
      name: '生活',
      key: 'life',
      iconName: flag ? 'icon-aircraft' : 'icon-A_69',
      options: [
        { label: '随笔', key: '随笔', url: '/list/essay' },
        { label: '图集', key: '图集', url: '/list/image' },
        { label: '摘抄', key: '摘抄', url: '/list/excerpt' },
      ],
    },
    {
      name: '文章',
      key: 'article',
      iconName: flag ? 'icon-brush' : 'icon-A_3',
      options: [
        { label: '归档', key: '', url: '/list/archived' },
        { label: '标签', key: '标签', url: '/list/tag' },
      ],
    },
    {
      name: '工具',
      key: 'utils',
      iconName: flag ? 'icon-biaoji' : 'icon-A9',
      options: [{ label: '导航', key: '导航', url: '/list/navigation' }],
    },
  ];
};