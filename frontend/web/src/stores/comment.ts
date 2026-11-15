import { defineStore } from 'pinia'
import { ref } from 'vue'
import { ElMessage } from 'element-plus'

import { getArticleComments, submitArticleComment } from '@/api/comment'
import type { CommentSubmitDTO, CommentVO } from '@/types/comment'

interface ReplyTarget {
  id: number
  nickname: string
}

export const useCommentStore = defineStore('comment', () => {
  const comments = ref<CommentVO[]>([])
  const totalCount = ref(0)
  const loading = ref(false)
  const submitting = ref(false)
  const replyTarget = ref<ReplyTarget | null>(null)

  function countComments(items: CommentVO[]): number {
    return items.reduce((sum, item) => sum + 1 + countComments(item.children ?? []), 0)
  }

  async function load(articleId: number) {
    loading.value = true
    comments.value = []
    totalCount.value = 0
    try {
      comments.value = await getArticleComments(articleId)
      totalCount.value = countComments(comments.value)
    } catch {
      comments.value = []
      totalCount.value = 0
    } finally {
      loading.value = false
    }
  }

  function setReplyTarget(id: number, nickname: string) {
    replyTarget.value = { id, nickname }
  }

  function clearReplyTarget() {
    replyTarget.value = null
  }

  async function submit(articleId: number, data: CommentSubmitDTO) {
    submitting.value = true
    try {
      await submitArticleComment(articleId, data)
      ElMessage.success('评论已提交，审核通过后展示')
      clearReplyTarget()
      return true
    } catch {
      return false
    } finally {
      submitting.value = false
    }
  }

  return {
    comments,
    totalCount,
    loading,
    submitting,
    replyTarget,
    load,
    setReplyTarget,
    clearReplyTarget,
    submit,
  }
})
