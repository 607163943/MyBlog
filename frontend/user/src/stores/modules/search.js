import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useSearchStore = defineStore('search', () => {
  const searchParams = ref({
    categoryId: '',
    tagId: '',
    keyword: '',
    pageNum: 1,
    pageSize: 10
  })

  const setPageNum = (newNum) => {
    searchParams.value.pageNum = newNum
  }

  const isSearchMode = ref(false)
  const isCategorySearch = ref(false)
  const categoryName = ref('')
  const isTagSearch = ref(false)
  const tagName = ref('')

  const resetMode = () => {
    isSearchMode.value = false
    searchParams.value.keyword = ''

    isCategorySearch.value = false
    categoryName.value = ''
    searchParams.value.categoryId = ''

    isTagSearch.value = false
    tagName.value = ''
    searchParams.value.tagId = ''
  }

  const setSearchMode = (keyword) => {
    isSearchMode.value = true
    searchParams.value.keyword = keyword

    isCategorySearch.value = false
    categoryName.value = ''
    searchParams.value.categoryId = ''

    isTagSearch.value = false
    tagName.value = ''
    searchParams.value.tagId = ''
  }

  const setCategorySearch = (category) => {
    isSearchMode.value = false
    searchParams.value.keyword = ''

    isCategorySearch.value = true
    searchParams.value.categoryId = category.categoryId
    categoryName.value = category.categoryName

    isTagSearch.value = false
    tagName.value = ''
    searchParams.value.tagId = ''
  }

  const setTagSearch = (tag) => {
    isSearchMode.value = false
    searchParams.value.keyword = ''

    isCategorySearch.value = false
    categoryName.value = ''
    searchParams.value.categoryId = ''

    isTagSearch.value = true
    tagName.value = tag.tagName
    searchParams.value.tagId = tag.tagId
  }

  const resetPage = () => {
    searchParams.value.pageNum = 1
    searchParams.value.pageSize = 10
  }
  return {
    searchParams,
    resetPage,
    resetMode,
    setPageNum,
    isSearchMode,
    isCategorySearch,
    categoryName,
    isTagSearch,
    tagName,
    setSearchMode,
    setCategorySearch,
    setTagSearch
  }
})
