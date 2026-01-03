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
import Dict from '../views/dict/index.vue'
import DictValue from '../views/dict/dict-value/index.vue'
import { useUserStore } from '@/stores'
import { isEmpty } from 'es-toolkit/compat'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/login',
      name: 'Login',
      component: Login,
      meta: {
        loginRequired: false
      }
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
          component: Home,
          meta: {
            loginRequired: true
          }
        },
        {
          path: '/admin/statistics',
          name: 'Statistics',
          component: Statistics,
          meta: {
            loginRequired: true
          }
        },
        {
          path: '/admin/article/edit',
          name: 'ArticleEdit',
          component: ArticleEdit,
          meta: {
            loginRequired: true
          }
        },
        {
          path: '/admin/article',
          name: 'Article',
          component: Article,
          meta: {
            loginRequired: true
          }
        },
        {
          path: '/admin/category',
          name: 'Category',
          component: Category,
          meta: {
            loginRequired: true
          }
        },
        {
          path: '/admin/tag',
          name: 'Tag',
          component: Tag,
          meta: {
            loginRequired: true
          }
        },
        {
          path: '/admin/config',
          name: 'Config',
          component: Config,
          meta: {
            loginRequired: true
          }
        },
        {
          path: '/admin/profile',
          name: 'Profile',
          component: Profile,
          meta: {
            loginRequired: true
          }
        },
        {
          path: '/admin/dict',
          name: 'Dict',
          component: Dict,
          meta: {
            loginRequired: true
          }
        },
        {
          path: '/admin/dict-value/:dictId',
          name: 'DictValue',
          component: DictValue,
          meta: {
            loginRequired: true
          }
        }
      ]
    }
  ]
})

export default router

router.beforeEach((to) => {
  if (to.meta.loginRequired) {
    const userStore = useUserStore()

    if (isEmpty(userStore.token)) {
      return { path: '/login' }
    }
  }
  return true
})
