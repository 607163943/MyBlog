<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Delete, Edit, Plus, Refresh, RefreshLeft, Search } from '@element-plus/icons-vue'
import TagDialog from '@/components/TagDialog.vue'
import { useTagStore } from '@/stores/tag'
import type { TagVO } from '@/types/tag'

const store = useTagStore()
const dialogVisible = ref(false)
const currentTag = ref<TagVO | null>(null)
const selection = ref<TagVO[]>([])

function handleSelectionChange(rows: TagVO[]) {
  selection.value = rows
}

function handleAdd() {
  currentTag.value = null
  dialogVisible.value = true
}

function handleEdit(row?: TagVO) {
  const target = row ?? (selection.value.length === 1 ? selection.value[0] : null)
  if (!target) {
    ElMessage.warning('请选择一条要修改的标签')
    return
  }
  currentTag.value = target
  dialogVisible.value = true
}

async function handleDelete(row?: TagVO) {
  const rows = row ? [row] : selection.value
  if (rows.length === 0) {
    ElMessage.warning('请先选择要删除的标签')
    return
  }

  try {
    await ElMessageBox.confirm(
      row ? `确定删除标签「${row.name}」吗？` : `确定删除选中的 ${rows.length} 个标签吗？`,
      '提示',
      { type: 'warning', confirmButtonText: '确定', cancelButtonText: '取消' },
    )
  } catch {
    return
  }

  const ids = rows.map((item) => item.id)
  try {
    if (ids.length === 1) {
      await store.removeTag(ids[0]!)
    } else {
      await store.removeTags(ids)
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
  <div class="tag-page">
    <el-card shadow="never" class="search-card">
      <el-form :model="store.query" inline @submit.prevent="handleSearch">
        <el-form-item label="标签名称">
          <el-input
            v-model="store.query.name"
            placeholder="请输入标签名称"
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
        <el-table-column prop="id" label="ID" width="100" align="center" />
        <el-table-column prop="name" label="标签名称" min-width="200" show-overflow-tooltip />
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

    <TagDialog v-model="dialogVisible" :tag="currentTag" />
  </div>
</template>

<style scoped lang="scss">
.tag-page {
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
