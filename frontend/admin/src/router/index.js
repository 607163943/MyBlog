import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/home/index.vue'
import ArticleEdit from '../views/article-edit/index.vue'
import Layout from '../views/layout/index.vue'
import Article from '../views/article/index.vue'
import Statistics from '../views/statistics/index.vue'
import Category from '../views/category/index.vue'
import Tag from '../views/tag/index.vue'
import Config from '../views/config/index.vue'
import Profile from '../views/profile/index.vue'
import Login from '../views/login/index.vue'
import RequestError from '../views/error/request-error.vue'
import NotFindError from '../views/error/notfind-error.vue'
import ServerError from '../views/error/server-error.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/login',
      name: 'Login',
      component: Login
    },
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
          path: '/admin/statistics',
          name: 'Statistics',
          component: Statistics
        },
        {
          path: '/admin/article/edit',
          name: 'ArticleEdit',
          component: ArticleEdit
        },
        {
          path: '/admin/article',
          name: 'Article',
          component: Article
        },
        {
          path: '/admin/category',
          name: 'Category',
          component: Category
        },
        {
          path: '/admin/tag',
          name: 'Tag',
          component: Tag
        },
        {
          path: '/admin/config',
          name: 'Config',
          component: Config
        },
        {
          path: '/admin/profile',
          name: 'Profile',
          component: Profile
        }
      ]
    },
    {
      path: '/error/400',
      name: 'RequestError',
      component: RequestError
    },
    {
      path: '/error/404',
      name: 'NotFindError',
      component: NotFindError
    },
    {
      path: '/error/500',
      name: 'ServerError',
      component: ServerError
    }
  ]
})

export default router
