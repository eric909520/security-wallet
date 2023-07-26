import request from '@/utils/request'
import {Decrypt,Encrypt} from '@/utils/aes'

// 查询角色列表
export function listRole(query) {
  return request({
    url: '/system/role/list',
    method: 'post',
    data: {
      data: Encrypt(query)
    }
  })
}

// 查询角色详细
export function getRole(roleId) {
  return request({
    url: '/system/role/query/' + roleId,
    method: 'post'
  })
}

// 新增角色
export function addRole(data) {
  return request({
    url: '/system/role/add',
    method: 'post',
    data: {
      data: Encrypt(data)
    }
  })
}

// 修改角色
export function updateRole(data) {
  return request({
    url: '/system/role/edit',
    method: 'post',
    data: {
      data: Encrypt(data)
    }
  })
}

// 角色数据权限
export function dataScope(data) {
  return request({
    url: '/system/role/dataScope',
    method: 'post',
    data: {
      data: Encrypt(data)
    }
  })
}

// 角色状态修改
export function changeRoleStatus(roleId, status) {
  const data = {
    roleId,
    status
  }
  return request({
    url: '/system/role/changeStatus/edit',
    method: 'post',
    data: {
      data: Encrypt(data)
    }
  })
}

// 删除角色
export function delRole(roleId) {
  return request({
    url: '/system/role/remove/' + roleId,
    method: 'post'
  })
}

// 导出角色
export function exportRole(query) {
  return request({
    url: '/system/role/export',
    method: 'post',
    data: query
  })
}