<script setup lang="ts">
import { computed, nextTick, onMounted, reactive, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import { ArrowLeft, Rank } from '@element-plus/icons-vue'
import MarkdownEditor from '@/components/MarkdownEditor.vue'
import CoverUpload from '@/components/CoverUpload.vue'
import ArticleSortDialog from '@/components/ArticleSortDialog.vue'
import { useArticleStore } from '@/stores/article'
import type { ArticleDTO, ArticleSortVO } from '@/types/article'

const route = useRoute()
const router = useRouter()
const store = useArticleStore()

const formRef = ref<FormInstance>()
const loading = ref(true)
const submitting = ref(false)
/** 封面上传中禁止提交，避免把旧封面当作新封面保存 */
const coverUploading = ref(false)

const articleId = computed(() => {
  const id = route.params.id
  if (typeof id !== 'string' || id === '') {
    return null
  }
  const parsed = Number(id)
  return Number.isNaN(parsed) ? null : parsed
})

const isEdit = computed(() => articleId.value !== null)
const pageTitle = computed(() => (isEdit.value ? '修改文章' : '新增文章'))

const form = reactive({
  title: '',
  description: '',
  content: '',
  cover: '',
  /** 所属分类 ID，0 表示无分类 */
  categoryId: 0,
  categorySort: null as number | null,
  tagIds: [] as number[],
})

/** 无分类文章不参与分类内排序 */
const hasCategory = computed(() => form.categoryId > 0)

/** 编辑页加载时的原始分类，换分类时清空排序值以避免沿用旧分类的编号 */
const loadedCategoryId = ref(0)

/** 原始分类下的排序值，切回原分类时恢复展示 */
const loadedCategorySort = ref<number | null>(null)

/** 当前文章的发布状态，null 表示新增文章 */
const articleStatus = ref<number | null>(null)

/** 分类内排序弹窗 */
const sortDialogVisible = ref(false)

/** 当前选中分类的名称，用于排序弹窗标题 */
const selectedCategoryName = computed(
  () => store.categoryOptions.find((category) => category.id === form.categoryId)?.name ?? '',
)

/** 分类排序字段的只读展示文案 */
const categorySortText = computed(() => {
  if (!hasCategory.value) {
    return '无分类不参与排序'
  }
  if (!isEdit.value) {
    return '保存后可调整'
  }
  if (form.categoryId !== loadedCategoryId.value) {
    return '保存分类后可调整'
  }
  if (articleStatus.value !== 1) {
    return '发布后可调整'
  }
  return form.categorySort == null ? '未编号（排在末尾）' : `第 ${form.categorySort} 位`
})

/** 无法打开排序弹窗的原因，空串表示可以打开 */
const sortDisabledReason = computed(() => {
  if (!hasCategory.value) {
    return '无分类文章不参与排序'
  }
  if (!isEdit.value) {
    return '请先保存文章'
  }
  if (form.categoryId !== loadedCategoryId.value) {
    return '请先保存新的分类'
  }
  if (articleStatus.value !== 1) {
    return '文章发布后才能调整分类内顺序'
  }
  return ''
})

const canOpenSortDialog = computed(() => sortDisabledReason.value === '')

function openSortDialog() {
  if (canOpenSortDialog.value) {
    sortDialogVisible.value = true
  }
}

/** 排序保存后同步当前文章在分类内的新位置 */
function handleSortSaved(list: ArticleSortVO[]) {
  const index = list.findIndex((item) => item.id === articleId.value)
  if (index >= 0) {
    form.categorySort = index + 1
    loadedCategorySort.value = form.categorySort
  }
}

const rules: FormRules = {
  title: [
    { required: true, message: '请输入文章标题', trigger: 'blur' },
    { max: 200, message: '文章标题不能超过 200 个字符', trigger: 'blur' },
  ],
  content: [{ required: true, message: '请输入文章正文', trigger: 'change' }],
  cover: [{ max: 255, message: '封面地址不能超过 255 个字符', trigger: 'blur' }],
  description: [{ max: 500, message: '文章摘要不能超过 500 个字符', trigger: 'blur' }],
}

function resetForm() {
  form.title = ''
  form.description = ''
  form.content = ''
  form.cover = ''
  form.categoryId = 0
  form.categorySort = null
  form.tagIds = []
  loadedCategoryId.value = 0
  loadedCategorySort.value = null
  articleStatus.value = null
}

async function loadPage() {
  loading.value = true
  resetForm()
  try {
    await store.fetchArticleOptions()

    if (articleId.value !== null) {
      const article = await store.fetchArticleDetail(articleId.value)
      form.title = article.title
      form.description = article.description ?? ''
      form.content = article.content ?? ''
      form.cover = article.cover ?? ''
      form.categoryId = article.categoryId ?? 0
      form.categorySort = form.categoryId > 0 ? (article.categorySort ?? null) : null
      form.tagIds = article.tagIds ?? []
      loadedCategoryId.value = form.categoryId
      loadedCategorySort.value = form.categorySort
      articleStatus.value = article.status
    }
    await nextTick()
    formRef.value?.clearValidate()
  } catch {
    // 详情加载失败时返回列表，请求错误已由 request.ts 统一提示
    router.replace({ name: 'article' })
  } finally {
    loading.value = false
  }
}

watch(articleId, loadPage)

async function handleSubmit(status: number) {
  if (!formRef.value) {
    return
  }
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) {
    return
  }
  if (!form.content.trim()) {
    ElMessage.warning('请输入文章正文')
    return
  }

  const data: ArticleDTO = {
    title: form.title.trim(),
    description: form.description.trim() || undefined,
    content: form.content,
    cover: form.cover.trim() || undefined,
    categoryId: form.categoryId,
    categorySort: hasCategory.value ? (form.categorySort ?? undefined) : undefined,
    tagIds: form.tagIds,
    status,
  }

  submitting.value = true
  try {
    if (articleId.value !== null) {
      await store.editArticle({ ...data, id: articleId.value })
      ElMessage.success(status === 1 ? '发布成功' : '保存成功')
    } else {
      await store.createArticle(data)
      ElMessage.success(status === 1 ? '发布成功' : '保存草稿成功')
    }
    router.push({ name: 'article' })
  } catch {
    // 请求错误已由 request.ts 统一提示
  } finally {
    submitting.value = false
  }
}

/** 换分类时清空排序值：排序值只在分类内有效，留空由服务端重新编号；无分类时不参与排序 */
function handleCategoryChange(value: number) {
  if (value <= 0 || value !== loadedCategoryId.value) {
    form.categorySort = null
  } else {
    form.categorySort = loadedCategorySort.value
  }
}

function handleCoverUploading(value: boolean) {
  coverUploading.value = value
}

function handleBack() {
  router.push({ name: 'article' })
}

onMounted(loadPage)
</script>

<template>
  <div v-loading="loading" class="article-edit-page">
    <el-card shadow="never" class="edit-card">
      <template #header>
        <div class="card-header">
          <span class="card-title">{{ pageTitle }}</span>
          <el-button link type="primary" :icon="ArrowLeft" @click="handleBack">返回列表</el-button>
        </div>
      </template>

      <el-form ref="formRef" :model="form" :rules="rules" label-position="top" class="edit-form">
        <el-form-item prop="title">
          <el-input
            v-model="form.title"
            class="title-input"
            placeholder="请输入文章标题"
            maxlength="200"
            show-word-limit
          />
        </el-form-item>

        <el-form-item prop="content">
          <MarkdownEditor
            v-if="!loading"
            v-model="form.content"
            placeholder="开始撰写文章正文..."
          />
        </el-form-item>

        <div class="meta-row">
          <el-form-item label="分类" prop="categoryId" class="meta-item">
            <el-select
              v-model="form.categoryId"
              placeholder="请选择分类"
              filterable
              class="full-width"
              @change="handleCategoryChange"
            >
              <el-option label="无分类" :value="0" />
              <el-option
                v-for="category in store.categoryOptions"
                :key="category.id"
                :label="category.name"
                :value="category.id"
              />
            </el-select>
          </el-form-item>

          <el-form-item label="分类排序" prop="categorySort" class="meta-item">
            <div class="sort-field">
              <span class="sort-value" :class="{ 'is-muted': !hasCategory }">
                {{ categorySortText }}
              </span>
              <el-tooltip
                :content="sortDisabledReason"
                :disabled="sortDisabledReason === ''"
                placement="top"
              >
                <span class="sort-button-wrap">
                  <el-button :icon="Rank" :disabled="!canOpenSortDialog" @click="openSortDialog">
                    调整顺序
                  </el-button>
                </span>
              </el-tooltip>
            </div>
          </el-form-item>

          <el-form-item label="标签" prop="tagIds" class="meta-item">
            <el-select
              v-model="form.tagIds"
              placeholder="请选择标签"
              multiple
              filterable
              clearable
              class="full-width"
            >
              <el-option
                v-for="tag in store.tagOptions"
                :key="tag.id"
                :label="tag.name"
                :value="tag.id"
              />
            </el-select>
          </el-form-item>
        </div>

        <el-form-item label="封面" prop="cover">
          <CoverUpload v-model="form.cover" @uploading="handleCoverUploading" />
        </el-form-item>

        <el-form-item label="摘要" prop="description">
          <el-input
            v-model="form.description"
            type="textarea"
            :rows="4"
            placeholder="请输入文章摘要"
            maxlength="500"
            show-word-limit
          />
        </el-form-item>

        <div class="actions">
          <el-button @click="handleBack">返回</el-button>
          <el-button
            type="primary"
            plain
            :loading="submitting"
            :disabled="coverUploading"
            @click="handleSubmit(0)"
          >
            保存草稿
          </el-button>
          <el-button
            type="primary"
            :loading="submitting"
            :disabled="coverUploading"
            @click="handleSubmit(1)"
          >
            发布
          </el-button>
        </div>
      </el-form>
    </el-card>

    <ArticleSortDialog
      v-model="sortDialogVisible"
      :category-id="form.categoryId"
      :category-name="selectedCategoryName"
      :current-article-id="articleId"
      @saved="handleSortSaved"
    />
  </div>
</template>

<style scoped lang="scss">
.article-edit-page {
  max-width: 1200px;
  margin: 0 auto;
}

.edit-card {
  :deep(.el-card__header) {
    padding: 14px 20px;
  }
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.card-title {
  font-size: 16px;
  font-weight: 600;
  color: var(--el-text-color-primary);
}

.edit-form {
  .el-form-item {
    margin-bottom: 20px;
  }
}

.title-input {
  :deep(.el-input__inner) {
    height: 52px;
    font-size: 24px;
    font-weight: 600;
  }
}

.meta-row {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;

  .meta-item {
    flex: 1;
    min-width: 260px;
  }
}

.full-width {
  width: 100%;
}

.sort-field {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  width: 100%;
}

.sort-value {
  color: var(--el-text-color-regular);
  font-size: 14px;
}

.sort-value.is-muted {
  color: var(--el-text-color-placeholder);
}

.sort-button-wrap {
  display: inline-flex;
  flex-shrink: 0;
}

.actions {
  display: flex;
  flex-wrap: wrap;
  justify-content: flex-end;
  gap: 12px;
  width: 100%;
  padding-top: 8px;
}
</style>
