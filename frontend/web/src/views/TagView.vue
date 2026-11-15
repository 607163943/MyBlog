<script setup lang="ts">
import { computed, onMounted } from 'vue'

import BlogSidebar from '@/components/BlogSidebar.vue'
import TagCard from '@/components/TagCard.vue'
import { useTagStore } from '@/stores/tag'

const tagStore = useTagStore()

const wallTags = computed(() =>
  [...tagStore.tags].sort(
    (a, b) => b.articleCount - a.articleCount || a.name.localeCompare(b.name, 'zh-CN'),
  ),
)

onMounted(() => {
  tagStore.fetchList()
})
</script>

<template>
  <div class="page-container page-container--wide">
    <div class="layout-with-aside">
      <div v-loading="tagStore.listLoading" class="layout-main">
        <el-card shadow="never">
          <template #header>
            <div class="card-heading">
              <div class="card-heading-title">
                <span class="heading-accent" />
                <h2>标签墙</h2>
              </div>
              <span class="card-heading-meta">
                共
                <strong>{{ wallTags.length }}</strong>
                个
              </span>
            </div>
          </template>

          <p class="tag-wall-hint">点击任意标签，查看该标签下的全部文章</p>

          <div v-if="wallTags.length" class="tag-wall">
            <TagCard v-for="tag in wallTags" :key="tag.id" :tag="tag" />
          </div>
          <el-empty v-else description="暂无标签" />
        </el-card>
      </div>

      <aside class="home-aside">
        <BlogSidebar />
      </aside>
    </div>
  </div>
</template>

<style scoped>
.tag-wall-hint {
  margin-bottom: 16px;
  color: var(--el-text-color-secondary);
  font-size: 13px;
}

/* 左侧主栏变窄后，宽屏可用宽度约为 876px；1200px 以下退回三列，避免卡片过窄 */
.tag-wall {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 12px;
}

@media (max-width: 1200px) {
  .tag-wall {
    grid-template-columns: repeat(3, minmax(0, 1fr));
  }
}

@media (max-width: 768px) {
  .tag-wall {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
}

@media (max-width: 480px) {
  .tag-wall {
    gap: 10px;
  }
}
</style>
