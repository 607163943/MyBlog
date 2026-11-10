/** 标签返回实体 */
export interface TagVO {
  id: number
  name: string
  createTime: string
  updateTime: string
}

/** 标签新增 / 修改参数 */
export interface TagDTO {
  id?: number
  name: string
}

/** 标签分页查询参数 */
export interface TagPageQuery {
  name: string
  page: number
  pageSize: number
}
