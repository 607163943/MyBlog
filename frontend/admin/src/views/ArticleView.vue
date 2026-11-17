<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Delete, Edit, Plus, Rank, Refresh, RefreshLeft, Search } from '@element-plus/icons-vue'
import ArticleSortDialog from '@/components/ArticleSortDialog.vue'
import { useArticleStore } from '@/stores/article'
import type { ArticleVO } from '@/types/article'

const store = useArticleStore()
const router = useRouter()
const selection = ref<ArticleVO[]>([])

/** 分类内排序弹窗 */
const sortDialogVisible = ref(false)

/** 排序弹窗的目标分类（支持从表格行内按钮直接指定） */
const sortTarget = ref<{
  categoryId: number
  categoryName: string
  articleId: number | null
} | null>(null)

/** 表格中仅选中一行时，该行文章 */
const onlySelected = computed(() => (selection.value.length === 1 ? selection.value[0] : undefined))

/** 选中行的共同分类：未选中为 undefined，含无分类文章或分类不一致为 null */
const selectedCategory = computed<{ id: number; name: string } | null | undefined>(() => {
  const rows = selection.value
  const first = rows[0]
  if (!first) {
    return undefined
  }
  if (first.categoryId <= 0 || !rows.every((row) => row.categoryId === first.categoryId)) {
    return null
  }
  return { id: first.categoryId, name: first.categoryName ?? '' }
})

/** 排序目标分类：选中行有共同分类时以选中行为准，否则退回筛选栏选中的分类 */
const sortCategoryId = computed<number | null>(() => {
  const selected = selectedCategory.value
  if (selected !== undefined) {
    return selected?.id ?? null
  }
  return store.query.categoryId === '' ? null : store.query.categoryId
})

const sortCategoryName = computed(() => {
  const selected = selectedCategory.value
  if (selected !== undefined) {
    return selected?.name ?? ''
  }
  const categoryId = sortCategoryId.value
  if (categoryId === null) {
    return ''
  }
  return store.categoryOptions.find((category) => category.id === categoryId)?.name ?? ''
})

function openSortFor(categoryId: number, categoryName: string, articleId: number | null) {
  sortTarget.value = { categoryId, categoryName, articleId }
  sortDialogVisible.value = true
}

function openSortDialog() {
  const categoryId = sortCategoryId.value
  if (categoryId === null) {
    ElMessage.warning(
      selection.value.length > 0
        ? '所选文章未设置分类或分类不一致，无法调整分类内排序'
        : '请先在筛选栏选择具体分类，或勾选同一分类的文章',
    )
    return
  }
  openSortFor(categoryId, sortCategoryName.value, onlySelected.value?.id ?? null)
}

function openRowSortDialog(row: ArticleVO) {
  if (row.categoryId <= 0) {
    return
  }
  openSortFor(row.categoryId, row.categoryName ?? '', row.id)
}

async function handleSortSaved() {
  await store.fetchList()
}

function handleSelectionChange(rows: ArticleVO[]) {
  selection.value = rows
}

function handleAdd() {
  router.push({ name: 'article-edit' })
}

function handleEdit(row?: ArticleVO) {
  const target = row ?? (selection.value.length === 1 ? selection.value[0] : null)
  if (!target) {
    ElMessage.warning('请选择一条要修改的文章')
    return
  }
  router.push({ name: 'article-edit-id', params: { id: target.id } })
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

function formatDateTime(value?: string | null) {
  return value ? value.replace('T', ' ').slice(0, 19) : '-'
}

onMounted(() => {
  store.fetchCategoryOptions()
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
          <el-select
            v-model="store.query.status"
            placeholder="请选择发布状态"
            class="status-select"
          >
            <el-option label="全部" value="" />
            <el-option label="草稿" :value="0" />
            <el-option label="已发布" :value="1" />
          </el-select>
        </el-form-item>
        <el-form-item label="分类">
          <el-select
            v-model="store.query.categoryId"
            placeholder="请选择分类"
            filterable
            class="category-select"
          >
            <el-option label="全部" value="" />
            <el-option
              v-for="category in store.categoryOptions"
              :key="category.id"
              :label="category.name"
              :value="category.id"
            />
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
        <el-tooltip
          content="请先在筛选栏选择具体分类，或勾选同一分类的文章"
          :disabled="sortCategoryId !== null"
          placement="top"
        >
          <span class="sort-button-wrap">
            <el-button :icon="Rank" :disabled="sortCategoryId === null" @click="openSortDialog">
              分类内排序
            </el-button>
          </span>
        </el-tooltip>
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
        <el-table-column label="排序" width="90" align="center">
          <template #default="{ row }">{{ row.categorySort ?? '-' }}</template>
        </el-table-column>
        <el-table-column label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'">
              {{ row.status === 1 ? '已发布' : '草稿' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="viewCount" label="浏览量" width="100" align="center" />
        <el-table-column label="发布时间" width="170" align="center">
          <template #default="{ row }">{{ formatDateTime(row.publishTime) }}</template>
        </el-table-column>
        <el-table-column label="更新时间" width="170" align="center">
          <template #default="{ row }">{{ formatDateTime(row.updateTime) }}</template>
        </el-table-column>
        <el-table-column label="操作" width="220" align="center" fixed="right">
          <template #default="{ row }">
            <el-tooltip
              content="该文章未设置分类，无法调整排序"
              :disabled="row.categoryId > 0"
              placement="top"
            >
              <span class="row-sort-wrap">
                <el-button
                  link
                  type="primary"
                  :icon="Rank"
                  :disabled="row.categoryId <= 0"
                  @click="openRowSortDialog(row)"
                >
                  排序
                </el-button>
              </span>
            </el-tooltip>
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

    <ArticleSortDialog
      v-model="sortDialogVisible"
      :category-id="sortTarget?.categoryId ?? 0"
      :category-name="sortTarget?.categoryName ?? ''"
      :current-article-id="sortTarget?.articleId ?? null"
      @saved="handleSortSaved"
    />
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

  .category-select {
    width: 180px;
  }
}

.toolbar {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 16px;
}

.sort-button-wrap {
  display: inline-flex;
}

.row-sort-wrap {
  display: inline-flex;
  margin-right: 12px;
}

.pagination {
  display: flex;
  justify-content: flex-end;
  margin-top: 16px;
}
</style>
