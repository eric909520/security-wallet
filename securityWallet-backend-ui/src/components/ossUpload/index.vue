<template>
  <div class="content">
    <el-upload
      action
      :http-request="Upload"
      :before-upload="beforeAvatarUpload"
      :on-preview="handlePreview"
      :before-remove="beforeRemove"
      :on-remove="handleRemove"
      :on-success="handleSuccess"
      :on-exceed="handleExceed"
      :multiple="multiple"
      :file-list="fileList"
    >
      <el-button icon="el-icon-upload" size="small" type="primary">点击上传</el-button>
    </el-upload>

    <el-progress
      v-show="showProgress"
      :text-inside="true"
      :stroke-width="15"
      :percentage="progress"
    ></el-progress>
  </div>
</template>
<script type="text/ecmascript-6">
import { getOssConfig, getFileNameUUID } from "@/api/oss";
import ossClient from "@/utils/aliyun.oss.client";

export default {
  name: "oss-upload",
  props: {
    multiple: {
      type: Boolean,
      default: false,
    },
    list: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      picList: [],
      picOne: "",

      //upload
      fileList: [],
      showProgress: false, //进度条的显示
      uploadConf: {}, //存签名信息
      progress: 0, //进度条数据
    };
  },
  methods: {
    // 文件超出个数限制时的钩子
    handleExceed(files, fileList) {
      // this.$message.warning(`每次只能上传 ${this.limit} 个文件`);
    },
    // 点击文件列表中已上传的文件时的钩子
    handlePreview(file) {},
    // 删除文件之前的钩子
    beforeRemove(file, fileList) {
      return this.$confirm(`确定移除 ${file.name}？`);
    },
    // 文件列表移除文件时的钩子
    handleRemove(file, fileList) {},
    // 文件上传成功时的钩子
    handleSuccess(response, file, fileList) {
      this.fileList = fileList;
      // //console.log(fileList)
    },
    //文件上传前的校验
    beforeAvatarUpload(file) {
      const isLt100M =
        file.size / 1024 / 1024 > 10 && file.size / 1024 / 1024 < 1024;
      const isLt30 = file.name.length < 30;
      // if (["video/mp4"].indexOf(file.type) == -1) {
      //   this.$message.error("请上传正确的视频格式");
      //   return false;
      // }
      // if (!isLt100M) {
      //   this.$message.error("上传视频大小要在10MB~1GB之间哦!");
      //   return false;
      // }
      // if (!isLt30) {
      //   this.$message.error("上传视频文件名称长度必须要小于30个文字哦!");
      //   return false;
      // }
      // 请求后台接口拿配置参数
      return new Promise(async (resolve, reject) => {
        await getOssConfig()
          .then((res) => {
            if (res.code === 200) {
              const date = new Date();

              let y = date.getFullYear();
              let m = date.getMonth() + 1;
              m = m < 10 ? "0" + m : m;
              let dateDir =
                "image/" + y.toString() + m.toString() + "/backend/";

              const {
                accessKeyId,
                bucketName,
                endpoint,
                accessKeySecret,
              } = res.data;
              const aliyun = {
                accessKeyId: accessKeyId,
                accessKeySecret: accessKeySecret,
                bucket: bucketName,
                endpoint: endpoint,
                dir: dateDir,
              };

              this.uploadConf = aliyun;
              resolve(true);
            } else {
              this.msgError("初始化上传失败");
              reject(false);
            }
          })
          .catch((err) => {
            // //console.log(err);
            reject(false);
          });
      });
    },
    // http-request属性来覆盖默认的上传行为（即action="url"），自定义上传的实现
    Upload(file) {
      const that = this;
      let temporary = file.file.name.lastIndexOf(".");
      let fileNameLength = file.file.name.length;
      let fileFormat = file.file.name.substring(temporary + 1, fileNameLength);

      function rx() {
        return (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1);
      }

      let fileUUID = `${+new Date()}_${rx()}${rx()}`;

      let fileName = fileUUID + "." + fileFormat;
      ossClient(that.uploadConf)
        .multipartUpload(that.uploadConf.dir + `${fileName}`, file.file, {
          progress: function (p) {
            //p进度条的值
            that.showProgress = true;
            that.progress = Math.floor(p * 100);
          },
        })
        .then((result) => {
          if (result.res.status === 200) {
            that.$emit(
              "on-upload",
              "https://" +
                that.uploadConf.bucket +
                "." +
                that.uploadConf.endpoint +
                "/" +
                result.name
            );
            that.fileList = [];
            that.showProgress = 0;
          }
        })
        .catch((err) => {
          //console.log("err:", err);
        });
    },
  },
  created() {},
};
</script>
