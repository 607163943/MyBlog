<script setup>
import { defineOptions } from 'vue'
defineOptions({
  name: 'LayoutHeader'
})
</script>

<template>
  <nav class="main-header navbar navbar-expand bg-white navbar-light border-bottom">
    <!-- Left navbar links -->
    <ul class="navbar-nav">
      <li class="nav-item">
        <a class="nav-link" data-widget="pushmenu" href="#"><i class="fa fa-bars"></i></a>
      </li>
      <li class="nav-item d-none d-sm-inline-block">
        <a href="/" class="nav-link">Dashboard</a>
      </li>
    </ul>
    <!-- Right navbar links -->
    <ul class="navbar-nav ml-auto">
      <li class="nav-item dropdown">
        <a class="nav-link" href="/">
          <i class="fa fa-paper-plane">&nbsp;&nbsp;博客首页</i>
        </a>
      </li>
      <li class="nav-item dropdown">
        <a class="nav-link" data-toggle="dropdown" href="#">
          <i class="fa fa-user">&nbsp;&nbsp;作者</i>
        </a>
        <div class="dropdown-menu dropdown-menu-lg dropdown-menu-right">
          <div class="dropdown-divider"></div>
          <a href="#" class="dropdown-item">
            <i class="fa fa-user-o mr-2"></i> 姓名
            <span class="float-right text-muted text-sm">十三 / 13</span>
          </a>
          <div class="dropdown-divider"></div>
          <a href="#" class="dropdown-item">
            <i class="fa fa-user-secret mr-2"></i> 身份
            <span class="float-right text-muted text-sm">Java开发工程师</span>
          </a>
          <div class="dropdown-divider"></div>
          <a href="#" class="dropdown-item">
            <i class="fa fa-address-card mr-2"></i> 邮箱
            <span class="float-right text-muted text-sm">2449207463@qq.com</span>
          </a>
        </div>
      </li>
    </ul>
  </nav>
</template>

<style lang="less"></style>
