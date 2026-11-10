import { reactive, ref } from 'vue'
import { defineStore } from 'pinia'
import {
  addCategory,
  deleteCategories,
  deleteCategory,
  getCategoryPage,
  updateCategory,
  type CategoryDTO,
  type CategoryPageQuery,
  type CategoryVO,
} from '@/api/category'

export const useCategoryStore = defineStore('category', () => {
  /** 表格加载状态 */
  const loading = ref(false)

  /** 当前页数据 */
  const records = ref<CategoryVO[]>([])

  /** 总记录数 */
  const total = ref(0)

  /** 查询条件 */
  const query = reactive<CategoryPageQuery>({
    name: '',
    page: 1,
    pageSize: 10,
  })

  /** 查询分类列表 */
  async function fetchList() {
    loading.value = true
    try {
      const data = await getCategoryPage({ ...query })
      records.value = data.records
      total.value = data.total
    } finally {
      loading.value = false
    }
  }

  /** 新增分类，新增后回到第一页 */
  async function createCategory(data: CategoryDTO) {
    await addCategory(data)
    query.page = 1
    await fetchList()
  }

  /** 修改分类 */
  async function editCategory(data: CategoryDTO) {
    await updateCategory(data)
    await fetchList()
  }

  /** 删除当前页数据后修正页码 */
  async function refreshAfterDelete(removedCount: number) {
    if (records.value.length <= removedCount && query.page > 1) {
      query.page -= 1
    }
    await fetchList()
  }

  /** 删除单个分类 */
  async function removeCategory(id: number) {
    await deleteCategory(id)
    await refreshAfterDelete(1)
  }

  /** 批量删除分类 */
  async function removeCategories(ids: number[]) {
    await deleteCategories(ids)
    await refreshAfterDelete(ids.length)
  }

  /** 重置查询条件并重新查询 */
  async function resetQuery() {
    query.name = ''
    query.page = 1
    await fetchList()
  }

  return {
    loading,
    records,
    total,
    query,
    fetchList,
    createCategory,
    editCategory,
    removeCategory,
    removeCategories,
    resetQuery,
  }
})
