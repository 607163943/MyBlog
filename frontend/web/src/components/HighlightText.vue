<script setup lang="ts">
import { computed } from 'vue'

interface TextSegment {
  text: string
  match: boolean
}

const { text, keyword = '' } = defineProps<{
  text: string
  keyword?: string
}>()

const segments = computed<TextSegment[]>(() => {
  const query = keyword.trim()
  if (!query) return [{ text, match: false }]

  const lowerText = text.toLowerCase()
  const lowerQuery = query.toLowerCase()
  const result: TextSegment[] = []
  let cursor = 0
  let matchIndex = lowerText.indexOf(lowerQuery)

  while (matchIndex !== -1) {
    if (matchIndex > cursor) {
      result.push({ text: text.slice(cursor, matchIndex), match: false })
    }
    result.push({ text: text.slice(matchIndex, matchIndex + query.length), match: true })
    cursor = matchIndex + query.length
    matchIndex = lowerText.indexOf(lowerQuery, cursor)
  }

  if (cursor < text.length) {
    result.push({ text: text.slice(cursor), match: false })
  }

  return result
})
</script>

<template>
  <span class="highlight-text">
    <template v-for="(segment, index) in segments" :key="index">
      <mark v-if="segment.match" class="highlight">{{ segment.text }}</mark>
      <template v-else>{{ segment.text }}</template>
    </template>
  </span>
</template>

<style scoped>
.highlight {
  border-radius: 2px;
  background-color: var(--el-color-primary-light-8);
  color: var(--el-color-primary);
}
</style>
