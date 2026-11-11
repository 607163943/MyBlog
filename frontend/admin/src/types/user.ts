/** 登录参数 */
export interface LoginDTO {
  username: string
  password: string
}

/** 登录返回结果 */
export interface LoginVO {
  tokenName: string
  tokenValue: string
}

/** 用户信息 */
export interface UserInfoVO {
  id: number
  username: string
  nickname: string
  avatar: string | null
  email: string | null
  createTime: string
  updateTime: string
}
