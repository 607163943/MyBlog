import request from '@/utils/request'

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

// 获取用户信息
export const userInfoService = () => {
  return request.get('/user/info')
}

// 修改用户信息
export const userUpdateInfoService = (data) => {
  return request.put('/user/info', data)
}

// 修改密码
export const userUpdatePasswordService = (data) => {
  return request.patch('/user/password', data)
}
