<script setup lang="ts">
import { ref } from 'vue'
import { useDashboardStore } from '@/stores/dashboard'
import { formatNumber } from '@/util/format'
import CategoryRankDialog from './CategoryRankDialog.vue'

const store = useDashboardStore()
const dialogVisible = ref(false)
</script>

<template>
  <el-card shadow="never" class="rank-card">
    <template #header>
      <div class="card-header">
        <span class="card-title">分类文章数 Top5</span>
        <el-button link type="primary" @click="dialogVisible = true">查看更多</el-button>
      </div>
    </template>

    <el-table v-loading="store.categoryTopLoading" :data="store.categoryTop5" border stripe>
      <el-table-column prop="name" label="分类名" min-width="160" show-overflow-tooltip />
      <el-table-column label="文章数" width="100" align="right">
        <template #default="{ row }">{{ formatNumber(row.articleCount) }}</template>
      </el-table-column>
    </el-table>

    <CategoryRankDialog v-model="dialogVisible" />
  </el-card>
</template>

<style scoped lang="scss">
.rank-card {
  height: 100%;
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.card-title {
  font-weight: 600;
  color: var(--el-text-color-primary);
}
</style>
