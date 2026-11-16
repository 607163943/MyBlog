import { ElMessage } from 'element-plus'
import { request } from '@/util/request'

/** 与后端 UploadServiceImpl.MAX_IMAGE_SIZE 保持一致 */
export const MAX_IMAGE_SIZE = 5 * 1024 * 1024

const ALLOWED_IMAGE_EXTENSIONS = new Set(['jpg', 'jpeg', 'png', 'gif', 'webp', 'bmp'])

const ALLOWED_IMAGE_TYPES = new Set([
  'image/jpeg',
  'image/png',
  'image/gif',
  'image/webp',
  'image/bmp',
])

/** 文件选择框 accept 列表 */
export const IMAGE_ACCEPT =
  '.jpg,.jpeg,.png,.gif,.webp,.bmp,image/jpeg,image/png,image/gif,image/webp,image/bmp'

/**
 * 前端前置校验，返回错误提示；通过返回 null。
 * 仅用于提升体验，最终仍以后端校验为准。
 */
export function validateImageFile(file: File): string | null {
  const dotIndex = file.name.lastIndexOf('.')
  const extension = dotIndex >= 0 ? file.name.slice(dotIndex + 1).toLowerCase() : ''
  if (!ALLOWED_IMAGE_EXTENSIONS.has(extension)) {
    return '仅支持 jpg、jpeg、png、gif、webp、bmp 格式的图片'
  }
  if (!ALLOWED_IMAGE_TYPES.has(file.type.toLowerCase())) {
    return '图片类型不正确'
  }
  if (file.size > MAX_IMAGE_SIZE) {
    return '图片大小不能超过 5MB'
  }
  return null
}

/**
 * 上传图片，成功返回后端生成的相对 URL，如 /image/2026/11/15/uuid.png。
 * 不要手动设置 Content-Type，浏览器会为 FormData 自动生成 multipart boundary。
 */
export function uploadImage(file: File): Promise<string> {
  const message = validateImageFile(file)
  if (message) {
    ElMessage.warning(message)
    return Promise.reject(new Error(message))
  }

  const formData = new FormData()
  formData.append('file', file)
  return request<string>({
    url: '/upload/image',
    method: 'post',
    data: formData,
    timeout: 30000,
  })
}
