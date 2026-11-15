/** 通用分页参数 */
export interface PageQuery {
  page: number
  pageSize: number
}

/** 通用分页返回结构 */
export interface PageVO<T> {
  records: T[]
  total: number
  size: number
  pages: number
}
