import axios from 'axios'
import { useUserStore } from '@/stores'
import { message } from 'ant-design-vue'
import { useRouter } from 'vue-router'
import { isEmpty } from 'es-toolkit/compat'

const instance = axios.create({
  baseURL: import.meta.env.VITE_BASE_URL,
  timeout: 10000
})

// 添加请求拦截器
instance.interceptors.request.use(
  function (config) {
    // 在发送请求之前做些什么
    const userStore = useUserStore()
    if (userStore.token) {
      config.headers.token = userStore.token
    }
    return config
  },
  function (error) {
    // 对请求错误做些什么
    return Promise.reject(error)
  }
)

// 添加响应拦截器
instance.interceptors.response.use(
  function (response) {
    // 2xx 范围内的状态码都会触发该函数。
    // 对响应数据做点什么
    return response
  },
  function (error) {
    // 超出 2xx 范围的状态码都会触发该函数。
    // 对响应错误做点什么
    const res = error.response
    if (isEmpty(res)) {
      message.error('请求超时')
      return Promise.reject(error)
    }
    // 用户未认证
    if (res.status === 401) {
      const router = useRouter()
      // 清除用户信息
      const userStore = useUserStore()
      userStore.setUserInfo(null)
      userStore.setToken('')
      // 跳转到登录页面
      router.push('/login')
    }
    if (res.data.msg) {
      message.error(res.data.msg)
    } else {
      message.error('未知错误，请联系管理员')
    }
    return Promise.reject(error)
  }
)

export default instance
