import request from '@/utils/request'

// 查询微信用户列表
export function listUser(query) {
  return request({
    url: '/cms/user/list',
    method: 'get',
    params: query,
  })
}

// 查询微信用户详细
export function getUser(id) {
  return request({
    url: '/cms/user/' + id,
    method: 'get',
  })
}

// 修改微信用户
export function updateUser(data) {
  return request({
    url: '/cms/user',
    method: 'put',
    data: data,
  })
}

// 删除微信用户
export function delUser(id) {
  return request({
    url: '/cms/user/' + id,
    method: 'delete',
  })
}
