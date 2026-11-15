import { defineStore } from 'pinia'
import { ref } from 'vue'

import { getArchiveMonthArticles, getArchiveOverview } from '@/api/archive'
import type { ArchiveYearVO } from '@/types/archive'
import type { ArticleVO } from '@/types/article'

export const useArchiveStore = defineStore('archive', () => {
  const years = ref<ArchiveYearVO[]>([])
  const loading = ref(false)
  const loaded = ref(false)

  const monthArticles = ref<ArticleVO[]>([])
  const monthLoading = ref(false)
  const monthError = ref('')

  async function fetchOverview(force = false) {
    if (loading.value || (loaded.value && !force)) {
      return
    }
    loading.value = true
    try {
      years.value = await getArchiveOverview()
      loaded.value = true
    } catch {
      years.value = []
    } finally {
      loading.value = false
    }
  }

  async function fetchMonth(year: number, month: number) {
    monthLoading.value = true
    monthError.value = ''
    monthArticles.value = []
    try {
      monthArticles.value = await getArchiveMonthArticles(year, month)
    } catch (error) {
      monthError.value = error instanceof Error ? error.message : '归档加载失败'
    } finally {
      monthLoading.value = false
    }
  }

  return {
    years,
    loading,
    loaded,
    monthArticles,
    monthLoading,
    monthError,
    fetchOverview,
    fetchMonth,
  }
})
