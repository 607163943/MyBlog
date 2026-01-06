import request from '@/utils/request'

// 图片上传
export const uploadImageService = (file) => {
  return request.post('/upload/image', file, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}
