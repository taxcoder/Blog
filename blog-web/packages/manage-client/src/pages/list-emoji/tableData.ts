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
    title: '图标',
    dataIndex: 'icon',
    width: 80,
    align: 'center',
  },
  {
    title: '名称',
    dataIndex: 'name',
    align: 'center',
    ellipsis: true,
    width: 150,
  },
  {
    title: '地址',
    dataIndex: 'address',
    align: 'center',
    ellipsis: true,
    responsive: ['sm'],
  },
  {
    title: '分组名',
    dataIndex: 'groupName',
    align: 'center',
    width: 200,
    responsive: ['xl'],
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    align: 'center',
    width: 210,
    responsive: ['sm'],
  },
  {
    title: '操作',
    dataIndex: 'operation',
    align: 'center',
    width: 120,
    fixed: 'right',
  },
];