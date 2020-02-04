<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="tableQuery.keyword" placeholder="请输入查询内容" class="filter-item" style="width: 200px;" @keyup.enter.native="handleFilter" />
      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">查询</el-button>
      <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-circle-plus-outline" @click="handleCreate">新增</el-button>
      <el-button :loading="downloadLoading" class="filter-item" type="primary" icon="el-icon-download" @click="handleDownload">导出</el-button>
    </div>

    <el-table v-loading="tableLoading" :data="tableData" border fit highlight-current-row style="width: 100%;" @sort-change="sortChange">
      <el-table-column label="ID" prop="id" sortable="custom" align="center" width="80">
        <template slot-scope="scope">
          <span>{{ scope.row.id }}</span>
        </template>
      </el-table-column>
      <el-table-column label="登录名" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.username }}</span>
        </template>
      </el-table-column>
      <el-table-column label="名称" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column label="电子邮件" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.email }}</span>
        </template>
      </el-table-column>
      <el-table-column label="角色" align="center">
        <template slot-scope="scope">
          <el-tag v-for="item in scope.row.roles" :key="'role2' + item.id">{{ item.description }}</el-tag>
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

    <pagination v-show="tableTotal>0" :total="tableTotal" :page.sync="tableQuery.page" :size.sync="tableQuery.size" @pagination="initPage" />

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="dataEntity" label-position="left" label-width="100px" style="width: 400px; margin-left:50px;">
        <el-form-item label="登录账号" prop="username">
          <el-input v-model="dataEntity.username" placeholder="登录账号" />
        </el-form-item>
        <el-form-item label="名称" prop="name">
          <el-input v-model="dataEntity.name" placeholder="名称" />
        </el-form-item>
        <el-form-item label="电子邮件" prop="email">
          <el-input v-model="dataEntity.email" placeholder="电子邮件" />
        </el-form-item>
        <el-form-item label="角色" prop="roles">
          <el-checkbox-group v-model="roleIds">
            <el-checkbox v-for="item in roles" :key="'role1' + item.id" :label="item.id">{{ item.description }}</el-checkbox>
          </el-checkbox-group>
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
import { getRoleSelects } from '@/api/sys/role'
import { getUsers, addUser, updateUser, deleteUser } from '@/api/sys/user'
import { parseTime, parseOrder } from '@/utils'
import Pagination from '@/components/Pagination'

let _this = null

export default {
  components: { Pagination },
  filters: {},
  data() {
    return {
      tableData: [],
      tableTotal: 0,
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
        username: '',
        name: '',
        avatar: '',
        email: '',
        roleIds: []
      },
      roles: [],
      roleIds: [],
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '修改',
        create: '新增'
      },
      rules: {
        username: [
          { required: true, message: '登录账号不能为空', trigger: 'change' },
          { pattern: /^\S{3,32}$/, message: '登录账号字符串长度大小在3-32之间', trigger: 'blur' }
        ],
        name: [
          { required: true, message: '名称不能为空', trigger: 'change' },
          { pattern: /^\S{3,32}$/, message: '名称字符串长度大小在3-32之间', trigger: 'blur' }
        ],
        email: [
          { required: true, message: '电子邮件不能为空', trigger: 'change' },
          { pattern: /^\S{3,32}$/, message: '电子邮件字符串长度大小在3-32之间', trigger: 'blur' }
        ]
      },
      downloadLoading: false
    }
  },
  mounted() {
    window.vue = this
  },
  created() {
    _this = this
    _this.initRoleSelect()
    _this.initPage()
  },
  methods: {
    // 初始化页面
    initPage() {
      getUsers(_this.tableQuery).then(response => {
        _this.tableData = response.data.content
        _this.tableTotal = response.data.totalElements
      })
    },
    // 初始化角色选择
    initRoleSelect() {
      getRoleSelects().then(response => {
        _this.roles = response.data
      })
    },
    // 重置实体
    resetEntity() {
      _this.dataEntity = {
        id: undefined,
        username: '',
        name: '',
        avatar: '',
        email: '',
        roleIds: []
      }
    },
    // 处理查询过滤
    handleFilter() {
      _this.tableQuery.page = 1
      _this.initPage()
    },
    // 条件排序
    sortChange(data) {
      const { prop, order } = data
      _this.tableQuery.sort = prop
      _this.tableQuery.order = parseOrder(order)
      _this.initPage()
    },
    // 弹出框
    openDialog(dialogStatus) {
      _this.roleIds = []
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
      _this.dataEntity.roleIds = _this.roleIds
      _this.$refs['dataForm'].validate(valid => {
        if (valid) {
          addUser(_this.dataEntity).then(response => {
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
      _this.roleIds = row.roles.map(function(role) {
        return role.id
      })
    },
    // 修改操作
    updateData() {
      _this.dataEntity.roleIds = _this.roleIds
      _this.$refs['dataForm'].validate(valid => {
        if (valid) {
          updateUser(_this.dataEntity).then(() => {
            _this.initPage()
            _this.dialogFormVisible = false
            _this.$notify({ title: '提示', message: '修改成功', type: 'success', duration: 2000 })
          })
        }
      })
    },
    // 删除操作
    handleDelete(row) {
      deleteUser(row.id).then(() => {
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
        const tHeader = ['ID', '登录名', '名称', '电子邮件', '创建时间']
        const filterVal = ['id', 'username', 'name', 'email', 'createTime']
        const data = _this.formatJson(filterVal, _this.tableData)
        excel.export_json_to_excel({ header: tHeader, data, filename: '用户列表' })
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
