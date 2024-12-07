export const columns = [
  {
    title: 'ID',
    dataIndex: 'id',
    align: 'center',
    width: 70,
    responsive: ['sm'],
  },
  {
    title: '分类名',
    dataIndex: 'name',
    align: 'center',
    ellipsis: true,
  },
  {
    title: '文章数',
    dataIndex: 'articleCount',
    align: 'center',
    width: 60,
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    align: 'center',
    responsive: ['sm'],
  },
  {
    title: '更新时间',
    dataIndex: 'updateTime',
    align: 'center',
    responsive: ['md'],
  },
  {
    title: '操作',
    dataIndex: 'operation',
    align: 'center',
    width: 140,
    fixed: 'right',
  },
];
