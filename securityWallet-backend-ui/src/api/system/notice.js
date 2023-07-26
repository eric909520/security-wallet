import request from '@/utils/request'
import { Decrypt, Encrypt } from '@/utils/aes'

/* 查新列表 */
export function noticeList(query) {
    return request({
        url: '/system/notice/list',
        method: 'post',
        data: {
            data: Encrypt(query)
        }
    })
}

/* 新增公告消息 */
export function noticeAdd(data) {
    return request({
        url: '/system/notice/add',
        method: 'post',
        data: {
            data: Encrypt(data)
        }
    })
}

/* 修改公告消息 */
export function noticeEdit(data) {
    return request({
        url: '/system/notice/edit',
        method: 'post',
        data: {
            data: Encrypt(data)
        }
    })
}

/* 新增公告消息 */
export function noticeQuery(id) {
    return request({
        url: '/system/notice/query/' + id,
        method: 'post',
    })
}