<script setup lang="ts">
import { computed, reactive, ref, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { RefreshLeft, Search } from '@element-plus/icons-vue'
import { getCategoryRankPage } from '@/api/dashboard'
import { formatNumber } from '@/util/format'
import type { CategoryRankQuery, CategoryRankVO } from '@/types/dashboard'

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
const records = ref<CategoryRankVO[]>([])
const total = ref(0)

const query = reactive<CategoryRankQuery>({
  name: '',
  minCount: undefined,
  maxCount: undefined,
  page: 1,
  pageSize: 10,
})

async function fetchPage() {
  loading.value = true
  try {
    const data = await getCategoryRankPage({ ...query })
    records.value = data.records
    total.value = data.total
  } finally {
    loading.value = false
  }
}

function resetQuery() {
  query.name = ''
  query.minCount = undefined
  query.maxCount = undefined
  query.page = 1
}

function handleSearch() {
  if (query.minCount != null && query.maxCount != null && query.minCount > query.maxCount) {
    ElMessage.warning('最小文章数不能大于最大文章数')
    return
  }
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
    fetchPage()
  },
)
</script>

<template>
  <el-dialog
    v-model="visible"
    title="全部分类文章数排行"
    width="640px"
    :close-on-click-modal="false"
  >
    <el-form :model="query" inline class="search-form" @submit.prevent="handleSearch">
      <el-form-item label="分类名称">
        <el-input
          v-model="query.name"
          placeholder="请输入分类名称"
          clearable
          class="name-input"
          @keyup.enter="handleSearch"
        />
      </el-form-item>
      <el-form-item label="文章数 ≥">
        <el-input-number
          v-model="query.minCount"
          :min="0"
          :precision="0"
          controls-position="right"
          class="count-input"
        />
      </el-form-item>
      <el-form-item label="文章数 ≤">
        <el-input-number
          v-model="query.maxCount"
          :min="0"
          :precision="0"
          controls-position="right"
          class="count-input"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
        <el-button :icon="RefreshLeft" @click="handleReset">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="records" border stripe max-height="60vh">
      <el-table-column prop="name" label="分类名" min-width="200" show-overflow-tooltip />
      <el-table-column label="文章数" width="120" align="right">
        <template #default="{ row }">{{ formatNumber(row.articleCount) }}</template>
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

.name-input {
  width: 170px;
}

.count-input {
  width: 120px;
}

.pagination {
  display: flex;
  justify-content: flex-end;
  margin-top: 16px;
}
</style>
