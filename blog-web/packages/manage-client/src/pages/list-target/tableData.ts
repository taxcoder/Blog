export const columns = [
  {
    title: 'ID',
    dataIndex: 'id',
    width: 60,
    align: 'center',
    responsive: ['sm'],
  },
  {
    title: '内容',
    dataIndex: 'content',
    align: 'center',
  },
  {
    title: '年份',
    dataIndex: 'year',
    width: 60,
    align: 'center',
    responsive: ['sm'],
  },
  {
    title: '当前状态',
    dataIndex: 'isSuccess',
    width: 90,
    align: 'center',
    responsive: ['md'],
  },
  {
    title: '操作',
    dataIndex: 'operation',
    align: 'center',
    width: 160,
    fixed: 'right',
    responsive: ['md'],
  },
];

export const innerColumns = [
  {
    title: '当前状态',
    dataIndex: 'isSuccess',
    width: 90,
    align: 'center',
  },
  {
    title: '操作',
    dataIndex: 'operation',
    align: 'center',
    width: 160,
    fixed: 'right',
  },
];