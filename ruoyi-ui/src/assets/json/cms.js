export const CmsOptions = {
  category: {
    status: {
      filter: {'0': 'success', '1': 'danger'},
      formatter: [{value: '0', label: '正常'}, {value: '1', label: '停用'}],
    },
  },
  room: {
    status: {
      filter: {
        '0': 'success',
        '1': 'warning',
        '2': '',
        '9': 'danger',
      },
      formatter: [
        {value: '0', label: '空闲中'},
        {value: '1', label: '整理中'},
        {value: '2', label: '入住中'},
        {value: '9', label: '已关闭'},
      ],
    },
  },
  guest: {
    status: {
      filter: {'0': 'success', '1': 'danger'},
      formatter: [{value: '0', label: '正常'}, {value: '1', label: '停用'}],
    },
    score: {
      status: {
        filter: {'0': 'success', '1': 'danger'},
        formatter: [{value: '0', label: '正常'}, {value: '1', label: '停用'}],
      },
    },
  },
  goods: {
    status: {
      filter: {'0': '', '1': 'warning'},
      formatter: [
        {value: '0', label: '换洗中'},
        {value: '1', label: '使用中'},
      ],
    },
    records: {
      status: {
        filter: {'0': 'success', '1': 'warning'},
        formatter: [
          {value: '0', label: '期初'},
          {value: '1', label: '换洗'},
        ],
      },
    },
  },
  user: {
    status: {
      filter: {'0': 'success', '1': 'danger'},
      formatter: [{value: '0', label: '正常'}, {value: '1', label: '停用'}],
    },
    source: {
      filter: {'0': 'info', '1': '', '2': 'success'},
      formatter: [
        {value: '0', label: '未知'},
        {value: '1', label: '手机'},
        {value: '2', label: '小程序'},
      ],
    },
  },
}



