<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { Lock, User } from '@element-plus/icons-vue'
import type { FormInstance, FormRules } from 'element-plus'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const formRef = ref<FormInstance>()
const loading = ref(false)

const form = reactive({
  username: '',
  password: '',
})

const rules: FormRules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
}

/** 登录成功后的跳转地址，默认回到首页 */
function getRedirectPath() {
  const redirect = route.query.redirect
  if (typeof redirect === 'string' && redirect !== '' && !redirect.startsWith('/login')) {
    return redirect
  }
  return '/'
}

/** 提交登录 */
async function handleLogin() {
  if (!formRef.value || loading.value) {
    return
  }
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) {
    return
  }

  loading.value = true
  try {
    await userStore.login({ ...form })
    await router.replace(getRedirectPath())
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="login-page">
    <div class="login-panel">
      <h1 class="login-title">管理后台登录</h1>
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        class="login-form"
        @submit.prevent
        @keyup.enter="handleLogin"
      >
        <el-form-item prop="username">
          <el-input
            v-model="form.username"
            :prefix-icon="User"
            placeholder="用户名"
            size="large"
            clearable
          />
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="form.password"
            :prefix-icon="Lock"
            type="password"
            placeholder="密码"
            size="large"
            show-password
          />
        </el-form-item>
        <el-form-item>
          <el-button
            class="login-button"
            type="primary"
            size="large"
            :loading="loading"
            @click="handleLogin"
          >
            登录
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<style scoped lang="scss">
.login-page {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  padding: 24px;
  background-color: var(--el-bg-color-page);
  background-image: radial-gradient(circle, var(--el-border-color) 1px, transparent 1px);
  background-size: 24px 24px;
}

.login-panel {
  width: 420px;
  max-width: 100%;
  padding: 40px 36px 20px;
  background-color: var(--el-bg-color);
  border: 1px solid var(--el-border-color-light);
  border-radius: 12px;
  box-shadow: var(--el-box-shadow-light);
}

.login-title {
  margin: 0 0 28px;
  font-size: 24px;
  font-weight: 600;
  color: var(--el-color-primary);
  text-align: center;
}

.login-form {
  :deep(.el-form-item) {
    margin-bottom: 22px;
  }
}

.login-button {
  width: 100%;
}
</style>
