import { ref } from 'vue'
import { defineStore } from 'pinia'
import {
  getArticleContribution,
  getArticleRankPage,
  getCategoryRankPage,
  getDashboardStatistics,
  getViewTrend,
} from '@/api/dashboard'
import type {
  ArticleContributionVO,
  ArticleRankVO,
  CategoryRankVO,
  DashboardStatisticsVO,
  ViewTrendVO,
} from '@/types/dashboard'

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

  /** 文章每日浏览量趋势（最近 30 天） */
  const viewTrend = ref<ViewTrendVO[]>([])

  /** 趋势图加载状态 */
  const viewTrendLoading = ref(false)

  /** 当前用户最近一年的文章每日贡献 */
  const contributions = ref<ArticleContributionVO[]>([])

  /** 贡献热力图加载状态 */
  const contributionLoading = ref(false)

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

  /** 查询文章每日浏览量趋势 */
  async function fetchViewTrend() {
    viewTrendLoading.value = true
    try {
      viewTrend.value = await getViewTrend()
    } finally {
      viewTrendLoading.value = false
    }
  }

  /** 查询当前用户最近一年的文章每日贡献 */
  async function fetchContribution() {
    contributionLoading.value = true
    try {
      contributions.value = await getArticleContribution()
    } finally {
      contributionLoading.value = false
    }
  }

  /** 并发加载总览数据，单块失败不影响其他区块 */
  async function fetchOverview() {
    await Promise.allSettled([
      fetchStats(),
      fetchArticleTop5(),
      fetchCategoryTop5(),
      fetchViewTrend(),
      fetchContribution(),
    ])
  }

  return {
    stats,
    statsLoading,
    articleTop5,
    articleTopLoading,
    categoryTop5,
    categoryTopLoading,
    viewTrend,
    viewTrendLoading,
    contributions,
    contributionLoading,
    fetchStats,
    fetchArticleTop5,
    fetchCategoryTop5,
    fetchViewTrend,
    fetchContribution,
    fetchOverview,
  }
})
