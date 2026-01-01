<script setup>
import LayoutFooter from './footer.vue'
import LayoutSidebar from './sidebar.vue'
import LayoutHeader from './header.vue'
</script>

<template>
  <!-- 顶部栏 -->
  <LayoutHeader />
  <!-- 侧边栏 -->
  <LayoutSidebar />
  <!-- 二级路由出口 -->
  <router-view></router-view>
  <!-- 底部栏 -->
  <LayoutFooter></LayoutFooter>
</template>

<style></style>
