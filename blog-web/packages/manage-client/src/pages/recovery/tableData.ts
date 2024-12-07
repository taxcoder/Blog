export const articleColumns = [
  {
    title: 'ID',
    dataIndex: 'id',
    align: 'center',
    width: 60,
    responsive: ['sm'],
  },
  {
    title: '标题',
    dataIndex: 'name',
    align: 'center',
    ellipsis: true,
  },
  {
    title: '分类名',
    dataIndex: 'classificationName',
    align: 'center',
    width: 75,
    ellipsis: true,
    responsive: ['sm'],
  },
  {
    title: '封面',
    dataIndex: 'image',
    align: 'center',
    width: 120,
    responsive: ['xl'],
  },
  {
    title: '文章存放地址',
    dataIndex: 'contentUrl',
    align: 'center',
    responsive: ['xl'],
  },
  {
    title: '删除时间',
    dataIndex: 'removeTime',
    align: 'center',
    width: 120,
    responsive: ['sm'],
  },
  {
    title: '操作',
    dataIndex: 'operation',
    align: 'center',
    width: 130,
    fixed: 'right',
  },
];

export const essayColumns = [
  {
    title: 'ID',
    dataIndex: 'id',
    align: 'center',
    width: 70,
  },
  {
    title: '内容',
    dataIndex: 'content',
    align: 'center',
    ellipsis: true,
  },
  {
    title: '心情',
    dataIndex: 'mood',
    align: 'center',
    width: 60,
    responsive: ['sm'],
  },
  {
    title: '点赞数',
    dataIndex: 'likeCount',
    align: 'center',
    width: 80,
    responsive: ['sm'],
  },
  {
    title: '删除时间',
    dataIndex: 'removeTime',
    align: 'center',
    width: 140,
    responsive: ['sm'],
  },
  {
    title: '操作',
    dataIndex: 'operation',
    align: 'center',
    width: 140,
    fixed: 'right',
  },
];
