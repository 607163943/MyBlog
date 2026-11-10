<script setup lang="ts">
import { computed, nextTick, onMounted, reactive, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import { ArrowLeft } from '@element-plus/icons-vue'
import MarkdownEditor from '@/components/MarkdownEditor.vue'
import { useArticleStore } from '@/stores/article'
import type { ArticleDTO } from '@/types/article'

const route = useRoute()
const router = useRouter()
const store = useArticleStore()

const formRef = ref<FormInstance>()
const loading = ref(true)
const submitting = ref(false)

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
  categoryId: null as number | null,
  tagIds: [] as number[],
})

const rules: FormRules = {
  title: [
    { required: true, message: '请输入文章标题', trigger: 'blur' },
    { max: 200, message: '文章标题不能超过 200 个字符', trigger: 'blur' },
  ],
  categoryId: [{ required: true, message: '请选择文章分类', trigger: 'change' }],
  content: [{ required: true, message: '请输入文章正文', trigger: 'change' }],
  cover: [{ max: 255, message: '封面地址不能超过 255 个字符', trigger: 'blur' }],
  description: [{ max: 500, message: '文章摘要不能超过 500 个字符', trigger: 'blur' }],
}

function resetForm() {
  form.title = ''
  form.description = ''
  form.content = ''
  form.cover = ''
  form.categoryId = null
  form.tagIds = []
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
      form.categoryId = article.categoryId
      form.tagIds = article.tagIds ?? []
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
  if (form.categoryId === null) {
    ElMessage.warning('请选择文章分类')
    return
  }

  const data: ArticleDTO = {
    title: form.title.trim(),
    description: form.description.trim() || undefined,
    content: form.content,
    cover: form.cover.trim() || undefined,
    categoryId: form.categoryId,
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
            >
              <el-option
                v-for="category in store.categoryOptions"
                :key="category.id"
                :label="category.name"
                :value="category.id"
              />
            </el-select>
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
          <el-input
            v-model="form.cover"
            placeholder="请输入封面图片地址"
            maxlength="255"
            clearable
          />
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
          <el-button type="primary" plain :loading="submitting" @click="handleSubmit(0)">
            保存草稿
          </el-button>
          <el-button type="primary" :loading="submitting" @click="handleSubmit(1)">发布</el-button>
        </div>
      </el-form>
    </el-card>
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

.actions {
  display: flex;
  flex-wrap: wrap;
  justify-content: flex-end;
  gap: 12px;
  width: 100%;
  padding-top: 8px;
}
</style>
