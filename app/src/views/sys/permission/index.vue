<template>
  <div class="app-container">
    <div class="filter-container">
      <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-circle-plus-outline" @click="handleCreate">新增</el-button>
      <el-button :loading="downloadLoading" class="filter-item" type="primary" icon="el-icon-download" @click="handleDownload">导出</el-button>
    </div>

    <el-table v-loading="tableLoading" :data="tableData" row-key="code" border fit highlight-current-row style="width: 100%;" default-expand-all :tree-props="{children: 'children', hasChildren: 'hasChildren'}">
      <el-table-column label="名称" align="left" header-align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column label="编码" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.code }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.createTime | parseTime }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Actions" align="center" width="230" class-name="small-padding fixed-width">
        <template slot-scope="{row}">
          <el-button type="primary" icon="el-icon-edit" size="mini" @click="handleUpdate(row)">修改</el-button>
          <el-button size="mini" type="danger" icon="el-icon-delete" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="dataEntity" label-position="left" label-width="100px" style="width: 400px; margin-left:50px;">
        <el-form-item label="名称" prop="name">
          <el-input v-model="dataEntity.name" placeholder="名称" />
        </el-form-item>
        <el-form-item label="权限编码" prop="code">
          <el-input v-model="dataEntity.code" placeholder="权限编码" />
        </el-form-item>
        <el-form-item label="父级菜单" prop="parentId">
          <el-cascader v-model="parentSelect" :options="parentSelectData" :props="{ checkStrictly: true, label:'name', value:'id' }" @change="handleParentIdChange">
            <template slot-scope="{ node, data }">
              <span>{{ data.name }}</span>
              <span v-if="!node.isLeaf"> ({{ data.children.length }}) </span>
            </template>
          </el-cascader>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="dialogStatus==='create'?createData():updateData()">保存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getPermissions, addPermission, updatePermission, deletePermission } from '@/api/sys/permission'
import { parseTime } from '@/utils'

let _this = null

export default {
  filters: {},
  data() {
    return {
      parentSelectData: [],
      parentSelect: [],
      tableData: [],
      tableLoading: false,
      dataEntity: {
        id: undefined,
        name: '',
        code: '',
        parentId: undefined
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '修改',
        create: '新增'
      },
      rules: {
        name: [
          { required: true, message: '名称不能为空', trigger: 'change' },
          { pattern: /^\S{3,32}$/, message: '名称字符长度大小在3-32之间', trigger: 'blur' }
        ],
        code: [
          { required: true, message: '权限编码不能为空', trigger: 'change' },
          { pattern: /^\S{3,64}$/, message: '权限编码字符长度大小在3-64之间', trigger: 'blur' }
        ]
      },
      downloadLoading: false
    }
  },
  created() {
    _this = this
    _this.initPage()
  },
  methods: {
    // 初始化页面
    initPage() {
      getPermissions().then(response => {
        _this.tableData = response.data
        _this.parentSelectData = [{ id: 0, name: '一级菜单' }].concat(response.data)
      })
    },
    // 重置实体
    resetEntity() {
      _this.dataEntity = {
        id: undefined,
        name: '',
        code: '',
        parentId: undefined
      }
    },
    // 父节点选择框
    handleParentIdChange(data) {
      _this.dataEntity.parentId = data[data.length - 1]
    },
    // 弹出框
    openDialog(dialogStatus) {
      _this.parentSelect = []
      _this.dialogStatus = dialogStatus
      _this.dialogFormVisible = true
      _this.$nextTick(() => {
        _this.$refs['dataForm'].clearValidate()
      })
    },
    // 创建按钮
    handleCreate() {
      _this.resetEntity()
      _this.openDialog('create')
    },
    // 创建保存
    createData() {
      _this.$refs['dataForm'].validate(valid => {
        if (valid) {
          addPermission(_this.dataEntity).then(response => {
            _this.initPage()
            _this.dialogFormVisible = false
            _this.$notify({ title: '提示', message: '新增成功', type: 'success', duration: 2000 })
          })
        }
      })
    },
    // 修改按钮
    handleUpdate(row) {
      _this.dataEntity = Object.assign({}, row)
      _this.openDialog('update')
      _this.getRootLocal(row)
    },
    // 修改操作
    updateData() {
      _this.$refs['dataForm'].validate(valid => {
        if (valid) {
          updatePermission(_this.dataEntity).then(response => {
            _this.initPage()
            _this.dialogFormVisible = false
            _this.$notify({ title: '提示', message: '修改成功', type: 'success', duration: 2000 })
          })
        }
      })
    },
    // 删除操作
    handleDelete(row) {
      deletePermission(row.id).then(() => {
        _this.$notify({ title: '提示', message: '删除成功', type: 'success', duration: 2000 })
        const index = _this.tableData.indexOf(row)
        _this.tableData.splice(index, 1)
      })
    },
    // 下载操作
    handleDownload() {
      _this.downloadLoading = true
      import('@/vendor/Export2Excel').then(excel => {
        const tHeader = ['ID', '模块', '资源', '处理器', '编码', '时间']
        const filterVal = ['id', 'module', 'resource', 'handle', 'code', 'createTime']
        const data = _this.formatJson(filterVal, _this.tableData)
        excel.export_json_to_excel({ header: tHeader, data, filename: 'table-list' })
        _this.downloadLoading = false
      })
    },
    // 获取根目录
    getRootLocal(row) {
      if (row.parentId === 0) {
        _this.parentSelect = [0]
      } else {
        _this.recursive(_this.tableData, row.id)
        _this.parentSelect.splice(_this.parentSelect.length - 1, 1)
      }
    },
    recursive(tableData, id) {
      // 遍历数组 查找节点的父节点
      const self = this
      if (tableData) {
        for (const data of tableData) {
          if (id === data.id) { // 找到该节点
            if (data.parentId) { // 继续寻找该节点父节点的父节点，以此类推
              self.recursive(_this.tableData, data.parentId)
            }
            self.parentSelect.push(data.id)
          }
          if (id !== data.id) {
            if (data.children) {
              self.recursive(data.children, id)
            }
          }
        }
      }
    },
    formatJson(filterVal, jsonData) {
      return jsonData.map(v =>
        filterVal.map(j => {
          if (j === 'createTime') {
            return parseTime(v[j])
          } else {
            return v[j]
          }
        })
      )
    }
  }
}
</script>
