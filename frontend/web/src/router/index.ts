import { createRouter, createWebHistory } from 'vue-router'

import { setPageTitle } from '@/util/title'

// App.vue 的页面切换使用 <Transition mode="out-in">，离场过渡为 0.18s。
// 浏览器前进/后退时需等旧页面离场、新页面挂载后再恢复位置，
// 否则滚动时 document 高度仍是旧页面的，位置会被浏览器钳制。
const PAGE_LEAVE_TRANSITION_MS = 200

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: () => import('@/views/HomeView.vue'),
      meta: { title: '首页' },
    },
    {
      path: '/category',
      name: 'category',
      component: () => import('@/views/CategoryView.vue'),
      meta: { title: '分类' },
    },
    {
      path: '/category/:id',
      name: 'category-detail',
      component: () => import('@/views/CategoryDetailView.vue'),
      meta: { title: '分类详情' },
    },
    {
      path: '/tag',
      name: 'tag',
      component: () => import('@/views/TagView.vue'),
      meta: { title: '标签' },
    },
    {
      path: '/tag/:id',
      name: 'tag-detail',
      component: () => import('@/views/TagDetailView.vue'),
      meta: { title: '标签详情' },
    },
    {
      path: '/archive',
      name: 'archive',
      component: () => import('@/views/ArchiveView.vue'),
      meta: { title: '归档' },
    },
    {
      path: '/archive/:year/:month',
      name: 'archive-month',
      component: () => import('@/views/ArchiveMonthView.vue'),
      meta: { title: '归档' },
    },
    {
      path: '/search',
      name: 'search',
      component: () => import('@/views/SearchView.vue'),
      meta: { title: '搜索' },
    },
    {
      path: '/post/:id',
      name: 'post',
      component: () => import('@/views/PostDetailView.vue'),
      meta: { title: '文章详情' },
    },
    {
      path: '/:pathMatch(.*)*',
      redirect: '/',
    },
  ],
  scrollBehavior(_to, from, savedPosition) {
    // 新导航（点击链接、push）没有 savedPosition，直接回顶部；
    // 只有浏览器前进/后退（popstate）和首次加载才返回历史位置。
    if (!savedPosition) return { top: 0 }

    // 首次加载（如刷新）没有页面切换过渡，立即恢复。
    if (!from.matched.length) return savedPosition

    return new Promise((resolve) => {
      setTimeout(() => resolve(savedPosition), PAGE_LEAVE_TRANSITION_MS)
    })
  },
})

router.afterEach((to) => {
  const title = typeof to.meta.title === 'string' ? to.meta.title : undefined
  setPageTitle(title)
})

export default router
