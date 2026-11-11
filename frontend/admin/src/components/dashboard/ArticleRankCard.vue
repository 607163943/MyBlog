<script setup lang="ts">
import { ref } from 'vue'
import { useDashboardStore } from '@/stores/dashboard'
import { formatNumber } from '@/util/format'
import ArticleRankDialog from './ArticleRankDialog.vue'

const store = useDashboardStore()
const dialogVisible = ref(false)
</script>

<template>
  <el-card shadow="never" class="rank-card">
    <template #header>
      <div class="card-header">
        <span class="card-title">文章浏览量 Top5</span>
        <el-button link type="primary" @click="dialogVisible = true">查看更多</el-button>
      </div>
    </template>

    <el-table v-loading="store.articleTopLoading" :data="store.articleTop5" border stripe>
      <el-table-column prop="title" label="标题" min-width="160" show-overflow-tooltip />
      <el-table-column label="分类" width="90">
        <template #default="{ row }">{{ row.categoryName ?? '-' }}</template>
      </el-table-column>
      <el-table-column label="标签" min-width="120">
        <template #default="{ row }">
          <template v-if="row.tagNames?.length">
            <el-tag
              v-for="name in row.tagNames"
              :key="name"
              size="small"
              type="info"
              class="tag-item"
            >
              {{ name }}
            </el-tag>
          </template>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="浏览量" width="90" align="right">
        <template #default="{ row }">{{ formatNumber(row.viewCount) }}</template>
      </el-table-column>
    </el-table>

    <ArticleRankDialog v-model="dialogVisible" />
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

.tag-item {
  margin: 2px 4px 2px 0;
}
</style>
