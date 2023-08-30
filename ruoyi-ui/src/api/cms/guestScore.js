import request from '@/utils/request'

// 查询旅客积分列表
export function listGuestScore(query) {
  return request({
    url: '/cms/guest/score/list',
    method: 'get',
    params: query,
  })
}
