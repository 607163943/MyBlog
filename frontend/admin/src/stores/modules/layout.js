import { ref } from 'vue'
import { defineStore } from 'pinia'

export const useLayoutStore = defineStore('layout', () => {
  const path = ref('index')

  const setPath = (newPath) => {
    path.value = newPath
  }

  return { path, setPath }
})
