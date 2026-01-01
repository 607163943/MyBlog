import request from '@/utils/request'

// 获取验证码
export const captchaCodeService = () => {
  return request.get('/captcha')
}
