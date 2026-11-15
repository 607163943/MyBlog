<script setup lang="ts">
import { computed, ref } from 'vue'

import { siteConfig } from '@/config/site'

const props = defineProps<{
  submitting: boolean
  replyToNickname?: string | null
}>()

const emit = defineEmits<{
  submit: [data: { nickname: string; email?: string; content: string }]
  'cancel-reply': []
}>()

const nickname = ref('')
const email = ref('')
const content = ref('')

const canSubmit = computed(() => nickname.value.trim() !== '' && content.value.trim() !== '')

function handleSubmit() {
  if (!canSubmit.value || props.submitting) {
    return
  }
  emit('submit', {
    nickname: nickname.value.trim(),
    email: email.value.trim() || undefined,
    content: content.value.trim(),
  })
}

function reset() {
  content.value = ''
}

defineExpose({ reset })
</script>

<template>
  <div class="comment-editor">
    <el-avatar class="comment-editor-avatar" :size="36" :src="siteConfig.avatar" alt="博主头像" />
    <div class="comment-editor-main">
      <div v-if="replyToNickname" class="reply-target">
        <span>回复 @{{ replyToNickname }}</span>
        <el-button text size="small" @click="$emit('cancel-reply')">取消</el-button>
      </div>

      <div class="comment-editor-fields">
        <el-input v-model="nickname" placeholder="你的昵称" maxlength="50" />
        <el-input v-model="email" placeholder="邮箱（选填，不会公开）" maxlength="100" />
      </div>

      <el-input
        v-model="content"
        type="textarea"
        :rows="4"
        maxlength="500"
        show-word-limit
        placeholder="留下你的想法..."
      />

      <div class="comment-editor-actions">
        <span class="comment-hint">友善的评论是交流的起点</span>
        <el-button
          type="primary"
          :disabled="!canSubmit"
          :loading="submitting"
          @click="handleSubmit"
        >
          发表评论
        </el-button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.comment-editor {
  display: flex;
  gap: 14px;
}

.comment-editor-avatar {
  flex-shrink: 0;
}

.comment-editor-main {
  display: flex;
  min-width: 0;
  flex: 1;
  flex-direction: column;
  gap: 10px;
}

.comment-editor-fields {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 10px;
}

.reply-target {
  display: flex;
  align-items: center;
  gap: 8px;
  color: var(--el-color-primary);
  font-size: 13px;
}

.comment-editor-actions {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
}

.comment-hint {
  color: var(--el-text-color-placeholder);
  font-size: 12px;
}

@media (max-width: 768px) {
  .comment-editor > :first-child {
    display: none;
  }

  .comment-editor-fields {
    grid-template-columns: minmax(0, 1fr);
  }

  .comment-editor-actions {
    flex-wrap: wrap;
  }
}
</style>
