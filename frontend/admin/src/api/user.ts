import { request } from '@/util/request'
import type {
  CaptchaVO,
  ChangePasswordDTO,
  LoginDTO,
  LoginVO,
  UpdateUserInfoDTO,
  UserInfoVO,
} from '@/types/user'

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

/** 获取图形验证码 */
export function getCaptcha() {
  return request<CaptchaVO>({
    url: '/user/captcha',
    method: 'get',
  })
}

/** 修改当前登录用户的基本信息 */
export function updateUserInfo(data: UpdateUserInfoDTO) {
  return request<void>({
    url: '/user/info',
    method: 'put',
    data,
  })
}

/** 修改当前登录用户的密码 */
export function changePassword(data: ChangePasswordDTO) {
  return request<void>({
    url: '/user/password',
    method: 'put',
    data,
  })
}
