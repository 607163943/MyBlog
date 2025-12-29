import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/home/index.vue'
import ArticleEdit from '../views/article-edit/index.vue'
import Layout from '../views/layout/index.vue'
import Article from '../views/article/index.vue'
import Comment from '../views/comment/index.vue'
import Category from '../views/category/index.vue'

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
          path: '/admin/comment',
          name: 'Comment',
          component: Comment
        },
        {
          path: '/admin/category',
          name: 'Category',
          component: Category
        }
      ]
    }
  ]
})

export default router
