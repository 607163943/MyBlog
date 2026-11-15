<script setup lang="ts">
import { onMounted } from 'vue'

import BlogSidebar from '@/components/BlogSidebar.vue'
import CategoryCard from '@/components/CategoryCard.vue'
import { useCategoryStore } from '@/stores/category'

const categoryStore = useCategoryStore()

onMounted(() => {
  categoryStore.fetchList()
})
</script>

<template>
  <div class="page-container page-container--wide">
    <div class="layout-with-aside">
      <div v-loading="categoryStore.listLoading" class="layout-main">
        <div v-if="categoryStore.categories.length" class="category-grid">
          <CategoryCard
            v-for="category in categoryStore.categories"
            :key="category.id"
            :category="category"
            :hot-articles="category.hotArticles"
          />
        </div>
        <el-card v-else-if="!categoryStore.listLoading" shadow="never">
          <el-empty description="暂无分类" />
        </el-card>
      </div>

      <aside class="home-aside">
        <BlogSidebar />
      </aside>
    </div>
  </div>
</template>

<style scoped>
/* 左侧主栏宽度随侧边栏收窄，用 auto-fill 让列数按可用宽度自适应 */
.category-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(260px, 1fr));
  gap: 24px;
}

@media (max-width: 1024px) {
  .category-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
    gap: 20px;
  }
}

@media (max-width: 640px) {
  .category-grid {
    grid-template-columns: minmax(0, 1fr);
    gap: 16px;
  }
}
</style>
