import { createRouter, createWebHistory } from 'vue-router'
import AdminLayout from '@/components/AdminLayout.vue'
import { useUserStore } from '@/stores/user'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/login',
      name: 'login',
      component: () => import('@/views/LoginView.vue'),
      meta: { title: '登录' },
    },
    {
      path: '/',
      component: AdminLayout,
      redirect: '/dashboard',
      children: [
        {
          path: 'dashboard',
          name: 'dashboard',
          component: () => import('@/views/DashboardView.vue'),
          meta: { title: '数据总览' },
        },
        {
          path: 'article',
          name: 'article',
          component: () => import('@/views/ArticleView.vue'),
          meta: { title: '文章管理' },
        },
        {
          path: 'article/edit',
          name: 'article-edit',
          component: () => import('@/views/ArticleEditView.vue'),
          meta: { title: '新增文章', activeMenu: '/article' },
        },
        {
          path: 'article/edit/:id',
          name: 'article-edit-id',
          component: () => import('@/views/ArticleEditView.vue'),
          meta: { title: '修改文章', activeMenu: '/article' },
        },
        {
          path: 'category',
          name: 'category',
          component: () => import('@/views/CategoryView.vue'),
          meta: { title: '分类管理' },
        },
        {
          path: 'tag',
          name: 'tag',
          component: () => import('@/views/TagView.vue'),
          meta: { title: '标签管理' },
        },
        {
          path: 'comment',
          name: 'comment',
          component: () => import('@/views/CommentView.vue'),
          meta: { title: '评论管理' },
        },
      ],
    },
  ],
})

// 未登录时跳转登录页，已登录时不再进入登录页
router.beforeEach((to) => {
  const userStore = useUserStore()
  if (to.name !== 'login' && !userStore.isLoggedIn) {
    return { path: '/login', query: { redirect: to.fullPath } }
  }
  if (to.name === 'login' && userStore.isLoggedIn) {
    return { path: '/' }
  }
  return true
})

export default router
