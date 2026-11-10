<script setup lang="ts">
import { computed, reactive, ref, watch } from 'vue'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import { useTagStore } from '@/stores/tag'
import type { TagVO } from '@/types/tag'

const props = defineProps<{
  modelValue: boolean
  tag: TagVO | null
}>()

const emit = defineEmits<{
  'update:modelValue': [value: boolean]
}>()

const store = useTagStore()
const formRef = ref<FormInstance>()
const submitting = ref(false)

const visible = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value),
})

const isEdit = computed(() => props.tag !== null)
const title = computed(() => (isEdit.value ? '修改标签' : '新增标签'))

const form = reactive<{
  id?: number
  name: string
}>({
  id: undefined,
  name: '',
})

const rules: FormRules = {
  name: [
    { required: true, message: '请输入标签名称', trigger: 'blur' },
    { max: 50, message: '标签名称不能超过 50 个字符', trigger: 'blur' },
  ],
}

watch(
  () => props.modelValue,
  (value) => {
    if (!value) {
      return
    }
    form.id = props.tag?.id
    form.name = props.tag?.name ?? ''
    formRef.value?.clearValidate()
  },
)

async function handleSubmit() {
  if (!formRef.value) {
    return
  }
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) {
    return
  }

  submitting.value = true
  try {
    if (isEdit.value) {
      await store.editTag({ ...form })
      ElMessage.success('修改成功')
    } else {
      await store.createTag({ ...form })
      ElMessage.success('新增成功')
    }
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
  <el-dialog v-model="visible" :title="title" width="500px" :close-on-click-modal="false">
    <el-form ref="formRef" :model="form" :rules="rules" label-width="90px">
      <el-form-item label="标签名称" prop="name">
        <el-input v-model="form.name" placeholder="请输入标签名称" maxlength="50" clearable />
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="handleCancel">取消</el-button>
      <el-button type="primary" :loading="submitting" @click="handleSubmit">确定</el-button>
    </template>
  </el-dialog>
</template>
