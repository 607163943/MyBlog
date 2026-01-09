<script setup>
import { defineOptions, ref, watch } from 'vue'
import { useRoute } from 'vue-router'

defineOptions({
  name: 'LayoutBreadcrumb'
})

const route = useRoute()

const items = [
  {
    label: '首页',
    path: '/admin/home'
  },
  {
    label: '统计',
    path: '/admin/statistics'
  },

  {
    label: '分类管理',
    path: '/admin/category'
  },
  {
    label: '文章管理',
    path: '/admin/article'
  },
  {
    label: '文章编辑',
    path: '/admin/article/edit'
  },
  {
    label: '文章预览',
    path: '/admin/article/preview'
  },
  {
    label: '标签管理',
    path: '/admin/tag'
  },
  {
    label: '个人中心',
    path: '/admin/profile'
  }
]

const breadcrumbList = ref([])

const createBreadcrumbList = (path) => {
  breadcrumbList.value = [
    {
      label: '首页',
      path: '/admin/home'
    }
  ]
  for (let item of items) {
    if (path.includes(item.path) && item.path !== '/admin/home') {
      breadcrumbList.value.push({
        label: item.label,
        path: item.path
      })
    }
  }
}

watch(
  route,
  () => {
    createBreadcrumbList(route.path)
  },
  {
    immediate: true
  }
)
</script>

<template>
  <a-breadcrumb style="margin: 16px 0" v-if="breadcrumbList.length > 1">
    <a-breadcrumb-item
      v-for="item in breadcrumbList.slice(0, breadcrumbList.length - 1)"
      :key="item.path"
    >
      <router-link :to="item.path">{{ item.label }}</router-link>
    </a-breadcrumb-item>
    <a-breadcrumb-item>{{ breadcrumbList[breadcrumbList.length - 1].label }}</a-breadcrumb-item>
  </a-breadcrumb>

  <a-breadcrumb style="margin: 16px 0" v-else>
    <a-breadcrumb-item>首页</a-breadcrumb-item>
  </a-breadcrumb>
</template>

<style lang="less"></style>
