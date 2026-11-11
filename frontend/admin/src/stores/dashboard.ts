import { ref } from 'vue'
import { defineStore } from 'pinia'
import { getArticleRankPage, getCategoryRankPage, getDashboardStatistics } from '@/api/dashboard'
import type { ArticleRankVO, CategoryRankVO, DashboardStatisticsVO } from '@/types/dashboard'

export const useDashboardStore = defineStore('dashboard', () => {
  /** 统计卡片数据 */
  const stats = ref<DashboardStatisticsVO | null>(null)

  /** 统计卡片加载状态 */
  const statsLoading = ref(false)

  /** 文章浏览量 Top5 */
  const articleTop5 = ref<ArticleRankVO[]>([])

  /** 文章 Top5 加载状态 */
  const articleTopLoading = ref(false)

  /** 分类文章数 Top5 */
  const categoryTop5 = ref<CategoryRankVO[]>([])

  /** 分类 Top5 加载状态 */
  const categoryTopLoading = ref(false)

  /** 查询统计卡片 */
  async function fetchStats() {
    statsLoading.value = true
    try {
      stats.value = await getDashboardStatistics()
    } finally {
      statsLoading.value = false
    }
  }

  /** 查询文章浏览量 Top5 */
  async function fetchArticleTop5() {
    articleTopLoading.value = true
    try {
      const data = await getArticleRankPage({
        title: '',
        page: 1,
        pageSize: 5,
      })
      articleTop5.value = data.records
    } finally {
      articleTopLoading.value = false
    }
  }

  /** 查询分类文章数 Top5 */
  async function fetchCategoryTop5() {
    categoryTopLoading.value = true
    try {
      const data = await getCategoryRankPage({
        name: '',
        page: 1,
        pageSize: 5,
      })
      categoryTop5.value = data.records
    } finally {
      categoryTopLoading.value = false
    }
  }

  /** 并发加载总览数据，单块失败不影响其他区块 */
  async function fetchOverview() {
    await Promise.allSettled([fetchStats(), fetchArticleTop5(), fetchCategoryTop5()])
  }

  return {
    stats,
    statsLoading,
    articleTop5,
    articleTopLoading,
    categoryTop5,
    categoryTopLoading,
    fetchStats,
    fetchArticleTop5,
    fetchCategoryTop5,
    fetchOverview,
  }
})
