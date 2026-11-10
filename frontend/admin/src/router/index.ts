import { createRouter, createWebHistory } from 'vue-router'
import AdminLayout from '@/components/AdminLayout.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      component: AdminLayout,
      redirect: '/article',
      children: [
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
      ],
    },
  ],
})

export default router
