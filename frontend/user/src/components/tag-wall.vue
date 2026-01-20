<script setup>
import { ref, onMounted, defineEmits } from 'vue'
import { tagWallService } from '@/api/tag'

// ✅ 示例：后端建议返回 id、name、articleCount
const tags = ref([])

// ✅ 多色调色板（稳定：按 id 取模）
const palettes = [
  'border-sky-100 bg-sky-50 text-sky-600',
  'border-emerald-100 bg-emerald-50 text-emerald-600',
  'border-amber-100 bg-amber-50 text-amber-700',
  'border-violet-100 bg-violet-50 text-violet-600',
  'border-rose-100 bg-rose-50 text-rose-600',
  'border-slate-200 bg-slate-50 text-slate-700'
]

// ✅ 5档字号（推荐离散档位，Tailwind 友好）
// 你也可以改成 4 档/6 档
const sizeSteps = ['text-xs', 'text-sm', 'text-base', 'text-lg', 'text-xl']
const weightSteps = ['font-medium', 'font-medium', 'font-semibold', 'font-semibold', 'font-bold']

const tagWall = ref(null)
const createTagWall = () => {
  // 计算 min/max（注意边界）
  const counts = tags.value.map((t) => {
    return t.articleCount ?? 0
  })
  const min = Math.min(...counts)
  const max = Math.max(...counts)
  const span = Math.max(1, max - min)

  function getLevel(count) {
    // 归一化到 0~1
    const w = (count - min) / span
    // 映射到 0~(steps-1)
    return Math.max(0, Math.min(sizeSteps.length - 1, Math.round(w * (sizeSteps.length - 1))))
  }
  tags.value.forEach((t) => {
    const colorCls = palettes[t.tagId % palettes.length]
    const level = getLevel(t.articleCount ?? 0)
    const sizeCls = sizeSteps[level]
    const weightCls = weightSteps[level]

    const a = document.createElement('a')

    a.className = [
      'cursor-pointer inline-flex items-center rounded-full border px-4 py-1.5',
      'transition hover:opacity-90 active:scale-[0.98]',
      // ✅ 动态字号/字重
      sizeCls,
      weightCls,
      // ✅ 稳定配色
      colorCls
    ].join(' ')

    a.textContent = t.tagName

    a.addEventListener('click', (e) => {
      e.preventDefault()
      handlerTagPush(t)
    })
    tagWall.value.appendChild(a)
  })
}

const handlerTagWall = async () => {
  const res = await tagWallService()
  tags.value = res.data.data
  createTagWall()
}

const emit = defineEmits(['tagPush'])
const handlerTagPush = (tag) => {
  emit('tagPush', tag)
}

onMounted(() => {
  handlerTagWall()
})
</script>
<template>
  <div class="rounded-2xl border border-slate-100 bg-[rgba(255,255,255,0.9)] p-6 shadow-card">
    <p class="text-base font-semibold text-slate-900">标签墙</p>

    <div ref="tagWall" class="mt-4 flex flex-wrap gap-3 text-sm"></div>
  </div>
</template>
<style scoped></style>
