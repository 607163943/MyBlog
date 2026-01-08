<script setup>
import { ref } from 'vue'
import { message } from 'ant-design-vue'
import { uploadImageService } from '@/api/upload'
import { PlusOutlined } from '@ant-design/icons-vue'
import { categoryAllService } from '@/api/category'
import { tagAllService } from '@/api/tag'
import { MdEditor } from 'md-editor-v3'
import 'md-editor-v3/lib/style.css'

const articleDialogFormRef = ref(null)

const articleDialogForm = ref({
  id: '',
  categoryId: '',
  tagIds: [],
  title: '',
  summary: '',
  cover: '',
  content: '',
  uploadFileRefId: ''
})

const categoryList = ref([])
const tagList = ref([])
const getCategoryList = async () => {
  const res = await categoryAllService()
  categoryList.value = res.data.data
}

const getTagList = async () => {
  const res = await tagAllService()
  tagList.value = res.data.data
}

const fileList = ref([])

// 上传图片前置操作
const beforeUpload = async (file) => {
  const isJpgOrPng =
    file.type === 'image/jpeg' || file.type === 'image/jpg' || file.type === 'image/png'
  if (!isJpgOrPng) {
    message.error('只能上传JPG、JPEG、PNG文件!')
    return false
  }
  const isLt2M = file.size / 1024 / 1024 < 10
  if (!isLt2M) {
    message.error('图片大小不能超过10MB!')
    return false
  }

  const formData = new FormData()
  formData.append('file', file)

  const res = await uploadImageService(formData)
  if (res.data.code === 200) {
    articleDialogForm.value.cover = res.data.data.url
    articleDialogForm.value.uploadFileRefId = res.data.data.uploadFileRefId
  }
  // 禁用自动上传
  return false
}

// 图片上传后操作
const handleChange = (info) => {
  // 添加待上传图片
  fileList.value = [info.file]
}

// 数据请求初始化
getCategoryList()
getTagList()
</script>

<template>
  <div class="article-form-container">
    <a-form :model="articleDialogForm" ref="articleDialogFormRef" autocomplete="off">
      <a-form-item label="标题" name="title">
        <a-input v-model:value="articleDialogForm.title" />
      </a-form-item>

      <a-form-item label="摘要" name="summary">
        <a-input v-model:value="articleDialogForm.summary" />
      </a-form-item>

      <a-row>
        <a-col :span="12">
          <a-form-item label="文章封面" name="cover">
            <a-upload
              :maxCount="1"
              v-model:file-list="fileList"
              list-type="picture-card"
              class="avatar-uploader"
              :show-upload-list="false"
              :before-upload="beforeUpload"
              @change="handleChange"
            >
              <img v-if="articleDialogForm.cover" :src="articleDialogForm.cover" alt="avatar" />
              <div v-else>
                <plus-outlined></plus-outlined>
                <div class="ant-upload-text">上传封面图</div>
              </div>
            </a-upload>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="分类" name="categoryId">
            <a-select v-model:value="articleDialogForm.categoryId" style="width: 180px">
              <a-select-option
                v-for="category in categoryList"
                :key="category.id"
                :value="category.id"
              >
                {{ category.name }}
              </a-select-option>
            </a-select>
          </a-form-item>

          <a-form-item label="标签" name="tagId">
            <a-select v-model:value="articleDialogForm.tagId" style="width: 180px">
              <a-select-option v-for="tag in tagList" :key="tag.id" :value="tag.id">
                {{ tag.name }}
              </a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
      </a-row>

      <a-form-item name="content">
        <MdEditor v-model="articleDialogForm.content" />
      </a-form-item>
    </a-form>
  </div>
  <div class="article-form-button">
    <div class="article-form-button-left">
      <a-button type="primary" @click="$router.back()">返回列表</a-button>
    </div>
    <div class="article-form-button-right">
      <a-button>保存草稿</a-button>
      <a-button style="margin-left: 12px">发布</a-button>
    </div>
  </div>
</template>

<style lang="less" scoped>
:deep(.avatar-uploader > .ant-upload) {
  width: 200px !important;
  height: 200px !important;
}
:deep(.ant-upload-select-picture-card i) {
  font-size: 32px;
  color: #999;
}
:deep(.ant-upload-select-picture-card .ant-upload-text) {
  margin-top: 8px;
  color: #666;
}
.article-form-button {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 20px;
}
</style>
