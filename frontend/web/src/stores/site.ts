import { defineStore } from 'pinia'
import { ref } from 'vue'

import { getLatestArticles } from '@/api/article'
import { getTopCategories } from '@/api/category'
import { getTopTags } from '@/api/tag'
import { getSiteStatistics } from '@/api/site'
import type { ArticleBriefVO } from '@/types/article'
import type { CategoryVO } from '@/types/category'
import type { SiteStatisticsVO } from '@/types/site'
import type { TagVO } from '@/types/tag'

export const useSiteStore = defineStore('site', () => {
  const statistics = ref<SiteStatisticsVO | null>(null)
  const latestArticles = ref<ArticleBriefVO[]>([])
  const topCategories = ref<CategoryVO[]>([])
  const topTags = ref<TagVO[]>([])
  const loading = ref(false)
  const loaded = ref(false)

  /** 加载侧栏与站点统计信息，单块失败不影响其他区域 */
  async function load(force = false) {
    if (loading.value || (loaded.value && !force)) {
      return
    }
    loading.value = true
    try {
      const [statisticsResult, latestResult, categoryResult, tagResult] = await Promise.allSettled([
        getSiteStatistics(),
        getLatestArticles(8),
        getTopCategories(5),
        getTopTags(10),
      ])
      if (statisticsResult.status === 'fulfilled') {
        statistics.value = statisticsResult.value
      }
      if (latestResult.status === 'fulfilled') {
        latestArticles.value = latestResult.value
      }
      if (categoryResult.status === 'fulfilled') {
        topCategories.value = categoryResult.value
      }
      if (tagResult.status === 'fulfilled') {
        topTags.value = tagResult.value
      }
      loaded.value = true
    } finally {
      loading.value = false
    }
  }

  return {
    statistics,
    latestArticles,
    topCategories,
    topTags,
    loading,
    loaded,
    load,
  }
})
