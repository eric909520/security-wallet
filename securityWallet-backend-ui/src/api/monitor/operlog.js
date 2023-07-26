import request from '@/utils/request'
import {Decrypt,Encrypt} from '@/utils/aes'

// 查询操作日志列表
export function list(query) {
  return request({
    url: '/monitor/operlog/list',
    method: 'post',
    data: {
      data: Encrypt(query)
    }
  })
}

// 删除操作日志
export function delOperlog(operId) {
  return request({
    url: '/monitor/operlog/' + operId,
    method: 'delete'
  })
}

// 清空操作日志
export function cleanOperlog() {
  return request({
    url: '/monitor/operlog/clean',
    method: 'delete'
  })
}

// 导出操作日志
export function exportOperlog(query) {
  return request({
    url: '/monitor/operlog/export',
    method: 'post',
    data: {
      data: Encrypt(query)
    }
  })
}