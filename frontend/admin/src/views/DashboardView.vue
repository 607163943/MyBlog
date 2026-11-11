<script setup lang="ts">
import { onMounted } from 'vue'
import { Document, FolderOpened, PriceTag } from '@element-plus/icons-vue'
import { useDashboardStore } from '@/stores/dashboard'
import { formatNumber } from '@/util/format'
import ArticleRankCard from '@/components/dashboard/ArticleRankCard.vue'
import CategoryRankCard from '@/components/dashboard/CategoryRankCard.vue'

const store = useDashboardStore()

onMounted(() => {
  store.fetchOverview()
})
</script>

<template>
  <div class="dashboard-page">
    <el-row :gutter="16" class="stat-row">
      <el-col :xs="24" :md="8">
        <el-card v-loading="store.statsLoading" shadow="never" class="stat-card">
          <div class="stat-header">
            <el-icon class="stat-icon article"><Document /></el-icon>
            <span>文章</span>
          </div>
          <div class="stat-main">{{ formatNumber(store.stats?.articleTotal) }}</div>
          <div class="stat-sub">
            <span class="draft">
              草稿 <strong>{{ formatNumber(store.stats?.draftCount) }}</strong>
            </span>
            <span class="published">
              已发布 <strong>{{ formatNumber(store.stats?.publishedCount) }}</strong>
            </span>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :md="8">
        <el-card v-loading="store.statsLoading" shadow="never" class="stat-card">
          <div class="stat-header">
            <el-icon class="stat-icon category"><FolderOpened /></el-icon>
            <span>分类</span>
          </div>
          <div class="stat-main">{{ formatNumber(store.stats?.categoryCount) }}</div>
          <div class="stat-sub">个分类</div>
        </el-card>
      </el-col>
      <el-col :xs="24" :md="8">
        <el-card v-loading="store.statsLoading" shadow="never" class="stat-card">
          <div class="stat-header">
            <el-icon class="stat-icon tag"><PriceTag /></el-icon>
            <span>标签</span>
          </div>
          <div class="stat-main">{{ formatNumber(store.stats?.tagCount) }}</div>
          <div class="stat-sub">个标签</div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="16" class="rank-row">
      <el-col :xs="24" :md="14">
        <ArticleRankCard />
      </el-col>
      <el-col :xs="24" :md="10">
        <CategoryRankCard />
      </el-col>
    </el-row>
  </div>
</template>

<style scoped lang="scss">
.dashboard-page {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.stat-row,
.rank-row {
  row-gap: 16px;
}

.stat-card {
  height: 100%;

  :deep(.el-card__body) {
    padding: 20px;
  }
}

.stat-header {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  color: var(--el-text-color-secondary);
}

.stat-icon {
  font-size: 18px;

  &.article {
    color: var(--el-color-primary);
  }

  &.category {
    color: var(--el-color-warning);
  }

  &.tag {
    color: var(--el-color-success);
  }
}

.stat-main {
  margin-top: 12px;
  font-size: 30px;
  font-weight: 600;
  line-height: 1.2;
  color: var(--el-text-color-primary);
}

.stat-sub {
  display: flex;
  gap: 16px;
  margin-top: 10px;
  font-size: 13px;
  color: var(--el-text-color-secondary);

  strong {
    color: var(--el-text-color-primary);
  }

  .draft strong {
    color: var(--el-color-warning);
  }

  .published strong {
    color: var(--el-color-success);
  }
}
</style>
