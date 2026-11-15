import type { ArticleBriefVO } from './article'

/** 分类信息 */
export interface CategoryVO {
  id: number
  name: string
  description: string | null
  articleCount: number
  hotArticles: ArticleBriefVO[]
}
