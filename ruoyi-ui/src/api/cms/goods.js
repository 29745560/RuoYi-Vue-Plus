import request from '@/utils/request'

// 查询物品列表
export function listGoods(query) {
  return request({
    url: '/cms/goods/list',
    method: 'get',
    params: query,
  })
}

// 查询物品详细
export function getGoods(id) {
  return request({
    url: '/cms/goods/' + id,
    method: 'get',
  })
}

// 新增物品
export function addGoods(data) {
  return request({
    url: '/cms/goods',
    method: 'post',
    data: data,
  })
}

// 修改物品
export function updateGoods(data) {
  return request({
    url: '/cms/goods',
    method: 'put',
    data: data,
  })
}

// 删除物品
export function delGoods(id) {
  return request({
    url: '/cms/goods/' + id,
    method: 'delete',
  })
}

// 添加物品与房间的关联
export function addRoomGoods(roomId, id) {
  return request({
    url: '/cms/goods/room/' + roomId + '/' + id,
    method: 'post',
  })
}

// 删除物品与房间的关联
export function delRoomGoods(roomId, id) {
  return request({
    url: '/cms/goods/room/' + roomId + '/' + id,
    method: 'delete',
  })
}
