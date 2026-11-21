import { request } from '@/util/request'
import type { FileAssetDetailVO, FileAssetPageQuery, FileAssetVO } from '@/types/fileAsset'
import type { PageVO } from '@/types/common'

/** 分页查询文件 */
export function getFileAssetPage(params: FileAssetPageQuery) {
  return request<PageVO<FileAssetVO>>({
    url: '/file-asset/page',
    method: 'get',
    params: {
      startDate: params.dateRange?.[0],
      endDate: params.dateRange?.[1],
      page: params.page,
      pageSize: params.pageSize,
    },
  })
}

/** 根据 ID 查询文件详情（含访问 URL） */
export function getFileAssetById(id: number) {
  return request<FileAssetDetailVO>({
    url: `/file-asset/${id}`,
    method: 'get',
  })
}

/** 删除文件：存在引用时后端会拒绝 */
export function deleteFileAsset(id: number) {
  return request<void>({
    url: `/file-asset/${id}`,
    method: 'delete',
  })
}
