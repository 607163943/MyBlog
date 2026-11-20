<script setup lang="ts">
import { ref, watch } from 'vue'
import { ElMessage, type UploadInstance, type UploadRequestOptions } from 'element-plus'
import { IMAGE_ACCEPT, uploadImage, validateImageFile } from '@/api/upload'

const props = withDefaults(
  defineProps<{
    modelValue?: string | null
    fileId?: number | null
    fallbackText?: string
    size?: number
  }>(),
  {
    modelValue: '',
    fileId: null,
    fallbackText: '头',
    size: 96,
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
    ElMessage.success('头像上传成功')
    return url
  } catch {
    // 校验与请求错误已由 uploadImage / request 统一提示
  } finally {
    uploading.value = false
    emit('uploading', false)
    uploadRef.value?.clearFiles()
  }
}
</script>

<template>
  <div class="avatar-upload">
    <el-upload
      ref="uploadRef"
      action="#"
      :accept="IMAGE_ACCEPT"
      :show-file-list="false"
      :disabled="uploading"
      :before-upload="beforeUpload"
      :http-request="handleUpload"
    >
      <div
        v-loading="uploading"
        class="avatar-upload__area"
        :style="{ width: `${size}px`, height: `${size}px` }"
      >
        <img
          v-if="modelValue && !previewFailed"
          class="avatar-upload__image"
          :src="modelValue"
          alt="头像"
          @error="previewFailed = true"
        />
        <span v-else class="avatar-upload__fallback">{{ fallbackText }}</span>
        <div class="avatar-upload__mask">更换头像</div>
      </div>
    </el-upload>
    <span class="avatar-upload__tip">支持 jpg/png/gif/webp/bmp，不超过 5MB</span>
  </div>
</template>

<style scoped lang="scss">
.avatar-upload {
  display: flex;
  align-items: center;
  gap: 16px;
}

.avatar-upload__area {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  cursor: pointer;
  background-color: var(--el-fill-color-lighter);
  border: 1px solid var(--el-border-color-light);
  border-radius: 50%;
  transition: border-color 0.2s;

  &:hover {
    border-color: var(--el-color-primary);
  }
}

.avatar-upload__image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.avatar-upload__fallback {
  font-size: 32px;
  color: var(--el-text-color-secondary);
}

.avatar-upload__mask {
  position: absolute;
  inset: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 13px;
  color: #fff;
  background-color: rgb(0 0 0 / 45%);
  opacity: 0;
  transition: opacity 0.2s;
}

.avatar-upload__area:hover .avatar-upload__mask {
  opacity: 1;
}

.avatar-upload__tip {
  font-size: 12px;
  color: var(--el-text-color-placeholder);
}
</style>
