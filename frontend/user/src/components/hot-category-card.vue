<script setup>
import { ref, defineEmits } from 'vue'
import { categoryHotTop5Service } from '@/api/category'

const hotCategoryList = ref([])
const handlerHotCategoryList = async () => {
  const res = await categoryHotTop5Service()
  hotCategoryList.value = res.data.data
}

const emit = defineEmits(['categoryPush'])

const handlerCategoryPush = (category) => {
  emit('categoryPush', category)
}

handlerHotCategoryList()
</script>
<template>
  <div class="rounded-2xl border border-slate-100 bg-[rgba(255,255,255,0.9)] p-6 shadow-card">
    <p class="text-base font-semibold text-slate-900">热门分类排行</p>
    <ol class="mt-4 space-y-4 text-sm">
      <li
        v-for="(category, index) in hotCategoryList"
        :key="category.categoryId"
        class="flex items-center justify-between text-slate-600"
      >
        <span class="font-medium text-slate-800"
          >{{ index + 1 }} ·
          <span
            class="cursor-pointer hover:text-sky-600 transition-all duration-300"
            @click="handlerCategoryPush(category)"
            >{{ category.categoryName }}</span
          ></span
        >
        <span class="text-xs text-slate-500">{{ category.articleCount }}篇文章</span>
      </li>
    </ol>
  </div>
</template>
<style scoped></style>
