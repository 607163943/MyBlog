<script setup>
import { ref } from 'vue'
import { categoryAllActiveService } from '@/api/category'
import { articlePreviewService } from '@/api/article'
import { useRoute } from 'vue-router'
import { MdPreview } from 'md-editor-v3'
// preview.css相比style.css少了编辑器那部分样式
import 'md-editor-v3/lib/preview.css'

const route = useRoute()
const isEdit = ref(false)
const openCoverModal = ref(false)
const articleEditForm = ref({
  id: '',
  categoryId: '',
  tags: [],
  title: '',
  summary: '',
  cover: '',
  content: '',
  status: ''
})

const categoryList = ref([])
const getCategoryList = async () => {
  try {
    const res = await categoryAllActiveService()
    categoryList.value = res?.data?.data ?? []
  } catch (error) {
    console.error('获取分类列表失败:', error)
    categoryList.value = []
  }
}

// 打开对话框
const articleEditInit = async () => {
  if (route.params.id) {
    // 编辑模式
    isEdit.value = true
    // 获取标签数据
    const res = await articlePreviewService(route.params.id)
    articleEditForm.value = res.data.data
  } else {
    // 添加模式
    isEdit.value = false
  }
}

const id = 'preview-only'

// 数据请求初始化
getCategoryList()
articleEditInit()
</script>

<template>
  <div class="article-preview-container">
    <div class="article-preview-toolbar">
      <a-row>
        <a-col :span="24">
          <div class="article-form-button">
            <div class="article-form-button-left">
              <a-button type="primary" @click="$router.back()">返回列表</a-button>
            </div>
          </div>
        </a-col>
      </a-row>
      <a-row class="article-preview-toolbar-row">
        <a-col :span="24">
          <a-space>
            <div>标题：</div>
            <div class="article-preview-title">{{ articleEditForm.title }}</div>
          </a-space>
        </a-col>
      </a-row>
      <a-row class="article-preview-toolbar-row">
        <a-col :span="24">
          <a-space>
            <div>摘要：</div>
            <div class="article-preview-summary">{{ articleEditForm.summary }}</div>
          </a-space>
        </a-col>
      </a-row>
      <a-row class="article-preview-toolbar-row">
        <a-col :span="6">
          <a-space>
            <div>状态：</div>
            <a-tag color="default" v-if="Number(articleEditForm.status) === 0">草稿</a-tag>
            <a-tag color="green" v-else-if="Number(articleEditForm.status) === 1">发布</a-tag>
            <a-tag color="orange" v-else-if="Number(articleEditForm.status) === 2">下线</a-tag>
          </a-space>
          <a-space>
            <div>分类：</div>
            <div v-for="category in categoryList" :key="category.id">
              <div v-if="Number(category.id) === Number(articleEditForm.categoryId)">
                {{ category.name }}
              </div>
            </div>
          </a-space>
        </a-col>
        <a-col :span="6">
          <a-space>
            <div>标签：</div>
            <a-space>
              <a-tag color="green" v-for="tag in articleEditForm.tags || []" :key="tag.id">
                {{ tag.name }}
              </a-tag>
            </a-space>
          </a-space>
        </a-col>
        <a-col :span="6">
          <a-space>
            <div>文章封面：</div>
            <!-- 没封面时禁用按钮 -->
            <a-button
              type="primary"
              :disabled="!articleEditForm.cover"
              @click="openCoverModal = true"
            >
              查看封面图
            </a-button>
          </a-space>
        </a-col>
      </a-row>
    </div>
    <div>
      <div style="background: #ececec; padding: 30px">
        <a-card title="文章内容" :bordered="false">
          <MdPreview :id="id" :modelValue="articleEditForm.content" />
        </a-card>
      </div>
    </div>
  </div>

  <a-modal v-model:open="openCoverModal" title="封面图">
    <img :src="articleEditForm.cover" alt="封面图" style="width: 100%; height: 100%" />
    <template #footer>
      <a-button @click="openCoverModal = false">关闭</a-button>
    </template>
  </a-modal>
</template>

<style lang="less" scoped>
.article-form-button {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 20px;
}

.article-preview-title {
  font-size: 24px;
  color: #333;
}

.article-preview-summary {
  font-size: 16px;
  color: #666;
}

.article-preview-toolbar-row {
  margin-bottom: 10px;
}
</style>
