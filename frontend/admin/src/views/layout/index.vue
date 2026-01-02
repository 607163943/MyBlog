<script setup>
import LayoutFooter from './footer.vue'
import LayoutSidebar from './sidebar.vue'
import LayoutHeader from './header.vue'
import LayoutBreadcrumb from './breadcrumb.vue'
import { ref } from 'vue'
const collapsed = ref(false)
</script>

<template>
  <a-layout style="min-height: 100vh">
    <!-- 侧边栏 -->
    <a-layout-sider v-model:collapsed="collapsed" collapsible>
      <LayoutSidebar />
    </a-layout-sider>
    <a-layout>
      <!-- 顶部栏 -->
      <a-layout-header style="background: #fff; padding: 0">
        <LayoutHeader />
      </a-layout-header>
      <a-layout-content style="margin: 0 16px">
        <!-- 面包屑 -->
        <LayoutBreadcrumb />
        <div :style="{ padding: '24px', background: '#fff', minHeight: '360px' }">
          <!-- 内容出口 -->
          <router-view></router-view>
        </div>
      </a-layout-content>
      <a-layout-footer style="text-align: center">
        <!-- 底部栏 -->
        <LayoutFooter></LayoutFooter>
      </a-layout-footer>
    </a-layout>
  </a-layout>
</template>

<style lang="less">
#components-layout-demo-side .logo {
  height: 32px;
  margin: 16px;
  background: rgba(255, 255, 255, 0.3);
}

.site-layout .site-layout-background {
  background: #fff;
}
[data-theme='dark'] .site-layout .site-layout-background {
  background: #141414;
}
</style>
