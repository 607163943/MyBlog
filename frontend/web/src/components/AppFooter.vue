<script setup lang="ts">
import { siteConfig } from '@/config/site'
</script>

<template>
  <footer class="app-footer">
    <p class="copyright">{{ siteConfig.copyright }}</p>
  </footer>
</template>

<style scoped>
/* 仅保留版权行：不设背景与分隔线，铺在页面底色上 */
.app-footer {
  padding: 0 var(--app-gutter) 28px;
  background-color: transparent;
}

.copyright {
  color: var(--el-text-color-secondary);
  font-size: 12px;
  text-align: center;
}
</style>
