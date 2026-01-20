<script setup>
import { ref } from 'vue'
import { userInfoService, userCountCardService } from '@/api/user'
const userInfo = ref({
  username: '',
  nickname: ''
})

const userCountCard = ref({
  articleCount: 0,
  categoryCount: 0,
  tagCount: 0
})

const handlerUserInfo = async () => {
  const res = await userInfoService()
  userInfo.value = res.data.data
}

const handlerUserCountCard = async () => {
  const res = await userCountCardService()
  userCountCard.value = res.data.data
}

handlerUserInfo()
handlerUserCountCard()
</script>
<template>
  <div class="rounded-2xl border border-slate-100 bg-[rgba(255,255,255,0.9)] p-6 shadow-card">
    <div class="flex items-center gap-4">
      <img
        alt="作者头像"
        class="h-14 w-14 rounded-2xl object-cover"
        src="https://images.unsplash.com/photo-1525130413817-d45c1d127c42?auto=format&fit=facearea&w=120&h=120&q=80"
      />
      <div>
        <p class="text-base font-semibold text-slate-900">
          {{ userInfo.nickname ? userInfo.nickname : userInfo.username }}
        </p>
        <p class="text-sm text-slate-500">一个热爱编程的普通人</p>
      </div>
    </div>

    <a-row>
      <a-col :span="8">
        <a-statistic title="文章" :value="userCountCard.articleCount" class="text-center" />
      </a-col>
      <a-col :span="8">
        <a-statistic title="分类" :value="userCountCard.categoryCount" class="text-center" />
      </a-col>
      <a-col :span="8">
        <a-statistic title="标签" :value="userCountCard.tagCount" class="text-center" />
      </a-col>
    </a-row>
  </div>
</template>

<style scoped></style>
