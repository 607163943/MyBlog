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
  /** 头像文件 ID */
  avatarFileId: number | null
  email: string | null
  createTime: string
  updateTime: string
}

/** 修改基本信息参数 */
export interface UpdateUserInfoDTO {
  nickname: string
  email: string | null
  avatar: string | null
  /** 头像文件 ID，为空时后端按头像 URL 反查 */
  avatarFileId: number | null
}

/** 修改密码参数 */
export interface ChangePasswordDTO {
  oldPassword: string
  newPassword: string
  captchaKey: string
  captchaCode: string
}

/** 图形验证码 */
export interface CaptchaVO {
  captchaKey: string
  image: string
}
