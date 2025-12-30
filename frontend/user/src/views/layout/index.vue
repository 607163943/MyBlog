<script setup>
$(function () {
  $('.navbar-toggle').click(function () {
    $('#toggle-items').fadeToggle('fast')
  })
})

import { ref } from 'vue'
import { homeGetDataService } from '@/api/home'

const configurations = ref({
  websiteName: '我的博客',
  websiteDescription: '我的博客',
  websiteIcon: '/favicon.ico',
  websiteUrl: 'https://www.example.com',
  websiteEmail: 'example@example.com',
})

const blogPageResult = ref({})
const hotTags = ref({})
const newBlogs = ref({})
const hotBlogs = ref({})

const getBlogHomeData = async () => {
  const res = await homeGetDataService()
  configurations.value = res.data.data.configurations
  blogPageResult.value = res.data.data.blogPageResult
  hotTags.value = res.data.data.hotTags
  newBlogs.value = res.data.data.newBlogs
  hotBlogs.value = res.data.data.hotBlogs
}

getBlogHomeData()
</script>

<template>
  <nav class="navbar navbar-default navbar-custom navbar-fixed-top">
    <div class="container-fluid">
      <!-- Brand and toggle get grouped for better mobile display -->
      <div class="navbar-header page-scroll">
        <button type="button" class="navbar-toggle" data-for="toggle-items">
          <span class="sr-only"></span>
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>
        </button>
        <a class="navbar-brand" href="/">{{ configurations.websiteName }}</a>
      </div>
      <div class="navbar-collapse" style="display: none" id="toggle-items">
        <ul class="nav navbar-nav navbar-right">
          <li>
            <a href="/home">主页</a>
          </li>
          <li>
            <a href="/link">友链</a>
          </li>
          <li>
            <a href="/about">关于</a>
          </li>
          <li>
            <a href="https://github.com/ZHENFENG13">GitHub</a>
          </li>
        </ul>
      </div>
      <div id="huxblog_navbar">
        <div class="navbar-collapse">
          <ul class="nav navbar-nav navbar-right">
            <li>
              <a href="/home">主页</a>
            </li>
            <li>
              <a href="/link">友链</a>
            </li>
            <li>
              <a href="/about">关于</a>
            </li>
            <li>
              <a href="https://github.com/ZHENFENG13">GitHub</a>
            </li>
          </ul>
        </div>
      </div>
      <!-- /.navbar-collapse -->
    </div>
    <!-- /.container -->
  </nav>
  <header
    class="intro-header"
    style="background-image: url(../../../public/blog/amaze/images/header.jpg)"
  >
    <div class="container">
      <div class="row">
        <div class="col-lg-8 col-lg-offset-2 col-md-10 col-md-offset-1">
          <div class="site-heading">
            <span class="subheading"> 我不怕千万人阻挡，只怕自己投降 </span>
          </div>
        </div>
      </div>
    </div>
  </header>
  <router-view />
  <footer class="footer blog-footer">
    <div class="blog-text-center">
      &copy;
      <a>{{ configurations.footerAbout }}</a> &nbsp; • &nbsp; Powered by
      <a href="https://github.com/ZHENFENG13" target="_blank">{{
        configurations.footerCopyRight
      }}</a
      ><br />
      ICP
      <a href="https://beian.miit.gov.cn">{{ configurations.footerICP }}</a>
      <span class="ft-warn">&heartsuit;</span>
      Theme
      <a rel="friend" href="https://github.com/spiritree/typecho-theme-amaze" target="_blank">
        Amaze</a
      >
      by <a href="https://github.com/spiritree" target="_blank">spiritree</a> &
      <a href="https://github.com/zjhch123" target="_blank">Jiahao.Zhang</a>
    </div>
  </footer>
</template>

<style scoped lang="less"></style>
