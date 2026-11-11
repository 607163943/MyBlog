import { request } from '@/util/request'
import type { LoginDTO, LoginVO, UserInfoVO } from '@/types/user'

/** 用户登录 */
export function login(data: LoginDTO) {
  return request<LoginVO>({
    url: '/user/login',
    method: 'post',
    data,
  })
}

/** 退出登录 */
export function logout() {
  return request<void>({
    url: '/user/logout',
    method: 'post',
  })
}

/** 获取当前登录用户信息 */
export function getUserInfo() {
  return request<UserInfoVO>({
    url: '/user/info',
    method: 'get',
  })
}
