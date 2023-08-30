import request from '@/utils/request'

// 查询酒店旅客列表
export function listGuest(query) {
  return request({
    url: '/cms/guest/list',
    method: 'get',
    params: query,
  })
}

// 查询酒店旅客详细
export function getGuest(cardId) {
  return request({
    url: '/cms/guest/' + cardId,
    method: 'get',
  })
}

// 新增酒店旅客
export function addGuest(data) {
  return request({
    url: '/cms/guest',
    method: 'post',
    data: data,
  })
}

// 修改酒店旅客
export function updateGuest(data) {
  return request({
    url: '/cms/guest',
    method: 'put',
    data: data,
  })
}
