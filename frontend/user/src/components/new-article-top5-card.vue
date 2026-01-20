<script setup>
import { ref } from 'vue'
import { articleNewTop5Service } from '@/api/article'

const newArticleList = ref([])
const handlerNewArticleList = async () => {
  const res = await articleNewTop5Service()
  newArticleList.value = res.data.data
}

handlerNewArticleList()
</script>
<template>
  <div class="rounded-2xl border border-slate-100 bg-[rgba(255,255,255,0.9)] p-6 shadow-card">
    <p class="text-base font-semibold text-slate-900">最新文章 Top5</p>
    <div class="mt-4 space-y-4 text-sm text-slate-600">
      <div
        v-for="(article, index) in newArticleList"
        :key="article.id"
        class="flex items-start gap-3 cursor-pointer hover:text-sky-600"
        @click="$router.push('/article/' + article.id)"
      >
        <span class="text-xs font-semibold text-slate-400">{{ index + 1 }}</span>
        <div>
          <p class="font-medium hover:text-sky-600 transition-all duration-300">
            {{ article.title }}
          </p>
          <p class="text-xs text-slate-400">{{ article.publishTime }}</p>
        </div>
      </div>
    </div>
  </div>
</template>
<style scoped></style>
