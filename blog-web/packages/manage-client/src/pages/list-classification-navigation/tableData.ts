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
  },
  {
    title: '名称',
    dataIndex: 'name',
    align: 'center',
  },
  {
    title: '网站数',
    dataIndex: 'navigationCount',
    align: 'center',
    width: 80,
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    align: 'center',
    width: 160,
    responsive: ['xl'],
  },
  {
    title: '更新时间',
    dataIndex: 'updateTime',
    align: 'center',
    width: 160,
    responsive: ['lg'],
  },
  {
    title: '操作',
    dataIndex: 'operation',
    align: 'center',
    width: 140,
    fixed: 'right',
  },
];