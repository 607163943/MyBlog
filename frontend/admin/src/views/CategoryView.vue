<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Delete, Edit, Plus, Refresh, RefreshLeft, Search } from '@element-plus/icons-vue'
import CategoryDialog from '@/components/CategoryDialog.vue'
import { useCategoryStore } from '@/stores/category'
import type { CategoryVO } from '@/api/category'

const store = useCategoryStore()
const dialogVisible = ref(false)
const currentCategory = ref<CategoryVO | null>(null)
const selection = ref<CategoryVO[]>([])

function handleSelectionChange(rows: CategoryVO[]) {
  selection.value = rows
}

function handleAdd() {
  currentCategory.value = null
  dialogVisible.value = true
}

function handleEdit(row?: CategoryVO) {
  const target = row ?? (selection.value.length === 1 ? selection.value[0] : null)
  if (!target) {
    ElMessage.warning('请选择一条要修改的分类')
    return
  }
  currentCategory.value = target
  dialogVisible.value = true
}

async function handleDelete(row?: CategoryVO) {
  const rows = row ? [row] : selection.value
  if (rows.length === 0) {
    ElMessage.warning('请先选择要删除的分类')
    return
  }

  try {
    await ElMessageBox.confirm(
      row ? `确定删除分类「${row.name}」吗？` : `确定删除选中的 ${rows.length} 个分类吗？`,
      '提示',
      { type: 'warning', confirmButtonText: '确定', cancelButtonText: '取消' },
    )
  } catch {
    return
  }

  const ids = rows.map((item) => item.id)
  try {
    if (ids.length === 1) {
      await store.removeCategory(ids[0]!)
    } else {
      await store.removeCategories(ids)
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
  <div class="category-page">
    <el-card shadow="never" class="search-card">
      <el-form :model="store.query" inline @submit.prevent="handleSearch">
        <el-form-item label="分类名称">
          <el-input
            v-model="store.query.name"
            placeholder="请输入分类名称"
            clearable
            @keyup.enter="handleSearch"
          />
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
        <el-button
          type="warning"
          :icon="Edit"
          :disabled="selection.length !== 1"
          @click="handleEdit()"
        >
          修改
        </el-button>
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
        <el-table-column prop="id" label="ID" width="80" align="center" />
        <el-table-column prop="name" label="分类名称" min-width="160" />
        <el-table-column prop="description" label="分类描述" min-width="240" show-overflow-tooltip />
        <el-table-column prop="sort" label="排序" width="90" align="center" />
        <el-table-column label="创建时间" width="170" align="center">
          <template #default="{ row }">{{ formatDateTime(row.createTime) }}</template>
        </el-table-column>
        <el-table-column label="修改时间" width="170" align="center">
          <template #default="{ row }">{{ formatDateTime(row.updateTime) }}</template>
        </el-table-column>
        <el-table-column label="操作" width="150" align="center" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" :icon="Edit" @click="handleEdit(row)">编辑</el-button>
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

    <CategoryDialog v-model="dialogVisible" :category="currentCategory" />
  </div>
</template>

<style scoped lang="scss">
.category-page {
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
