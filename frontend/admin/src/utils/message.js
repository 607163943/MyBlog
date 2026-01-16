import { message } from 'ant-design-vue'
import { isEmpty } from 'es-toolkit/compat'

// 错误消息提醒
export const errorMessage = (errorRes) => {
  if (isEmpty(errorRes)) {
    message.error('请求超时')
  } else {
    message.error(errorRes.data ? errorRes.data.msg : '未知错误，请联系管理员')
  }
}
