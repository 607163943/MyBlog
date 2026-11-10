<script setup lang="ts">
import { computed, reactive, ref, watch } from 'vue'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import { useCategoryStore } from '@/stores/category'
import type { CategoryVO } from '@/api/category'

const props = defineProps<{
  modelValue: boolean
  category: CategoryVO | null
}>()

const emit = defineEmits<{
  'update:modelValue': [value: boolean]
}>()

const store = useCategoryStore()
const formRef = ref<FormInstance>()
const submitting = ref(false)

const visible = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value),
})

const isEdit = computed(() => props.category !== null)
const title = computed(() => (isEdit.value ? '修改分类' : '新增分类'))

const form = reactive<{
  id?: number
  name: string
  description: string
  sort: number
}>({
  id: undefined,
  name: '',
  description: '',
  sort: 0,
})

const rules: FormRules = {
  name: [
    { required: true, message: '请输入分类名称', trigger: 'blur' },
    { max: 50, message: '分类名称不能超过 50 个字符', trigger: 'blur' },
  ],
  description: [{ max: 200, message: '分类描述不能超过 200 个字符', trigger: 'blur' }],
}

watch(
  () => props.modelValue,
  (value) => {
    if (!value) {
      return
    }
    form.id = props.category?.id
    form.name = props.category?.name ?? ''
    form.description = props.category?.description ?? ''
    form.sort = props.category?.sort ?? 0
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
      await store.editCategory({ ...form })
      ElMessage.success('修改成功')
    } else {
      await store.createCategory({ ...form })
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
      <el-form-item label="分类名称" prop="name">
        <el-input v-model="form.name" placeholder="请输入分类名称" maxlength="50" clearable />
      </el-form-item>
      <el-form-item label="分类描述" prop="description">
        <el-input
          v-model="form.description"
          type="textarea"
          :rows="3"
          placeholder="请输入分类描述"
          maxlength="200"
          show-word-limit
        />
      </el-form-item>
      <el-form-item label="排序" prop="sort">
        <el-input-number v-model="form.sort" :min="0" :max="9999" controls-position="right" />
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="handleCancel">取消</el-button>
      <el-button type="primary" :loading="submitting" @click="handleSubmit">确定</el-button>
    </template>
  </el-dialog>
</template>
