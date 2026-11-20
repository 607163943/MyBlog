<script setup lang="ts">
import { computed, onMounted, reactive, ref, watch } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import AvatarUpload from '@/components/AvatarUpload.vue'
import { changePassword, getCaptcha, updateUserInfo } from '@/api/user'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()

const activeTab = ref('basic')

const userInfo = computed(() => userStore.userInfo)
const displayName = computed(
  () => userInfo.value?.nickname || userInfo.value?.username || '管理员',
)
const avatarText = computed(() => displayName.value.charAt(0).toUpperCase())

/** 基本信息表单 */
interface BasicForm {
  nickname: string
  email: string
  avatar: string
  avatarFileId: number | null
}

const basicFormRef = ref<FormInstance>()
const basicForm = reactive<BasicForm>({
  nickname: '',
  email: '',
  avatar: '',
  avatarFileId: null,
})
const savingBasic = ref(false)
/** 头像上传中禁止保存，避免把旧头像当作新头像提交 */
const avatarUploading = ref(false)

const basicRules: FormRules = {
  nickname: [
    { required: true, message: '请输入昵称', trigger: 'blur' },
    { max: 20, message: '昵称长度不能超过 20 个字符', trigger: 'blur' },
  ],
  email: [{ type: 'email', message: '邮箱格式不正确', trigger: 'blur' }],
}

/** 密码表单 */
interface PasswordForm {
  oldPassword: string
  newPassword: string
  confirmPassword: string
  captchaCode: string
}

const passwordFormRef = ref<FormInstance>()
const passwordForm = reactive<PasswordForm>({
  oldPassword: '',
  newPassword: '',
  confirmPassword: '',
  captchaCode: '',
})
const savingPassword = ref(false)

/** 图形验证码 */
const captchaKey = ref('')
const captchaImage = ref('')
const captchaLoading = ref(false)

function validateConfirmPassword(
  _rule: unknown,
  value: string,
  callback: (error?: string | Error) => void,
) {
  if (!value) {
    callback(new Error('请再次输入新密码'))
    return
  }
  if (value !== passwordForm.newPassword) {
    callback(new Error('两次输入的新密码不一致'))
    return
  }
  callback()
}

const passwordRules: FormRules = {
  oldPassword: [{ required: true, message: '请输入旧密码', trigger: 'blur' }],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, max: 20, message: '新密码长度需为 6-20 个字符', trigger: 'blur' },
  ],
  confirmPassword: [{ validator: validateConfirmPassword, trigger: 'blur' }],
  captchaCode: [
    { required: true, message: '请输入验证码', trigger: 'blur' },
    { len: 4, message: '验证码为 4 位数字', trigger: 'blur' },
  ],
}

function formatDateTime(value?: string) {
  return value ? value.replace('T', ' ').slice(0, 19) : '-'
}

/** 按当前用户信息回填基本信息表单 */
function resetBasicForm() {
  const info = userStore.userInfo
  basicForm.nickname = info?.nickname ?? ''
  basicForm.email = info?.email ?? ''
  basicForm.avatar = info?.avatar ?? ''
  basicForm.avatarFileId = info?.avatarFileId ?? null
  basicFormRef.value?.clearValidate()
}

function resetPasswordForm() {
  passwordForm.oldPassword = ''
  passwordForm.newPassword = ''
  passwordForm.confirmPassword = ''
  passwordForm.captchaCode = ''
  passwordFormRef.value?.clearValidate()
}

async function loadCaptcha() {
  captchaLoading.value = true
  try {
    const data = await getCaptcha()
    captchaKey.value = data.captchaKey
    captchaImage.value = data.image
  } catch {
    captchaKey.value = ''
    captchaImage.value = ''
  } finally {
    captchaLoading.value = false
  }
}

async function handleSaveBasic() {
  if (!basicFormRef.value) {
    return
  }
  try {
    await basicFormRef.value.validate()
  } catch {
    return
  }

  savingBasic.value = true
  try {
    await updateUserInfo({
      nickname: basicForm.nickname.trim(),
      email: basicForm.email.trim() || null,
      avatar: basicForm.avatar || null,
      avatarFileId: basicForm.avatar ? basicForm.avatarFileId : null,
    })
    ElMessage.success('基本信息已更新')
    await userStore.fetchUserInfo()
    resetBasicForm()
  } catch {
    // 请求错误已由 request.ts 统一提示
  } finally {
    savingBasic.value = false
  }
}

function handleAvatarUploading(value: boolean) {
  avatarUploading.value = value
}

async function handleChangePassword() {
  if (!passwordFormRef.value) {
    return
  }
  try {
    await passwordFormRef.value.validate()
  } catch {
    return
  }

  savingPassword.value = true
  try {
    await changePassword({
      oldPassword: passwordForm.oldPassword,
      newPassword: passwordForm.newPassword,
      captchaKey: captchaKey.value,
      captchaCode: passwordForm.captchaCode.trim(),
    })
    ElMessage.success('密码修改成功，请使用新密码重新登录')
    try {
      await userStore.logout()
    } catch {
      // 退出接口失败时仍清理本地状态并返回登录页
    }
    await router.replace('/login')
  } catch {
    // 验证码一次性使用，失败后重新获取
    passwordForm.captchaCode = ''
    loadCaptcha()
  } finally {
    savingPassword.value = false
  }
}

watch(activeTab, (tab) => {
  if (tab === 'password' && !captchaImage.value) {
    loadCaptcha()
  }
})

onMounted(async () => {
  try {
    await userStore.fetchUserInfo()
  } catch {
    // 请求错误已由 request.ts 统一提示
  }
  resetBasicForm()
  loadCaptcha()
})
</script>

<template>
  <div class="profile-page">
    <el-card shadow="never" class="profile-card">
      <div class="profile-card__avatar">
        <el-avatar :size="96" :src="userInfo?.avatar ?? undefined">
          {{ avatarText }}
        </el-avatar>
      </div>
      <div class="profile-card__name">{{ displayName }}</div>
      <div class="profile-card__username">@{{ userInfo?.username ?? '-' }}</div>

      <el-divider />

      <div class="profile-card__item">
        <span class="profile-card__label">邮箱</span>
        <span class="profile-card__value">{{ userInfo?.email || '未填写' }}</span>
      </div>
      <div class="profile-card__item">
        <span class="profile-card__label">注册时间</span>
        <span class="profile-card__value">{{ formatDateTime(userInfo?.createTime) }}</span>
      </div>
      <div class="profile-card__item">
        <span class="profile-card__label">最近修改</span>
        <span class="profile-card__value">{{ formatDateTime(userInfo?.updateTime) }}</span>
      </div>
    </el-card>

    <el-card shadow="never" class="profile-form-card">
      <el-tabs v-model="activeTab">
        <el-tab-pane label="基本信息" name="basic">
          <el-form
            ref="basicFormRef"
            class="profile-form"
            :model="basicForm"
            :rules="basicRules"
            label-width="96px"
          >
            <el-form-item label="头像" prop="avatar">
              <AvatarUpload
                v-model="basicForm.avatar"
                v-model:file-id="basicForm.avatarFileId"
                :fallback-text="avatarText"
                @uploading="handleAvatarUploading"
              />
            </el-form-item>
            <el-form-item label="昵称" prop="nickname">
              <el-input
                v-model="basicForm.nickname"
                maxlength="20"
                show-word-limit
                placeholder="请输入昵称"
              />
            </el-form-item>
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="basicForm.email" maxlength="100" placeholder="请输入邮箱（选填）" />
            </el-form-item>
            <el-form-item label="用户名">
              <el-input :model-value="userInfo?.username ?? ''" disabled />
            </el-form-item>
            <el-form-item>
              <el-button
                type="primary"
                :loading="savingBasic"
                :disabled="avatarUploading"
                @click="handleSaveBasic"
              >
                保存修改
              </el-button>
              <el-button :disabled="savingBasic || avatarUploading" @click="resetBasicForm">
                重置
              </el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <el-tab-pane label="修改密码" name="password">
          <el-form
            ref="passwordFormRef"
            class="profile-form"
            :model="passwordForm"
            :rules="passwordRules"
            label-width="96px"
          >
            <el-form-item label="旧密码" prop="oldPassword">
              <el-input
                v-model="passwordForm.oldPassword"
                type="password"
                show-password
                placeholder="请输入当前密码"
              />
            </el-form-item>
            <el-form-item label="新密码" prop="newPassword">
              <el-input
                v-model="passwordForm.newPassword"
                type="password"
                show-password
                placeholder="请输入新密码（6-20 位）"
              />
            </el-form-item>
            <el-form-item label="确认新密码" prop="confirmPassword">
              <el-input
                v-model="passwordForm.confirmPassword"
                type="password"
                show-password
                placeholder="请再次输入新密码"
              />
            </el-form-item>
            <el-form-item label="验证码" prop="captchaCode">
              <div class="captcha-row">
                <el-input
                  v-model="passwordForm.captchaCode"
                  maxlength="4"
                  placeholder="请输入验证码"
                />
                <div
                  class="captcha-image"
                  :class="{ 'is-loading': captchaLoading }"
                  title="点击刷新验证码"
                  @click="loadCaptcha"
                >
                  <img v-if="captchaImage" :src="captchaImage" alt="验证码" />
                  <span v-else>点击获取</span>
                </div>
              </div>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :loading="savingPassword" @click="handleChangePassword">
                确认修改
              </el-button>
              <el-button :disabled="savingPassword" @click="resetPasswordForm">重置</el-button>
            </el-form-item>
          </el-form>

          <el-alert
            class="profile-form__tip"
            type="info"
            :closable="false"
            show-icon
            title="修改成功后需要重新登录"
          />
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<style scoped lang="scss">
.profile-page {
  display: flex;
  align-items: flex-start;
  gap: 16px;
}

.profile-card {
  flex: none;
  width: 280px;
  text-align: center;
}

.profile-card__avatar {
  display: flex;
  justify-content: center;
}

.profile-card__name {
  margin-top: 12px;
  font-size: 18px;
  font-weight: 600;
  color: var(--el-text-color-primary);
}

.profile-card__username {
  margin-top: 4px;
  font-size: 13px;
  color: var(--el-text-color-secondary);
}

.profile-card__item {
  display: flex;
  justify-content: space-between;
  gap: 12px;
  padding: 6px 0;
  font-size: 13px;
  text-align: left;
}

.profile-card__label {
  flex: none;
  color: var(--el-text-color-secondary);
}

.profile-card__value {
  color: var(--el-text-color-regular);
  text-align: right;
  word-break: break-all;
}

.profile-form-card {
  flex: 1;
  min-width: 0;
}

.profile-form {
  max-width: 520px;
  margin-top: 8px;
}

.profile-form__tip {
  max-width: 520px;
}

.captcha-row {
  display: flex;
  align-items: center;
  gap: 12px;
  width: 100%;
}

.captcha-row :deep(.el-input) {
  flex: 1;
}

.captcha-image {
  display: flex;
  flex: none;
  align-items: center;
  justify-content: center;
  width: 120px;
  height: 40px;
  overflow: hidden;
  font-size: 12px;
  color: var(--el-text-color-secondary);
  cursor: pointer;
  background-color: var(--el-fill-color-lighter);
  border: 1px solid var(--el-border-color);
  border-radius: var(--el-border-radius-base);
}

.captcha-image img {
  display: block;
  width: 100%;
  height: 100%;
}

.captcha-image.is-loading {
  cursor: wait;
  opacity: 0.6;
}

@media (max-width: 900px) {
  .profile-page {
    flex-direction: column;
  }

  .profile-card {
    width: 100%;
  }

  .profile-form,
  .profile-form__tip {
    max-width: 100%;
  }
}
</style>
