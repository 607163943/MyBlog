<script setup lang="ts">
import { onMounted } from 'vue'

import BlogSidebar from '@/components/BlogSidebar.vue'
import ListPagination from '@/components/ListPagination.vue'
import PostListItem from '@/components/PostListItem.vue'
import { useArticleStore } from '@/stores/article'

const articleStore = useArticleStore()

onMounted(() => {
  articleStore.fetchHomePage(1)
})

function changePage(page: number) {
  articleStore.fetchHomePage(page)
}
</script>

<template>
  <div class="page-container page-container--wide">
    <div class="layout-with-aside">
      <div v-loading="articleStore.loading" class="layout-main">
        <div v-if="articleStore.records.length" class="post-list">
          <PostListItem v-for="post in articleStore.records" :key="post.id" :post="post" />
        </div>
        <el-card v-else-if="!articleStore.loading" shadow="never">
          <el-empty description="暂无文章" />
        </el-card>

        <ListPagination
          v-if="articleStore.total > 0"
          :total="articleStore.total"
          :page="articleStore.page"
          :page-size="articleStore.pageSize"
          @update:page="changePage"
        />
      </div>

      <aside class="home-aside">
        <BlogSidebar />
      </aside>
    </div>
  </div>
</template>
