import { computed, ref } from 'vue'
import { defineStore } from 'pinia'
import { getUserInfo, login as loginApi, logout as logoutApi } from '@/api/user'
import type { LoginDTO, UserInfoVO } from '@/types/user'

export const useUserStore = defineStore(
  'user',
  () => {
    /** token 名称，作为请求头名称使用 */
    const tokenName = ref('')

    /** token 值 */
    const tokenValue = ref('')

    /** 当前登录用户信息 */
    const userInfo = ref<UserInfoVO | null>(null)

    /** 是否已登录 */
    const isLoggedIn = computed(() => tokenValue.value !== '')

    /** 登录并获取用户信息 */
    async function login(dto: LoginDTO) {
      const data = await loginApi(dto)
      tokenName.value = data.tokenName
      tokenValue.value = data.tokenValue
      await fetchUserInfo()
    }

    /** 获取当前登录用户信息 */
    async function fetchUserInfo() {
      userInfo.value = await getUserInfo()
    }

    /** 退出登录，无论接口是否成功都清理本地状态 */
    async function logout() {
      try {
        await logoutApi()
      } finally {
        reset()
      }
    }

    /** 清空登录状态 */
    function reset() {
      tokenName.value = ''
      tokenValue.value = ''
      userInfo.value = null
    }

    return {
      tokenName,
      tokenValue,
      userInfo,
      isLoggedIn,
      login,
      fetchUserInfo,
      logout,
      reset,
    }
  },
  {
    // 持久化到 localStorage，键名为 user
    persist: {
      key: 'user',
    },
  },
)
