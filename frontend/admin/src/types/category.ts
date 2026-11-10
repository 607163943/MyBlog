/** 分类返回实体 */
export interface CategoryVO {
  id: number
  name: string
  description: string | null
  sort: number
  createTime: string
  updateTime: string
}

/** 分类新增 / 修改参数 */
export interface CategoryDTO {
  id?: number
  name: string
  description?: string
  sort?: number
}

/** 分类分页查询参数 */
export interface CategoryPageQuery {
  name: string
  page: number
  pageSize: number
}
