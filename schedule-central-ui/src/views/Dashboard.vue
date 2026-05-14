<template>
  <div class="dashboard-container">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-number">128</div>
            <div class="stat-label">任务总数</div>
          </div>
          <el-icon class="stat-icon" :size="40" color="#409EFF"><Odometer /></el-icon>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-number">95</div>
            <div class="stat-label">运行中</div>
          </div>
          <el-icon class="stat-icon" :size="40" color="#67C23A"><VideoPlay /></el-icon>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-number">28</div>
            <div class="stat-label">成功数</div>
          </div>
          <el-icon class="stat-icon" :size="40" color="#E6A23C"><SuccessFilled /></el-icon>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-number">5</div>
            <div class="stat-label">失败数</div>
          </div>
          <el-icon class="stat-icon" :size="40" color="#F56C6C"><CircleCloseFilled /></el-icon>
        </el-card>
      </el-col>
    </el-row>
    
    <el-card class="chart-card" style="margin-top: 20px;">
      <template #header>
        <div class="card-header">
          <span>调度趋势</span>
        </div>
      </template>
      <div style="height: 300px; display: flex; align-items: center; justify-content: center; color: #909399;">
        图表占位区域
      </div>
    </el-card>
  </div>
</template>

<script setup>
</script>

<style scoped>
.dashboard-container {
  padding: 0;
}

.stat-card {
  margin-bottom: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.stat-content {
  flex: 1;
}

.stat-number {
  font-size: 32px;
  font-weight: bold;
  color: #303133;
}

.stat-label {
  font-size: 14px;
  color: #909399;
  margin-top: 8px;
}

.stat-icon {
  opacity: 0.3;
}

.card-header {
  font-size: 16px;
  font-weight: 500;
}
</style>
