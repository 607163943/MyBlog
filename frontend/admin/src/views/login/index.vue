<script setup>
import { ref, onMounted } from 'vue'
import { UserOutlined, LockOutlined } from '@ant-design/icons-vue'
import { captchaCodeService } from '@/api/captcha'
import { userLoginService } from '@/api/user'
import { message } from 'ant-design-vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const loginFormRef = ref(null)
// 表单
const loginForm = ref({
  username: '',
  password: '',
  captchaCode: ''
})

// 验证码
const captchaImageBase64 = ref('')

const getCaptchaImage = async () => {
  const res = await captchaCodeService()
  captchaImageBase64.value = 'data:image/png;base64,' + res.data.data.imageBase64
}

// 校验规则
const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  captchaCode: [{ required: true, message: '请输入验证码', trigger: 'blur' }]
}

// 登录
const login = async () => {
  // 进行校验
  await loginFormRef.value.validate()
  const res = await userLoginService(loginForm.value)
  if (res.data.code === 200) {
    message.success('登录成功')
    router.push('/')
  } else {
    message.error(res.data.message)
  }
}

onMounted(() => {
  getCaptchaImage()
})
</script>
<template>
  <div class="login-index">
    <div class="left"></div>
    <div class="right">
      <div class="login-content">
        <div class="login-head-text">MyBlog Admin</div>
        <div class="login-title">登录</div>

        <div class="login-form">
          <a-form
            layout="vertical"
            ref="loginFormRef"
            :rules="rules"
            :model="loginForm"
            autocomplete="off"
          >
            <a-form-item label="用户名" name="username">
              <a-input size="large" v-model:value="loginForm.username" placeholder="请输入用户名">
                <template #prefix>
                  <UserOutlined />
                </template>
              </a-input>
            </a-form-item>

            <a-form-item label="密码" name="password">
              <a-input-password
                size="large"
                v-model:value="loginForm.password"
                placeholder="请输入密码"
              >
                <template #prefix>
                  <LockOutlined />
                </template>
              </a-input-password>
            </a-form-item>

            <a-form-item label="验证码" name="captchaCode">
              <a-row>
                <a-col :span="12">
                  <a-input
                    size="large"
                    v-model:value="loginForm.captchaCode"
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
              <a-button style="width: 100%" size="large" type="primary" @click="login"
                >登录</a-button
              >
            </a-form-item>
          </a-form>
        </div>
      </div>
    </div>
  </div>
</template>

<style lang="less">
.login-index {
  height: 100%;
  display: flex;

  .left {
    flex: 1;
    background-color: #ececec;
  }

  .right {
    flex: 1;
    padding: 24px;
    background-color: #fff;
  }
}

.login-content {
  margin: 0 auto;
  width: 400px;

  .login-head-text {
    text-align: center;
    margin-top: 64px;
    font-size: 32px;
    font-weight: 700;
    color: #5099ff;
  }

  .login-title {
    text-align: center;
    margin-top: 32px;
    font-size: 24px;
    font-weight: 700;
    color: 333;
  }
}
</style>
