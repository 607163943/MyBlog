import { defineStore } from 'pinia'
import { ref } from 'vue'

import { getArticlePage } from '@/api/article'
import type { ArticleVO } from '@/types/article'

export const useSearchStore = defineStore('search', () => {
  const keyword = ref('')
  const results = ref<ArticleVO[]>([])
  const total = ref(0)
  const page = ref(1)
  const pageSize = ref(10)
  const loading = ref(false)
  const searched = ref(false)
  const error = ref('')

  function setKeyword(value: string) {
    keyword.value = value
  }

  async function search(value?: string, targetPage = 1) {
    const query = (value ?? keyword.value).trim()
    keyword.value = query
    page.value = targetPage
    error.value = ''
    if (!query) {
      results.value = []
      total.value = 0
      searched.value = false
      return
    }
    loading.value = true
    try {
      const result = await getArticlePage({
        keyword: query,
        page: targetPage,
        pageSize: pageSize.value,
      })
      results.value = result.records
      total.value = result.total
      searched.value = true
    } catch (err) {
      results.value = []
      total.value = 0
      searched.value = true
      error.value = err instanceof Error ? err.message : '搜索失败'
    } finally {
      loading.value = false
    }
  }

  function reset() {
    keyword.value = ''
    results.value = []
    total.value = 0
    page.value = 1
    searched.value = false
    error.value = ''
  }

  return {
    keyword,
    results,
    total,
    page,
    pageSize,
    loading,
    searched,
    error,
    setKeyword,
    search,
    reset,
  }
})
