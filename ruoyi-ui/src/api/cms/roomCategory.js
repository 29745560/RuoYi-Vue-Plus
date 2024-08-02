import request from '@/utils/request'

// 查询客房类型列表
export function listRoomCategory(query) {
  return request({
    url: '/cms/room/category/list',
    method: 'get',
    params: query,
  })
}

// 查询客房类型详细
export function getRoomCategory(id) {
  return request({
    url: '/cms/room/category/' + id,
    method: 'get',
  })
}

// 新增客房类型
export function addRoomCategory(data) {
  return request({
    url: '/cms/room/category',
    method: 'post',
    data: data,
  })
}

// 修改客房类型
export function updateRoomCategory(data) {
  return request({
    url: '/cms/room/category',
    method: 'put',
    data: data,
  })
}

// 删除客房类型
export function delRoomCategory(id) {
  return request({
    url: '/cms/room/category/' + id,
    method: 'delete',
  })
}
