import request from '@/utils/request'

export function getPermissions() {
  return request({
    url: '/permission/list',
    method: 'post'
  })
}

export function addPermission(data) {
  return request({
    url: '/permission/add',
    method: 'post',
    data
  })
}

export function updatePermission(data) {
  return request({
    url: '/permission/update',
    method: 'post',
    data
  })
}

export function deletePermission(id) {
  return request({
    url: `/permission/delete/${id}`,
    method: 'get'
  })
}
