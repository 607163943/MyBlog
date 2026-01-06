<script setup>
import { defineOptions, defineExpose, ref, defineEmits, createVNode, nextTick } from 'vue'
import { message, Modal } from 'ant-design-vue'
import { ExclamationCircleOutlined, PlusOutlined } from '@ant-design/icons-vue'
import { categoryAddService, categoryUpdateService, categoryByIdService } from '@/api/category'
import { uploadImageService } from '@/api/upload'

defineOptions({
  name: 'CategoryDialog'
})

const loading = ref(false)
const open = ref(false)
const isEdit = ref(false)
const categoryDialogFormRef = ref(null)

const categoryDialogForm = ref({
  id: '',
  name: '',
  cover: '',
  sort: '',
  status: '',
  uploadFileRefId: ''
})
const fileList = ref([])

// 图片上传后操作
const handleChange = (info) => {
  // 添加待上传图片
  fileList.value = [info.file]
}

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
    categoryDialogForm.value.cover = res.data.data.url
    categoryDialogForm.value.uploadFileRefId = res.data.data.uploadFileRefId
  }
  // 禁用自动上传
  return false
}

// 表单校验规则
const rules = {
  name: [{ required: true, message: '请输入标签名称', trigger: 'blur' }],
  sort: [{ required: true, message: '请输入排序', trigger: 'blur' }],
  status: [{ required: true, message: '请选择是否启用', trigger: 'change' }]
}

// 打开对话框
const openDialog = async (obj) => {
  if (obj) {
    // 编辑模式
    isEdit.value = true
    // 获取标签数据
    const res = await categoryByIdService(obj.id)
    if (res.data.code === 200) {
      open.value = true
      nextTick(() => {
        categoryDialogForm.value = res.data.data
      })
    }
  } else {
    // 添加模式
    isEdit.value = false
    open.value = true
  }
}

defineExpose({
  openDialog
})

const emit = defineEmits(['success'])

const handleOk = async () => {
  await categoryDialogFormRef.value.validate()
  loading.value = true

  if (isEdit.value) {
    handleUpdate()
  } else {
    handleAdd()
  }
}

// 修改
const handleUpdate = async () => {
  Modal.confirm({
    title: '编辑分类',
    icon: createVNode(ExclamationCircleOutlined),
    content: '确定要保存该分类吗？',
    async onOk() {
      try {
        await categoryUpdateService(categoryDialogForm.value)
        message.success('操作成功')
        emit('success')
      } finally {
        loading.value = false
        open.value = false

        // 重置对话框
        categoryDialogFormRef.value.resetFields()
      }
    },
    onCancel() {
      loading.value = false
      open.value = false
    }
  })
}

// 添加
const handleAdd = async () => {
  try {
    await categoryAddService(categoryDialogForm.value)
    message.success('操作成功')
    emit('success')
  } finally {
    loading.value = false
    open.value = false
    // 重置对话框
    categoryDialogFormRef.value.resetFields()
  }
}

const handleCancel = () => {
  // 重置对话框
  categoryDialogFormRef.value.resetFields()
  open.value = false
}
</script>

<template>
  <a-modal
    v-model:open="open"
    :title="isEdit ? '编辑分类' : '新增分类'"
    @ok="handleOk"
    @cancel="handleCancel"
  >
    <template #footer>
      <a-button @click="handleCancel">取消</a-button>
      <a-button type="primary" :loading="loading" @click="handleOk">确定</a-button>
    </template>

    <!-- 表单 -->
    <a-form
      ref="categoryDialogFormRef"
      :rules="rules"
      :model="categoryDialogForm"
      autocomplete="off"
      :label-col="{ span: 4 }"
      style="margin-top: 12px"
    >
      <a-form-item name="id"></a-form-item>
      <a-form-item name="uploadFileRefId"></a-form-item>
      <a-form-item label="分类名称" name="name">
        <a-input v-model:value="categoryDialogForm.name" placeholder="分类名称" />
      </a-form-item>

      <a-form-item label="分类封面" name="cover">
        <a-upload
          :maxCount="1"
          v-model:file-list="fileList"
          :isImageUrl="true"
          list-type="picture-card"
          class="avatar-uploader"
          :show-upload-list="false"
          :before-upload="beforeUpload"
          @change="handleChange"
        >
          <img v-if="categoryDialogForm.cover" :src="categoryDialogForm.cover" alt="avatar" />
          <div v-else>
            <plus-outlined></plus-outlined>
            <div class="ant-upload-text">上传封面图</div>
          </div>
        </a-upload>
      </a-form-item>

      <a-form-item label="排序" name="sort">
        <a-input-number v-model:value="categoryDialogForm.sort" placeholder="排序" :min="0" />
      </a-form-item>

      <a-form-item label="是否启用" name="status">
        <a-radio-group v-model:value="categoryDialogForm.status">
          <a-radio :value="0">启用</a-radio>
          <a-radio :value="1">禁用</a-radio>
        </a-radio-group>
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<style lang="less">
.avatar-uploader > .ant-upload {
  width: 128px;
  height: 128px;
}

.avatar-uploader {
  img {
    border-radius: 8px;
    width: 128px;
    height: 128px;
  }
}
.ant-upload-select-picture-card i {
  font-size: 32px;
  color: #999;
}

.ant-upload-select-picture-card .ant-upload-text {
  margin-top: 8px;
  color: #666;
}
</style>
