import { createRouter, createWebHistory } from 'vue-router'
import AdminLayout from '@/components/AdminLayout.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      component: AdminLayout,
      redirect: '/category',
      children: [
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
