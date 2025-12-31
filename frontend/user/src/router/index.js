import { createRouter, createWebHistory } from 'vue-router'
import Layout from '../views/layout/index.vue'
import Home from '../views/home/index.vue'
import Article from '../views/article/index.vue'
import Tag from '../views/tag/index.vue'
import Search from '../views/search/index.vue'
import Link from '../views/link/index.vue'
import About from '../views/about/index.vue'
import Category from '../views/category/index.vue'
import RequestError from '../views/error/request-error.vue'
import NotFindError from '../views/error/notfind-error.vue'
import ServerError from '../views/error/server-error.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
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
          path: '/article/:articleId',
          name: 'Article',
          component: Article
        },
        {
          path: '/tag/:tagName',
          name: 'Tag',
          component: Tag
        },
        {
          path: '/search/:keyword',
          name: 'Search',
          component: Search
        },
        {
          path: '/link',
          name: 'Link',
          component: Link
        },
        {
          path: '/about',
          name: 'About',
          component: About
        },
        {
          path: '/category/:categoryName',
          name: 'Category',
          component: Category
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
