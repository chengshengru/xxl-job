<template>
  <div class="job-group-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>执行器管理</span>
        </div>
      </template>
      
      <el-button type="primary" style="margin-bottom: 20px;" @click="handleAdd">
        <el-icon><Plus /></el-icon>新增执行器
      </el-button>
      
      <el-table :data="tableData" border style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="appname" label="AppName" width="180" />
        <el-table-column prop="title" label="名称" width="180" />
        <el-table-column prop="addressType" label="注册方式" width="120">
          <template #default="{ row }">
            <el-tag :type="row.addressType === 0 ? 'success' : 'warning'">
              {{ row.addressType === 0 ? '自动注册' : '手动录入' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="addressList" label="机器地址" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small">编辑</el-button>
            <el-button type="danger" link size="small">删除</el-button>
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

const tableData = ref([
  {
    id: 1,
    appname: 'xxl-job-executor-sample',
    title: '示例执行器',
    addressType: 0,
    addressList: '127.0.0.1:9999'
  }
])

const pagination = reactive({
  page: 1,
  size: 10,
  total: 1
})

const handleAdd = () => {
  console.log('新增')
}
</script>

<style scoped>
.card-header {
  font-size: 16px;
  font-weight: 500;
}
</style>
