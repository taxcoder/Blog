export const columns = [
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
    title: '创建时间',
    dataIndex: 'createTime',
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
