<script setup>
import UserInfoCard from '@/components/user-info-card.vue'
import HotCategoryCard from '@/components/hot-category-card.vue'
import NewArticleTop5Card from '@/components/new-article-top5-card.vue'
import TagWall from '@/components/tag-wall.vue'
import { useSearchStore } from '@/stores'
import { useRouter } from 'vue-router'
import { useRoute } from 'vue-router'
import { ref, watch } from 'vue'
import { articleByIdService } from '@/api/article'
import { MdPreview } from 'md-editor-v3'
// preview.css相比style.css少了编辑器那部分样式
import 'md-editor-v3/lib/preview.css'

const route = useRoute()
const router = useRouter()
const searchStore = useSearchStore()

// 文章内容
const article = ref({
  id: '',
  title: '',
  content: '',
  categoryId: '',
  categoryName: '',
  tags: []
})

const id = 'preview-only'

const handlerArticleQuery = async () => {
  const res = await articleByIdService(route.params.articleId)
  article.value = res.data.data
}

watch(
  route,
  () => {
    handlerArticleQuery()
  },
  {
    deep: true,
    immediate: true
  }
)

// 热门分类卡片
const handlerCategoryPush = (category) => {
  searchStore.setCategorySearch(category)
  searchStore.resetPage()
  router.push('/home')
}

// 标签墙
const handlerTagPush = (tag) => {
  searchStore.setTagSearch(tag)
  searchStore.resetPage()
  router.push('/home')
}
</script>
<template>
  <main class="flex-1 pb-16">
    <div class="mx-auto max-w-[1200px] px-6 py-10 space-y-10">
      <div class="grid grid-cols-1 gap-8 xl:grid-cols-[2fr_1fr]">
        <section>
          <article class="rounded-[32px] border border-slate-100 bg-white p-10 shadow-card">
            <nav class="text-sm text-slate-500">
              <a
                class="transition hover:text-slate-700 cursor-pointer"
                @click="router.push('/home')"
                >首页</a
              >
              <span class="px-2 text-slate-300">/</span>
              <a
                class="transition hover:text-slate-700 cursor-pointer"
                @click="
                  handlerCategoryPush({
                    categoryId: article.categoryId,
                    categoryName: article.categoryName
                  })
                "
                >{{ article.categoryName }}</a
              >
              <span class="px-2 text-slate-300">/</span>
              <span class="text-slate-400">正文</span>
            </nav>

            <div class="mt-6 space-y-4">
              <h2 class="text-3xl font-semibold text-slate-900">
                {{ article.title }}
              </h2>
              <p class="text-sm text-slate-500" v-if="article.categoryId">
                所属分类：<span class="font-semibold text-slate-800">{{
                  article.categoryName
                }}</span>
              </p>
              <div class="flex flex-wrap gap-2 text-sm">
                <span
                  v-for="tag in article.tags"
                  :key="tag.id"
                  class="rounded-full border border-sky-100 bg-sky-50 px-3 py-1 text-sky-600"
                >
                  #{{ tag.name }}
                </span>
              </div>
              <p class="text-sm text-slate-500">发布时间 · {{ article.publishTime }}</p>
            </div>

            <!-- 内容渲染区域 -->
            <MdPreview :id="id" :modelValue="article.content" />

            <nav class="mt-10 flex flex-wrap gap-3 text-sm text-slate-500">
              <a class="transition hover:text-slate-700 cursor-pointer" @click="router.back()"
                >返回</a
              >
            </nav>
          </article>
        </section>

        <aside class="space-y-6">
          <!-- 用户卡片 -->
          <UserInfoCard />
          <!-- 热门分类卡片 -->
          <HotCategoryCard @categoryPush="handlerCategoryPush" />

          <!-- 最新文章卡片 -->
          <NewArticleTop5Card />

          <!-- 标签墙卡片 -->
          <TagWall @tagPush="handlerTagPush" />
        </aside>
      </div>
    </div>
  </main>
</template>
<style scoped lang="less"></style>
