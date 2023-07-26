import request from '@/utils/request'

// oss上传接口
export function getOssConfig() {
  return request({
    url: '/system/alioss/getConfig',
    method: 'post',
  })
}
