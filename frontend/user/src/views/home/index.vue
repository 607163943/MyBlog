<script setup>
import { ref } from 'vue'
import { articlePageQueryService } from '@/api/article'
import { SearchOutlined } from '@ant-design/icons-vue'
import { Button } from '@/components/ui/button'
import UserInfoCard from '@/components/user-info-card.vue'
import HotCategoryCard from '@/components/hot-category-card.vue'
import NewArticleTop5Card from '@/components/new-article-top5-card.vue'
import TagWall from '@/components/tag-wall.vue'
import { useSearchStore } from '@/stores'
import { storeToRefs } from 'pinia'

const searchStore = useSearchStore()

// 文章+搜索
const articleList = ref([])

const { pageNum } = storeToRefs(searchStore)
const total = ref(0)
const pages = ref(0)

const pageQuery = async () => {
  const res = await articlePageQueryService(searchStore.searchParams)
  articleList.value = res.data.data.result
  total.value = res.data.data.total
  pages.value = res.data.data.pages
}

const handlerSearch = () => {
  searchStore.setSearchMode()
  pageQuery()
}

const handlerPageQuery = (page) => {
  searchStore.setPageNum(page)
  pageQuery()
}

// 热门分类卡片
const handlerCategoryPush = (category) => {
  searchStore.setCategorySearch(category)
  searchStore.resetPage()
  pageQuery()
}

// 标签墙
const handlerTagPush = (tag) => {
  searchStore.setTagSearch(tag)
  searchStore.resetPage()
  pageQuery()
}

// 数据初始化
pageQuery()
</script>
<template>
  <main class="flex-1 pb-16">
    <div class="mx-auto max-w-[1200px] px-6 py-10 space-y-10">
      <h1
        class="text-3xl text-center p-1 font-semibold text-white line-1 anim-typewriter"
        v-if="
          !searchStore.isSearchMode && !searchStore.isCategorySearch && !searchStore.isTagSearch
        "
      >
        MyBlog · 个人博客网站
      </h1>

      <h1
        class="text-3xl text-center p-1 font-semibold text-white"
        v-else-if="searchStore.isCategorySearch"
      >
        {{ searchStore.categoryName }}
      </h1>
      <h1
        class="text-3xl text-center p-1 font-semibold text-white"
        v-else-if="searchStore.isTagSearch"
      >
        {{ searchStore.tagName }}
      </h1>
      <h1 class="text-3xl text-center p-1 font-semibold text-white" v-else>搜索结果</h1>
      <!-- 搜索栏 -->
      <div class="rounded-[32px] max-w-[80%] mx-auto bg-linear-to-br bg-[rgba(255,255,255,0.9)]">
        <div class="flex flex-col lg:flex-row lg:items-center h-full">
          <div
            class="flex flex-1 p-1.5 items-center bg-transparent rounded-l-full px-5 py-2.5 text-slate-500"
          >
            <svg
              xmlns="http://www.w3.org/2000/svg"
              fill="none"
              viewBox="0 0 24 24"
              stroke-width="1.5"
              stroke="currentColor"
              class="h-5 w-5 text-slate-400"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                d="m21 21-4.35-4.35m0-6.15a6.15 6.15 0 1 1-12.3 0 6.15 6.15 0 0 1 12.3 0Z"
              />
            </svg>
            <input
              class="w-full bg-transparent text-base text-slate-700 placeholder:text-slate-400 focus:outline-none"
              :placeholder="
                searchStore.isSearchMode
                  ? '修改关键词或直接回车更新'
                  : '输入关键词，快速定位深度内容'
              "
              @keydown.enter="handlerSearch"
              type="text"
            />
          </div>
          <div class="h-full">
            <Button
              size="lg"
              class="cursor-pointer rounded-l-none rounded-r-full bg-transparent hover:bg-[rgba(0,0,0,0.1)]"
              @click="handlerSearch"
            >
              <SearchOutlined />
            </Button>
          </div>
        </div>
      </div>

      <div class="grid grid-cols-1 gap-8 xl:grid-cols-[2fr_1fr]">
        <!-- 左侧文章区域 -->
        <section class="space-y-6">
          <div
            v-if="searchStore.isSearchMode"
            class="rounded-2xl border border-sky-100 bg-[rgba(255,255,255,0.9)] p-5 text-sm text-slate-600 shadow-card"
          >
            <p class="font-semibold text-slate-900">
              已搜索 “分布式系统”，匹配文章 12 篇，可点击分类或标签继续跳转。
            </p>
            <div class="flex flex-wrap items-center gap-3 text-xs">
              <span
                class="rounded-full border border-sky-100 bg-sky-50 px-4 py-1.5 font-medium text-sky-600"
              >
                分类 · 基础架构
              </span>
              <span
                class="rounded-full border border-amber-100 bg-amber-50 px-4 py-1.5 font-medium text-amber-500"
              >
                标签 · #分布式系统
              </span>
            </div>
          </div>

          <article
            v-for="article in articleList"
            :key="article.id"
            class="flex flex-col gap-4 rounded-2xl border border-slate-100 bg-[rgba(255,255,255,0.9)] p-6 shadow-card transition hover:-translate-y-0.5"
          >
            <div class="flex flex-wrap items-center gap-3 text-xs">
              <span
                class="rounded-full border border-sky-100 bg-sky-50 px-3 py-0.5 font-medium text-sky-600"
                >{{ article.categoryName }}</span
              >
              <span class="text-slate-400">{{ article.updateTime }}</span>
            </div>
            <h3 class="text-[1.1rem] font-semibold leading-6 text-slate-900 line-clamp-2">
              {{ article.title }}
            </h3>
            <p class="text-sm leading-6 text-slate-600">
              {{ article.summary }}
            </p>
            <div class="flex flex-wrap items-center gap-3 text-xs text-slate-500">
              <span>标签</span>
              <div class="flex flex-wrap gap-2">
                <span
                  v-for="tag in article.tags"
                  :key="tag.id"
                  class="rounded-full border border-sky-100 bg-sky-50 px-3 py-1 text-sky-600"
                >
                  #{{ tag.name }}
                </span>
              </div>
            </div>
          </article>

          <div
            class="text-center gap-4 rounded-2xl border border-slate-100 bg-[rgba(255,255,255,0.9)] p-5 shadow-card sm:flex-row sm:items-center sm:justify-between"
          >
            <a-pagination
              @change="handlerPageQuery"
              v-model:current="pageNum"
              :total="total"
              show-less-items
              :showSizeChanger="false"
              :show-total="(total) => `共 ${total} 篇文章`"
            />
          </div>
        </section>

        <!-- 右侧卡片区域 -->
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
<style scoped lang="less">
// 标题打字机动画
.line-1 {
  position: relative;
  top: 50%;
  width: 12em;
  margin: 0 auto;
  border-right: 2px solid rgba(255, 255, 255, 0.75);
  font-size: 180%;
  text-align: center;
  white-space: nowrap;
  overflow: hidden;
  transform: translateY(-50%);
}

/* Animation */
.anim-typewriter {
  animation:
    typewriter 2.5s steps(24) 1s 1 normal both,
    /* 关键点：
       - delay=3.5s：打字期间(1s延迟+2.5s打字)光标一直常亮
       - 900ms：闪烁更慢一点（时间略微延长）
       - steps(1,end)：硬切换亮/灭，不做渐变动画
    */
    blinkTextCursor 900ms steps(1, end) 3.5s infinite both;
}
@keyframes typewriter {
  from {
    width: 0;
  }
  to {
    width: 12em;
  }
}
/* 让“亮”的占比更高，更像真实光标：亮约65%，灭约35% */
@keyframes blinkTextCursor {
  0%,
  65% {
    border-right-color: rgba(255, 255, 255, 0.75);
  }
  66%,
  100% {
    border-right-color: transparent;
  }
}
</style>
