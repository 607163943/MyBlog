<script setup lang="ts">
import { ref, watch } from 'vue'
import { ElMessage, type UploadInstance, type UploadRequestOptions } from 'element-plus'
import { Delete, Plus, Refresh } from '@element-plus/icons-vue'
import { IMAGE_ACCEPT, uploadImage, validateImageFile } from '@/api/upload'

const props = withDefaults(
  defineProps<{
    modelValue?: string
    fileId?: number | null
  }>(),
  {
    modelValue: '',
    fileId: null,
  },
)

const emit = defineEmits<{
  'update:modelValue': [value: string]
  'update:fileId': [value: number | null]
  uploading: [value: boolean]
}>()

const uploadRef = ref<UploadInstance>()
const uploading = ref(false)
const previewFailed = ref(false)

watch(
  () => props.modelValue,
  () => {
    previewFailed.value = false
  },
)

function beforeUpload(file: File) {
  const message = validateImageFile(file)
  if (message) {
    ElMessage.warning(message)
    return false
  }
  return true
}

async function handleUpload(options: UploadRequestOptions) {
  uploading.value = true
  emit('uploading', true)
  try {
    const { fileId, url } = await uploadImage(options.file)
    emit('update:modelValue', url)
    emit('update:fileId', fileId)
    ElMessage.success('封面上传成功')
    return url
  } finally {
    uploading.value = false
    emit('uploading', false)
    uploadRef.value?.clearFiles()
  }
}

function handleRemove() {
  emit('update:modelValue', '')
  emit('update:fileId', null)
  uploadRef.value?.clearFiles()
}
</script>

<template>
  <div class="cover-upload">
    <el-upload
      ref="uploadRef"
      class="cover-upload__trigger"
      action="#"
      :accept="IMAGE_ACCEPT"
      :show-file-list="false"
      :disabled="uploading"
      :before-upload="beforeUpload"
      :http-request="handleUpload"
    >
      <div v-loading="uploading" class="cover-upload__area">
        <template v-if="modelValue && !previewFailed">
          <img
            class="cover-upload__image"
            :src="modelValue"
            alt="文章封面"
            @error="previewFailed = true"
          />
          <div class="cover-upload__mask">
            <el-icon><Refresh /></el-icon>
            <span>点击替换封面</span>
          </div>
        </template>
        <div v-else class="cover-upload__empty">
          <el-icon :size="28"><Plus /></el-icon>
          <span>{{ previewFailed ? '封面加载失败，点击重新上传' : '点击上传封面' }}</span>
          <span class="cover-upload__tip">支持 jpg/jpeg/png/gif/webp/bmp，不超过 5MB</span>
        </div>
      </div>
    </el-upload>

    <el-button
      v-if="modelValue"
      link
      type="danger"
      :icon="Delete"
      :disabled="uploading"
      @click="handleRemove"
    >
      清除封面
    </el-button>
  </div>
</template>

<style scoped lang="scss">
.cover-upload {
  width: 100%;
  max-width: 360px;
}

.cover-upload__trigger {
  display: block;
  width: 100%;
}

.cover-upload__area {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 180px;
  overflow: hidden;
  cursor: pointer;
  background-color: var(--el-fill-color-lighter);
  border: 1px dashed var(--el-border-color);
  border-radius: var(--el-border-radius-base);
  transition: border-color 0.2s;

  &:hover {
    border-color: var(--el-color-primary);
  }
}

.cover-upload__image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.cover-upload__mask {
  position: absolute;
  inset: 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 6px;
  font-size: 13px;
  color: #fff;
  background-color: rgba(0, 0, 0, 0.45);
  opacity: 0;
  transition: opacity 0.2s;
}

.cover-upload__area:hover .cover-upload__mask {
  opacity: 1;
}

.cover-upload__empty {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  color: var(--el-text-color-secondary);
}

.cover-upload__tip {
  font-size: 12px;
  color: var(--el-text-color-placeholder);
}
</style>
