<script setup lang="ts">
import { computed, reactive, ref, watch } from 'vue'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import { useCommentStore } from '@/stores/comment'
import type { CommentVO } from '@/types/comment'

const props = defineProps<{
  modelValue: boolean
  comment: CommentVO | null
}>()

const emit = defineEmits<{
  'update:modelValue': [value: boolean]
}>()

const store = useCommentStore()
const formRef = ref<FormInstance>()
const submitting = ref(false)

const visible = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value),
})

const form = reactive<{
  id?: number
  nickname: string
  email: string
  content: string
  status: number
}>({
  id: undefined,
  nickname: '',
  email: '',
  content: '',
  status: 0,
})

const rules: FormRules = {
  nickname: [
    { required: true, message: '请输入评论者昵称', trigger: 'blur' },
    { max: 50, message: '评论者昵称不能超过 50 个字符', trigger: 'blur' },
  ],
  email: [
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' },
    { max: 100, message: '邮箱不能超过 100 个字符', trigger: 'blur' },
  ],
  content: [{ required: true, message: '请输入评论内容', trigger: 'blur' }],
  status: [{ required: true, message: '请选择审核状态', trigger: 'change' }],
}

watch(
  () => props.modelValue,
  (value) => {
    if (!value) {
      return
    }
    form.id = props.comment?.id
    form.nickname = props.comment?.nickname ?? ''
    form.email = props.comment?.email ?? ''
    form.content = props.comment?.content ?? ''
    form.status = props.comment?.status ?? 0
    formRef.value?.clearValidate()
  },
)

async function handleSubmit() {
  if (!formRef.value) {
    return
  }
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid || form.id === undefined) {
    return
  }

  submitting.value = true
  try {
    await store.editComment({ ...form, id: form.id })
    ElMessage.success('修改成功')
    visible.value = false
  } catch {
    // 请求错误已由 request.ts 统一提示
  } finally {
    submitting.value = false
  }
}

function handleCancel() {
  visible.value = false
}
</script>

<template>
  <el-dialog v-model="visible" title="修改评论" width="600px" :close-on-click-modal="false">
    <el-form ref="formRef" :model="form" :rules="rules" label-width="90px">
      <el-form-item label="所属文章">
        <el-input :model-value="comment?.articleTitle ?? '-'" disabled />
      </el-form-item>
      <el-form-item label="评论者" prop="nickname">
        <el-input v-model="form.nickname" placeholder="请输入评论者昵称" maxlength="50" clearable />
      </el-form-item>
      <el-form-item label="邮箱" prop="email">
        <el-input v-model="form.email" placeholder="请输入评论者邮箱" maxlength="100" clearable />
      </el-form-item>
      <el-form-item label="评论内容" prop="content">
        <el-input
          v-model="form.content"
          type="textarea"
          :rows="5"
          placeholder="请输入评论内容"
          resize="vertical"
        />
      </el-form-item>
      <el-form-item label="审核状态" prop="status">
        <el-select v-model="form.status" placeholder="请选择审核状态" class="status-select">
          <el-option label="待审核" :value="0" />
          <el-option label="已通过" :value="1" />
        </el-select>
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="handleCancel">取消</el-button>
      <el-button type="primary" :loading="submitting" @click="handleSubmit">确定</el-button>
    </template>
  </el-dialog>
</template>

<style scoped lang="scss">
.status-select {
  width: 100%;
}
</style>
