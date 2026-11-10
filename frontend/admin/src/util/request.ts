import axios, { type AxiosRequestConfig } from 'axios'
import { ElMessage } from 'element-plus'

/** 后端统一响应结构 */
export interface Result<T = unknown> {
  code: number
  msg: string
  data: T
}

/** axios 请求实例，开发环境由 Vite 代理 /api 到后端 8080 端口 */
const instance = axios.create({
  baseURL: '/api',
  timeout: 10000,
})

instance.interceptors.response.use(
  (response) => response,
  (error) => {
    const msg = error.response?.data?.msg || error.message || '网络异常，请稍后重试'
    ElMessage.error(msg)
    return Promise.reject(error)
  },
)

/**
 * 发起请求并解包统一响应结构
 */
export function request<T = unknown>(config: AxiosRequestConfig): Promise<T> {
  return instance.request<Result<T>>(config).then((response) => {
    const result = response.data
    if (result.code !== 200) {
      ElMessage.error(result.msg || '请求失败')
      return Promise.reject(new Error(result.msg || '请求失败'))
    }
    return result.data
  })
}

export default instance
