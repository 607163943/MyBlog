<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Delete, Edit, Refresh, RefreshLeft, Search } from '@element-plus/icons-vue'
import CommentDialog from '@/components/CommentDialog.vue'
import { useCommentStore } from '@/stores/comment'
import type { CommentVO } from '@/types/comment'

const store = useCommentStore()
const dialogVisible = ref(false)
const currentComment = ref<CommentVO | null>(null)
const selection = ref<CommentVO[]>([])

function handleSelectionChange(rows: CommentVO[]) {
  selection.value = rows
}

function handleEdit(row?: CommentVO) {
  const target = row ?? (selection.value.length === 1 ? selection.value[0] : null)
  if (!target) {
    ElMessage.warning('请选择一条要修改的评论')
    return
  }
  currentComment.value = target
  dialogVisible.value = true
}

async function handleDelete(row?: CommentVO) {
  const rows = row ? [row] : selection.value
  if (rows.length === 0) {
    ElMessage.warning('请先选择要删除的评论')
    return
  }

  const hasParent = rows.some((item) => item.parentId === 0)
  const message = row
    ? hasParent
      ? '确定删除该评论吗？其下的回复将一并删除。'
      : '确定删除该评论吗？'
    : hasParent
      ? `确定删除选中的 ${rows.length} 条评论吗？一级评论下的回复将一并删除。`
      : `确定删除选中的 ${rows.length} 条评论吗？`

  try {
    await ElMessageBox.confirm(message, '提示', {
      type: 'warning',
      confirmButtonText: '确定',
      cancelButtonText: '取消',
    })
  } catch {
    return
  }

  const ids = rows.map((item) => item.id)
  try {
    if (ids.length === 1) {
      await store.removeComment(ids[0]!)
    } else {
      await store.removeComments(ids)
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
  <div class="comment-page">
    <el-card shadow="never" class="search-card">
      <el-form :model="store.query" inline @submit.prevent="handleSearch">
        <el-form-item label="所属文章">
          <el-input
            v-model="store.query.articleTitle"
            placeholder="请输入文章标题"
            clearable
            @keyup.enter="handleSearch"
          />
        </el-form-item>
        <el-form-item label="审核状态">
          <el-select v-model="store.query.status" placeholder="请选择审核状态" class="status-select">
            <el-option label="全部" value="" />
            <el-option label="待审核" :value="0" />
            <el-option label="已通过" :value="1" />
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
        <el-table-column prop="id" label="ID" width="90" align="center" />
        <el-table-column label="所属文章" min-width="170" show-overflow-tooltip>
          <template #default="{ row }">{{ row.articleTitle ?? '-' }}</template>
        </el-table-column>
        <el-table-column prop="nickname" label="评论者" width="120" show-overflow-tooltip />
        <el-table-column label="邮箱" width="150" show-overflow-tooltip>
          <template #default="{ row }">{{ row.email ?? '-' }}</template>
        </el-table-column>
        <el-table-column prop="content" label="评论内容" min-width="200" show-overflow-tooltip />
        <el-table-column label="审核状态" width="90" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'warning'">
              {{ row.status === 1 ? '已通过' : '待审核' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="评论时间" width="170" align="center">
          <template #default="{ row }">{{ formatDateTime(row.createTime) }}</template>
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

    <CommentDialog v-model="dialogVisible" :comment="currentComment" />
  </div>
</template>

<style scoped lang="scss">
.comment-page {
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
