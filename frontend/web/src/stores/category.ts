import { defineStore } from 'pinia'
import { ref } from 'vue'

import { getArticlePage } from '@/api/article'
import { getCategoryDetail, getCategoryList } from '@/api/category'
import type { ArticleVO } from '@/types/article'
import type { CategoryVO } from '@/types/category'

export const useCategoryStore = defineStore('category', () => {
  const categories = ref<CategoryVO[]>([])
  const listLoading = ref(false)
  const loaded = ref(false)

  const current = ref<CategoryVO | null>(null)
  const currentLoading = ref(false)
  const currentError = ref('')

  const articles = ref<ArticleVO[]>([])
  const total = ref(0)
  const page = ref(1)
  const pageSize = ref(10)
  const articlesLoading = ref(false)

  async function fetchList(force = false) {
    if (listLoading.value || (loaded.value && !force)) {
      return
    }
    listLoading.value = true
    try {
      categories.value = await getCategoryList()
      loaded.value = true
    } catch {
      categories.value = []
    } finally {
      listLoading.value = false
    }
  }

  async function fetchDetail(id: number) {
    currentLoading.value = true
    currentError.value = ''
    current.value = null
    try {
      current.value = await getCategoryDetail(id)
    } catch (error) {
      currentError.value = error instanceof Error ? error.message : '分类加载失败'
    } finally {
      currentLoading.value = false
    }
  }

  async function fetchArticles(categoryId: number, targetPage = 1) {
    articlesLoading.value = true
    try {
      const result = await getArticlePage({
        categoryId,
        page: targetPage,
        pageSize: pageSize.value,
      })
      articles.value = result.records
      total.value = result.total
      page.value = targetPage
    } catch {
      articles.value = []
      total.value = 0
    } finally {
      articlesLoading.value = false
    }
  }

  function resetArticles() {
    articles.value = []
    total.value = 0
    page.value = 1
  }

  return {
    categories,
    listLoading,
    loaded,
    current,
    currentLoading,
    currentError,
    articles,
    total,
    page,
    pageSize,
    articlesLoading,
    fetchList,
    fetchDetail,
    fetchArticles,
    resetArticles,
  }
})
