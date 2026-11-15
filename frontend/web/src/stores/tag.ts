import { defineStore } from 'pinia'
import { ref } from 'vue'

import { getArticlePage } from '@/api/article'
import { getTagDetail, getTagList } from '@/api/tag'
import type { ArticleVO } from '@/types/article'
import type { TagVO } from '@/types/tag'

export const useTagStore = defineStore('tag', () => {
  const tags = ref<TagVO[]>([])
  const listLoading = ref(false)
  const loaded = ref(false)

  const current = ref<TagVO | null>(null)
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
      tags.value = await getTagList()
      loaded.value = true
    } catch {
      tags.value = []
    } finally {
      listLoading.value = false
    }
  }

  async function fetchDetail(id: number) {
    currentLoading.value = true
    currentError.value = ''
    current.value = null
    try {
      current.value = await getTagDetail(id)
    } catch (error) {
      currentError.value = error instanceof Error ? error.message : '标签加载失败'
    } finally {
      currentLoading.value = false
    }
  }

  async function fetchArticles(tagId: number, targetPage = 1) {
    articlesLoading.value = true
    try {
      const result = await getArticlePage({
        tagId,
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
    tags,
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
