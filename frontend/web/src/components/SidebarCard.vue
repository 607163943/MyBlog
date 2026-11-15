<script setup lang="ts">
import type { Component } from 'vue'

defineProps<{
  title: string
  icon?: Component
  /** 整张卡片是否带悬浮阴影：含列表交互的卡片按需开启 */
  hoverable?: boolean
}>()
</script>

<template>
  <el-card class="sidebar-card" :class="{ 'is-hoverable': hoverable }" shadow="never">
    <template #header>
      <div class="sidebar-card-heading">
        <el-icon v-if="icon" class="sidebar-card-icon"><component :is="icon" /></el-icon>
        <h2 class="sidebar-card-title">{{ title }}</h2>
      </div>
    </template>

    <slot />
  </el-card>
</template>

<style scoped>
.sidebar-card.is-hoverable {
  transition: box-shadow var(--app-card-hover-duration);
}

.sidebar-card.is-hoverable:hover {
  box-shadow: var(--app-card-hover-shadow);
}

.sidebar-card-heading {
  display: flex;
  align-items: center;
  gap: 7px;
}

.sidebar-card-icon {
  color: var(--el-text-color-primary);
  font-size: 19px;
}

.sidebar-card-title {
  color: var(--el-text-color-primary);
  font-size: 15px;
  font-weight: 600;
}

:deep(.el-card__body) {
  padding: 16px 20px;
}
</style>
