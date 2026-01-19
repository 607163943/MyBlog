<script setup>
import { ref, onMounted } from 'vue'
import { articlePageQueryService, articleNewTop5Service } from '@/api/article'
import { SearchOutlined } from '@ant-design/icons-vue'
import { Button } from '@/components/ui/button'
import { userInfoService, userCountCardService } from '@/api/user'
import { categoryHotTop5Service } from '@/api/category'
import { tagWallService } from '@/api/tag'

// 文章+搜索
const articleList = ref([])
const searchParams = ref({
  categoryId: '',
  tagId: '',
  pageNum: 1,
  pageSize: 10
})

const total = ref(0)
const pages = ref(0)

const pageQuery = async () => {
  const res = await articlePageQueryService(searchParams.value)
  articleList.value = res.data.data.result
  total.value = res.data.data.total
  pages.value = res.data.data.pages
}

const isSearchMode = ref(false)
const isCategorySearch = ref(false)
const categoryName = ref('')
const isTagSearch = ref(false)
const tagName = ref('')

const handlerSearch = () => {
  isSearchMode.value = true
  isCategorySearch.value = false
  isTagSearch.value = false
  pageQuery()
}

const handlerPageQuery = (page) => {
  searchParams.value.pageNum = page
  pageQuery()
}

// 用户卡片
const userInfo = ref({
  username: '',
  nickname: ''
})

const userCountCard = ref({
  articleCount: 0,
  categoryCount: 0,
  tagCount: 0
})

const handlerUserInfo = async () => {
  const res = await userInfoService()
  userInfo.value = res.data.data
}

const handlerUserCountCard = async () => {
  const res = await userCountCardService()
  userCountCard.value = res.data.data
}

// 热门分类卡片
const hotCategoryList = ref([])
const handlerHotCategoryList = async () => {
  const res = await categoryHotTop5Service()
  hotCategoryList.value = res.data.data
}

const handlerCategoryPush = (category) => {
  searchParams.value.categoryId = category.categoryId
  searchParams.value.pageNum = 1
  searchParams.value.pageSize = 10

  isCategorySearch.value = true
  categoryName.value = category.categoryName
  isSearchMode.value = false
  isTagSearch.value = false
  pageQuery()
}

// 最新文章卡片
const newArticleList = ref([])
const handlerNewArticleList = async () => {
  const res = await articleNewTop5Service()
  newArticleList.value = res.data.data
}

// 标签墙
const handlerTagPush = (tag) => {
  searchParams.value.tagId = tag.tagId
  searchParams.value.pageNum = 1
  searchParams.value.pageSize = 10

  isTagSearch.value = true
  tagName.value = tag.tagName
  isSearchMode.value = false
  isCategorySearch.value = false
  pageQuery()
}
// ✅ 示例：后端建议返回 id、name、articleCount
const tags = ref([])

// ✅ 多色调色板（稳定：按 id 取模）
const palettes = [
  'border-sky-100 bg-sky-50 text-sky-600',
  'border-emerald-100 bg-emerald-50 text-emerald-600',
  'border-amber-100 bg-amber-50 text-amber-700',
  'border-violet-100 bg-violet-50 text-violet-600',
  'border-rose-100 bg-rose-50 text-rose-600',
  'border-slate-200 bg-slate-50 text-slate-700'
]

// ✅ 5档字号（推荐离散档位，Tailwind 友好）
// 你也可以改成 4 档/6 档
const sizeSteps = ['text-xs', 'text-sm', 'text-base', 'text-lg', 'text-xl']
const weightSteps = ['font-medium', 'font-medium', 'font-semibold', 'font-semibold', 'font-bold']

const tagWall = ref(null)
const createTagWall = () => {
  // 计算 min/max（注意边界）
  const counts = tags.value.map((t) => {
    return t.articleCount ?? 0
  })
  const min = Math.min(...counts)
  const max = Math.max(...counts)
  const span = Math.max(1, max - min)

  function getLevel(count) {
    // 归一化到 0~1
    const w = (count - min) / span
    // 映射到 0~(steps-1)
    return Math.max(0, Math.min(sizeSteps.length - 1, Math.round(w * (sizeSteps.length - 1))))
  }
  tags.value.forEach((t) => {
    const colorCls = palettes[t.tagId % palettes.length]
    const level = getLevel(t.articleCount ?? 0)
    console.log(level)
    const sizeCls = sizeSteps[level]
    const weightCls = weightSteps[level]

    const a = document.createElement('a')

    a.className = [
      'cursor-pointer inline-flex items-center rounded-full border px-4 py-1.5',
      'transition hover:opacity-90 active:scale-[0.98]',
      // ✅ 动态字号/字重
      sizeCls,
      weightCls,
      // ✅ 稳定配色
      colorCls
    ].join(' ')

    a.textContent = t.tagName

    a.addEventListener('click', (e) => {
      e.preventDefault()
      handlerTagPush(t)
    })
    tagWall.value.appendChild(a)
  })
}

const handleTagWall = async () => {
  const res = await tagWallService()
  tags.value = res.data.data
  createTagWall()
}

// 数据初始化
pageQuery()
handlerUserInfo()
handlerUserCountCard()
handlerHotCategoryList()
handlerNewArticleList()

onMounted(() => {
  handleTagWall()
})
</script>
<template>
  <main class="flex-1 pb-16">
    <div class="mx-auto max-w-[1200px] px-6 py-10 space-y-10">
      <h1
        class="text-3xl text-center p-1 font-semibold text-white line-1 anim-typewriter"
        v-if="!isSearchMode && !isCategorySearch && !isTagSearch"
      >
        MyBlog · 个人博客网站
      </h1>

      <h1 class="text-3xl text-center p-1 font-semibold text-white" v-else-if="isCategorySearch">
        {{ categoryName }}
      </h1>
      <h1 class="text-3xl text-center p-1 font-semibold text-white" v-else-if="isTagSearch">
        {{ tagName }}
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
                isSearchMode ? '修改关键词或直接回车更新' : '输入关键词，快速定位深度内容'
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
            v-if="isSearchMode"
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
              v-model:current="searchParams.pageNum"
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
          <div
            class="rounded-2xl border border-slate-100 bg-[rgba(255,255,255,0.9)] p-6 shadow-card"
          >
            <div class="flex items-center gap-4">
              <img
                alt="作者头像"
                class="h-14 w-14 rounded-2xl object-cover"
                src="https://images.unsplash.com/photo-1525130413817-d45c1d127c42?auto=format&fit=facearea&w=120&h=120&q=80"
              />
              <div>
                <p class="text-base font-semibold text-slate-900">
                  {{ userInfo.nickname ? userInfo.nickname : userInfo.username }}
                </p>
                <p class="text-sm text-slate-500">一个热爱编程的普通人</p>
              </div>
            </div>

            <a-row>
              <a-col :span="8">
                <a-statistic title="文章" :value="userCountCard.articleCount" class="text-center" />
              </a-col>
              <a-col :span="8">
                <a-statistic
                  title="分类"
                  :value="userCountCard.categoryCount"
                  class="text-center"
                />
              </a-col>
              <a-col :span="8">
                <a-statistic title="标签" :value="userCountCard.tagCount" class="text-center" />
              </a-col>
            </a-row>
          </div>

          <!-- 热门分类卡片 -->
          <div
            class="rounded-2xl border border-slate-100 bg-[rgba(255,255,255,0.9)] p-6 shadow-card"
          >
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

          <!-- 最新文章卡片 -->
          <div
            class="rounded-2xl border border-slate-100 bg-[rgba(255,255,255,0.9)] p-6 shadow-card"
          >
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

          <!-- 标签墙卡片 -->
          <div
            class="rounded-2xl border border-slate-100 bg-[rgba(255,255,255,0.9)] p-6 shadow-card"
          >
            <p class="text-base font-semibold text-slate-900">标签墙</p>

            <div ref="tagWall" class="mt-4 flex flex-wrap gap-3 text-sm"></div>
          </div>
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
