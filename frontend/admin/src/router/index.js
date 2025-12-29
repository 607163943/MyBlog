import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/home/index.vue'
import BlogEdit from '../views/blog-edit/index.vue'
import Layout from '../views/layout/index.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'Layout',
      component: Layout,
      redirect: '/admin/home',
      children: [
        {
          path: '/admin/home',
          name: 'Home',
          component: Home
        },
        {
          path: '/admin/blogs/edit',
          name: 'BlogEdit',
          component: BlogEdit
        }
      ]
    }
  ]
})

export default router
