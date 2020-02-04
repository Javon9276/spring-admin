import request from '@/utils/request'

export function getRoleSelects() {
  return request({
    url: '/role/select',
    method: 'get'
  })
}

export function getRoles(data) {
  return request({
    url: '/role/list',
    method: 'post',
    data
  })
}

export function addRole(data) {
  return request({
    url: '/role/add',
    method: 'post',
    data
  })
}

export function updateRole(data) {
  return request({
    url: '/role/update',
    method: 'post',
    data
  })
}

export function deleteRole(id) {
  return request({
    url: `/role/delete/${id}`,
    method: 'get'
  })
}
