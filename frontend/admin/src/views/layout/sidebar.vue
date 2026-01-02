<script setup>
import { defineOptions, ref, h } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import {
  PieChartOutlined,
  DesktopOutlined,
  UserOutlined,
  TeamOutlined
} from '@ant-design/icons-vue'

defineOptions({
  name: 'LayoutSidebar'
})

const route = useRoute()
const router = useRouter()

const selectedKeys = ref(['1'])
const openKeys = ref([])
const items = ref([
  {
    key: '1',
    icon: () => h(PieChartOutlined),
    label: '首页',
    title: '首页',
    path: '/admin/home'
  },
  {
    key: '2',
    icon: () => h(DesktopOutlined),
    label: '统计',
    title: '统计',
    path: '/admin/statistics'
  },
  {
    key: '3',
    icon: () => h(UserOutlined),
    label: '业务管理',
    title: '业务管理',
    children: [
      {
        key: '3-1',
        label: '分类管理',
        title: '分类管理',
        path: '/admin/category'
      },
      {
        key: '3-2',
        label: '文章管理',
        title: '文章管理',
        path: '/admin/article'
      },
      {
        key: '3-3',
        label: '标签管理',
        title: '标签管理',
        path: '/admin/tag'
      }
    ]
  },
  {
    key: '4',
    icon: () => h(TeamOutlined),
    label: '系统管理',
    title: '系统管理',
    children: [
      {
        key: '4-1',
        label: '用户管理',
        title: '用户管理',
        path: '/admin/user'
      },
      {
        key: '4-2',
        label: '系统字典',
        title: '系统字典',
        path: '/admin/dictionary'
      }
    ]
  }
])

// 路由跳转
const handlerMenuClick = ({ item }) => {
  router.push(item.path)
}

// 获取当前激活菜单项key
const getActiveKeys = () => {
  for (let item of items.value) {
    if (item.children) {
      for (let child of item.children) {
        if (child.path === route.path) {
          selectedKeys.value = [child.key]
          openKeys.value = [item.key]
          return
        }
      }
    } else {
      if (item.path === route.path) {
        selectedKeys.value = [item.key]
        openKeys.value = []
        return
      }
    }
  }
}

getActiveKeys()
</script>
<template>
  <!-- logo -->
  <div class="side-menu-logo">
    <a href="/" class="brand-link">
      <img src="../../../public/dist/img/logo.png" />
    </a>
  </div>
  <a-menu
    @click="handlerMenuClick"
    v-model:selectedKeys="selectedKeys"
    v-model:openKeys="openKeys"
    :items="items"
    theme="dark"
    mode="inline"
  >
  </a-menu>
</template>

<style lang="less">
.side-menu-logo {
  text-align: center;
  height: 32px;
  background: rgba(255, 255, 255, 0.3);
  margin: 16px;
  img {
    width: 36px;
  }
}
</style>
