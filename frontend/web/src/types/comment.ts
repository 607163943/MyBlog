/** 前台评论信息（二级树） */
export interface CommentVO {
  id: number
  parentId: number
  replyToId: number
  nickname: string
  content: string
  replyToNickname: string | null
  createTime: string
  children: CommentVO[]
}

/** 评论提交参数 */
export interface CommentSubmitDTO {
  nickname: string
  email?: string
  content: string
  replyToId?: number
}
