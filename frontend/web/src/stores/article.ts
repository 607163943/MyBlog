import { defineStore } from 'pinia'
import { ref } from 'vue'

import { getArticleDetail, getArticlePage, getCategoryArticleIndex } from '@/api/article'
import type { ArticleBriefVO, ArticleDetailVO, ArticleVO } from '@/types/article'

export const useArticleStore = defineStore('article', () => {
  // 首页文章列表
  const records = ref<ArticleVO[]>([])
  const total = ref(0)
  const page = ref(1)
  const pageSize = ref(8)
  const loading = ref(false)

  // 文章详情
  const detail = ref<ArticleDetailVO | null>(null)
  const detailLoading = ref(false)
  const detailError = ref('')

  // 文章详情页的分类目录（按分类 ID 缓存）
  const categoryIndex = ref<Record<number, ArticleBriefVO[]>>({})
  const categoryIndexLoading = ref<Record<number, boolean>>({})

  async function fetchHomePage(targetPage = page.value) {
    loading.value = true
    try {
      const result = await getArticlePage({
        page: targetPage,
        pageSize: pageSize.value,
      })
      records.value = result.records
      total.value = result.total
      page.value = targetPage
    } catch {
      records.value = []
      total.value = 0
    } finally {
      loading.value = false
    }
  }

  async function loadDetail(id: number) {
    detailLoading.value = true
    detailError.value = ''
    detail.value = null
    try {
      detail.value = await getArticleDetail(id)
    } catch (error) {
      detailError.value = error instanceof Error ? error.message : '文章加载失败'
    } finally {
      detailLoading.value = false
    }
  }

  async function ensureCategoryIndex(categoryId: number, force = false) {
    if (!categoryId) {
      return
    }
    if (categoryIndex.value[categoryId] && !force) {
      return
    }
    if (categoryIndexLoading.value[categoryId]) {
      return
    }
    categoryIndexLoading.value = {
      ...categoryIndexLoading.value,
      [categoryId]: true,
    }
    try {
      const items = await getCategoryArticleIndex(categoryId)
      categoryIndex.value = { ...categoryIndex.value, [categoryId]: items }
    } catch {
      categoryIndex.value = { ...categoryIndex.value, [categoryId]: [] }
    } finally {
      categoryIndexLoading.value = {
        ...categoryIndexLoading.value,
        [categoryId]: false,
      }
    }
  }

  function resetDetail() {
    detail.value = null
    detailError.value = ''
  }

  return {
    records,
    total,
    page,
    pageSize,
    loading,
    detail,
    detailLoading,
    detailError,
    categoryIndex,
    categoryIndexLoading,
    fetchHomePage,
    loadDetail,
    ensureCategoryIndex,
    resetDetail,
  }
})
