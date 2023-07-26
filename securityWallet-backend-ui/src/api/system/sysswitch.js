import request from '@/utils/request'
import { Decrypt, Encrypt } from '@/utils/aes'

// 查询系统开关列表
export function listSysswitch(query) {
    return request({
        url: '/system/sysswitch/list',
        method: 'post',
        data: {
            data: Encrypt(query)
        }
    })
}

// 查询系统开关详细
export function postSysswitch(id) {
    return request({
        url: '/system/sysswitch/query/' + id,
        method: 'post'
    })
}

// 新增系统开关
export function addSysswitch(data) {
    return request({
        url: '/system/sysswitch/add',
        method: 'post',
        data: {
            data: Encrypt(data)
        }
    })
}

// 修改系统开关
export function updateSysswitch(data) {
    return request({
        url: '/system/sysswitch/edit',
        method: 'post',
        data: {
            data: Encrypt(data)
        }
    })
}

// 删除系统开关
export function delSysswitch(id) {
    return request({
        url: '/system/sysswitch/' + id,
        method: 'delete'
    })
}

// 导出系统开关
export function exportSysswitch(query) {
    return request({
        url: '/system/sysswitch/export',
        method: 'post',
        data: {
            data: Encrypt(query)
        }
    })
}
