import { reactive, ref } from 'vue'
import { defineStore } from 'pinia'
import { addTag, deleteTag, deleteTags, getTagPage, updateTag } from '@/api/tag'
import type { TagDTO, TagPageQuery, TagVO } from '@/types/tag'

export const useTagStore = defineStore('tag', () => {
  /** 表格加载状态 */
  const loading = ref(false)

  /** 当前页数据 */
  const records = ref<TagVO[]>([])

  /** 总记录数 */
  const total = ref(0)

  /** 查询条件 */
  const query = reactive<TagPageQuery>({
    name: '',
    page: 1,
    pageSize: 10,
  })

  /** 查询标签列表 */
  async function fetchList() {
    loading.value = true
    try {
      const data = await getTagPage({ ...query })
      records.value = data.records
      total.value = data.total
    } finally {
      loading.value = false
    }
  }

  /** 新增标签，新增后回到第一页 */
  async function createTag(data: TagDTO) {
    await addTag(data)
    query.page = 1
    await fetchList()
  }

  /** 修改标签 */
  async function editTag(data: TagDTO) {
    await updateTag(data)
    await fetchList()
  }

  /** 删除当前页数据后修正页码 */
  async function refreshAfterDelete(removedCount: number) {
    if (records.value.length <= removedCount && query.page > 1) {
      query.page -= 1
    }
    await fetchList()
  }

  /** 删除单个标签 */
  async function removeTag(id: number) {
    await deleteTag(id)
    await refreshAfterDelete(1)
  }

  /** 批量删除标签 */
  async function removeTags(ids: number[]) {
    await deleteTags(ids)
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
    createTag,
    editTag,
    removeTag,
    removeTags,
    resetQuery,
  }
})
