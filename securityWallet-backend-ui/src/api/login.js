import request from '@/utils/request'
import {Decrypt,Encrypt} from '@/utils/aes'

// 登录方法
export function login(username, password, code, uuid, googleCode) {
  const data = {
    username,
    password,
    code,
    uuid,
    googleCode
  }
  return request({
    url: '/login',
    method: 'post',
    data: {
      sign: '',
      data: Encrypt(data)
    }
  })
}

// 获取用户详细信息
export function getInfo() {
  return request({
    url: '/getInfo',
    method: 'post'
  })
}

// 退出方法
export function logout() {
  return request({
    url: '/logout',
    method: 'post'
  })
}

// 获取验证码
// export function getCodeImg() {
//   return request({
//     url: '/captchaImage',
//     method: 'post'
//   })
// }