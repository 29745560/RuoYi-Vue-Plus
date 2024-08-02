export const CmsOptions = {
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
    records: {
      pay: {
        filter: {'0': 'info', '1': 'success'},
        formatter: [
          {value: '0', label: '待支付'},
          {value: '1', label: '已支付'},
        ],
      },
    },
  },
  guest: {
    status: {
      filter: {'0': 'success', '1': 'danger'},
      formatter: [{value: '0', label: '正常'}, {value: '1', label: '停用'}],
    },
    sex: {
      filter: {'0': '', '1': 'danger', '2': 'info'},
      formatter: [
        {value: '0', label: '男'},
        {value: '1', label: '女'},
        {value: '2', label: '未知'}],
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
    type: {
      filter: {'0': '', '1': 'success'},
      formatter: [
        {value: '0', label: '普通'},
        {value: '1', label: '环保'},
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
    category: {
      status: {
        filter: {'0': 'success', '1': 'danger'},
        formatter: [{value: '0', label: '正常'}, {value: '1', label: '停用'}],
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



