<template>
  <div class="app-container">
    <!-- 系统开关配置 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="98px">
      <el-form-item label="key" prop="sKey">
        <el-input
          v-model.trim="queryParams.sKey"
          placeholder="请输入key"
          clearable
          value=" "
          onkeyup="value=value.replace(/[^\w\.\/]/ig,'')"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="启用状态" prop="enable">
        <el-select v-model.trim="queryParams.enable" placeholder="请选择">
          <el-option
            v-for="(item,index) in openStateData"
            :key="index"
            :value="parseInt(item.dictValue)"
            :label="item.dictLabel"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增</el-button>
      </el-col>
    </el-row>

    <el-table border v-loading="loading" :data="sysswitchList">
      <el-table-column label="key" align="center" prop="sKey" />
      <el-table-column label="启用状态" align="center" prop="enable" :formatter="enable" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">修改</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改系统开关对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px">
      <el-form ref="form" :model="form" :rules="rules" @submit.native.prevent label-width="80px">
        <el-form-item label="键" prop="sKey">
          <el-input v-model.trim="form.sKey" placeholder="请输入key" />
        </el-form-item>
        <el-form-item label="启用状态" prop="enable">
          <el-select v-model.trim="form.enable" placeholder="请选择">
            <el-option
              v-for="(item,index) in openStateData"
              :key="index"
              :value="parseInt(item.dictValue)"
              :label="item.dictLabel"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model.trim="form.remark" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button :disabled="submitDisabled" type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listSysswitch,
  postSysswitch,
  updateSysswitch,
  addSysswitch,
} from "@/api/system/sysswitch";

export default {
  data() {
    return {
      submitDisabled: false,
      // 遮罩层
      loading: false,
      // 启用状态字典
      openStateData: [],
      // 总条数
      total: 0,
      // 系统开关表格数据
      sysswitchList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        sKey: undefined,
        enable: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        enable: [
          {
            required: true,
            message: "启用状态不能为空",
            trigger: "blur",
          },
        ],
        sKey: [
          {
            required: true,
            message: "键不能为空",
            trigger: "blur",
          },
        ],
      },
    };
  },
  created() {
    this.getDicts("switch_status").then((res) => {
      this.openStateData = res.data;
    });
    this.getList();
  },
  methods: {
    /** 查询系统开关列表 */
    getList() {
      this.loading = true;
      listSysswitch(this.queryParams).then((response) => {
        this.sysswitchList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: undefined,
        sKey: undefined,
        enable: undefined,
        remark: undefined,
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加系统开关";
      this.submitDisabled = false;
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      postSysswitch(row.id).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改系统开关";
        this.submitDisabled = false;
      });
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          this.submitDisabled = true;
          if (this.form.id != undefined) {
            updateSysswitch(this.form)
              .then((response) => {
                if (response.code === 200) {
                  this.msgSuccess("修改成功");
                  this.open = false;
                  this.getList();
                } else {
                  this.msgError(response.msg);
                }
              })
              .catch(() => {
                this.submitDisabled = false;
              });
          } else {
            addSysswitch(this.form)
              .then((response) => {
                if (response.code === 200) {
                  this.msgSuccess("新增成功");
                  this.open = false;
                  this.getList();
                } else {
                  this.msgError(response.msg);
                }
              })
              .catch(() => {
                this.submitDisabled = false;
              });
          }
        }
      });
    },
    // 格式化
    enable(row, column) {
      return this.selectDictLabel(this.openStateData, row.enable);
    },
  },
};
</script>