export const columns = [
  {
    title: 'ID',
    dataIndex: 'id',
    align: 'center',
    width: 60,
    sorter: {
      compare: (a: any, b: any) => a.id - b.id,
      multiple: 3,
    },
    responsive: ['sm'],
  },
  {
    title: '标题',
    dataIndex: 'title',
    align: 'center',
  },
  {
    title: '描述',
    dataIndex: 'description',
    align: 'center',
    responsive: ['xl'],
  },
  {
    title: '网址',
    dataIndex: 'url',
    align: 'center',
    width: 350,
    responsive: ['md'],
  },
  {
    title: '分类',
    dataIndex: 'classificationNavigationName',
    align: 'center',
    width: 200,
    responsive: ['md'],
  },
  {
    title: '操作',
    dataIndex: 'operation',
    align: 'center',
    width: 120,
    fixed: 'right',
  },
];

export const innerColumns = [
  {
    title: '图标',
    dataIndex: 'favicon',
    align: 'center',
    width: 160,
  },
  {
    title: '状态',
    dataIndex: 'ok',
    align: 'center',
    width: 100,
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    align: 'center',
    width: 120,
  },
];