import request from '@/utils/request'
import { praseStrEmpty } from "@/utils/ruoyi";
import {Decrypt,Encrypt} from '@/utils/aes'

// 查询用户列表
export function listUser(query) {
  return request({
    url: '/system/user/list',
    method: 'post',
    data: {
      data: Encrypt(query)
    }
  })
}

// 查询用户详细
export function getUser(userId) {
  return request({
    url: '/system/user/query/' + praseStrEmpty(userId),
    method: 'post'
  })
}

// 新增用户
export function addUser(data) {
  return request({
    url: '/system/user/add',
    method: 'post',
    data: {
      data: Encrypt(data)
    }
  })
}

// 修改用户
export function updateUser(data) {
  return request({
    url: '/system/user/edit',
    method: 'post',
    data: {
      data: Encrypt(data)
    }
  })
}

// 删除用户
export function delUser(userId) {
  return request({
    url: '/system/user/remove/' + userId,
    method: 'post'
  })
}

// 导出用户
export function exportUser(query) {
  return request({
    url: '/system/user/export',
    method: 'post',
    data: {
      data: Encrypt(query)
    }
  })
}

// 用户密码重置
export function resetUserPwd(userId, password) {
  const data = {
    userId,
    password
  }
  return request({
    url: '/system/user/resetPwd',
    method: 'post',
    data: {
      data: Encrypt(data)
    }
  })
}

// 用户状态修改
export function changeUserStatus(userId, status) {
  const data = {
    userId,
    status
  }
  return request({
    url: '/system/user/changeStatus',
    method: 'post',
    data: {
      data: Encrypt(data)
    }
  })
}

// 查询用户个人信息
export function getUserProfile() {
  return request({
    url: '/system/user/profile/getProfile',
    method: 'post'
  })
}

// 修改用户个人信息
export function updateUserProfile(data) {
  return request({
    url: '/system/user/profile/edit',
    method: 'post',
    data: {
      data: Encrypt(data)
    }
  })
}

// 用户密码重置
export function updateUserPwd(oldPassword, newPassword) {
  const data = {
    oldPassword,
    newPassword
  }
  return request({
    url: '/system/user/profile/updatePwd',
    method: 'post',
    data: {
      data: Encrypt(data)
    }
  })
}

// 用户头像上传
export function uploadAvatar(data) {
  return request({
    url: '/system/user/profile/avatar',
    method: 'post',
    data: {
      data: Encrypt(data)
    }
  })
}

// 下载用户导入模板
export function importTemplate() {
  return request({
    url: '/system/user/importTemplate',
    method: 'post'
  })
}

