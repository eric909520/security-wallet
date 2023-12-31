import request from '@/utils/request'
import {Decrypt,Encrypt} from '@/utils/aes'

// 查询生成表数据
export function listTable(query) {
  return request({
    url: '/tool/gen/list',
    method: 'post',
    data: {
      data: Encrypt(query)
    }
  })
}
// 查询db数据库列表
export function listDbTable(query) {
  return request({
    url: '/tool/gen/db/list',
    method: 'post',
    data: {
      data: Encrypt(query)
    }
  })
}

// 查询表详细信息
export function getGenTable(tableId) {
  return request({
    url: '/tool/gen/query/' + tableId,
    method: 'post'
  })
}

// 修改代码生成信息
export function updateGenTable(data) {
  return request({
    url: '/tool/gen/edit',
    method: 'post',
    data: {
      data: Encrypt(data)
    }
  })
}

// 导入表
export function importTable(data) {
  return request({
    url: '/tool/gen/importTable',
    method: 'post',
    data: {
      data: Encrypt(data)
    }
  })
}
// 预览生成代码
export function previewTable(tableId) {
  return request({
    url: '/tool/gen/preview/' + tableId,
    method: 'post'
  })
}
// 删除表数据
export function delTable(tableId) {
  return request({
    url: '/tool/gen/remove/' + tableId,
    method: 'post'
  })
}

