<script setup lang="ts">
import { computed, reactive, ref, watch } from 'vue'
import { RefreshLeft, Search } from '@element-plus/icons-vue'
import { getArticleRankPage } from '@/api/dashboard'
import { getCategoryOptions } from '@/api/category'
import { getTagOptions } from '@/api/tag'
import { formatNumber } from '@/util/format'
import type { ArticleRankQuery, ArticleRankVO } from '@/types/dashboard'
import type { CategoryVO } from '@/types/category'
import type { TagVO } from '@/types/tag'

const props = defineProps<{
  modelValue: boolean
}>()

const emit = defineEmits<{
  'update:modelValue': [value: boolean]
}>()

const visible = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value),
})

const loading = ref(false)
const records = ref<ArticleRankVO[]>([])
const total = ref(0)
const categoryOptions = ref<CategoryVO[]>([])
const tagOptions = ref<TagVO[]>([])
const optionsLoaded = ref(false)

const query = reactive<ArticleRankQuery>({
  title: '',
  categoryId: undefined,
  tagId: undefined,
  page: 1,
  pageSize: 10,
})

async function fetchPage() {
  loading.value = true
  try {
    const data = await getArticleRankPage({ ...query })
    records.value = data.records
    total.value = data.total
  } finally {
    loading.value = false
  }
}

async function loadOptions() {
  if (optionsLoaded.value) {
    return
  }
  try {
    const [categories, tags] = await Promise.all([getCategoryOptions(), getTagOptions()])
    categoryOptions.value = categories
    tagOptions.value = tags
    optionsLoaded.value = true
  } catch {
    // 请求错误已由 request.ts 统一提示
  }
}

function resetQuery() {
  query.title = ''
  query.categoryId = undefined
  query.tagId = undefined
  query.page = 1
}

function handleSearch() {
  query.page = 1
  fetchPage()
}

function handleReset() {
  resetQuery()
  fetchPage()
}

function handlePageChange(page: number) {
  query.page = page
  fetchPage()
}

function handleSizeChange(pageSize: number) {
  query.pageSize = pageSize
  query.page = 1
  fetchPage()
}

watch(
  () => props.modelValue,
  (value) => {
    if (!value) {
      return
    }
    resetQuery()
    loadOptions()
    fetchPage()
  },
)
</script>

<template>
  <el-dialog
    v-model="visible"
    title="全部文章浏览量排行"
    width="min(1000px, 92vw)"
    top="6vh"
    :close-on-click-modal="false"
  >
    <el-form :model="query" inline class="search-form" @submit.prevent="handleSearch">
      <el-form-item label="文章标题">
        <el-input
          v-model="query.title"
          placeholder="请输入文章标题"
          clearable
          @keyup.enter="handleSearch"
        />
      </el-form-item>
      <el-form-item label="分类">
        <el-select
          v-model="query.categoryId"
          placeholder="全部分类"
          clearable
          class="filter-select"
        >
          <el-option
            v-for="item in categoryOptions"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="标签">
        <el-select v-model="query.tagId" placeholder="全部标签" clearable class="filter-select">
          <el-option
            v-for="item in tagOptions"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
        <el-button :icon="RefreshLeft" @click="handleReset">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="records" border stripe max-height="60vh">
      <el-table-column prop="title" label="文章标题" min-width="260" show-overflow-tooltip />
      <el-table-column label="分类" width="140">
        <template #default="{ row }">{{ row.categoryName ?? '-' }}</template>
      </el-table-column>
      <el-table-column label="标签" min-width="200">
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
      <el-table-column label="浏览量" width="120" align="right">
        <template #default="{ row }">{{ formatNumber(row.viewCount) }}</template>
      </el-table-column>
    </el-table>

    <div class="pagination">
      <el-pagination
        :current-page="query.page"
        :page-size="query.pageSize"
        :page-sizes="[10, 20, 50, 100]"
        :total="total"
        layout="total, sizes, prev, pager, next, jumper"
        background
        @current-change="handlePageChange"
        @size-change="handleSizeChange"
      />
    </div>
  </el-dialog>
</template>

<style scoped lang="scss">
.search-form {
  margin-bottom: 4px;

  .el-form-item {
    margin-bottom: 12px;
  }
}

.filter-select {
  width: 160px;
}

.tag-item {
  margin: 2px 4px 2px 0;
}

.pagination {
  display: flex;
  justify-content: flex-end;
  margin-top: 16px;
}
</style>
