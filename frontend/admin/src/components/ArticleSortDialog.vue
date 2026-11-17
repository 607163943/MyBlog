<script setup lang="ts">
import { computed, ref, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { Rank } from '@element-plus/icons-vue'
import { VueDraggable } from 'vue-draggable-plus'
import { useArticleStore } from '@/stores/article'
import type { ArticleSortVO } from '@/types/article'

const props = defineProps<{
  modelValue: boolean
  categoryId: number
  categoryName: string
  currentArticleId?: number | null
}>()

const emit = defineEmits<{
  'update:modelValue': [value: boolean]
  saved: [list: ArticleSortVO[]]
}>()

const store = useArticleStore()
const loading = ref(false)
const submitting = ref(false)
const sortList = ref<ArticleSortVO[]>([])
const originalIds = ref<number[]>([])

const visible = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value),
})

const title = computed(() =>
  props.categoryName ? `调整分类内排序 · ${props.categoryName}` : '调整分类内排序',
)

/** 顺序是否发生变化：未变化时禁用保存，避免无意义的更新 */
const hasChanges = computed(() =>
  sortList.value.some((item, index) => item.id !== originalIds.value[index]),
)

async function loadList() {
  loading.value = true
  try {
    const list = await store.fetchArticleSortList(props.categoryId)
    sortList.value = list
    originalIds.value = list.map((item) => item.id)
  } catch {
    sortList.value = []
    originalIds.value = []
  } finally {
    loading.value = false
  }
}

watch(
  () => props.modelValue,
  (value) => {
    if (value) {
      loadList()
    }
  },
)

async function handleSubmit() {
  if (!hasChanges.value || sortList.value.length === 0) {
    return
  }

  submitting.value = true
  try {
    const ids = sortList.value.map((item) => item.id)
    await store.saveArticleSort(props.categoryId, ids)
    ElMessage.success('排序保存成功')
    emit(
      'saved',
      sortList.value.map((item, index) => ({ ...item, categorySort: index + 1 })),
    )
    visible.value = false
  } catch {
    // 请求错误已由 request.ts 统一提示
  } finally {
    submitting.value = false
  }
}
</script>

<template>
  <el-dialog
    v-model="visible"
    :title="title"
    width="620px"
    :close-on-click-modal="false"
    class="article-sort-dialog"
  >
    <div class="sort-tip">拖动文章调整顺序，保存后序号将按当前顺序重新编号并立即生效。</div>

    <div v-loading="loading" class="sort-list-wrap">
      <VueDraggable
        v-if="sortList.length > 0"
        v-model="sortList"
        :animation="150"
        handle=".sort-drag-handle"
        ghost-class="sort-ghost"
        class="sort-list"
      >
        <div
          v-for="(item, index) in sortList"
          :key="item.id"
          class="sort-item"
          :class="{ 'is-current': item.id === currentArticleId }"
        >
          <el-icon class="sort-drag-handle" title="拖动调整顺序"><Rank /></el-icon>
          <span class="sort-index">{{ index + 1 }}</span>
          <span class="sort-title" :title="item.title">{{ item.title }}</span>
          <el-tag v-if="item.id === currentArticleId" type="primary" size="small">
            当前文章
          </el-tag>
          <span v-if="item.categorySort == null" class="sort-unnumbered">未编号</span>
        </div>
      </VueDraggable>

      <el-empty v-else-if="!loading" description="该分类下暂无已发布文章" :image-size="80" />
    </div>

    <template #footer>
      <div class="sort-footer">
        <span class="sort-footer-tip">保存后立即生效</span>
        <div>
          <el-button @click="visible = false">取消</el-button>
          <el-button
            type="primary"
            :loading="submitting"
            :disabled="!hasChanges || sortList.length === 0"
            @click="handleSubmit"
          >
            保存
          </el-button>
        </div>
      </div>
    </template>
  </el-dialog>
</template>

<style scoped lang="scss">
.sort-tip {
  margin-bottom: 12px;
  color: var(--el-text-color-secondary);
  font-size: 13px;
  line-height: 1.6;
}

.sort-list-wrap {
  min-height: 120px;
  max-height: 60vh;
  overflow-y: auto;
}

.sort-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
  padding: 2px;
}

.sort-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 10px 12px;
  border: 1px solid var(--el-border-color-light);
  border-radius: 8px;
  background: var(--el-bg-color);
  transition:
    border-color 0.15s,
    background-color 0.15s,
    box-shadow 0.15s;
}

.sort-item:hover {
  border-color: var(--el-color-primary-light-5);
}

.sort-item.is-current {
  border-color: var(--el-color-primary);
  background: var(--el-color-primary-light-9);
}

.sort-drag-handle {
  flex-shrink: 0;
  color: var(--el-text-color-secondary);
  font-size: 18px;
  cursor: grab;
}

.sort-drag-handle:active {
  cursor: grabbing;
}

.sort-index {
  display: inline-flex;
  flex-shrink: 0;
  align-items: center;
  justify-content: center;
  width: 28px;
  height: 28px;
  border-radius: 50%;
  background: var(--el-fill-color-light);
  color: var(--el-text-color-regular);
  font-size: 13px;
}

.sort-title {
  flex: 1;
  min-width: 0;
  overflow: hidden;
  color: var(--el-text-color-primary);
  text-overflow: ellipsis;
  white-space: nowrap;
}

.sort-unnumbered {
  flex-shrink: 0;
  color: var(--el-text-color-secondary);
  font-size: 12px;
}

.sort-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.sort-footer-tip {
  color: var(--el-text-color-secondary);
  font-size: 12px;
}

.sort-ghost {
  border-style: dashed;
  border-color: var(--el-color-primary);
  opacity: 0.5;
}
</style>
