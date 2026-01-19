import { createRouter, createWebHistory } from 'vue-router'
import Layout from '@/views/layout/index.vue'
import Home from '@/views/home/index.vue'
import Category from '@/views/category/index.vue'
import ArticleView from '@/views/article/index.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '',
      name: 'Layout',
      component: Layout,
      redirect: '/home',
      children: [
        {
          path: '/home',
          name: 'Home',
          component: Home
        },
        {
          path: '/category',
          name: 'Category',
          component: Category
        },
        {
          path: '/article/:articleId',
          name: 'ArticleView',
          component: ArticleView
        }
      ]
    }
  ]
})

export default router
