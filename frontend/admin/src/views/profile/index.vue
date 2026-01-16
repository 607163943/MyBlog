<script setup>
import { onMounted, ref } from 'vue'
import { UserOutlined } from '@ant-design/icons-vue'
import { useUserStore } from '@/stores'
import {
  userInfoService,
  userUpdateInfoService,
  userCaptchaCodeService,
  userUpdatePasswordService
} from '@/api/user'
import { message } from 'ant-design-vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const userStore = useUserStore()

const userInfo = ref({
  id: '',
  username: '',
  nickname: ''
})

const infoFormRef = ref(null)
const infoForm = ref({
  id: '',
  username: '',
  nickname: ''
})

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  nickname: [{ required: true, message: '请输入昵称', trigger: 'blur' }]
}

const handlerGetUserInfo = async () => {
  const res = await userInfoService()
  // 赋值给userInfo
  userInfo.value.id = res.data.data.id
  userInfo.value.username = res.data.data.username
  userInfo.value.nickname = res.data.data.nickname
  infoForm.value = res.data.data
}

const handlerUpdateUserInfo = async () => {
  await infoFormRef.value.validate()
  const res = await userUpdateInfoService(infoForm.value)
  // 确保infoForm修改不会立刻影响到userStore.userInfo
  infoForm.value.id = res.data.data.id
  infoForm.value.username = res.data.data.username
  infoForm.value.nickname = res.data.data.nickname
  userStore.setUserInfo(res.data.data)
  message.success('保存成功')
}

const updatePasswordFormRef = ref(null)
const updatePasswordForm = ref({
  oldPassword: '',
  newPassword: '',
  confirmPassword: '',
  captchaCode: '',
  captchaKey: ''
})

const updatePasswordRules = {
  oldPassword: [{ required: true, message: '请输入旧密码', trigger: 'blur' }],
  newPassword: [{ required: true, message: '请输入新密码', trigger: 'blur' }],
  confirmPassword: [{ required: true, message: '请输入确认密码', trigger: 'blur' }],
  captchaCode: [{ required: true, message: '请输入验证码', trigger: 'blur' }]
}

// 验证码
const captchaImageBase64 = ref('')

const getCaptchaImage = async () => {
  const res = await userCaptchaCodeService()
  captchaImageBase64.value = 'data:image/png;base64,' + res.data.data.imageBase64
  updatePasswordForm.value.captchaKey = res.data.data.captchaKey
}

const handlerUpdatePassword = async () => {
  await updatePasswordFormRef.value.validate()
  // 去除确认密码项数据
  const tempUpdatePasswordForm = {
    oldPassword: updatePasswordForm.value.oldPassword,
    newPassword: updatePasswordForm.value.newPassword,
    captchaCode: updatePasswordForm.value.captchaCode,
    captchaKey: updatePasswordForm.value.captchaKey
  }
  try {
    await userUpdatePasswordService(tempUpdatePasswordForm)

    // 清空用户信息和token
    userStore.setUserInfo(null)
    userStore.setToken('')
    message.success('密码修改成功')
    router.push('/login')
  } catch (error) {
    console.log(error)

    // 重新获取验证码图片
    updatePasswordForm.value.captchaCode = ''
    getCaptchaImage()
  }
}

// 数据初始化
handlerGetUserInfo()

onMounted(() => {
  getCaptchaImage()
})
</script>
<template>
  <div class="profile-container">
    <div class="profile-info">
      <a-card title="个人信息卡片">
        <!-- 头像 -->
        <div class="info-avatar">
          <a-avatar shape="circle" :size="120">
            <template #icon><UserOutlined /></template>
          </a-avatar>
        </div>

        <!-- 基本信息 -->
        <div class="info-content">
          <div class="info-username info-item">
            <span class="info-label">用户名：</span>
            <span>{{ userInfo.username }}</span>
          </div>
          <div class="info-nickname info-item">
            <span class="info-label">昵称：</span>
            <span>{{ userInfo.nickname ? userInfo.nickname : '--' }}</span>
          </div>
        </div>
      </a-card>
    </div>
    <div class="profile-update">
      <a-card>
        <a-tabs type="card">
          <a-tab-pane key="1" tab="用户信息修改">
            <div class="profile-update-form">
              <a-form
                :rules="rules"
                ref="infoFormRef"
                :model="infoForm"
                autocomplete="off"
                :label-col="{ span: 2 }"
              >
                <a-form-item label="用户名" name="username">
                  <a-input v-model:value="infoForm.username" />
                </a-form-item>

                <a-form-item label="昵称" name="nickname">
                  <a-input v-model:value="infoForm.nickname" />
                </a-form-item>

                <a-form-item>
                  <a-button type="primary" style="margin-right: 10px" @click="handlerUpdateUserInfo"
                    >保存</a-button
                  >
                  <a-button type="default" @click="$router.back()">关闭</a-button>
                </a-form-item>
              </a-form>
            </div>
          </a-tab-pane>
          <a-tab-pane key="2" tab="密码修改">
            <div class="profile-update-form">
              <a-form
                :rules="updatePasswordRules"
                ref="updatePasswordFormRef"
                :model="updatePasswordForm"
                autocomplete="off"
                :label-col="{ span: 3 }"
              >
                <a-form-item label="旧密码" name="oldPassword">
                  <a-input-password
                    size="large"
                    v-model:value="updatePasswordForm.oldPassword"
                    placeholder="请输入密码"
                  >
                  </a-input-password>
                </a-form-item>

                <a-form-item label="新密码" name="newPassword">
                  <a-input-password
                    size="large"
                    v-model:value="updatePasswordForm.newPassword"
                    placeholder="请输入密码"
                  >
                  </a-input-password>
                </a-form-item>

                <a-form-item label="确认密码" name="confirmPassword">
                  <a-input-password
                    size="large"
                    v-model:value="updatePasswordForm.confirmPassword"
                    placeholder="请输入确认密码"
                  >
                  </a-input-password>
                </a-form-item>

                <a-form-item label="验证码" name="captchaCode">
                  <a-row>
                    <a-col :span="12">
                      <a-input
                        size="large"
                        v-model:value="updatePasswordForm.captchaCode"
                        placeholder="请输入验证码"
                      />
                    </a-col>
                    <a-col :span="6" :offset="2">
                      <div>
                        <img
                          alt="单击图片刷新！"
                          class="pointer"
                          :src="captchaImageBase64"
                          @click="getCaptchaImage"
                        />
                      </div>
                    </a-col>
                  </a-row>
                </a-form-item>

                <a-form-item>
                  <a-button
                    type="primary"
                    style="margin-left: 20px; margin-right: 10px"
                    @click="handlerUpdatePassword"
                    >保存</a-button
                  >
                  <a-button type="default" @click="$router.back()">关闭</a-button>
                </a-form-item>
              </a-form>

              <span style="color: #999; font-size: 14px">Tips: 修改成功后需要重新登录</span>
            </div>
          </a-tab-pane>
        </a-tabs>
      </a-card>
    </div>
  </div>
</template>

<style lang="less" scoped>
.profile-container {
  display: flex;

  .profile-info {
    flex: 1;

    .info-avatar {
      text-align: center;
    }

    .info-content {
      margin-top: 20px;
      text-align: center;

      .info-item {
        display: flex;
        justify-content: space-between;
        border-top: 1px solid #f0f0f0;
        font-size: 16px;

        &:nth-child(3) {
          border-bottom: 1px solid #f0f0f0;
        }
      }
    }
  }
  .profile-update {
    flex: 3;
    margin-left: 20px;

    :deep(.ant-card .ant-card-body) {
      padding: 0;
    }

    .profile-update-form {
      padding: 20px;
    }
  }
}
</style>
