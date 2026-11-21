/** 文件上传返回结果 */
export interface FileAssetUploadVO {
  fileId: number
  url: string
}

/** 文件列表项 */
export interface FileAssetVO {
  id: number
  originalName: string
  contentType: string
  fileSize: number
  createTime: string
  /** 引用次数 */
  refCount: number
}

/** 文件详情（含访问 URL） */
export interface FileAssetDetailVO {
  id: number
  originalName: string
  contentType: string
  fileSize: number
  /** 访问 URL，OSS 等暂不支持的类型为 null */
  url: string | null
}

/** 文件分页查询参数 */
export interface FileAssetPageQuery {
  /** [开始日期, 结束日期]，格式 YYYY-MM-DD，null 表示全部 */
  dateRange: string[] | null
  page: number
  pageSize: number
}
