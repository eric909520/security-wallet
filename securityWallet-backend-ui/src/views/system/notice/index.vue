<template>
  <div class="app-container">
    <!-- 公告消息管理 -->
    <el-form ref="queryForm" :model="queryParams" :inline="true">
      <el-form-item label="标题" prop="title">
        <el-input
          v-model.trim="queryParams.title"
          placeholder="请输入标题"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="启用状态" prop="enable">
        <el-select
          clearable
          v-model="queryParams.enable"
          placeholder="请选择启用状态"
        >
          <el-option
            v-for="(item, index) in this.enabledStateDict"
            :key="index"
            :value="parseInt(item.dictValue)"
            :label="item.dictLabel"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="消息类型" prop="type">
        <el-select
          clearable
          v-model="queryParams.type"
          placeholder="请选择消息类型"
        >
          <el-option
            v-for="(item, index) in this.sysNoticeTypeDict"
            :key="index"
            :value="parseInt(item.dictValue)"
            :label="item.dictLabel"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery"
          >搜索</el-button
        >
        <el-button type="info" icon="el-icon-refresh" @click="resetQuery"
          >重置</el-button
        >
        <el-button type="warning" icon="el-icon-plus" @click="handleAdd"
          >新增</el-button
        >
      </el-form-item>
    </el-form>

    <el-table
      :max-height="680"
      stripe
      border
      v-loading="loading"
      :data="noticeListData"
    >
      <el-table-column label="标题" align="center" prop="title" />
      <el-table-column label="内容" align="center" prop="content" />
      <el-table-column
        label="启用状态"
        align="center"
        prop="enable"
        :formatter="enable"
      />
      <el-table-column
        label="消息类型"
        align="center"
        prop="type"
        :formatter="type"
      />
      <el-table-column label="修改人" align="center" prop="modifiedUser" />
      <el-table-column
        label="修改时间"
        align="center"
        prop="modifiedTime"
        :formatter="modifiedTime"
      />
      <el-table-column label="创建人" align="center" prop="createUser" />
      <el-table-column
        label="创建时间"
        align="center"
        prop="createTime"
        :formatter="createTime"
      />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center">
        <template slot-scope="scope">
          <el-button size="mini" type="success" @click="editBtn(scope.row)"
            >修改</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <el-dialog :title="title" :visible.sync="open" width="500px">
      <el-form ref="form" :rules="rules" :model="form" label-width="98px">
        <el-form-item label="标题" prop="title">
          <el-input
            v-model.trim="form.title"
            placeholder="请输入标题"
            clearable
          />
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input
            autosize
            type="textarea"
            v-model.trim="form.content"
            placeholder="请输入内容"
            clearable
          />
        </el-form-item>
        <el-form-item label="启用状态" prop="enable">
          <el-select
            clearable
            v-model="form.enable"
            placeholder="请选择启用状态"
          >
            <el-option
              v-for="(item, index) in this.enabledStateDict"
              :key="index"
              :value="parseInt(item.dictValue)"
              :label="item.dictLabel"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="消息类型" prop="type">
          <el-select clearable v-model="form.type" placeholder="请选择消息类型">
            <el-option
              v-for="(item, index) in this.sysNoticeTypeDict"
              :key="index"
              :value="parseInt(item.dictValue)"
              :label="item.dictLabel"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            clearable
            v-model.trim="form.remark"
            placeholder="请输入备注"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button :disabled="submitDisabled" type="primary" @click="submitForm"
          >确 定</el-button
        >
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  noticeList,
  noticeAdd,
  noticeQuery,
  noticeEdit,
} from "@/api/system/notice";
export default {
  data() {
    return {
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        title: undefined,
        enable: undefined,
        type: undefined,
      },
      /* 启用状态字典 */
      enabledStateDict: [],
      /* 通知类型字典 */
      sysNoticeTypeDict: [],
      /* 表格数据 */
      noticeListData: [],
      total: 0,
      loading: false,
      /* 弹窗 */
      title: undefined,
      open: false,
      submitDisabled: false,
      form: {},
      rules: {
        title: [{ required: true, message: "标题不能为空", trigger: "blur" }],
        content: [{ required: true, message: "内容不能为空", trigger: "blur" }],
        enable: [
          { required: true, message: "启用状态不能为空", trigger: "blur" },
        ],
        type: [
          { required: true, message: "消息类型不能为空", trigger: "blur" },
        ],
      },
    };
  },

  created() {
    this.getDicts("switch_status").then((res) => {
      this.enabledStateDict = res.data;
    });
    this.getDicts("sys_notice_type").then((res) => {
      this.sysNoticeTypeDict = res.data;
    });

    this.getList();
  },

  methods: {
    getList() {
      this.loading = true;
      noticeList(this.queryParams).then((res) => {
        this.noticeListData = res.rows;
        this.total = res.total;
        this.loading = false;
      });
    },
    /* 新增 */
    handleAdd() {
      this.form = {};
      this.title = "新增公告消息";
      this.open = true;
      this.submitDisabled = false;
    },
    /* 修改 */
    editBtn(row) {
      this.form = {};
      noticeQuery(row.id).then((res) => {
        this.form = res.data;
        this.open = true;
        this.title = "修改公告消息";
        this.submitDisabled = false;
      });
    },
    /* 提交 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          this.submitDisabled = true;
          if (this.form.id) {
            noticeEdit(this.form)
              .then((res) => {
                if (res.code === 200) {
                  this.$message.success("修改成功");
                  this.open = false;
                  this.getList();
                } else {
                  this.submitDisabled = false;
                }
              })
              .catch(() => {
                this.submitDisabled = false;
              });
          } else {
            noticeAdd(this.form)
              .then((res) => {
                if (res.code === 200) {
                  this.$message.success("新增成功");
                  this.open = false;
                  this.getList();
                } else {
                  this.submitDisabled = false;
                }
              })
              .catch(() => {
                this.submitDisabled = false;
              });
          }
        }
      });
    },
    /* 取消 */
    cancel() {
      this.open = false;
    },
    /* 搜索 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /* 重置 */
    resetQuery() {
      this.queryParams = {
        pageNum: 1,
        pageSize: 10,
        title: undefined,
        enable: undefined,
        type: undefined,
      };
    },

    /* 字典回显 */
    enable(row) {
      return this.selectDictLabel(this.enabledStateDict, row.enable);
    },
    type(row) {
      return this.selectDictLabel(this.sysNoticeTypeDict, row.type);
    },
    modifiedTime(row) {
      return this.parseTime(row.modifiedTime);
    },
    createTime(row) {
      return this.parseTime(row.createTime);
    },
  },
};
</script>

<style>
</style>