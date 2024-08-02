import request from '@/utils/request'

// 查询物品分类列表
export function listGoodsCategory(query) {
  return request({
    url: '/cms/goods/category/list',
    method: 'get',
    params: query,
  })
}

// 查询物品分类详细
export function getGoodsCategory(id) {
  return request({
    url: '/cms/goods/category/' + id,
    method: 'get',
  })
}

// 新增物品分类
export function addGoodsCategory(data) {
  return request({
    url: '/cms/goods/category',
    method: 'post',
    data: data,
  })
}

// 修改物品分类
export function updateGoodsCategory(data) {
  return request({
    url: '/cms/goods/category',
    method: 'put',
    data: data,
  })
}

// 删除物品分类
export function delGoodsCategory(id) {
  return request({
    url: '/cms/goods/category/' + id,
    method: 'delete',
  })
}
