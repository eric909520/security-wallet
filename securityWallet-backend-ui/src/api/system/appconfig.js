import request from '@/utils/request'
import { Decrypt, Encrypt } from '@/utils/aes'

// 查询系统配置列表
export function listAppconfig(query) {
    return request({
        url: '/system/appconfig/list',
        method: 'post',
        data: {
            data: Encrypt(query)
        }
    })
}

// 查询系统配置详细
export function postAppconfig(id) {
    return request({
        url: '/system/appconfig/query/' + id,
        method: 'post'
    })
}

// 新增系统配置
export function addAppconfig(data) {
    return request({
        url: '/system/appconfig/add',
        method: 'post',
        data: {
            data: Encrypt(data)
        }
    })
}

// 修改系统配置
export function updateAppconfig(data) {
    return request({
        url: '/system/appconfig/edit',
        method: 'post',
        data: {
            data: Encrypt(data)
        }
    })
}

// 删除系统配置
export function delAppconfig(id) {
    return request({
        url: '/system/appconfig/' + id,
        method: 'delete'
    })
}

// 导出系统配置
export function exportAppconfig(query) {
    return request({
        url: '/system/appconfig/export',
        method: 'post',
        data: {
            data: Encrypt(query)
        }
    })
}
