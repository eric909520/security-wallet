<template>
  <div class="app-container">
    <!-- 系统参数配置 -->
    <el-form ref="queryForm" :inline="true" :model="queryParams">
      <el-form-item label="键" prop="cKey">
        <el-input
          v-model.trim="queryParams.cKey"
          clearable
          onkeyup="value=value.replace(/[^\w\.\/]/ig,'')"
          placeholder="请输入键"
          value=" "
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="值" prop="cValue">
        <el-input
          v-model.trim="queryParams.cValue"
          clearable
          onkeyup="value=value.replace(/[^\w\.\/]/ig,'')"
          placeholder="请输入值"
          value=" "
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button icon="el-icon-search" type="primary" @click="handleQuery"
          >搜索</el-button
        >
        <el-button type="info" icon="el-icon-refresh" @click="resetQuery"
          >重置</el-button
        >
        <el-button icon="el-icon-plus" type="warning" @click="handleAdd"
          >新增</el-button
        >
      </el-form-item>
    </el-form>

    <el-tabs v-model.trim="tabName" tabPosition="left" @tab-click="handleClick">
      <el-tab-pane
        v-for="(item, index) in parameterConfigurationTypeData"
        :key="index"
        :label="item.dictLabel"
        :name="item.dictValue"
      />
      <el-table
        :max-height="680"
        stripe
        v-loading="loading"
        :data="appconfigList"
        border
      >
        <el-table-column align="center" label="键" prop="cKey" />
        <el-table-column
          :formatter="cValue"
          align="center"
          label="值"
          prop="cValue"
        />
        <el-table-column
          :formatter="type"
          align="center"
          label="类型"
          prop="type"
        />
        <el-table-column align="center" label="备注" prop="remark" />
        <el-table-column align="center" label="操作">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="success"
              @click="handleUpdate(scope.row)"
              >修改</el-button
            >
          </template>
        </el-table-column>
      </el-table>
    </el-tabs>

    <pagination
      v-show="total > 0"
      :limit.sync="queryParams.pageSize"
      :page.sync="queryParams.pageNum"
      :total="total"
      @pagination="getList"
    />

    <!-- 添加或修改系统配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px">
      <el-form
        ref="form"
        :model="form"
        :rules="rules"
        label-width="118px"
        @submit.native.prevent
      >
        <el-form-item label="键" prop="cKey">
          <el-input
            v-model.trim="form.cKey"
            :disabled="cKeyDisabled"
            placeholder="请输入键"
          />
        </el-form-item>
        <el-form-item label="值" prop="cValue">
          <el-input
            v-if="cValueShow"
            v-model.trim="form.cValue"
            autosize
            placeholder="请输入值"
            type="textarea"
          />
          <el-time-picker
            v-show="false"
            v-model.trim="timeValue"
            placeholder="任意时间点"
            type="datetime"
            value-format="HH:mm"
            @change="timeChange"
          ></el-time-picker>
        </el-form-item>
        <el-form-item label="是否金额型值" prop="whetherAmt">
          <el-select v-model.trim="form.whetherAmt">
            <el-option :value="1" label="是" />
            <el-option :value="0" label="否" />
          </el-select>
        </el-form-item>
        <el-form-item label="类型" prop="type">
          <el-select v-model.trim="form.type" placeholder="请选择类型">
            <el-option
              v-for="(item, index) in parameterConfigurationTypeData"
              :key="index"
              :label="item.dictLabel"
              :value="parseInt(item.dictValue)"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model.trim="form.remark" posinset="请输入备注" />
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
  addAppconfig,
  listAppconfig,
  postAppconfig,
  updateAppconfig,
} from "@/api/system/appconfig";

export default {
  data() {
    return {
      id: undefined,
      cValueShow: false,
      cKeyDisabled: false,
      timeValue: "",
      submitDisabled: false,
      tabName: "3",
      // 遮罩层
      loading: false,
      // 参数配置类型字典
      parameterConfigurationTypeData: [],
      // 总条数
      total: 0,
      // 系统配置表格数据
      appconfigList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        cKey: undefined,
        cValue: undefined,
        type: 3,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        cKey: [{ required: true, message: "类型不能为空", trigger: "blur" }],
        type: [{ required: true, message: "类型不能为空", trigger: "blur" }],
        whetherAmt: [
          { required: true, message: "是否金额型值不能为空", trigger: "blur" },
        ],
        cValue: [{ required: true, message: "值不能为空", trigger: "blur" }],
      },
    };
  },
  created() {
    this.getDicts("app_config_type").then((res) => {
      this.parameterConfigurationTypeData = res.data;
    });
    this.getList();
  },
  methods: {
    handleClick(envet) {
      this.tabName = envet.name;
      this.queryParams.type = parseInt(this.tabName);
      this.getList();
    },
    /** 查询系统配置列表 */
    getList() {
      this.loading = true;
      listAppconfig(this.queryParams).then((response) => {
        this.appconfigList = response.rows;
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
        cKey: undefined,
        cValue: undefined,
        type: undefined,
        remark: undefined,
        whetherAmt: undefined,
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
      this.queryParams = {
        pageNum: 1,
        pageSize: 10,
        cKey: undefined,
        cValue: undefined,
        type: 1,
      };
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加系统配置";
      this.submitDisabled = false;
      this.cValueShow = true;
      this.cKeyDisabled = false;
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      this.id = row.id;
      if (row.id == 3 || row.id == 4) {
        this.cValueShow = true;
        this.timeValue = row.cValue;
      } else {
        this.cValueShow = true;
      }
      postAppconfig(row.id).then((response) => {
        this.form = response.data;
        if (row.whetherAmt === 1) {
          this.form.cValue = response.data.cValue / 100;
        } else {
          this.form.cValue = response.data.cValue;
        }
        this.open = true;
        this.title = "修改系统配置";
        this.submitDisabled = false;
        this.cKeyDisabled = true;
      });
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          this.submitDisabled = true;
          if (this.form.id != undefined) {
            updateAppconfig(this.form)
              .then((response) => {
                if (response.code === 200) {
                  this.msgSuccess("修改成功");
                  this.open = false;
                  this.rules.cValue = [
                    { required: true, message: "值不能为空", trigger: "blur" },
                  ];
                  this.getList();
                } else {
                  this.msgError(response.msg);
                }
              })
              .catch(() => {
                this.submitDisabled = false;
              });
          } else {
            addAppconfig(this.form)
              .then((response) => {
                if (response.code === 200) {
                  this.msgSuccess("新增成功");
                  this.open = false;
                  this.rules.cValue = [
                    { required: true, message: "值不能为空", trigger: "blur" },
                  ];
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
    timeChange(value) {
      this.form.cValue = value;
    },
    // 格式化
    type(row, column) {
      return this.selectDictLabel(
        this.parameterConfigurationTypeData,
        row.type
      );
    },
    cValue(row, column) {
      if (row.whetherAmt === 1) {
        return this.formatBalance(row.cValue);
      }
      return row.cValue;
    },
  },
};
</script>
