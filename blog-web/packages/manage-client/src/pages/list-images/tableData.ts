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
    title: '图片',
    dataIndex: 'url',
    align: 'center',
  },
  {
    title: '所处日期',
    dataIndex: 'uploadTime',
    align: 'center',
    width: 160,
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    align: 'center',
    width: 160,
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