/**
 * 阿里云oss上传工具
 *
 *  @description conf: {
                region: null,
                accessKeyId: null,
                accessKeySecret: null,
                bucket: null,
                stsToken: null
            }
 */
let OSS = require('ali-oss')
export default (conf) => {//data后端提供数据
  return new OSS(conf)
}
