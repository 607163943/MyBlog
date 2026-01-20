<script setup>
import UserInfoCard from '@/components/user-info-card.vue'
import HotCategoryCard from '@/components/hot-category-card.vue'
import NewArticleTop5Card from '@/components/new-article-top5-card.vue'
import TagWall from '@/components/tag-wall.vue'
import { useSearchStore } from '@/stores'
import { useRouter } from 'vue-router'
import { categoryPageQueryService } from '@/api/category'
import { ref } from 'vue'

const router = useRouter()
const searchStore = useSearchStore()

// 分类卡片
const searchParams = ref({
  pageNum: 1,
  pageSize: 12
})
const total = ref(0)

const categoryList = ref([])
const pageQuery = async () => {
  const res = await categoryPageQueryService(searchParams.value)
  categoryList.value = res.data.data.result
  total.value = res.data.data.total
}

const handlerPageQuery = (page) => {
  searchStore.setPageNum(page)
  pageQuery()
}

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

pageQuery()
</script>
<template>
  <main class="flex-1 pb-16">
    <div class="mx-auto max-w-[1200px] px-6 py-10 space-y-10">
      <h1 class="text-3xl text-center p-1 font-semibold text-white line-1 anim-typewriter">
        分类列表
      </h1>
      <div class="grid grid-cols-1 gap-8 xl:grid-cols-[2fr_1fr]">
        <section class="space-y-8">
          <div class="grid grid-cols-1 gap-6 sm:grid-cols-2 xl:grid-cols-3">
            <article
              v-for="category in categoryList"
              :key="category.id"
              class="gap-4 rounded-2xl border border-slate-100 bg-white shadow-card"
            >
              <div class="overflow-hidden w-full rounded-2xl max-h-[132px]">
                <img
                  :src="
                    category.cover ? category.cover : '../../src/assets/default-category-cover.jpg'
                  "
                  class="w-full h-full object-cover"
                />
              </div>
              <div class="p-6">
                <div>
                  <h2 class="text-lg font-semibold text-slate-900">{{ category.name }}</h2>
                </div>
                <div class="flex items-center justify-between text-xs text-slate-500">
                  <span>17 篇文章</span>
                </div>
              </div>
            </article>
          </div>

          <div
            class="text-center gap-4 rounded-2xl border border-slate-100 bg-[rgba(255,255,255,0.9)] p-5 shadow-card sm:flex-row sm:items-center sm:justify-between"
          >
            <a-pagination
              @change="handlerPageQuery"
              v-model:current="searchParams.pageNum"
              :total="total"
              show-less-items
              :showSizeChanger="false"
              :show-total="(total) => `共 ${total} 个分类`"
            />
          </div>
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
