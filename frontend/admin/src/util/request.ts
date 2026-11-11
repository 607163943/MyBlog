import axios, { type AxiosRequestConfig } from 'axios'
import { ElMessage } from 'element-plus'

/** 后端统一响应结构 */
export interface Result<T = unknown> {
  code: number
  msg: string
  data: T
}

/** 登录状态在 localStorage 中的持久化键名 */
const USER_STORAGE_KEY = 'user'

/** axios 请求实例，开发环境由 Vite 代理 /api 到后端 8080 端口 */
const instance = axios.create({
  baseURL: '/api',
  timeout: 10000,
})

/** 清理本地登录状态并跳转登录页 */
function redirectToLogin() {
  try {
    localStorage.removeItem(USER_STORAGE_KEY)
  } catch {
    // 忽略存储不可用的情况
  }
  if (window.location.pathname === '/login') {
    return
  }
  const redirect = encodeURIComponent(window.location.pathname + window.location.search)
  window.location.replace(`/login?redirect=${redirect}`)
}

// 自动携带登录 token，请求头名称为登录接口返回的 tokenName
instance.interceptors.request.use(async (config) => {
  const { useUserStore } = await import('@/stores/user')
  const userStore = useUserStore()
  if (userStore.tokenName !== '' && userStore.tokenValue !== '') {
    config.headers.set(userStore.tokenName, userStore.tokenValue)
  }
  return config
})

instance.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      redirectToLogin()
    }
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
      if (result.code === 401) {
        redirectToLogin()
      }
      ElMessage.error(result.msg || '请求失败')
      return Promise.reject(new Error(result.msg || '请求失败'))
    }
    return result.data
  })
}

export default instance
