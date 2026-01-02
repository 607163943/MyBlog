import request from '@/utils/request'

// 获取用户信息
export const userGetInfo = () => {
  return request.get('/user/info')
}

// 用户登录
export const userLoginService = (data) => {
  return request.post('/user/login', data)
}

// 用户登出
export const userLogoutService = () => {
  return request.post('/user/logout')
}

// 获取验证码
export const userCaptchaCodeService = () => {
  return request.get('/user/captcha')
}
