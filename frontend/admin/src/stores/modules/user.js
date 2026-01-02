import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useUserStore = defineStore(
  'user',
  () => {
    const token = ref('')

    const userInfo = ref(null)

    const setToken = (newToken) => {
      token.value = newToken
    }

    const setUserInfo = (newUserInfo) => {
      userInfo.value = newUserInfo
    }

    return { token, userInfo, setToken, setUserInfo }
  },
  {
    persist: {
      storage: localStorage,
      paths: ['token', 'userInfo'],
      key: 'user'
    }
  }
)
