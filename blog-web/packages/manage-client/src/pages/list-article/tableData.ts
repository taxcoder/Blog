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
    dataIndex: 'name',
    align: 'center',
    ellipsis: true,
    customFilterDropdown: true,
    onFilter: (value: any, record: any) => record.name.toString().toLowerCase().includes(value.toLowerCase()),
    onFilterDropdownOpenChange: (visible: boolean) => {
      if (visible) {
        setTimeout(() => {}, 100);
      }
    },
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
    title: '标签',
    dataIndex: 'tags',
    align: 'center',
    responsive: ['lg'],
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
    title: '操作',
    dataIndex: 'operation',
    align: 'center',
    width: 130,
    fixed: 'right',
  },
];

export const innerColumns = [
  {
    title: '点赞数',
    dataIndex: 'likeCount',
    align: 'center',
  },
  {
    title: '位置',
    dataIndex: 'address',
    align: 'center',
  },
  {
    title: '置顶',
    dataIndex: 'top',
    align: 'center',
  },
  {
    title: '更新时间',
    dataIndex: 'updateTime',
    align: 'center',
  },
];
