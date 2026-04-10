<template>
  <div class="user-manage-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>用户管理</span>
        </div>
      </template>
      
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="用户名">
          <el-input v-model="searchForm.username" placeholder="请输入" clearable />
        </el-form-item>
        <el-form-item label="角色">
          <el-select v-model="searchForm.role" placeholder="请选择">
            <el-option label="全部" :value="-1" />
            <el-option label="管理员" :value="1" />
            <el-option label="普通用户" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
      
      <el-button type="primary" style="margin-bottom: 20px;" @click="handleAdd">
        <el-icon><Plus /></el-icon>新增用户
      </el-button>
      
      <el-table :data="tableData" border style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="username" label="用户名" width="150" />
        <el-table-column prop="role" label="角色" width="120">
          <template #default="{ row }">
            <el-tag :type="row.role === 1 ? 'danger' : ''">
              {{ row.role === 1 ? '管理员' : '普通用户' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="permission" label="权限" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small">编辑</el-button>
            <el-button type="danger" link size="small" v-if="row.role !== 1">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <el-pagination
        v-model:current-page="pagination.page"
        v-model:page-size="pagination.size"
        :page-sizes="[10, 20, 50, 100]"
        :total="pagination.total"
        layout="total, sizes, prev, pager, next, jumper"
        style="margin-top: 20px; justify-content: flex-end;"
      />
    </el-card>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'

const searchForm = reactive({
  username: '',
  role: -1
})

const tableData = ref([
  {
    id: 1,
    username: 'admin',
    role: 1,
    permission: '全部权限'
  },
  {
    id: 2,
    username: 'user',
    role: 0,
    permission: '1,2'
  }
])

const pagination = reactive({
  page: 1,
  size: 10,
  total: 2
})

const handleSearch = () => {
  console.log('搜索')
}

const handleReset = () => {
  Object.assign(searchForm, {
    username: '',
    role: -1
  })
}

const handleAdd = () => {
  console.log('新增')
}
</script>

<style scoped>
.card-header {
  font-size: 16px;
  font-weight: 500;
}

.search-form {
  margin-bottom: 20px;
}
</style>
