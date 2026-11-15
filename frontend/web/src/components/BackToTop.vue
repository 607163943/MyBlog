<script setup lang="ts">
import { onBeforeUnmount, onMounted, ref } from 'vue'
import { Top } from '@element-plus/icons-vue'

const visible = ref(false)

function updateVisible() {
  visible.value = window.scrollY > 0
}

function scrollToTop() {
  const reduceMotion = window.matchMedia('(prefers-reduced-motion: reduce)').matches
  window.scrollTo({ top: 0, behavior: reduceMotion ? 'auto' : 'smooth' })
}

onMounted(() => {
  updateVisible()
  window.addEventListener('scroll', updateVisible, { passive: true })
})

onBeforeUnmount(() => {
  window.removeEventListener('scroll', updateVisible)
})
</script>

<template>
  <Transition name="back-to-top">
    <el-button
      v-show="visible"
      class="back-to-top"
      type="primary"
      aria-label="回到顶部"
      title="回到顶部"
      @click="scrollToTop"
    >
      <el-icon :size="20"><Top /></el-icon>
    </el-button>
  </Transition>
</template>

<style scoped>
/* 与分类目录入口同款蓝色圆角方块，提高选择器优先级避免被 el-button 默认样式覆盖 */
.back-to-top.el-button {
  width: 44px;
  height: 44px;
  padding: 0;
  border-radius: 12px;
}

.back-to-top {
  position: fixed;
  right: var(--app-gutter);
  bottom: 32px;
  z-index: 90;
  box-shadow: 0 8px 18px color-mix(in srgb, var(--el-color-primary) 28%, transparent);
  transition: box-shadow var(--el-transition-duration) ease;
}

/* 悬停只改阴影、不用 transform，避免与出现 / 消失的位移动画互相覆盖 */
.back-to-top:hover {
  box-shadow: 0 10px 24px color-mix(in srgb, var(--el-color-primary) 38%, transparent);
}

/* 出现 / 消失：淡入淡出 + 上移缩放 */
.back-to-top-enter-active,
.back-to-top-leave-active {
  transition:
    opacity 0.25s ease,
    transform 0.25s ease;
}

.back-to-top-enter-from,
.back-to-top-leave-to {
  opacity: 0;
  transform: translateY(12px) scale(0.9);
}

@media (max-width: 768px) {
  .back-to-top {
    right: 16px;
    bottom: 20px;
  }
}

@media (prefers-reduced-motion: reduce) {
  .back-to-top,
  .back-to-top-enter-active,
  .back-to-top-leave-active {
    transition: none;
  }
}
</style>
