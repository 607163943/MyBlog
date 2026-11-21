<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Delete, Refresh, RefreshLeft, Search, View } from '@element-plus/icons-vue'
import { useFileAssetStore } from '@/stores/fileAsset'
import type { FileAssetDetailVO, FileAssetVO } from '@/types/fileAsset'

const store = useFileAssetStore()

/** 查看文件弹窗 */
const previewVisible = ref(false)
const previewFile = ref<FileAssetDetailVO | null>(null)
const previewError = ref(false)
/** 正在加载详情的文件 ID */
const previewLoadingId = ref<number | null>(null)

function formatDateTime(value?: string | null) {
  return value ? value.replace('T', ' ').slice(0, 19) : '-'
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

function handleSizeChange(size: number) {
  store.query.pageSize = size
  store.query.page = 1
  store.fetchList()
}

async function handlePreview(row: FileAssetVO) {
  if (previewLoadingId.value !== null) {
    return
  }
  previewLoadingId.value = row.id
  try {
    const detail = await store.fetchFileAssetDetail(row.id)
    if (!detail.url) {
      ElMessage.warning('该文件暂不支持在线查看')
      return
    }
    previewFile.value = detail
    previewError.value = false
    previewVisible.value = true
  } catch {
    // 请求错误已由 request.ts 统一提示
  } finally {
    previewLoadingId.value = null
  }
}

async function handleDelete(row: FileAssetVO) {
  try {
    await ElMessageBox.confirm(`确定删除文件「${row.originalName}」吗？`, '删除文件', {
      type: 'warning',
      confirmButtonText: '删除',
      cancelButtonText: '取消',
    })
  } catch {
    return
  }

  try {
    await store.removeFileAsset(row.id)
    ElMessage.success('删除成功')
  } catch {
    // 请求错误已由 request.ts 统一提示
  }
}

onMounted(() => {
  store.fetchList()
})
</script>

<template>
  <div class="file-page">
    <el-card shadow="never" class="search-card">
      <el-form :model="store.query" inline @submit.prevent="handleSearch">
        <el-form-item label="上传日期">
          <el-date-picker
            v-model="store.query.dateRange"
            type="daterange"
            value-format="YYYY-MM-DD"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            unlink-panels
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
        <el-button :icon="Refresh" @click="handleRefresh">刷新</el-button>
      </div>

      <el-table v-loading="store.loading" :data="store.records" border stripe>
        <el-table-column prop="id" label="ID" width="90" align="center" />
        <el-table-column
          prop="originalName"
          label="文件原名"
          min-width="240"
          show-overflow-tooltip
        />
        <el-table-column
          prop="contentType"
          label="Content-Type"
          width="160"
          show-overflow-tooltip
        />
        <el-table-column label="上传日期" width="170" align="center">
          <template #default="{ row }">{{ formatDateTime(row.createTime) }}</template>
        </el-table-column>
        <el-table-column prop="refCount" label="引用次数" width="100" align="center" />
        <el-table-column label="操作" width="180" align="center" fixed="right">
          <template #default="{ row }">
            <el-button
              link
              type="primary"
              :icon="View"
              :loading="previewLoadingId === row.id"
              @click="handlePreview(row)"
            >
              查看文件
            </el-button>
            <el-tooltip
              content="文件正在使用中，无法删除"
              :disabled="row.refCount === 0"
              placement="top"
            >
              <span class="delete-button-wrap">
                <el-button
                  link
                  type="danger"
                  :icon="Delete"
                  :disabled="row.refCount > 0"
                  @click="handleDelete(row)"
                >
                  删除
                </el-button>
              </span>
            </el-tooltip>
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

    <el-dialog
      v-model="previewVisible"
      :title="previewFile?.originalName ?? '查看文件'"
      width="720px"
      destroy-on-close
    >
      <div class="file-preview">
        <img
          v-if="previewFile?.url && !previewError"
          :src="previewFile?.url ?? undefined"
          :alt="previewFile?.originalName ?? ''"
          @error="previewError = true"
        />
        <el-empty v-else description="图片加载失败" />
      </div>
      <template #footer>
        <el-button @click="previewVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped lang="scss">
.file-page {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.search-card {
  :deep(.el-card__body) {
    padding-bottom: 2px;
  }
}

.toolbar {
  display: flex;
  gap: 12px;
  margin-bottom: 16px;
}

.pagination {
  display: flex;
  justify-content: flex-end;
  margin-top: 16px;
}

.delete-button-wrap {
  display: inline-flex;
  margin-left: 8px;
}

.file-preview {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 200px;
}

.file-preview img {
  display: block;
  max-width: 100%;
  max-height: 60vh;
  object-fit: contain;
}
</style>
