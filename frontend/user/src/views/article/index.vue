<script setup></script>
<template>
  <main class="flex-1 pb-16">
    <div class="mx-auto max-w-[1200px] px-6 py-10 space-y-10">
      <section
        class="rounded-[32px] border border-white/80 bg-linear-to-br from-white to-[#EEF2FB] p-8 shadow-card"
      >
        <div class="flex flex-col gap-4">
          <p class="text-xs font-semibold tracking-[0.3em] text-slate-400">ARTICLE VIEW</p>
          <h1 class="text-3xl font-semibold text-slate-900">
            回到文字本身：让内容在克制框架里呼吸
          </h1>
          <p class="text-base text-slate-600">
            格态白昼主题强调“Cards-First Feed”。长文在纯白卡片中排布，
            以适度阴影与圆角让阅读更舒缓，保持导航与回到列表的清晰动线。
          </p>
        </div>
      </section>

      <div class="grid grid-cols-1 gap-8 xl:grid-cols-[2fr_1fr]">
        <section>
          <article class="rounded-[32px] border border-slate-100 bg-white p-10 shadow-card">
            <nav class="text-sm text-slate-500">
              <a class="transition hover:text-slate-700" href="#">首页</a>
              <span class="px-2 text-slate-300">/</span>
              <a class="transition hover:text-slate-700" href="#">产品策略</a>
              <span class="px-2 text-slate-300">/</span>
              <span class="text-slate-400">正文</span>
            </nav>

            <div class="mt-6 space-y-4">
              <h2 class="text-3xl font-semibold text-slate-900">
                长期主义者的增长留存手记：重构用户价值的三种方式
              </h2>
              <p class="text-sm text-slate-500">
                所属分类：<span class="font-semibold text-slate-800">产品策略</span>
              </p>
              <div class="flex flex-wrap gap-2 text-sm">
                <span class="rounded-full border border-sky-100 bg-sky-50 px-3 py-1 text-sky-600">
                  #增长模型
                </span>
                <span class="rounded-full border border-sky-100 bg-sky-50 px-3 py-1 text-sky-600">
                  #长期主义
                </span>
                <span class="rounded-full border border-sky-100 bg-sky-50 px-3 py-1 text-sky-600">
                  #体验设计
                </span>
              </div>
              <p class="text-sm text-slate-500">发布时间 · 2025/11/12 · 阅读时间 12 分钟</p>
            </div>

            <div class="mt-8 space-y-6 text-base leading-8 text-slate-700">
              <p>
                这是一次面向“长期关系”的复盘。我们从一线运营的访谈里发现，真正决定留存的不是推送频次，而是“用户能否理解我们在做什么”。于是，我将项目分成三个阶段：叙事、节奏与交付。
              </p>
              <p>
                叙事阶段聚焦“我们要解决什么问题”。用 OKR 里最核心的 O 来定义价值，再用三条 KR
                列出可验证的衡量方式。团队成员讨论时可以迅速对齐“我们是否在推动 KR”
                而非“我喜欢哪个方案”。
              </p>
              <p>
                节奏阶段强调“留白”。在低对比背景中，任何多余装饰都会破坏专注。因此我们设置了“沉浸区”，只允许主色出现一次，并将分类与标签收拢为轻描边的胶囊。
              </p>
              <p>
                交付阶段则是“可被复制的胜利”。我拆解出四个动作：采集、过滤、打磨、分发。每个动作都以卡片组件承载，读者在滑动时能够明确知道自己处于哪一个阶段。
              </p>
              <p>
                长期主义并不意味着缓慢，而是维持一致的节奏感。格态白昼主题正是这种节奏的具象化，它允许内容自由呼吸，又不失去结构化的秩序。
              </p>
            </div>

            <div class="mt-10 rounded-2xl bg-slate-50 p-6">
              <p class="text-sm font-semibold text-slate-800">相关提要</p>
              <ul class="mt-3 space-y-2 text-sm text-slate-600">
                <li>· 长期留存的三个衡量指标</li>
                <li>· 如何设计沉浸式阅读节奏</li>
                <li>· Cards-First Feed 的模块化思路</li>
              </ul>
            </div>

            <nav class="mt-10 flex flex-wrap gap-3 text-sm text-slate-500">
              <a class="transition hover:text-slate-700" href="#">返回首页</a>
              <span class="text-slate-300">/</span>
              <a class="transition hover:text-slate-700" href="#">回到产品策略列表</a>
              <span class="text-slate-300">/</span>
              <a class="transition hover:text-slate-700" href="#">下一篇 · 体验设计里的节奏控制</a>
            </nav>
          </article>
        </section>

        <aside class="space-y-6">
          <div class="rounded-2xl border border-slate-100 bg-white p-6 shadow-card">
            <div class="flex items-center gap-4">
              <img
                alt="作者头像"
                class="h-14 w-14 rounded-2xl object-cover"
                src="https://images.unsplash.com/photo-1504593811423-6dd665756598?auto=format&fit=facearea&w=120&h=120&q=80"
              />
              <div>
                <p class="text-base font-semibold text-slate-900">云巢</p>
                <p class="text-sm text-slate-500">设计 · 写作者</p>
              </div>
            </div>
            <p class="mt-4 text-sm leading-6 text-slate-600">
              专注长期主义的产品与写作实践，记录真实的指标、叙事与工程故事。
            </p>
            <div class="mt-4 flex items-center gap-3 text-xs text-slate-500">
              <span>订阅 · 12.4K</span>
              <span>建立于 · 2017</span>
            </div>
          </div>

          <div class="rounded-2xl border border-slate-100 bg-white p-6 shadow-card">
            <p class="text-base font-semibold text-slate-900">热门分类排行</p>
            <ol class="mt-4 space-y-4 text-sm">
              <li class="flex items-center justify-between text-slate-600">
                <span class="font-medium text-slate-800">01 · 产品策略</span>
                <span class="text-xs text-slate-500">67%</span>
              </li>
              <li class="flex items-center justify-between text-slate-600">
                <span class="font-medium text-slate-800">02 · 体验设计</span>
                <span class="text-xs text-slate-500">54%</span>
              </li>
              <li class="flex items-center justify-between text-slate-600">
                <span class="font-medium text-slate-800">03 · 工程效率</span>
                <span class="text-xs text-slate-500">41%</span>
              </li>
              <li class="flex items-center justify-between text-slate-600">
                <span class="font-medium text-slate-800">04 · AI 协作</span>
                <span class="text-xs text-slate-500">32%</span>
              </li>
              <li class="flex items-center justify-between text-slate-600">
                <span class="font-medium text-slate-800">05 · 写作手记</span>
                <span class="text-xs text-slate-500">25%</span>
              </li>
            </ol>
          </div>

          <div class="rounded-2xl border border-slate-100 bg-white p-6 shadow-card">
            <p class="text-base font-semibold text-slate-900">最新文章 Top5</p>
            <div class="mt-4 space-y-4 text-sm text-slate-600">
              <div class="flex items-start gap-3">
                <span class="text-xs font-semibold text-slate-400">01</span>
                <div>
                  <p class="font-medium text-slate-800">结构化复盘：五步模型</p>
                  <p class="text-xs text-slate-400">2 小时前</p>
                </div>
              </div>
              <div class="flex items-start gap-3">
                <span class="text-xs font-semibold text-slate-400">02</span>
                <div>
                  <p class="font-medium text-slate-800">云原生内容交付体验</p>
                  <p class="text-xs text-slate-400">1 天前</p>
                </div>
              </div>
              <div class="flex items-start gap-3">
                <span class="text-xs font-semibold text-slate-400">03</span>
                <div>
                  <p class="font-medium text-slate-800">暗色界面安全边距</p>
                  <p class="text-xs text-slate-400">3 天前</p>
                </div>
              </div>
              <div class="flex items-start gap-3">
                <span class="text-xs font-semibold text-slate-400">04</span>
                <div>
                  <p class="font-medium text-slate-800">写给协作者的十件事</p>
                  <p class="text-xs text-slate-400">1 周前</p>
                </div>
              </div>
              <div class="flex items-start gap-3">
                <span class="text-xs font-semibold text-slate-400">05</span>
                <div>
                  <p class="font-medium text-slate-800">长文排版节奏技巧</p>
                  <p class="text-xs text-slate-400">2 周前</p>
                </div>
              </div>
            </div>
          </div>

          <div class="rounded-2xl border border-slate-100 bg-white p-6 shadow-card">
            <p class="text-base font-semibold text-slate-900">标签墙</p>
            <div class="mt-4 flex flex-wrap gap-3 text-sm">
              <span class="rounded-full border border-sky-100 bg-sky-50 px-4 py-1.5 text-sky-600">
                产品冷启动
              </span>
              <span class="rounded-full border border-sky-100 bg-sky-50 px-4 py-1.5 text-sky-600">
                体验筹划
              </span>
              <span class="rounded-full border border-sky-100 bg-sky-50 px-4 py-1.5 text-sky-600">
                写作手记
              </span>
              <span class="rounded-full border border-sky-100 bg-sky-50 px-4 py-1.5 text-sky-600">
                设计系统
              </span>
              <span class="rounded-full border border-sky-100 bg-sky-50 px-4 py-1.5 text-sky-600">
                信息架构
              </span>
              <span class="rounded-full border border-sky-100 bg-sky-50 px-4 py-1.5 text-sky-600">
                效率工具
              </span>
            </div>
          </div>
        </aside>
      </div>
    </div>

    <button
      aria-label="查看更多"
      class="fixed bottom-6 right-6 hidden h-11 w-11 items-center justify-center rounded-full border border-slate-200 bg-white text-slate-600 shadow-lg transition hover:text-sky-500 md:flex"
      type="button"
    >
      <svg
        xmlns="http://www.w3.org/2000/svg"
        fill="none"
        viewBox="0 0 24 24"
        stroke-width="1.5"
        stroke="currentColor"
        class="h-4 w-4"
      >
        <path stroke-linecap="round" stroke-linejoin="round" d="M8 5l8 7-8 7" />
      </svg>
    </button>
  </main>
</template>
<style scoped lang="less"></style>
