<script setup></script>
<template>
  <div
    class="min-h-screen bg-[#F5F7FB] text-slate-700 bg-[url(../../src/assets/bg.jpg)] bg-no-repeat bg-fixed bg-center"
  >
    <div class="flex min-h-screen flex-col">
      <header
        class="sticky top-0 z-30000 border-b border-slate-100 bg-white/95 shadow-sm backdrop-blur"
      >
        <div class="mx-auto flex h-16 max-w-[1200px] items-center px-6">
          <a class="flex items-center gap-3 cursor-pointer" href="/">
            <div
              class="flex h-11 w-11 items-center justify-center rounded-2xl bg-sky-500/10 text-base font-semibold text-sky-600"
            >
              M
            </div>
            <div>
              <p class="text-sm font-semibold text-slate-900">MyBlog</p>
              <p class="text-xs text-slate-400">个人网站</p>
            </div>
          </a>
          <div class="ml-auto flex items-center gap-6 text-sm font-medium text-slate-600">
            <a-button shape="round" @click="$router.push('/category')">分类</a-button>
          </div>
        </div>
      </header>

      <router-view />

      <footer
        class="border-t border-slate-200 py-8 text-center text-sm text-slate-500 bg-[rgba(255,255,255,0.9)]"
      >
        <p>© 2026 MyBlog · 个人网站 · 保留所有权利</p>
      </footer>
    </div>
  </div>
</template>
<style scoped lang="less"></style>
