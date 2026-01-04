<script setup>
import { defineOptions, defineExpose, ref, defineEmits, createVNode } from 'vue'
import { message, Modal } from 'ant-design-vue'
import { ExclamationCircleOutlined } from '@ant-design/icons-vue'
import { dictAddService, dictUpdateService, dictByIdService } from '@/api/tag'

defineOptions({
  name: 'DictDialog'
})

const loading = ref(false)
const open = ref(false)
const isEdit = ref(false)
const dictDialogFormRef = ref(null)

const dictDialogForm = ref({
  id: '',
  dictType: '',
  status: '',
  remark: ''
})

// 表单校验规则
const rules = {
  dictType: [{ required: true, message: '请输入字典类型', trigger: 'blur' }],
  status: [{ required: true, message: '请选择是否启用', trigger: 'change' }],
  remark: [{ required: true, message: '请输入备注', trigger: 'blur' }]
}

// 打开对话框
const openDialog = async (obj) => {
  if (obj) {
    // 编辑模式
    isEdit.value = true
    // 获取字典数据
    const res = await dictByIdService(obj.id)
    if (res.data.code === 200) {
      dictDialogForm.value = res.data.data
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
  await dictDialogFormRef.value.validate()
  loading.value = true

  if (isEdit.value) {
    Modal.confirm({
      title: '编辑字典',
      icon: createVNode(ExclamationCircleOutlined),
      content: '确定要保存该字典吗？',
      async onOk() {
        await dictUpdateService(dictDialogForm.value)
        message.success('操作成功')

        loading.value = false
        open.value = false

        // 重置对话框
        dictDialogFormRef.value.resetFields()
        emit('success')
      },
      onCancel() {
        loading.value = false
        open.value = false
      }
    })
  } else {
    await dictAddService(dictDialogForm.value)
    message.success('操作成功')

    loading.value = false
    open.value = false

    // 重置对话框
    dictDialogFormRef.value.resetFields()
    emit('success')
  }
}

const handleCancel = () => {
  // 重置对话框
  dictDialogFormRef.value.resetFields()
  open.value = false
}
</script>

<template>
  <a-modal
    v-model:open="open"
    :title="isEdit ? '编辑字典' : '新增字典'"
    @ok="handleOk"
    @cancel="handleCancel"
  >
    <template #footer>
      <a-button @click="handleCancel">取消</a-button>
      <a-button type="primary" :loading="loading" @click="handleOk">确定</a-button>
    </template>

    <!-- 表单 -->
    <a-form
      ref="dictDialogFormRef"
      :rules="rules"
      :model="dictDialogForm"
      autocomplete="off"
      :label-col="{ span: 4 }"
      style="margin-top: 12px"
    >
      <a-form-item label="字典类型" name="dictType">
        <a-input v-model:value="dictDialogForm.dictType" placeholder="字典类型" />
      </a-form-item>

      <a-form-item label="是否启用" name="status">
        <a-radio-group v-model:value="dictDialogForm.status">
          <a-radio :value="0">启用</a-radio>
          <a-radio :value="1">禁用</a-radio>
        </a-radio-group>
      </a-form-item>

      <a-form-item label="备注" name="remark">
        <a-textarea
          v-model:value="dictDialogForm.remark"
          placeholder="备注"
          :auto-size="{ minRows: 2, maxRows: 5 }"
        />
      </a-form-item>

      <a-row v-if="isEdit">
        <a-col :span="24"> 辅助信息： </a-col>
      </a-row>
      <a-row v-if="isEdit">
        <a-col :span="12">
          <div class="create-time">
            <span>创建时间：</span>
            <span>{{ dictDialogForm.createTime }}</span>
          </div>
        </a-col>
        <a-col :span="12">
          <div class="update-time">
            <span>更新时间：</span>
            <span>{{ dictDialogForm.updateTime }}</span>
          </div>
        </a-col>
      </a-row>
    </a-form>
  </a-modal>
</template>

<style lang="less"></style>
