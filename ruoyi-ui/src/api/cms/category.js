import request from '@/utils/request'

// 查询物品分类列表
export function listCategory(query) {
  return request({
    url: '/cms/category/list',
    method: 'get',
    params: query,
  })
}

// 查询物品分类详细
export function getCategory(id) {
  return request({
    url: '/cms/category/' + id,
    method: 'get',
  })
}

// 新增物品分类
export function addCategory(data) {
  return request({
    url: '/cms/category',
    method: 'post',
    data: data,
  })
}

// 修改物品分类
export function updateCategory(data) {
  return request({
    url: '/cms/category',
    method: 'put',
    data: data,
  })
}

// 删除物品分类
export function delCategory(id) {
  return request({
    url: '/cms/category/' + id,
    method: 'delete',
  })
}
