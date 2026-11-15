<script setup lang="ts">
import { onMounted, ref, watch } from 'vue'

import CommentEditor from './CommentEditor.vue'
import { useCommentStore } from '@/stores/comment'
import type { CommentSubmitDTO, CommentVO } from '@/types/comment'
import { formatDateTime } from '@/util/format'

const props = defineProps<{
  articleId: number
}>()

const commentStore = useCommentStore()
const editorRef = ref<InstanceType<typeof CommentEditor>>()

const commentColors = [
  'var(--el-color-primary)',
  'var(--el-color-success)',
  'var(--el-color-warning)',
]

function commentColor(index: number) {
  return commentColors[index % commentColors.length] ?? 'var(--el-color-primary)'
}

async function load() {
  await commentStore.load(props.articleId)
}

async function handleSubmit(data: Omit<CommentSubmitDTO, 'replyToId'>) {
  const ok = await commentStore.submit(props.articleId, {
    ...data,
    replyToId: commentStore.replyTarget?.id,
  })
  if (ok) {
    editorRef.value?.reset()
  }
}

function reply(comment: CommentVO) {
  commentStore.setReplyTarget(comment.id, comment.nickname)
}

onMounted(load)

watch(
  () => props.articleId,
  () => {
    commentStore.clearReplyTarget()
    load()
  },
)
</script>

<template>
  <div v-loading="commentStore.loading" class="comment-section">
    <CommentEditor
      ref="editorRef"
      :submitting="commentStore.submitting"
      :reply-to-nickname="commentStore.replyTarget?.nickname"
      @submit="handleSubmit"
      @cancel-reply="commentStore.clearReplyTarget()"
    />

    <el-divider />

    <ul v-if="commentStore.comments.length" class="comment-list">
      <li v-for="(comment, index) in commentStore.comments" :key="comment.id" class="comment-item">
        <el-avatar
          class="comment-avatar"
          :size="36"
          :style="{ backgroundColor: commentColor(index) }"
        >
          {{ comment.nickname.slice(0, 1) }}
        </el-avatar>

        <div class="comment-main">
          <div class="comment-meta">
            <strong>{{ comment.nickname }}</strong>
            <span>{{ formatDateTime(comment.createTime) }}</span>
          </div>
          <p class="comment-content">{{ comment.content }}</p>
          <el-button text type="primary" size="small" @click="reply(comment)">回复</el-button>

          <div v-if="comment.children.length" class="comment-replies">
            <div
              v-for="(replyItem, replyIndex) in comment.children"
              :key="replyItem.id"
              class="comment-reply"
            >
              <el-avatar
                class="comment-avatar"
                :size="30"
                :style="{ backgroundColor: commentColor(index + replyIndex + 1) }"
              >
                {{ replyItem.nickname.slice(0, 1) }}
              </el-avatar>

              <div class="comment-main">
                <div class="comment-meta">
                  <strong>{{ replyItem.nickname }}</strong>
                  <span v-if="replyItem.replyToNickname" class="reply-to">
                    回复 @{{ replyItem.replyToNickname }}
                  </span>
                  <span>{{ formatDateTime(replyItem.createTime) }}</span>
                </div>
                <p class="comment-content">{{ replyItem.content }}</p>
                <el-button text type="primary" size="small" @click="reply(replyItem)">
                  回复
                </el-button>
              </div>
            </div>
          </div>
        </div>
      </li>
    </ul>

    <el-empty v-else-if="!commentStore.loading" description="还没有评论，快来抢沙发" />
  </div>
</template>

<style scoped>
.comment-list {
  display: flex;
  flex-direction: column;
}

.comment-item {
  display: flex;
  gap: 14px;
  padding: 16px 0;
  border-top: 1px solid var(--el-border-color-lighter);
}

.comment-item:first-child {
  padding-top: 0;
  border-top: none;
}

.comment-avatar {
  flex-shrink: 0;
  color: var(--el-color-white);
  font-size: 14px;
}

.comment-main {
  min-width: 0;
  flex: 1;
}

.comment-meta {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 10px;
}

.comment-meta strong {
  font-size: 14px;
  font-weight: 600;
}

.comment-meta span {
  color: var(--el-text-color-secondary);
  font-size: 12px;
}

.comment-meta .reply-to {
  color: var(--el-color-primary);
}

.comment-content {
  margin: 8px 0 4px;
  color: var(--el-text-color-regular);
  font-size: 14px;
  line-height: 1.8;
  overflow-wrap: anywhere;
}

.comment-replies {
  display: flex;
  flex-direction: column;
  margin-top: 12px;
  padding-left: 14px;
  border-left: 2px solid var(--el-border-color-lighter);
}

.comment-reply {
  display: flex;
  gap: 12px;
  padding: 10px 0;
}

.comment-reply + .comment-reply {
  border-top: 1px dashed var(--el-border-color-lighter);
}
</style>
