import request from '@/utils/request'
import { Decrypt, Encrypt } from '@/utils/aes'

// 获取分享页列表
export const getSharelist = (query) => {
    return request({
        url: '/system/tshareimg/list',
        method: 'post',
        data: {
            data: Encrypt(query)
        }
    })
}

// 新增分享页
export const addShare = (data) => {
    return request({
        url: '/system/tshareimg/add',
        method: 'post',
        data: {
            data: Encrypt(data)
        }
    })
}

// 修改分享页
export const editShare = (data) => {
    return request({
        url: '/system/tshareimg/edit',
        method: 'post',
        data: {
            data: Encrypt(data)
        }
    })
}

// 分享页详情
export const getDetails = (id) => {
    return request({
        url: '/system/tshareimg/query/' + id,
        method: 'post',
    })
}