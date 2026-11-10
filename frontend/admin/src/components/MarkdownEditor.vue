<script setup lang="ts">
import { onBeforeUnmount, onMounted, ref } from 'vue'
import { Crepe } from '@milkdown/crepe'
import '@milkdown/crepe/theme/common/style.css'
import '@milkdown/crepe/theme/frame.css'

const props = withDefaults(
  defineProps<{
    modelValue?: string
    placeholder?: string
  }>(),
  {
    modelValue: '',
    placeholder: '请输入文章正文...',
  },
)

const emit = defineEmits<{
  'update:modelValue': [value: string]
}>()

const editorRef = ref<HTMLDivElement>()
let crepe: Crepe | null = null
let creating = false
let destroyed = false

onMounted(async () => {
  if (!editorRef.value) {
    return
  }

  const instance = new Crepe({
    root: editorRef.value,
    defaultValue: props.modelValue,
    featureConfigs: {
      [Crepe.Feature.Placeholder]: { text: props.placeholder },
    },
  })
  crepe = instance

  instance.on((listener) => {
    listener.markdownUpdated((_ctx, markdown) => {
      emit('update:modelValue', markdown)
    })
  })

  creating = true
  await instance.create()
  creating = false

  if (destroyed) {
    await instance.destroy()
    if (crepe === instance) {
      crepe = null
    }
  }
})

onBeforeUnmount(() => {
  destroyed = true
  if (crepe && !creating) {
    void crepe.destroy()
    crepe = null
  }
})
</script>

<template>
  <div ref="editorRef" class="markdown-editor"></div>
</template>

<style scoped lang="scss">
.markdown-editor {
  width: 100%;
  overflow: hidden;
  background-color: var(--el-bg-color);
  border: 1px solid var(--el-border-color);
  border-radius: var(--el-border-radius-base);

  :deep(.milkdown) {
    min-height: 520px;
    --crepe-color-background: var(--el-bg-color);
    --crepe-font-title:
      'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', Arial,
      sans-serif;
    --crepe-font-default:
      'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', Arial,
      sans-serif;
  }
}
</style>
