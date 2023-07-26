import request from '@/utils/request'
import {Decrypt,Encrypt} from '@/utils/aes'

// 查询岗位列表
export function listPost(query) {
  return request({
    url: '/system/post/list',
    method: 'post',
    data: {
      data: Encrypt(query)
    }
  })
}

// 查询岗位详细
export function getPost(postId) {
  return request({
    url: '/system/post/query/' + postId,
    method: 'post'
  })
}

// 新增岗位
export function addPost(data) {
  return request({
    url: '/system/post/add',
    method: 'post',
    data: {
      data: Encrypt(data)
    }
  })
}

// 修改岗位
export function updatePost(data) {
  return request({
    url: '/system/post/edit',
    method: 'post',
    data: {
      data: Encrypt(data)
    }
  })
}

// 删除岗位
export function delPost(postId) {
  return request({
    url: '/system/post/remove/' + postId,
    method: 'post'
  })
}

// 导出岗位
export function exportPost(query) {
  return request({
    url: '/system/post/export',
    method: 'post',
    data: {
      data: Encrypt(query)
    }
  })
}