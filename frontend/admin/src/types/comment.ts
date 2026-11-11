/** 评论返回实体 */
export interface CommentVO {
  id: number
  articleId: number
  articleTitle: string | null
  /** 所属一级评论 ID：0 表示自身为一级评论 */
  parentId: number
  /** 被回复的评论 ID：0 表示无回复对象 */
  replyToId: number
  nickname: string
  email: string | null
  content: string
  /** 审核状态：0-待审核，1-已通过 */
  status: number
  createTime: string
  updateTime: string
}

/** 评论修改参数 */
export interface CommentDTO {
  id: number
  nickname: string
  email?: string
  content: string
  status: number
}

/** 评论分页查询参数 */
export interface CommentPageQuery {
  /** 所属文章标题 */
  articleTitle: string
  /** 审核状态：0-待审核，1-已通过，空串表示全部 */
  status: number | ''
  page: number
  pageSize: number
}
