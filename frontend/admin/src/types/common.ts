/** 通用分页返回结构 */
export interface PageVO<T> {
  records: T[]
  total: number
  size: number
  pages: number
}
