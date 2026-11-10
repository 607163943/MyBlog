<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Delete, Edit, Plus, Refresh, RefreshLeft, Search } from '@element-plus/icons-vue'
import { useArticleStore } from '@/stores/article'
import type { ArticleVO } from '@/types/article'

const store = useArticleStore()
const selection = ref<ArticleVO[]>([])

function handleSelectionChange(rows: ArticleVO[]) {
  selection.value = rows
}

function handleAdd() {
  ElMessage.info('新增文章功能暂未实现')
}

function handleEdit() {
  ElMessage.info('修改文章功能暂未实现')
}

async function handleDelete(row?: ArticleVO) {
  const rows = row ? [row] : selection.value
  if (rows.length === 0) {
    ElMessage.warning('请先选择要删除的文章')
    return
  }

  try {
    await ElMessageBox.confirm(
      row ? `确定删除文章「${row.title}」吗？` : `确定删除选中的 ${rows.length} 篇文章吗？`,
      '提示',
      { type: 'warning', confirmButtonText: '确定', cancelButtonText: '取消' },
    )
  } catch {
    return
  }

  const ids = rows.map((item) => item.id)
  try {
    if (ids.length === 1) {
      await store.removeArticle(ids[0]!)
    } else {
      await store.removeArticles(ids)
    }
    ElMessage.success('删除成功')
  } catch {
    // 请求错误已由 request.ts 统一提示
  }
}

function handleSearch() {
  store.query.page = 1
  store.fetchList()
}

function handleReset() {
  store.resetQuery()
}

function handleRefresh() {
  store.fetchList()
}

function handlePageChange(page: number) {
  store.query.page = page
  store.fetchList()
}

function handleSizeChange(pageSize: number) {
  store.query.pageSize = pageSize
  store.query.page = 1
  store.fetchList()
}

function formatDateTime(value?: string) {
  return value ? value.replace('T', ' ').slice(0, 19) : '-'
}

onMounted(() => {
  store.fetchList()
})
</script>

<template>
  <div class="article-page">
    <el-card shadow="never" class="search-card">
      <el-form :model="store.query" inline @submit.prevent="handleSearch">
        <el-form-item label="文章标题">
          <el-input
            v-model="store.query.title"
            placeholder="请输入文章标题"
            clearable
            @keyup.enter="handleSearch"
          />
        </el-form-item>
        <el-form-item label="发布状态">
          <el-select v-model="store.query.status" placeholder="请选择发布状态" class="status-select">
            <el-option label="全部" value="" />
            <el-option label="草稿" :value="0" />
            <el-option label="已发布" :value="1" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
          <el-button :icon="RefreshLeft" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never" class="table-card">
      <div class="toolbar">
        <el-button type="primary" :icon="Plus" @click="handleAdd">新增</el-button>
        <el-button type="warning" :icon="Edit" @click="handleEdit">修改</el-button>
        <el-button
          type="danger"
          :icon="Delete"
          :disabled="selection.length === 0"
          @click="handleDelete()"
        >
          删除
        </el-button>
        <el-button :icon="Refresh" @click="handleRefresh">刷新</el-button>
      </div>

      <el-table
        v-loading="store.loading"
        :data="store.records"
        border
        stripe
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column prop="id" label="ID" width="100" align="center" />
        <el-table-column prop="title" label="标题" min-width="240" show-overflow-tooltip />
        <el-table-column label="分类" width="140" align="center">
          <template #default="{ row }">{{ row.categoryName ?? '-' }}</template>
        </el-table-column>
        <el-table-column label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'">
              {{ row.status === 1 ? '已发布' : '草稿' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="viewCount" label="浏览量" width="100" align="center" />
        <el-table-column label="更新时间" width="170" align="center">
          <template #default="{ row }">{{ formatDateTime(row.updateTime) }}</template>
        </el-table-column>
        <el-table-column label="操作" width="150" align="center" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" :icon="Edit" @click="handleEdit">编辑</el-button>
            <el-button link type="danger" :icon="Delete" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination
          :current-page="store.query.page"
          :page-size="store.query.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="store.total"
          layout="total, sizes, prev, pager, next, jumper"
          background
          @current-change="handlePageChange"
          @size-change="handleSizeChange"
        />
      </div>
    </el-card>
  </div>
</template>

<style scoped lang="scss">
.article-page {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.search-card {
  :deep(.el-card__body) {
    padding-bottom: 2px;
  }

  .el-form-item {
    margin-bottom: 14px;
  }

  .status-select {
    width: 160px;
  }
}

.toolbar {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 16px;
}

.pagination {
  display: flex;
  justify-content: flex-end;
  margin-top: 16px;
}
</style>
