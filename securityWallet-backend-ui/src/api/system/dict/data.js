import request from '@/utils/request'
import {Decrypt,Encrypt} from '@/utils/aes'

// 查询字典数据列表
export function listData(query) {
  return request({
    url: '/system/dict/data/list',
    method: 'post',
    data: {
      data: Encrypt(query)
    }
  })
}

// 查询字典数据详细
export function getData(dictCode) {
  return request({
    url: '/system/dict/data/' + dictCode,
    method: 'post'
  })
}

// 根据字典类型查询字典数据信息
export function getDicts(dictType) {
  return request({
    url: '/system/dict/data/dictType/' + dictType,
    method: 'post'
  })
}

// 新增字典数据
export function addData(data) {
  return request({
    url: '/system/dict/data/add',
    method: 'post',
    data: {
      data: Encrypt(data)
    }
  })
}

// 修改字典数据
export function updateData(data) {
  return request({
    url: '/system/dict/data/edit',
    method: 'post',
    data: {
      data: Encrypt(data)
    }
  })
}

// 删除字典数据
export function delData(dictCode) {
  return request({
    url: '/system/dict/data/remove/' + dictCode,
    method: 'post'
  })
}

// 导出字典数据
export function exportData(query) {
  return request({
    url: '/system/dict/data/export',
    method: 'post',
    data: {
      data: Encrypt(query)
    }
  })
}