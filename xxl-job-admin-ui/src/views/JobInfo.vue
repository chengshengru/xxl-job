<template>
  <div class="job-info-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>任务管理</span>
        </div>
      </template>
      
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="执行器">
          <el-select v-model="searchForm.jobGroup" placeholder="请选择">
            <el-option label="示例执行器" :value="1" />
          </el-select>
        </el-form-item>
        <el-form-item label="任务描述">
          <el-input v-model="searchForm.jobDesc" placeholder="请输入" clearable />
        </el-form-item>
        <el-form-item label="负责人">
          <el-input v-model="searchForm.author" placeholder="请输入" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
      
      <el-button type="primary" style="margin-bottom: 20px;" @click="handleAdd">
        <el-icon><Plus /></el-icon>新增任务
      </el-button>
      
      <el-table :data="tableData" border style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="jobDesc" label="任务描述" width="180" />
        <el-table-column prop="author" label="负责人" width="120" />
        <el-table-column prop="alarmEmail" label="报警邮件" width="180" />
        <el-table-column prop="scheduleType" label="调度类型" width="120" />
        <el-table-column prop="scheduleConf" label="Cron" width="150" />
        <el-table-column prop="executorHandler" label="执行器" width="150" />
        <el-table-column prop="triggerStatus" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.triggerStatus === 1 ? 'success' : 'info'">
              {{ row.triggerStatus === 1 ? '运行中' : '停止' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="280" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small">编辑</el-button>
            <el-button type="success" link size="small">启动</el-button>
            <el-button type="warning" link size="small">停止</el-button>
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

const searchForm = reactive({
  jobGroup: '',
  jobDesc: '',
  author: ''
})

const tableData = ref([
  {
    id: 1,
    jobDesc: '示例任务1',
    author: 'admin',
    alarmEmail: 'admin@example.com',
    scheduleType: 'CRON',
    scheduleConf: '0 0 12 * * ?',
    executorHandler: 'demoJobHandler',
    triggerStatus: 1
  },
  {
    id: 2,
    jobDesc: '示例任务2',
    author: 'admin',
    alarmEmail: 'admin@example.com',
    scheduleType: 'CRON',
    scheduleConf: '0 0 0 * * ?',
    executorHandler: 'demoJobHandler2',
    triggerStatus: 0
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
    jobDesc: '',
    author: ''
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
