<script setup></script>
<template>
  <main class="flex-1 pb-16">
    <div class="mx-auto max-w-[1200px] px-6 py-10 space-y-10">
      <section
        class="rounded-[32px] border border-white/80 bg-linear-to-br from-white to-[#EEF2FB] p-8 shadow-card"
      >
        <div class="space-y-4">
          <p class="text-xs font-semibold tracking-[0.3em] text-slate-400">CATEGORY LIBRARY</p>
          <h1 class="text-3xl font-semibold text-slate-900">
            以主题为线索的内容树 · 共收录 18 个分类
          </h1>
          <p class="text-base text-slate-600">
            每个分类都以三列卡片呈现关键信息与文章数量，点击即可跳到对应 Feed。
          </p>
        </div>
        <div class="mt-6 space-y-3">
          <label class="text-sm font-medium text-slate-600">快速查找分类</label>
          <div
            class="flex flex-1 items-center gap-3 rounded-full border border-slate-200 bg-slate-50 px-5 py-2.5 text-slate-500 shadow-[0_0_0_1px_rgba(226,232,240,0.6)] focus-within:ring-1 focus-within:ring-sky-400"
          >
            <svg
              xmlns="http://www.w3.org/2000/svg"
              fill="none"
              viewBox="0 0 24 24"
              stroke-width="1.5"
              stroke="currentColor"
              class="h-5 w-5 text-slate-400"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                d="m21 21-4.35-4.35m0-6.15a6.15 6.15 0 1 1-12.3 0 6.15 6.15 0 0 1 12.3 0Z"
              />
            </svg>
            <input
              class="w-full bg-transparent text-base text-slate-700 caret-sky-500 placeholder:text-slate-400 focus:outline-none"
              placeholder="输入分类名或主题关键字"
              type="text"
            />
          </div>
          <div class="border-y border-slate-100 bg-white/90">
            <div class="flex gap-3 overflow-x-auto py-3 text-sm snap-x snap-mandatory">
              <button
                class="snap-start rounded-full border border-sky-500 bg-sky-500 px-4 py-2 font-medium text-white"
                type="button"
              >
                全部
              </button>
              <button
                class="snap-start rounded-full border border-slate-200 bg-white px-4 py-2 text-slate-600"
                type="button"
              >
                产品策略
              </button>
              <button
                class="snap-start rounded-full border border-slate-200 bg-white px-4 py-2 text-slate-600"
                type="button"
              >
                体验设计
              </button>
              <button
                class="snap-start rounded-full border border-slate-200 bg-white px-4 py-2 text-slate-600"
                type="button"
              >
                工程效率
              </button>
              <button
                class="snap-start rounded-full border border-slate-200 bg-white px-4 py-2 text-slate-600"
                type="button"
              >
                系统架构
              </button>
              <button
                class="snap-start rounded-full border border-slate-200 bg-white px-4 py-2 text-slate-600"
                type="button"
              >
                写作手记
              </button>
            </div>
          </div>
        </div>
      </section>

      <div class="grid grid-cols-1 gap-8 xl:grid-cols-[2fr_1fr]">
        <section class="space-y-8">
          <div class="grid grid-cols-1 gap-6 sm:grid-cols-2 xl:grid-cols-3">
            <article
              class="flex flex-col gap-4 rounded-2xl border border-slate-100 bg-white p-6 shadow-card"
            >
              <p class="text-xs font-semibold text-slate-500">NO.01</p>
              <div>
                <h2 class="text-lg font-semibold text-slate-900">产品策略与长期主义</h2>
                <p class="mt-2 text-sm leading-6 text-slate-600">
                  记录增长、留存与价值创新的真实案例，与数据背后的叙事框架。
                </p>
              </div>
              <div class="flex items-center justify-between text-xs text-slate-500">
                <span>17 篇文章</span>
                <a class="text-sky-600" href="#">查看</a>
              </div>
            </article>

            <article
              class="flex flex-col gap-4 rounded-2xl border border-slate-100 bg-white p-6 shadow-card"
            >
              <p class="text-xs font-semibold text-slate-500">NO.02</p>
              <div>
                <h2 class="text-lg font-semibold text-slate-900">体验设计与信息秩序</h2>
                <p class="mt-2 text-sm leading-6 text-slate-600">
                  探索暗色主题、信息层级与动效节奏等“克制设计”手法。
                </p>
              </div>
              <div class="flex items-center justify-between text-xs text-slate-500">
                <span>14 篇文章</span>
                <a class="text-sky-600" href="#">查看</a>
              </div>
            </article>

            <article
              class="flex flex-col gap-4 rounded-2xl border border-slate-100 bg-white p-6 shadow-card"
            >
              <p class="text-xs font-semibold text-slate-500">NO.03</p>
              <div>
                <h2 class="text-lg font-semibold text-slate-900">工程效率与协同</h2>
                <p class="mt-2 text-sm leading-6 text-slate-600">
                  讲述工具链、AI 协作与跨团队协同的实战手记。
                </p>
              </div>
              <div class="flex items-center justify-between text-xs text-slate-500">
                <span>11 篇文章</span>
                <a class="text-sky-600" href="#">查看</a>
              </div>
            </article>

            <article
              class="flex flex-col gap-4 rounded-2xl border border-slate-100 bg-white p-6 shadow-card"
            >
              <p class="text-xs font-semibold text-slate-500">NO.04</p>
              <div>
                <h2 class="text-lg font-semibold text-slate-900">基础架构与系统写作</h2>
                <p class="mt-2 text-sm leading-6 text-slate-600">
                  深入分布式系统、可观测性与高可用策略的实践记录。
                </p>
              </div>
              <div class="flex items-center justify-between text-xs text-slate-500">
                <span>9 篇文章</span>
                <a class="text-sky-600" href="#">查看</a>
              </div>
            </article>

            <article
              class="flex flex-col gap-4 rounded-2xl border border-slate-100 bg-white p-6 shadow-card"
            >
              <p class="text-xs font-semibold text-slate-500">NO.05</p>
              <div>
                <h2 class="text-lg font-semibold text-slate-900">写作手记与叙事</h2>
                <p class="mt-2 text-sm leading-6 text-slate-600">
                  复盘写作方法、结构与笔记系统，分享轻社交感的表达。
                </p>
              </div>
              <div class="flex items-center justify-between text-xs text-slate-500">
                <span>13 篇文章</span>
                <a class="text-sky-600" href="#">查看</a>
              </div>
            </article>

            <article
              class="flex flex-col gap-4 rounded-2xl border border-slate-100 bg-white p-6 shadow-card"
            >
              <p class="text-xs font-semibold text-slate-500">NO.06</p>
              <div>
                <h2 class="text-lg font-semibold text-slate-900">个人效率与工具栈</h2>
                <p class="mt-2 text-sm leading-6 text-slate-600">
                  评测效率工具、自动化脚本与跨平台工作流的最佳实践。
                </p>
              </div>
              <div class="flex items-center justify-between text-xs text-slate-500">
                <span>10 篇文章</span>
                <a class="text-sky-600" href="#">查看</a>
              </div>
            </article>
          </div>

          <div
            class="flex flex-col gap-4 rounded-2xl border border-slate-100 bg-white p-5 shadow-card sm:flex-row sm:items-center sm:justify-between"
          >
            <p class="text-sm text-slate-500">第 1/3 页 · 共 18 个分类</p>
            <div class="flex items-center gap-2 text-sm font-medium">
              <button
                class="rounded-full border border-slate-200 bg-white px-4 py-2 text-slate-600 transition hover:border-slate-300"
              >
                上一页
              </button>
              <button class="rounded-full border border-sky-500 bg-sky-500 px-4 py-2 text-white">
                下一页
              </button>
            </div>
          </div>
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
              分类体系持续更新，欢迎通过邮件告诉我你希望补充的主题内容。
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
                  <p class="font-medium text-slate-800">结构化复盘的五步模型</p>
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
                  <p class="font-medium text-slate-800">暗色界面中的节奏控制</p>
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
