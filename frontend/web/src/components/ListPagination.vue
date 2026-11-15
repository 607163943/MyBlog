<script setup lang="ts">
defineProps<{
  total: number
  page: number
  pageSize: number
}>()

const emit = defineEmits<{
  'update:page': [page: number]
}>()

function handleCurrentChange(page: number) {
  emit('update:page', page)
}
</script>

<template>
  <div class="list-pagination">
    <el-pagination
      :current-page="page"
      :page-size="pageSize"
      :pager-count="5"
      :total="total"
      background
      layout="prev, pager, next, total"
      @current-change="handleCurrentChange"
    />
  </div>
</template>

<style scoped>
/* 分页独立于文章卡片：白色胶囊底条，与文章卡片等宽，内容居中 */
.list-pagination {
  display: flex;
  width: 100%;
  max-width: 100%;
  justify-content: center;
  padding: 10px 24px;
  border-radius: 999px;
  background-color: var(--el-bg-color);
}

.list-pagination :deep(.el-pagination) {
  flex-wrap: wrap;
  justify-content: center;
  row-gap: 8px;
}

/* background 模式下默认按钮间距为 4px，这里略放大到 6px：
 * 按钮保持紧凑成组，鼠标在相邻页码间移动的距离更短
 */
.list-pagination :deep(.el-pagination.is-background .btn-prev),
.list-pagination :deep(.el-pagination.is-background .btn-next),
.list-pagination :deep(.el-pagination.is-background .el-pager li) {
  margin: 0 6px;
}

/* 翻页按钮组与总数文字之间留出距离，区分两组信息 */
.list-pagination :deep(.el-pagination__total) {
  margin-left: 12px;
}

@media (max-width: 768px) {
  .list-pagination {
    padding: 8px 16px;
  }
}

/* 窄屏下分页会换行：进一步收紧间距并改用圆角矩形，避免胶囊被撑成多行椭圆 */
@media (max-width: 600px) {
  .list-pagination {
    padding: 8px 16px;
    border-radius: 24px;
  }

  .list-pagination :deep(.el-pagination.is-background .btn-prev),
  .list-pagination :deep(.el-pagination.is-background .btn-next),
  .list-pagination :deep(.el-pagination.is-background .el-pager li) {
    margin: 0 4px;
  }

  .list-pagination :deep(.el-pagination__total) {
    margin-left: 8px;
  }
}
</style>
