import request from '@/utils/request'
import {Decrypt,Encrypt} from '@/utils/aes'

// 查询字典类型列表
export function listType(query) {
  return request({
    url: '/system/dict/type/list',
    method: 'post',
    data: {
      data: Encrypt(query)
    }
  })
}

// 查询字典类型详细
export function getType(dictId) {
  return request({
    url: '/system/dict/type/' + dictId,
    method: 'post'
  })
}

// 新增字典类型
export function addType(data) {
  return request({
    url: '/system/dict/type/add',
    method: 'post',
    data: {
      data: Encrypt(data)
    }
  })
}

// 修改字典类型
export function updateType(data) {
  return request({
    url: '/system/dict/type/edit',
    method: 'post',
    data: {
      data: Encrypt(data)
    }
  })
}

// 删除字典类型
export function delType(dictId) {
  return request({
    url: '/system/dict/type/remove/' + dictId,
    method: 'post'
  })
}

// 导出字典类型
export function exportType(query) {
  return request({
    url: '/system/dict/type/export',
    method: 'post',
    data: {
      data: Encrypt(query)
    }
  })
}

// 获取字典选择框列表
export function optionselect() {
  return request({
    url: '/system/dict/type/optionselect',
    method: 'post'
  })
}