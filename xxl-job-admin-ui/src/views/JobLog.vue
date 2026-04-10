<template>
  <div class="job-log-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>调度日志</span>
        </div>
      </template>
      
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="执行器">
          <el-select v-model="searchForm.jobGroup" placeholder="请选择">
            <el-option label="示例执行器" :value="1" />
          </el-select>
        </el-form-item>
        <el-form-item label="任务ID">
          <el-input v-model="searchForm.jobId" placeholder="请输入" clearable />
        </el-form-item>
        <el-form-item label="执行状态">
          <el-select v-model="searchForm.logStatus" placeholder="请选择">
            <el-option label="全部" :value="-1" />
            <el-option label="成功" :value="1" />
            <el-option label="失败" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
      
      <el-table :data="tableData" border style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="jobGroup" label="执行器" width="120" />
        <el-table-column prop="jobId" label="任务ID" width="80" />
        <el-table-column prop="executorAddress" label="执行器地址" width="150" />
        <el-table-column prop="triggerTime" label="触发时间" width="180" />
        <el-table-column prop="handleCode" label="执行结果" width="100">
          <template #default="{ row }">
            <el-tag :type="row.handleCode === 200 ? 'success' : 'danger'">
              {{ row.handleCode === 200 ? '成功' : '失败' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small">查看日志</el-button>
            <el-button type="warning" link size="small">终止</el-button>
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
  jobGroup: '',
  jobId: '',
  logStatus: -1
})

const tableData = ref([
  {
    id: 1,
    jobGroup: '示例执行器',
    jobId: 1,
    executorAddress: '127.0.0.1:9999',
    triggerTime: '2024-04-10 12:00:00',
    handleCode: 200
  },
  {
    id: 2,
    jobGroup: '示例执行器',
    jobId: 2,
    executorAddress: '127.0.0.1:9999',
    triggerTime: '2024-04-10 00:00:00',
    handleCode: 500
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
    jobGroup: '',
    jobId: '',
    logStatus: -1
  })
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
