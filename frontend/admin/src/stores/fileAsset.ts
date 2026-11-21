import { reactive, ref } from 'vue'
import { defineStore } from 'pinia'
import { deleteFileAsset, getFileAssetById, getFileAssetPage } from '@/api/fileAsset'
import type { FileAssetPageQuery, FileAssetVO } from '@/types/fileAsset'

export const useFileAssetStore = defineStore('file-asset', () => {
  /** 表格加载状态 */
  const loading = ref(false)

  /** 当前页数据 */
  const records = ref<FileAssetVO[]>([])

  /** 总记录数 */
  const total = ref(0)

  /** 查询条件 */
  const query = reactive<FileAssetPageQuery>({
    dateRange: null,
    page: 1,
    pageSize: 10,
  })

  /** 查询文件详情（含访问 URL） */
  function fetchFileAssetDetail(id: number) {
    return getFileAssetById(id)
  }

  /** 查询文件列表 */
  async function fetchList() {
    loading.value = true
    try {
      const data = await getFileAssetPage({ ...query })
      records.value = data.records
      total.value = data.total
    } finally {
      loading.value = false
    }
  }

  /** 删除单个文件，删除后修正页码 */
  async function removeFileAsset(id: number) {
    await deleteFileAsset(id)
    if (records.value.length <= 1 && query.page > 1) {
      query.page -= 1
    }
    await fetchList()
  }

  /** 重置查询条件并重新查询 */
  async function resetQuery() {
    query.dateRange = null
    query.page = 1
    await fetchList()
  }

  return {
    loading,
    records,
    total,
    query,
    fetchList,
    fetchFileAssetDetail,
    removeFileAsset,
    resetQuery,
  }
})
