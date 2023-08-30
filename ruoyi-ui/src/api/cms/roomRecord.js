import request from '@/utils/request'

// 查询房间入住记录列表
export function listRoomRecord(query) {
  return request({
    url: '/cms/room/record/list',
    method: 'get',
    params: query,
  })
}

// 查询房间入住记录详细
export function getRoomRecord(id) {
  return request({
    url: '/cms/room/record/' + id,
    method: 'get',
  })
}

// 新增房间入住记录
export function addRoomRecord(data) {
  return request({
    url: '/cms/room/record',
    method: 'post',
    data: data,
  })
}

// 修改房间入住记录
export function updateRoomRecord(data) {
  return request({
    url: '/cms/room/record',
    method: 'put',
    data: data,
  })
}

// 删除房间入住记录
export function delRoomRecord(id) {
  return request({
    url: '/cms/room/record/' + id,
    method: 'delete',
  })
}
