import request from '@/utils/request'

// 查询房间换洗记录列表
export function listGoodsRecord(query) {
  return request({
    url: '/cms/goods/record/list',
    method: 'get',
    params: query,
  })
}
