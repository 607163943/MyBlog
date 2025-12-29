import { createRouter, createWebHistory } from 'vue-router'
import AdminHome from '../views/admin-home/index.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'Home',
      redirect: '/admin'
    },
    {
      path: '/admin',
      name: 'AdminHome',
      component: AdminHome
    }
  ]
})

export default router
