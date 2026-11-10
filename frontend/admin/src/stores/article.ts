import { reactive, ref } from 'vue'
import { defineStore } from 'pinia'
import {
  addArticle,
  deleteArticle,
  deleteArticles,
  getArticleById,
  getArticlePage,
  updateArticle,
} from '@/api/article'
import { getCategoryOptions } from '@/api/category'
import { getTagOptions } from '@/api/tag'
import type { ArticleDTO, ArticlePageQuery, ArticleVO } from '@/types/article'
import type { CategoryVO } from '@/types/category'
import type { TagVO } from '@/types/tag'

export const useArticleStore = defineStore('article', () => {
  /** 表格加载状态 */
  const loading = ref(false)

  /** 当前页数据 */
  const records = ref<ArticleVO[]>([])

  /** 总记录数 */
  const total = ref(0)

  /** 查询条件 */
  const query = reactive<ArticlePageQuery>({
    title: '',
    status: '',
    page: 1,
    pageSize: 10,
  })

  /** 分类下拉选项 */
  const categoryOptions = ref<CategoryVO[]>([])

  /** 标签下拉选项 */
  const tagOptions = ref<TagVO[]>([])

  /** 查询文章详情 */
  function fetchArticleDetail(id: number) {
    return getArticleById(id)
  }

  /** 查询编辑页所需的分类、标签选项 */
  async function fetchArticleOptions() {
    const [categories, tags] = await Promise.all([getCategoryOptions(), getTagOptions()])
    categoryOptions.value = categories
    tagOptions.value = tags
  }

  /** 查询文章列表 */
  async function fetchList() {
    loading.value = true
    try {
      const data = await getArticlePage({ ...query })
      records.value = data.records
      total.value = data.total
    } finally {
      loading.value = false
    }
  }

  /** 新增文章，新增后回到第一页 */
  async function createArticle(data: ArticleDTO) {
    await addArticle(data)
    query.page = 1
    await fetchList()
  }

  /** 修改文章 */
  async function editArticle(data: ArticleDTO) {
    await updateArticle(data)
    await fetchList()
  }

  /** 删除当前页数据后修正页码 */
  async function refreshAfterDelete(removedCount: number) {
    if (records.value.length <= removedCount && query.page > 1) {
      query.page -= 1
    }
    await fetchList()
  }

  /** 删除单个文章 */
  async function removeArticle(id: number) {
    await deleteArticle(id)
    await refreshAfterDelete(1)
  }

  /** 批量删除文章 */
  async function removeArticles(ids: number[]) {
    await deleteArticles(ids)
    await refreshAfterDelete(ids.length)
  }

  /** 重置查询条件并重新查询 */
  async function resetQuery() {
    query.title = ''
    query.status = ''
    query.page = 1
    await fetchList()
  }

  return {
    loading,
    records,
    total,
    query,
    categoryOptions,
    tagOptions,
    fetchArticleDetail,
    fetchArticleOptions,
    fetchList,
    createArticle,
    editArticle,
    removeArticle,
    removeArticles,
    resetQuery,
  }
})
