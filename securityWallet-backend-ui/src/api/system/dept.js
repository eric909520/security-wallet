import request from '@/utils/request'
import {Decrypt,Encrypt} from '@/utils/aes'

// 查询部门列表
export function listDept(query) {
  return request({
    url: '/system/dept/list',
    method: 'post',
    data: {
      data:Encrypt(query)
    }
  })
}

// 查询部门详细
export function getDept(deptId) {
  return request({
    url: '/system/dept/query/' + deptId,
    method: 'post'
  })
}

// 查询部门下拉树结构
export function treeselect(data) {
  return request({
    url: '/system/dept/treeselect',
    method: 'post',
    data: {
      data: Encrypt(data)
    }
  })
}

// 根据角色ID查询部门树结构
export function roleDeptTreeselect(roleId) {
  return request({
    url: '/system/dept/roleDeptTreeselect/' + roleId,
    method: 'post'
  })
}

// 新增部门
export function addDept(data) {
  return request({
    url: '/system/dept/add',
    method: 'post',
    data: {
      data:Encrypt(data)
    }
  })
}

// 修改部门
export function updateDept(data) {
  return request({
    url: '/system/dept/edit',
    method: 'post',
    data: {
      data:Encrypt(data)
    }
  })
}

// 删除部门
export function delDept(deptId) {
  return request({
    url: '/system/dept/remove/' + deptId,
    method: 'post'
  })
}