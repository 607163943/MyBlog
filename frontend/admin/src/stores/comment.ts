import { reactive, ref } from 'vue'
import { defineStore } from 'pinia'
import { deleteComment, deleteComments, getCommentPage, updateComment } from '@/api/comment'
import type { CommentDTO, CommentPageQuery, CommentVO } from '@/types/comment'

export const useCommentStore = defineStore('comment', () => {
  /** 表格加载状态 */
  const loading = ref(false)

  /** 当前页数据 */
  const records = ref<CommentVO[]>([])

  /** 总记录数 */
  const total = ref(0)

  /** 查询条件 */
  const query = reactive<CommentPageQuery>({
    articleTitle: '',
    status: '',
    page: 1,
    pageSize: 10,
  })

  /** 查询评论列表 */
  async function fetchList() {
    loading.value = true
    try {
      const data = await getCommentPage({ ...query })
      records.value = data.records
      total.value = data.total
    } finally {
      loading.value = false
    }
  }

  /** 修改评论 */
  async function editComment(data: CommentDTO) {
    await updateComment(data)
    await fetchList()
  }

  /** 删除当前页数据后修正页码 */
  async function refreshAfterDelete(removedCount: number) {
    if (records.value.length <= removedCount && query.page > 1) {
      query.page -= 1
    }
    await fetchList()
  }

  /** 删除单个评论 */
  async function removeComment(id: number) {
    await deleteComment(id)
    await refreshAfterDelete(1)
  }

  /** 批量删除评论 */
  async function removeComments(ids: number[]) {
    await deleteComments(ids)
    await refreshAfterDelete(ids.length)
  }

  /** 重置查询条件并重新查询 */
  async function resetQuery() {
    query.articleTitle = ''
    query.status = ''
    query.page = 1
    await fetchList()
  }

  return {
    loading,
    records,
    total,
    query,
    fetchList,
    editComment,
    removeComment,
    removeComments,
    resetQuery,
  }
})
