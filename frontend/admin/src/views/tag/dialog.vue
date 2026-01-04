<script setup>
import { defineOptions, defineExpose, ref, defineEmits, createVNode } from 'vue'
import { message, Modal } from 'ant-design-vue'
import { ExclamationCircleOutlined } from '@ant-design/icons-vue'
import { tagAddService, tagUpdateService, tagByIdService } from '@/api/tag'

defineOptions({
  name: 'TagDialog'
})

const loading = ref(false)
const open = ref(false)
const isEdit = ref(false)
const tagDialogFormRef = ref(null)

const tagDialogForm = ref({
  id: '',
  name: '',
  status: ''
})

// 表单校验规则
const rules = {
  name: [{ required: true, message: '请输入标签名称', trigger: 'blur' }],
  status: [{ required: true, message: '请选择是否启用', trigger: 'change' }]
}

// 打开对话框
const openDialog = async (obj) => {
  if (obj) {
    // 编辑模式
    isEdit.value = true
    // 获取标签数据
    const res = await tagByIdService(obj.id)
    if (res.data.code === 200) {
      tagDialogForm.value = res.data.data
    }
  } else {
    // 添加模式
    isEdit.value = false
  }
  open.value = true
}

defineExpose({
  openDialog
})

const emit = defineEmits(['success'])

const handleOk = async () => {
  await tagDialogFormRef.value.validate()
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
    title: '编辑标签',
    icon: createVNode(ExclamationCircleOutlined),
    content: '确定要保存该标签吗？',
    async onOk() {
      try {
        await tagUpdateService(tagDialogForm.value)
        message.success('操作成功')
        emit('success')
      } finally {
        loading.value = false
        open.value = false

        // 重置对话框
        tagDialogFormRef.value.resetFields()
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
    await tagAddService(tagDialogForm.value)
    message.success('操作成功')
    emit('success')
  } finally {
    loading.value = false
    open.value = false
    // 重置对话框
    tagDialogFormRef.value.resetFields()
  }
}

const handleCancel = () => {
  // 重置对话框
  tagDialogFormRef.value.resetFields()
  open.value = false
}
</script>

<template>
  <a-modal
    v-model:open="open"
    :title="isEdit ? '编辑标签' : '新增标签'"
    @ok="handleOk"
    @cancel="handleCancel"
  >
    <template #footer>
      <a-button @click="handleCancel">取消</a-button>
      <a-button type="primary" :loading="loading" @click="handleOk">确定</a-button>
    </template>

    <!-- 表单 -->
    <a-form
      ref="tagDialogFormRef"
      :rules="rules"
      :model="tagDialogForm"
      autocomplete="off"
      :label-col="{ span: 4 }"
      style="margin-top: 12px"
    >
      <a-form-item label="标签名称" name="name">
        <a-input v-model:value="tagDialogForm.name" placeholder="标签名称" />
      </a-form-item>

      <a-form-item label="是否启用" name="status">
        <a-radio-group v-model:value="tagDialogForm.status">
          <a-radio :value="0">启用</a-radio>
          <a-radio :value="1">禁用</a-radio>
        </a-radio-group>
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<style lang="less"></style>
