<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="tableQuery.keyword" placeholder="请输入查询内容" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" />
      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">查询</el-button>
      <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-circle-plus-outline" @click="handleCreate">新增</el-button>
      <!-- <el-button :loading="downloadLoading" class="filter-item" type="primary" icon="el-icon-download" @click="handleDownload">导出</el-button> -->
    </div>

    <el-table v-loading="tableLoading" :data="tableData" border fit highlight-current-row style="width: 100%;" @sort-change="sortChange">
      <el-table-column label="ID" prop="id" sortable="custom" align="center" width="80">
        <template slot-scope="scope">
          <span>{{ scope.row.id }}</span>
        </template>
      </el-table-column>
      <el-table-column label="角色编码" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.code }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.description }}</span>
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
          <el-button v-if="row.status!='deleted'" size="mini" type="danger" icon="el-icon-delete" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="tableTotal>0" :total="tableTotal" :page.sync="tableQuery.page" :size.sync="tableQuery.size" @pagination="initPage" />

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="dataEntity" label-position="left" label-width="100px" style="width: 400px; margin-left:50px;">
        <el-form-item label="角色编码" prop="code">
          <el-input v-model="dataEntity.code" placeholder="角色编码" />
        </el-form-item>
        <el-form-item label="备注" prop="description">
          <el-input v-model="dataEntity.description" placeholder="备注" />
        </el-form-item>
        <el-form-item label="权限信息">
          <el-tree ref="permissionTree" show-checkbox default-expand-all node-key="id" :data="permissionData" :props="{children: 'children', value: 'id', label: 'name'}" class="permission-tree" />
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
import { getRoles, addRole, updateRole, deleteRole } from '@/api/sys/role'
import { getPermissions } from '@/api/sys/permission'
import { parseTime, parseOrder } from '@/utils'
import Pagination from '@/components/Pagination' // secondary package based on el-pagination

let _this = null

export default {
  components: { Pagination },
  filters: {},
  data() {
    return {
      tableData: [],
      tableTotal: 0,
      permissionData: [],
      tableLoading: false,
      tableQuery: {
        page: 1,
        size: 20,
        keyword: undefined,
        sort: 'id',
        order: 'desc'
      },
      dataEntity: {
        id: undefined,
        code: '',
        description: '',
        permissions: []
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '修改',
        create: '新增'
      },
      rules: {
        code: [
          { required: true, message: '模块不能为空', trigger: 'change' },
          { pattern: /^\S{3,32}$/, message: '模块字符串长度大小在3-32之间', trigger: 'blur' }
        ],
        description: [
          { required: true, message: '资源不能为空', trigger: 'change' },
          { pattern: /^\S{3,32}$/, message: '资源字符串长度大小在3-32之间', trigger: 'blur' }
        ]
      },
      downloadLoading: false
    }
  },
  created() {
    _this = this
    _this.initPage()
    _this.initPermission()
  },
  methods: {
    // 初始化页面
    initPage() {
      getRoles(_this.tableQuery).then(res => {
        _this.tableData = res.data.content
        _this.tableTotal = res.data.totalElements
      })
    },
    // 初始化权限选择框
    initPermission() {
      getPermissions().then(res => {
        _this.permissionData = res.data
      })
    },
    // 查询条件过滤
    handleFilter() {
      _this.tableQuery.page = 1
      _this.initPage()
    },
    // 排序
    sortChange(data) {
      const { prop, order } = data
      _this.tableQuery.sort = prop
      _this.tableQuery.order = parseOrder(order)
      _this.initPage()
    },
    // 重置实体
    resetEntity() {
      _this.dataEntity = {
        id: undefined,
        code: '',
        description: '',
        permissions: []
      }
    },
    // 弹出框
    openDialog(dialogStatus) {
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
      setTimeout(function() {
        _this.$refs.permissionTree.setCheckedNodes([])
      }, 500)
    },
    // 创建保存
    createData() {
      _this.dataEntity.permissions = _this.$refs.permissionTree.getCheckedNodes()
      _this.$refs['dataForm'].validate(valid => {
        if (valid) {
          addRole(_this.dataEntity).then(response => {
            _this.$refs.permissionTree.setCheckedKeys([])
            _this.initPage()
            _this.dialogFormVisible = false
            _this.$notify({ title: '提示', message: '新增成功', type: 'success', duration: 2000 })
          })
        }
      })
    },
    // 修改按钮
    handleUpdate(row) {
      _this.dataEntity = Object.assign({}, row) // copy obj
      _this.openDialog('update')
      setTimeout(function() {
        _this.$refs.permissionTree.setCheckedNodes(row.permissions)
      }, 500)
    },
    // 修改操作
    updateData() {
      _this.dataEntity.permissions = _this.$refs.permissionTree.getCheckedNodes()
      _this.$refs['dataForm'].validate(valid => {
        if (valid) {
          updateRole(_this.dataEntity).then(response => {
            _this.$refs.permissionTree.setCheckedKeys([])
            _this.initPage()
            _this.dialogFormVisible = false
            _this.$notify({ title: '提示', message: '修改成功', type: 'success', duration: 2000 })
          })
        }
      })
    },
    // 删除操作
    handleDelete(row) {
      deleteRole(row.id).then(() => {
        _this.$notify({ title: '提示', message: '删除成功', type: 'success', duration: 2000 })
        const index = _this.tableData.indexOf(row)
        _this.tableData.splice(index, 1)
        _this.tableTotal = _this.tableTotal - 1
      })
    },
    // 下载操作
    handleDownload() {
      _this.downloadLoading = true
      import('@/vendor/Export2Excel').then(excel => {
        const tHeader = ['ID', '角色编码', '备注', '创建时间']
        const filterVal = ['id', 'code', 'description', 'createTime']
        const data = _this.formatJson(filterVal, _this.tableData)
        excel.export_json_to_excel({ header: tHeader, data, filename: '角色列表' })
        _this.downloadLoading = false
      })
    },
    formatJson(filterVal, jsonData) {
      return jsonData.map(v =>
        filterVal.map(j => {
          if (j === 'timestamp') {
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
